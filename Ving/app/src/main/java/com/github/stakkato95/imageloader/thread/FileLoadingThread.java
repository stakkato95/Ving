package com.github.stakkato95.imageloader.thread;

import android.graphics.Bitmap;
import android.util.Log;

import com.github.stakkato95.imageloader.ImageLoader;
import com.github.stakkato95.imageloader.LoaderCallback;
import com.github.stakkato95.imageloader.cache.DiskCache;

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
        Bitmap bmp = null;

        try {
            bmp = mDiskCache.get(mUrl);
        } catch (Exception e) {
            performReceivedError(e);
        }

        //if bitmap is null we mustn't finish loading correct
        if (bmp != null) {
            performLoadingFinished(bmp);
        }
    }

    private void performLoadingFinished(final Bitmap bmp) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mLoaderCallback.onLoadingFinished(bmp, mUrl);
            }
        });
    }

    private void performReceivedError(final Exception e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mLoaderCallback.onReceivedError(mUrl, e);
            }
        });
    }

}
