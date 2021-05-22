package com.hi.sample.videoplayer.filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLException;
import android.opengl.GLSurfaceView;
import android.view.Surface;

import com.hi.sample.videoplayer.filter.render.GPUPlayerRenderer;
import com.hi.sample.videoplayer.ijkplayer.widget.IRenderView;
import com.hi.sample.videoplayer.ijkplayer.widget.MeasureHelper;
import com.daasuu.gpuv.composer.FillMode;
import com.daasuu.gpuv.egl.filter.GlFilter;

import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;


public class GPUPlayerView extends GLSurfaceView {
    private static final String TAG = "GPUPlayerView";


    public interface GPUPlayerViewListener {
        void onSurfaceCreated(Surface surface);
    }

    private final GPUPlayerRenderer renderer;
    private GPUPlayerViewListener mGPUPlayerViewListener;
    private MeasureHelper mMeasureHelper;
    private int mVideoWidth;
    private int mVideoHeight;
    private int mDegree = 0;

    public int getVideoWidth() {
        return mVideoWidth;
    }

    public int getVideoHeight() {
        return mVideoHeight;
    }


    private final GPUPlayerRenderer.GPUPlayerRendererListener mGPUPlayerRendererListener = surface -> {
        if (mGPUPlayerViewListener != null) {
            mGPUPlayerViewListener.onSurfaceCreated(surface);
        }
    };


    public GPUPlayerView(Context context, GPUPlayerViewListener listener) {
        super(context, null);
        mGPUPlayerViewListener = listener;
        mMeasureHelper = new MeasureHelper(this);
        renderer = new GPUPlayerRenderer(this, mGPUPlayerRendererListener);
        setRenderer(renderer);
    }

    public void setGlFilter(GlFilter glFilter) {
        renderer.setGlFilter(glFilter);
    }

    public int getDegree() {
        return mDegree;
    }

    @Override
    public void onPause() {
        super.onPause();
        renderer.release();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMeasureHelper.doMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mMeasureHelper.getMeasuredWidth(), mMeasureHelper.getMeasuredHeight());
    }

    @Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if ((changed && mVideoWidth > 0 && mVideoHeight > 0) || mMeasureHelper.getVideoRotation() != mDegree) {
            mMeasureHelper.setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);
            mMeasureHelper.setVideoRotation(mDegree);

            float[] scale = getScale();
            renderer.updateRotation(mDegree, scale);
        }
    }

    public void setVideoSize(int videoWidth, int videoHeight, int degree) {
        if (videoWidth > 0 && videoHeight > 0) {
            this.mDegree = degree;
            this.mVideoWidth = videoWidth;
            this.mVideoHeight = videoHeight;
            mMeasureHelper.setVideoSize(videoWidth, videoHeight);
            getHolder().setFixedSize(videoWidth, videoHeight);
            requestLayout();
        }
    }


    public void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen) {
        if (videoSarNum > 0 && videoSarDen > 0) {
            mMeasureHelper.setVideoSampleAspectRatio(videoSarNum, videoSarDen);
            requestLayout();
        }
    }

    public void setDegree(int degree) {
        mDegree = degree;
    }

    private float[] getScale() {
        float[] scale;
        if (mDegree == 90 || mDegree == 270 || mDegree == -90) {
            scale = FillMode.getScaleAspectFit(mDegree, mVideoHeight, mVideoWidth, getWidth(), getHeight());
        } else {
            scale = FillMode.getScaleAspectFit(mDegree, mVideoWidth, mVideoHeight, getWidth(), getHeight());
        }
        return scale;
    }

    public void setFlipVertical(boolean flipVertical) {
        float[] scale = getScale();
        renderer.setFlipVertical(flipVertical, scale);
    }

    public void setFlipHorizontal(boolean flipHorizontal) {
        float[] scale = getScale();
        renderer.setFlipHorizontal(flipHorizontal, scale);
    }

    public GlFilter getCurrentFilter() {
        return renderer.getCurrentFilter();
    }
}
