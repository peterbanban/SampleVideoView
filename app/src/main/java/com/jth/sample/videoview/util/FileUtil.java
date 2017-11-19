package com.jth.sample.videoview.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jth on 17-11-18.
 */

public class FileUtil {

    //从assets拷贝文件到磁盘
    public static boolean copyFileAssetToDisk(Context context, String assetPath, String diskPath) {
        AssetManager assetManager = context.getAssets();
        InputStream inStream = null;
        try {
            inStream = assetManager.open(assetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //文件不存在
        if (inStream == null) return false;

        File diskFile = new File(diskPath);
        File parent = diskFile.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        if (diskFile.exists()) {
            diskFile.delete();
        }
        return copyInputStreamToFile(inStream, diskFile);
    }

    //流对象拷贝到文件对象
    private static boolean copyInputStreamToFile(InputStream inStream, File targetFile) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fos == null) return false;
        if (inStream == null) return false;

        byte[] buffer = new byte[1024];
        int byteCount;
        try {
            while ((byteCount = inStream.read(buffer)) != -1) {// 循环从输入流读取
                // buffer字节
                fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
            }
            fos.flush();// 刷新缓冲区
            inStream.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
