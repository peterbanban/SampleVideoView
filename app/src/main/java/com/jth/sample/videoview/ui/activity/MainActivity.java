package com.jth.sample.videoview.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.jth.sample.videoview.R;
import com.jth.sample.videoview.thread.CopyMediaFileThread;
import com.jth.sample.videoview.thread.ThreadFactory;
import com.jth.sample.videoview.util.ToastUtil;

import java.io.File;

public class MainActivity extends BaseActivity implements View.OnClickListener, CopyMediaFileThread.CopyMediaFileListener {

    private static final int HANDLER_WHAT_COPY_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_play).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play:
                //videoView 不支持读取assets目录，拷贝到sd卡 /sdcard/Android/data/<PackageName>/files/cache/media/下播放
                String assetPath = "media/zbazls.mp4";//zbazls
               String diskPath = mContext.getExternalFilesDir("cache/media") + File.separator + "zbazls.mp4";//zbazls
                //String diskPath="http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4";
                File diskFile = new File(diskPath);
                if (!diskFile.exists()) {
                    CopyMediaFileThread thread = new CopyMediaFileThread(mContext, assetPath, diskPath, this);
                    ThreadFactory.getInstance().execute(thread);
                } else {
                    openVideo(diskPath);
                }

                break;
        }
    }

    private void openVideo(String path) {
        Intent intent = new Intent(mContext, VideoViewActivity.class);
        intent.putExtra("path", path);
        startActivity(intent);
    }

    @Override
    public void onMediaCopyResult(boolean result, String filePath) {
        Message msg = new Message();
        msg.what = HANDLER_WHAT_COPY_FILE;
        msg.arg1 = result ? 1 : 0;
        msg.obj = filePath;
        mHandler.sendMessage(msg);
    }

    @Override
    protected void onMessageCallback(Message msg) {
        super.onMessageCallback(msg);
        switch (msg.what) {
            case HANDLER_WHAT_COPY_FILE:
                String path = (String) msg.obj;
                if (msg.arg1 == 1) {
                    openVideo(path);
                } else {
                    ToastUtil.show(mContext, "file not exists!");
                }
                break;
        }
    }
}
