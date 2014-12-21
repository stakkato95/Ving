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
public class FileSavingThread extends Thread {

    private final String mUrl;
    private final DiskCache mDiskCache;
    private final Bitmap mBmp;
    private final LoaderCallback mLoaderCallback;
    private final Handler mHandler;

    public FileSavingThread(String url, Bitmap bmp, LoaderCallback loaderCallback, Handler handler, DiskCache diskCache) {
        mHandler = handler;
        mUrl = url;
        mBmp = bmp;
        mDiskCache = diskCache;
        mLoaderCallback = loaderCallback;
    }

    @Override
    public void run() {
        try {
            mDiskCache.put(mUrl, mBmp);
        } catch (Exception e) {
            performReceivedError(e);
        }
    }

    private void performReceivedError(final Exception e) {
        Log.d(ImageLoader.TAG, "file saving in " + currentThread().getId() + " thread finished with error");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mLoaderCallback.onReceivedError("writing_error" + mUrl, e);
            }
        });
    }

}
