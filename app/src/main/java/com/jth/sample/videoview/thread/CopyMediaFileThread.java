package com.jth.sample.videoview.thread;

import android.content.Context;

import com.jth.sample.videoview.util.FileUtil;

/**
 * Created by jth on 17-11-18.
 */

public class CopyMediaFileThread implements Runnable {

    public interface CopyMediaFileListener {

        void onMediaCopyResult(boolean result, String filePath);

    }

    private Context mContext;
    private String assetPath;
    private String diskPath;
    private CopyMediaFileListener listener;

    public CopyMediaFileThread(Context context, String assetPath, String diskPath, CopyMediaFileListener listener) {
        mContext = context;
        this.assetPath = assetPath;
        this.diskPath = diskPath;
        this.listener = listener;
    }

    @Override
    public void run() {
        boolean result = FileUtil.copyFileAssetToDisk(mContext, assetPath, diskPath);
        if (listener != null) {
            listener.onMediaCopyResult(result, diskPath);
        }
    }

}
