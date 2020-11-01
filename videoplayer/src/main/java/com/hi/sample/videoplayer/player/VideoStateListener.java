package com.hi.sample.videoplayer.player;

public interface VideoStateListener {
    void onVideoSizeChanged(int width, int height);

    void onPrepared(long maxDuration);

}