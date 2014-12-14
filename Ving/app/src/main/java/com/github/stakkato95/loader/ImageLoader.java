package com.github.stakkato95.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.github.stakkato95.ving.os.LIFOLinkedBlockingDeque;

import java.util.Map;
import java.util.Set;
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
    private final Map<ImageView, String> mRequestsMap;

    private static final String TAG = "image_loading";

    static {
        mExecutorService = new ThreadPoolExecutor(CPU_COUNT,
                CPU_COUNT,
                0,
                TimeUnit.NANOSECONDS,
                new LIFOLinkedBlockingDeque<Runnable>());
    }

    public ImageLoader(@NonNull Context context) {
        mContext = context;
        mMemoryCache = new MemoryCache();
        mImageLoaderCallback = new ImageLoaderCallback();
        mRequestsMap = new ConcurrentHashMap<ImageView, String>();
    }

    public void obtainImage(@NonNull ImageView imageView,@NonNull String url) {

        if (mMemoryCache.containsKey(url)) {
            Log.d(TAG, "image" + url + " is obtained from lruCache");
            Bitmap targetBmp = mMemoryCache.get(url);
            setBmpToView(targetBmp, url);
        } else {
            synchronized (mRequestsMap) {
                if (!mRequestsMap.containsValue(url)) {
                    //loading of the image is required
                    Handler mHandler = new Handler(Looper.myLooper());
                    mExecutorService.execute(new ImageLoadingThread(mContext, url, mImageLoaderCallback, mHandler));
                }
                mRequestsMap.put(imageView, url);
            }
        }


    }

    private void setBmpToView(Bitmap bmp, String url) {

        Set<Map.Entry<ImageView, String>> keyValuePair = mRequestsMap.entrySet();

        for (Map.Entry<ImageView, String> pair : keyValuePair) {

            if(pair.getValue().equals(url)) {
                ImageView targetView = pair.getKey();

                if (targetView != null) {
                    targetView.setImageBitmap(bmp);
                    Log.d(TAG, "image" + url + " is laid");
                }

                mRequestsMap.remove(targetView);
                break;
            }
        }

    }


    private class ImageLoaderCallback implements LoaderCallback {
        @Override
        public void onLoadingFinished(final Bitmap bmp, final String url) {

            synchronized (mMemoryCache) {
                mMemoryCache.put(url, bmp);
                setBmpToView(bmp, url);
            }

        }
    }

}
