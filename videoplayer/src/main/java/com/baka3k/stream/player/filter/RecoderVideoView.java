package com.baka3k.stream.player.filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLException;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGL;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;

public class RecoderVideoView extends FilterVideoView {
    public RecoderVideoView(@NonNull Context context) {
        super(context);
    }

    public RecoderVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecoderVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void captureBitmap(BitmapReadyCallbacks bitmapReadyCallbacks) {
        if (mGPUPlayerView == null) {
            bitmapReadyCallbacks.onBitmapReady(null);
            return;
        }
        mGPUPlayerView.queueEvent(() -> {
            EGL egl = EGLContext.getEGL();
            if (egl instanceof EGL10) {
                EGLContext gl = ((EGL10) egl).eglGetCurrentContext();
                if (gl instanceof GL10) {
                    Bitmap snapShotBitmap = createBitmapFromGLSurface(mGPUPlayerView.getMeasuredWidth(), mGPUPlayerView.getMeasuredHeight(), (GL10) gl);
                    bitmapReadyCallbacks.onBitmapReady(snapShotBitmap);
                    return;
                }
            }
            bitmapReadyCallbacks.onBitmapReady(null);
        });
    }

    private Bitmap createBitmapFromGLSurface(int w, int h, GL10 gl)
            throws OutOfMemoryError {
        int bitmapBuffer[] = new int[w * h];
        int bitmapSource[] = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);

        try {
            gl.glReadPixels(0, 0, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
            int offset1, offset2;
            for (int i = 0; i < h; i++) {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++) {
                    int texturePixel = bitmapBuffer[offset1 + j];
                    int blue = (texturePixel >> 16) & 0xff;
                    int red = (texturePixel << 16) & 0x00ff0000;
                    int pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            return null;
        }
        return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
    }
}
