package com.hi.sample.videoplayer.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hi.sample.videoplayer.R;

import com.hi.sample.videoplayer.player.view.RtspViewer;
import com.hi.sample.videoplayer.utils.BitmapUtils;

import java.io.File;

public class VideoActivity extends AppCompatActivity {
    private static final String TAG = "VideoActivity";

    private static final String mVideoPath = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
    private String mOutPutRecord;
    private String mOutPutCaptureFrame;
    private RtspViewer mVideoView;
    private boolean mBackPressed;
    private Button btnRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_ijk);
        btnRecord = findViewById(R.id.btnRecord);
        mVideoView = findViewById(R.id.video_view);
        if (mVideoPath != null) {
            mVideoView.setVideoPath(mVideoPath);
        } else {
            Log.e(TAG, "Null Data Source\n");
            finish();
            return;
        }
        mVideoView.start();
        mOutPutRecord = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/a.mp4";
        mOutPutCaptureFrame = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/a.jpg";
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermission();
    }

    public void buttonRecordOnCliked(View v) {
        if (btnRecord.getText().toString().equals("Recording")) {
            btnRecord.setText("Start Record");
            mVideoView.stopRecord();

        } else {
            File f = new File(mOutPutRecord);
            if (f.exists()) {
                f.delete();
            }
            btnRecord.setText("Recording");
            mVideoView.startRecord(mOutPutRecord);
        }
    }

    @Override
    public void onBackPressed() {
        mBackPressed = true;
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBackPressed) {
            mVideoView.stopPlayback();
            mVideoView.release(true);

        }
    }

    public void buttonCaptureOnClicked(View view) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                Bitmap bitmap = Bitmap.createBitmap(1280, 720, Bitmap.Config.ARGB_8888);
                boolean result = false;
                mVideoView.getCurrentFrame(bitmap);

                if (bitmap != null) {
                    File f = new File(mOutPutCaptureFrame);
                    if (f.exists()) {
                        f.delete();
                    }
                    result = BitmapUtils.saveBitmap(bitmap, mOutPutCaptureFrame);
                } else {
                    Log.w("VideoActivity", "bitmap null");
                }
                return result;
            }

            @Override
            protected void onPostExecute(Boolean aVoid) {
                super.onPostExecute(aVoid);
                if (aVoid) {
                    Toast.makeText(getApplicationContext(), "file save to " + mOutPutCaptureFrame, Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(getApplicationContext(), "err, can not save frame" + mOutPutCaptureFrame, Toast.LENGTH_SHORT);
                }

            }
        }.execute();
    }
}
