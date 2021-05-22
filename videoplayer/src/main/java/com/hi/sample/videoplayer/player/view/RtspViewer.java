package com.hi.sample.videoplayer.player.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hi.sample.videoplayer.ijkplayer.widget.IjkVideoView;
import com.hi.sample.videoplayer.recorder.IRecord;

import java.io.IOException;

public class RtspViewer extends IjkVideoView implements IPlayer, IRecord {
    private static final String TAG = "RtspViewer";

    public RtspViewer(@NonNull Context context) {
        super(context);
    }

    public RtspViewer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RtspViewer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RtspViewer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private Uri createURI(String url) {
        return Uri.parse(url);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void play() {
        super.start();
    }

    @Override
    public void prepareDataSource(String url) {
        super.setVideoPath(url);
    }

    @Override
    public void prepareDataSource(String url, boolean autoStartAfterPrepared) throws IllegalArgumentException, IOException {

    }


    @Override
    public void seekTo(long position) {
        super.seekTo((int) position);
    }

    @Override
    public boolean isPlaying() {
        return super.isPlaying();
    }


    @Override
    public void startRecord(String filepath) {
        if (mMediaPlayer != null) {
            mMediaPlayer.startRecord(filepath);
        } else {
            Log.w(TAG, "#startRecord() err: mediaplayer null");
        }
    }

    @Override
    public void stopRecord() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stopRecord();
        } else {
            Log.w(TAG, "#startRecord() err: mediaplayer null");
        }
    }

    @Override
    public void release() {
        super.releasePlayer();
    }

    public void getCurrentFrame(Bitmap bitmap) {
        mMediaPlayer.getCurrentFrame(bitmap);
    }

    public int getVideoWidth() {
        return mMediaPlayer.getVideoWidth();
    }

    public int getVideoHeight() {
        return mMediaPlayer.getVideoHeight();
    }

    public Bitmap getCurrentFrame() {
        int widthFrame = getVideoWidth(); // must be smaller or equal video frame
        int heightFrame = getVideoHeight();  // must be smaller or equal video frame
        Bitmap bitmap = Bitmap.createBitmap(widthFrame, heightFrame, Bitmap.Config.ARGB_8888);
        getCurrentFrame(bitmap);
        return bitmap;
    }

}
