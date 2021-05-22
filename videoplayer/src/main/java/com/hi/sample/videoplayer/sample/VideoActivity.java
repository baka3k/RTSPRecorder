package com.hi.sample.videoplayer.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
    private static final String mVideoPath = "rtsp://wowzaec2demo.streamlock.net/vod/mp4";

    private RtspViewer mVideoView;
    private ImageView mImageView;
    private TextView mTextViewUrl;
    private Button mBtnRecord;

    private String mOutPutRecord;
    private String mOutPutCaptureFrame;
    private boolean mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_ijk);
        mImageView = findViewById(R.id.imageView);
        mTextViewUrl = findViewById(R.id.edtUrl);
        mBtnRecord = findViewById(R.id.btnRecord);
        mVideoView = findViewById(R.id.video_view);

        mTextViewUrl.setText(mVideoPath);
        if (!TextUtils.isEmpty(mVideoPath)) {
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
        if (mBtnRecord.getText().toString().equals("Recording")) {
            mBtnRecord.setText("Start Record");
            mVideoView.stopRecord();
            Toast.makeText(getApplicationContext(), "" + mOutPutRecord, Toast.LENGTH_LONG).show();
        } else {
            File f = new File(mOutPutRecord);
            if (f.exists()) {
                f.delete();
            }
            mBtnRecord.setText("Recording");
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
        if (!isAllowClicked()) {
            return;
        }
        if (!mVideoView.isPlaying()) {
            Toast.makeText(getApplicationContext(), "Please waitting - you just get frame while video is playing!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                Bitmap bitmap = mVideoView.getCurrentFrame();
                if (bitmap != null) {
                    File f = new File(mOutPutCaptureFrame);
                    if (f.exists()) {
                        f.delete();
                    }
                    BitmapUtils.saveBitmap(bitmap, mOutPutCaptureFrame); // you can try to save bitmap
                } else {
                    Log.w("VideoActivity", "bitmap null");
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if (bitmap != null) {
                    mImageView.setImageBitmap(bitmap);
                    Toast.makeText(getApplicationContext(), "Frame saved: " + mOutPutCaptureFrame, Toast.LENGTH_LONG).show();
                } else {
                    mImageView.setImageBitmap(null);
                    Toast.makeText(getApplicationContext(), "err, can not save frame" + mOutPutCaptureFrame, Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();
    }
    /*********************************************************************************************/
    /*********************************************************************************************/
    /*********************************************************************************************/
    /*********************************************************************************************/
    private long PREVIOUS_TIME_CLICKED = 0L;

    private boolean isAllowClicked() {
        long spentTime = System.currentTimeMillis() - PREVIOUS_TIME_CLICKED;
        if (spentTime >= 300) {
            PREVIOUS_TIME_CLICKED = System.currentTimeMillis();
            return true;
        }
        return false;
    }
}
