package com.hi.sample.videoplayer.utils;

import android.graphics.Bitmap;
import android.opengl.GLException;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;
import java.io.*;
import java.nio.IntBuffer;

public final class BitmapUtils {
    private static final String TAG = "BitmapUtils";
    private BitmapUtils() {
    }
    public static boolean saveBitmap(Bitmap bitmap, String path) {
        if (bitmap == null || path == null || path.isEmpty()) {
            Log.e(TAG, "#saveBitmap() err bitmap or path is null/empty");
            return false;
        } else {
            boolean status = false;
            OutputStream fOut = null;
            File file = new File(path);
            try {
                fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.flush(); // Not really required
                status = true;
            } catch (FileNotFoundException e) {
                status = false;
                Log.e(TAG, "#saveBitmap() err FileNotFoundException");
            } catch (IOException e) {
                status = false;
                Log.e(TAG, "#saveBitmap() err IOException");
            } finally {
                if (fOut != null) {
                    try {
                        fOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return status;
        }
    }
}
