package com.jth.sample.videoview.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by jth on 17-11-18.
 */

public class BaseActivity extends Activity {

    protected Context mContext;
    protected Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                onMessageCallback(msg);
                return true;
            }
        });

    }

    protected void onMessageCallback(Message msg) {

    }
}
