package com.jth.sample.videoview.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jth on 17-11-18.
 */

public class ThreadFactory {

    private static ThreadFactory instance;
    private ExecutorService cachedThreadPool;

    public static ThreadFactory getInstance() {
        if (instance == null) {
            instance = new ThreadFactory();
        }
        return instance;
    }

    public void execute(Runnable runnable) {
        if (cachedThreadPool == null) {
            cachedThreadPool = Executors.newCachedThreadPool();
        }
        cachedThreadPool.execute(runnable);
    }


}
