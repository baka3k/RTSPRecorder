package com.hi.recorderijkplayersample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.hi.sample.videoplayer.sample.VideoActivity;
import com.hi.sample.videoplayer.sample.VideoFilterActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
    }
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
    }
    public void btnOpenAddFilterClicked(View view) {
        Intent i = new Intent(this, VideoFilterActivity.class);
        startActivity(i);
    }

    public void btnOpenVideoPlayerClicked(View view) {
        Intent i = new Intent(this, VideoActivity.class);
        startActivity(i);
    }
}