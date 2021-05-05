# IJKPlayer Sample
In previous time, I added 2 functions: RecordVideo & Capture Frame Video to IJKPlayer
(https://github.com/baka3k/IjkPlayerRecorder)

Someone sent email to me that they can not build .so file, because they met issue with environment

So, I created sample project(included .so file) - Hope this help

Good luck !!!

## Table of contents

- [Features](#features)
- [Requirements](#requirements)
- [Usage](#usage)
- [Sample](#sample)
- [Authors](#authors)
- [License](#license)

## Features

- [x] RTSP Player By IJKPlayer
- [x] Support Get Frame Video
- [x] Support Record Video
- [x] Support Add Filter Video
- [ ] Record Video withFilter: Not at this time

## Requirements

- Android 5.1+


## Gradle
build.gradle:
```groovy
allprojects {
    repositories {
        .....
        maven { url 'https://jitpack.io' } // add this line to build.gradle
    }
}
```
Add the dependency
```groovy
dependencies {
    implementation "com.github.baka3k:RTSPRecorder:1.0"
}
```
## Usage
Capture Video Frame
```Java
// 1280 x 720 is frame size video
Bitmap bitmap = Bitmap.createBitmap(1280, 720, Bitmap.Config.ARGB_8888);
mVideoView.getCurrentFrame(bitmap);
```
RecordVideo
```Java
mVideoView.startRecord(mOutPutRecord);
//then MUST call stop Record
mVideoView.stopRecord();
```


Add Filter to video:
(Many thanks to MasayukiSuda https://github.com/MasayukiSuda/GPUVideo-android)
```Java
GlHazeFilter hazeFilter = new GlHazeFilter();
hazeFilter.setSlope(-0.8f);
contentView.setFilter(hazeFilter);
```

## Sample

![Output sample](https://github.com/baka3k/RecorderIJKPlayerSample/blob/master/sample.gif)

refer sample in below package
```Java
com.hi.sample.videoplayer.sample.VideoActivity
com.hi.sample.videoplayer.sample.VideoFilterActivity
```
## Authors

baka3k@gmail.com

## License
Apache licensed
