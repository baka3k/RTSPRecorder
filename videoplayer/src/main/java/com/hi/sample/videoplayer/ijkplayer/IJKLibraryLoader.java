package com.hi.sample.videoplayer.ijkplayer;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public final class IJKLibraryLoader {
    private IJKLibraryLoader() {
    }

    public static void releaseNativeLib() {
        IjkMediaPlayer.native_profileEnd();
    }

    public static void loadNativeLib() {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
    }
}
