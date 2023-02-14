package com.baka3k.stream.player.player.view;

import java.io.IOException;

public interface IPlayer {
    void pause();

    void play();

    void prepareDataSource(String url) throws IllegalArgumentException, IOException;

    void prepareDataSource(String url, boolean autoStartAfterPrepared)  throws IllegalArgumentException, IOException;

    void release();
    
    void seekTo(long position);

    boolean isPlaying();
}
