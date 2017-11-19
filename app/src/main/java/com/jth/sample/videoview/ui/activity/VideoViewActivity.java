package com.jth.sample.videoview.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.MediaController;

import com.jth.sample.videoview.R;
import com.jth.sample.videoview.ui.view.MyMediaController;
import com.jth.sample.videoview.ui.view.MyVideoView;

/**
 * MyVideoView 取自android.widget.VideoView源码
 * MyMediaController 取自android.widget.MediaController源码
 */
public class VideoViewActivity extends BaseActivity implements View.OnClickListener {

    private MyVideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video_view);
        mVideoView = (MyVideoView) findViewById(R.id.video_view);
        String path = getIntent().getStringExtra("path");
        //设置操作控件
        mVideoView.setMediaController(new MyMediaController(VideoViewActivity.this));
        mVideoView.setVideoPath(path);
        mVideoView.start();
        mVideoView.requestFocus();
    }
    @Override
    public void onClick(View v) {

    }
}
