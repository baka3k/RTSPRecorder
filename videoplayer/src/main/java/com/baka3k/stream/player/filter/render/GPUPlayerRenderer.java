package com.baka3k.stream.player.filter.render;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.Surface;

import com.baka3k.stream.player.filter.GPUPlayerView;
import com.daasuu.gpuv.egl.EglUtil;
import com.daasuu.gpuv.egl.GlConfigChooser;
import com.daasuu.gpuv.egl.GlContextFactory;
import com.daasuu.gpuv.egl.GlFrameBufferObjectRenderer;
import com.daasuu.gpuv.egl.GlFramebufferObject;
import com.daasuu.gpuv.egl.GlPreviewFilter;
import com.daasuu.gpuv.egl.GlSurfaceTexture;
import com.daasuu.gpuv.egl.filter.GlFilter;
import com.daasuu.gpuv.egl.filter.GlLookUpTableFilter;

import java.util.ConcurrentModificationException;

import javax.microedition.khronos.egl.EGLConfig;

import tv.danmaku.ijk.media.player.pragma.DebugLog;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_MAX_TEXTURE_SIZE;
import static android.opengl.GLES20.GL_NEAREST;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glViewport;

public class GPUPlayerRenderer extends GlFrameBufferObjectRenderer implements SurfaceTexture.OnFrameAvailableListener {
    private static final String TAG = GPUPlayerRenderer.class.getSimpleName();

    public interface GPUPlayerRendererListener {
        void onSurfaceCreated(Surface surface);
    }

    private GPUPlayerRendererListener mGPUPlayerRendererListener;
    private GlSurfaceTexture previewTexture;
    private boolean updateSurface = false;

    private int texName;
    private int mRotate = 0;
    private float[] mScale;
    private float[] MVPMatrix = new float[16];
    private float[] ProjMatrix = new float[16];
    private float[] VMatrix = new float[16];
    private float[] MMatrix = new float[16];
    private float[] STMatrix = new float[16];


    private GlFramebufferObject filterFramebufferObject;
    private GlPreviewFilter previewFilter;

    private GlFilter glFilter;
    private GlFilter oldGlFilter;
    private boolean isNewFilter;
    private final GPUPlayerView glPreview;

    private float aspectRatio = 1f;
    private boolean mFlipVertical = false;
    private boolean mFlipHorizontal = false;
    private int height;
    private int width;

    public GPUPlayerRenderer(GPUPlayerView glPreview, GPUPlayerRendererListener gpuPlayerRendererListener) {
        super();
        Matrix.setIdentityM(STMatrix, 0);
        this.glPreview = glPreview;
        mGPUPlayerRendererListener = gpuPlayerRendererListener;
        this.glPreview.setEGLConfigChooser(new GlConfigChooser(false));
        this.glPreview.setEGLContextFactory(new GlContextFactory());
    }

    public void setGlFilter(final GlFilter filter) {
        oldGlFilter = filter;
        glPreview.queueEvent(() -> {
            if (glFilter != null) {
                glFilter.release();
                if (glFilter instanceof GlLookUpTableFilter) {
                    ((GlLookUpTableFilter) glFilter).releaseLutBitmap();
                }
                glFilter = null;
            }
            glFilter = filter;
            isNewFilter = true;
            glPreview.requestRender();
        });
    }


    @Override
    public void onSurfaceCreated(final EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        final int[] args = new int[1];

        GLES20.glGenTextures(args.length, args, 0);
        texName = args[0];


        previewTexture = new GlSurfaceTexture(texName);
        previewTexture.setOnFrameAvailableListener(this);


        GLES20.glBindTexture(previewTexture.getTextureTarget(), texName);
        // GL_TEXTURE_EXTERNAL_OES
        EglUtil.setupSampler(previewTexture.getTextureTarget(), GL_LINEAR, GL_NEAREST);
        GLES20.glBindTexture(GL_TEXTURE_2D, 0);

        filterFramebufferObject = new GlFramebufferObject();
        // GL_TEXTURE_EXTERNAL_OES
        previewFilter = new GlPreviewFilter(previewTexture.getTextureTarget());
        previewFilter.setup();

        Surface surface = new Surface(previewTexture.getSurfaceTexture());
        handleOnSurfaceCreated(surface);
        Matrix.setLookAtM(VMatrix, 0,
                0.0f, 0.0f, 5.0f,
                0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f
        );

        synchronized (this) {
            updateSurface = false;
        }

        if (glFilter != null) {
            isNewFilter = true;
        }

        GLES20.glGetIntegerv(GL_MAX_TEXTURE_SIZE, args, 0);

    }

    private void handleOnSurfaceCreated(Surface surface) {
        if (mGPUPlayerRendererListener != null) {
            mGPUPlayerRendererListener.onSurfaceCreated(surface);
        }
    }

    @Override
    public void onSurfaceChanged(final int width, final int height) {
        this.width = width;
        this.height = height;
        filterFramebufferObject.setup(width, height);
        previewFilter.setFrameSize(width, height);
        if (glFilter != null) {
            glFilter.setFrameSize(width, height);
        }
        aspectRatio = 1f;// =(float) width / height;
        Matrix.frustumM(ProjMatrix, 0, -aspectRatio, aspectRatio, -1, 1, 5, 7);
        Matrix.setIdentityM(MMatrix, 0);
    }

    @Override
    public void onDrawFrame(final GlFramebufferObject fbo) {

        synchronized (this) {
            if (updateSurface) {
                previewTexture.updateTexImage();
                previewTexture.getTransformMatrix(STMatrix);
                updateSurface = false;
            }
        }

        try {
            if (isNewFilter) {
                if (glFilter != null) {
                    glFilter.setup();
                    glFilter.setFrameSize(fbo.getWidth(), fbo.getHeight());
                }
                isNewFilter = false;
            }

            if (glFilter != null) {
                filterFramebufferObject.enable();
                glViewport(0, 0, filterFramebufferObject.getWidth(), filterFramebufferObject.getHeight());
            }

            GLES20.glClear(GL_COLOR_BUFFER_BIT);

            Matrix.multiplyMM(MVPMatrix, 0, VMatrix, 0, MMatrix, 0);
            Matrix.multiplyMM(MVPMatrix, 0, ProjMatrix, 0, MVPMatrix, 0);
            if (mScale != null && mScale.length > 1) {
                float scaleDirectionX = mFlipHorizontal ? -1 : 1;
                float scaleDirectionY = mFlipVertical ? -1 : 1;
                if (mRotate == 90 || mRotate == 270 || mRotate == -90) {
                    Matrix.scaleM(MVPMatrix, 0, mScale[1] * scaleDirectionX, mScale[0] * scaleDirectionY, 1);
                } else {
                    Matrix.scaleM(MVPMatrix, 0, mScale[0] * scaleDirectionX, mScale[1] * scaleDirectionY, 1);
                }

                Matrix.rotateM(MVPMatrix, 0, -mRotate, 0.f, 0.f, 1.f);
            }

            previewFilter.draw(texName, MVPMatrix, STMatrix, aspectRatio);

            if (glFilter != null) {
                fbo.enable();
                GLES20.glClear(GL_COLOR_BUFFER_BIT);
                glFilter.draw(filterFramebufferObject.getTexName(), fbo);
            }
        } catch (ConcurrentModificationException | IllegalStateException e) {
            DebugLog.e(TAG, e + " " + TAG);
            setUpRenderer();
        } catch (RuntimeException e) {
            DebugLog.e(TAG, e + " " + TAG);
            release();
            setUpRenderer();
        } catch (Exception e) {
            DebugLog.e(TAG, e + " " + TAG);
            setUpRenderer();
        }
    }

    @Override
    public synchronized void onFrameAvailable(final SurfaceTexture previewTexture) {
        updateSurface = true;
        glPreview.requestRender();
    }

    public void release() {
        if (glFilter != null) {
            glFilter.release();
        }
        if (previewTexture != null) {
            previewTexture.release();
        }
    }

    private void setUpRenderer() {
        release();
        onSurfaceCreated(null);
        onSurfaceChanged(width, height);
        setGlFilter(oldGlFilter);
    }

    public GlFilter getCurrentFilter() {
        return glFilter;
    }

    public void updateRotation(int rotate, float[] scale) {
        mRotate = rotate;
        this.mScale = scale;
    }

    public void setFlipVertical(boolean flipVertical,float[] scale) {
        mScale = scale;
        mFlipVertical = flipVertical;
    }

    public void setFlipHorizontal(boolean flipHorizontal,float[] scale) {
        mScale = scale;
        mFlipHorizontal = flipHorizontal;
    }
}
