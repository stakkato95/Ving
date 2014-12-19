package com.github.stakkato95.loader.thread;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import com.github.stakkato95.loader.ImageLoader;
import com.github.stakkato95.loader.LoaderCallback;
import com.github.stakkato95.loader.assist.ImageLoaderAssistant;
import com.github.stakkato95.ving.processing.BitmapProcessor;
import com.github.stakkato95.ving.source.HttpDataSource;

import java.io.InputStream;

/**
 * Created by Artyom on 14.12.2014.
 */
public class MemoryLoadingThread extends Thread {

    private final Handler mHandler;
    private final String mUrl;
    private final HttpDataSource mDataSource;
    private final LoaderCallback mLoaderCallback;
    private final BitmapProcessor mBitmapProcessor;
    private static final String TAG = ImageLoader.class.getSimpleName();

    public MemoryLoadingThread(String url, LoaderCallback loaderCallback, Handler handler, HttpDataSource httpDataSource, BitmapProcessor bitmapProcessor) {
        mUrl = url;
        mDataSource = httpDataSource;
        mLoaderCallback = loaderCallback;
        mHandler = handler;
        mBitmapProcessor = bitmapProcessor;
    }

    @Override
    public void run() {

        Log.d(TAG, currentThread().getId() + " thread is launched");

        InputStream inputStream = null;
        Bitmap bmp = null;
        try {
            inputStream = mDataSource.getResult(mUrl);
            bmp = mBitmapProcessor.process(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ImageLoaderAssistant.closeStream(inputStream);
        }

        loadingInThreadFinished(bmp);
    }

    private void loadingInThreadFinished(final Bitmap bmp) {
        Log.d(TAG,"loading in " + currentThread().getId() + " thread finished");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mLoaderCallback.onLoadingFinished(bmp, mUrl);
            }
        });
    }

}
