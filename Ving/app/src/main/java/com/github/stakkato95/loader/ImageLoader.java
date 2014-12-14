package com.github.stakkato95.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.github.stakkato95.ving.os.LIFOLinkedBlockingDeque;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Artyom on 11.12.2014.
 */
public class ImageLoader {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private static ExecutorService mExecutorService;
    private final Context mContext;
    private final MemoryCache mMemoryCache;
    private final ImageLoaderCallback mImageLoaderCallback;
    private final Map<String,ImageView> mRequestsMap;

    private static final String TAG = "image_loading";

    static {
        mExecutorService = new ThreadPoolExecutor(CPU_COUNT,
                CPU_COUNT,
                0,
                TimeUnit.NANOSECONDS,
                new LIFOLinkedBlockingDeque<Runnable>());
    }

    public ImageLoader(Context context) {
        mContext = context;
        mMemoryCache = new MemoryCache();
        mImageLoaderCallback = new ImageLoaderCallback();
        mRequestsMap = new ConcurrentHashMap<String, ImageView>();
    }

    public void obtainImage(@NonNull ImageView imageView,@NonNull String url) {

        imageView.setImageBitmap(null);
        imageView.setTag(url);
        Handler mHandler = new Handler(Looper.myLooper());

        synchronized (mMemoryCache) {
            Bitmap bmp = mMemoryCache.get(url);
            if (bmp != null) {
                setBmpToView(bmp, url, imageView);
            } else {
                mExecutorService.execute(new ImageLoadingThread(mContext, url, mImageLoaderCallback, mHandler, imageView));
            }
        }

    }

    private void setBmpToView(Bitmap bmp, String url, ImageView imageView) {
        Log.d(TAG, "image" + url + " is tried to be laid");
        if (imageView.getTag().equals(url)) {
            imageView.setImageBitmap(bmp);
            Log.d(TAG, "image" + url + " is laid");
        }
    }


    private class ImageLoaderCallback implements LoaderCallback {
        @Override
        public void onLoadingFinished(final Bitmap bmp, final String url, final ImageView imageView) {

            synchronized (mMemoryCache) {
                mMemoryCache.put(url, bmp);
                setBmpToView(bmp, url, imageView);
            }

        }
    }

}
