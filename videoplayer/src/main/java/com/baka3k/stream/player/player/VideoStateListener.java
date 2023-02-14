package com.baka3k.stream.player.player;

public interface VideoStateListener {
    void onVideoSizeChanged(int width, int height);

    void onPrepared(long maxDuration);

}