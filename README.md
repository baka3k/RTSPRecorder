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
    implementation "com.github.baka3k:RTSPRecorder:1.0.1"
}
```
## Usage
Capture Video Frame
```Java
Bitmap bitmap = mVideoView.getCurrentFrame();
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
Apache licensed Copyright <2021> baka3k@gmail.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
