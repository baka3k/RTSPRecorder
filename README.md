# IJKPlayer Sample
At below repository, I add Record & capture frame function to IJKPlayer
https://github.com/baka3k/IjkPlayerRecorder
Someone send email to me that they can not build .so file. 
So, I created sample project for that

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
