package com.hi.sample.videoplayer.sample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hi.sample.videoplayer.R;
import com.hi.sample.videoplayer.filter.FilterVideoView;
import com.daasuu.gpuv.egl.filter.GlBilateralFilter;
import com.daasuu.gpuv.egl.filter.GlFilter;
import com.daasuu.gpuv.egl.filter.GlHazeFilter;
import com.daasuu.gpuv.egl.filter.GlLookUpTableFilter;

public class VideoPlayerFragment extends Fragment {
    private static final String mVideoPath = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";
//    private static final String mVideoPath = "rtsp://wowzaec2demo.streamlock.net/vod/mp4";
    private FilterVideoView contentView;
    boolean isRTSPSource = true;
    private Button btnChangeSource;
    private Button btnFilter1;
    private Button btnFilter2;
    private Button btnFilter3;
    private Button btnFilter4;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonFilterOnCliked(v);
        }
    };

    public static VideoPlayerFragment newInstance() {
        return new VideoPlayerFragment();
    }

    public VideoPlayerFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contentView = view.findViewById(R.id.contentView);
        btnChangeSource = view.findViewById(R.id.btnCHangeSource);
        btnFilter1 = view.findViewById(R.id.btnFilter1);
        btnFilter2 = view.findViewById(R.id.btnFilter2);
        btnFilter3 = view.findViewById(R.id.btnFilter3);
        btnFilter4 = view.findViewById(R.id.btnFilter4);

        btnChangeSource.setOnClickListener(mOnClickListener);
        btnFilter1.setOnClickListener(mOnClickListener);
        btnFilter2.setOnClickListener(mOnClickListener);
        btnFilter3.setOnClickListener(mOnClickListener);
        btnFilter4.setOnClickListener(mOnClickListener);

        contentView.prepareDataSource(mVideoPath, true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player_ijk_filter, container, false);
    }

    private void buttonFilterOnCliked(View v) {
        if (v.getId() == R.id.btnFilter1) {
            contentView.setFilter(new GlBilateralFilter());

        }
        if (v.getId() == R.id.btnFilter2) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_test);
            contentView.setFilter(new GlLookUpTableFilter(bitmap));
        }
        if (v.getId() == R.id.btnFilter3) {
            GlHazeFilter hazeFilter = new GlHazeFilter();
            hazeFilter.setSlope(-0.8f);
            contentView.setFilter(hazeFilter);
        }
        if (v.getId() == R.id.btnFilter4) {
            contentView.setFilter(new GlFilter());
        }
        if (v.getId() == R.id.btnCHangeSource) {
            if (isRTSPSource) {
                contentView.prepareDataSource(mVideoPath, true);
            } else {
                contentView.prepareDataSource("rtsp://wowzaec2demo.streamlock.net/vod/mp4", true);
            }
            isRTSPSource = !isRTSPSource;
        }
    }
}
