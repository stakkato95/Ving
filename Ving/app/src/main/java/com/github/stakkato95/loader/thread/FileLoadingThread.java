package com.github.stakkato95.loader.thread;

import android.graphics.Bitmap;
import android.util.Log;

import com.github.stakkato95.loader.ImageLoader;
import com.github.stakkato95.loader.LoaderCallback;
import com.github.stakkato95.loader.cache.DiskCache;

import android.os.Handler;

/**
 * Created by Artyom on 18.12.2014.
 */
public class FileLoadingThread extends Thread {

    private final Handler mHandler;
    private final String mUrl;
    private final DiskCache mDiskCache;
    private final LoaderCallback mLoaderCallback;
    private static final String TAG = ImageLoader.class.getSimpleName();

    public FileLoadingThread(String url, LoaderCallback loaderCallback, Handler handler, DiskCache diskCache) {
        mHandler = handler;
        mUrl = url;
        mDiskCache = diskCache;
        mLoaderCallback = loaderCallback;
    }


    @Override
    public void run() {
        Bitmap bmp = mDiskCache.get(mUrl);
        loadingInThreadFinished(bmp);
    }

    private void loadingInThreadFinished(final Bitmap bmp) {
        Log.d(TAG, "loading from file in " + currentThread().getId() + " thread finished");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mLoaderCallback.onLoadingFinished(bmp, mUrl);
            }
        });
    }
}
