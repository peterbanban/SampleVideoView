package com.jth.sample.videoview.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by jth on 17-11-18.
 */

public class ToastUtil {

    public static void show(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
