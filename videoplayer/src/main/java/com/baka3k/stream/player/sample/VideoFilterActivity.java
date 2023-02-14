package com.baka3k.stream.player.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hi.sample.videoplayer.R;

public class VideoFilterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_ijk_filter);
        checkPermission();
        getSupportFragmentManager().beginTransaction().replace(R.id.contentView, VideoPlayerFragment.newInstance()).commit();
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

}
