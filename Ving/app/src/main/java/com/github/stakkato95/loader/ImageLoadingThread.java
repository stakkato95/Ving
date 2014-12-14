package com.github.stakkato95.loader;

import android.content.Context;
import android.graphics.Bitmap;

import com.github.stakkato95.ving.processing.BitmapProcessor;
import com.github.stakkato95.ving.source.HttpDataSource;

import java.io.IOException;
import java.io.InputStream;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Artyom on 14.12.2014.
 */
public class ImageLoadingThread extends Thread {

    private final Handler mHandler;
    private final String mUrl;
    private final HttpDataSource mDataSource;
    private final LoaderCallback mLoaderCallback;
    private final BitmapProcessor mBitmapProcessor;
    private static final String TAG = "image_loading";
    private final ImageView mImageView;

    public ImageLoadingThread(Context context, String url, LoaderCallback loaderCallback, Handler handler, ImageView imageView) {
        mUrl = url;
        mDataSource = HttpDataSource.get(context);
        mLoaderCallback = loaderCallback;
        mHandler = handler;
        mBitmapProcessor = new BitmapProcessor();
        mImageView = imageView;
    }

    @Override
    public void run() {

        Log.d(TAG, currentThread().getId() + " thread is launched");

        InputStream inputStream = null;
        try {
            inputStream = mDataSource.getResult(mUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bitmap bmp = null;
        try {
            bmp = mBitmapProcessor.process(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeStream(inputStream);

        if (bmp != null) {
            loadingInThreadFinished(bmp);
        }

    }

    private void loadingInThreadFinished(final Bitmap bmp) {
        Log.d(TAG,"loading in " + currentThread().getId() + " thread finished");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mLoaderCallback.onLoadingFinished(bmp, mUrl, mImageView);
            }
        });
    }

    private void closeStream(final InputStream inputStream) {
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
