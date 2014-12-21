package com.github.stakkato95.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.github.stakkato95.loader.cache.DiskCache;
import com.github.stakkato95.loader.cache.MemoryCache;
import com.github.stakkato95.loader.thread.FileLoadingThread;
import com.github.stakkato95.loader.thread.FileSavingThread;
import com.github.stakkato95.loader.thread.MemoryLoadingThread;
import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.os.LIFOLinkedBlockingDeque;
import com.github.stakkato95.ving.processing.BitmapProcessor;
import com.github.stakkato95.ving.source.HttpDataSource;

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
    private final HttpDataSource mDataSource;
    private final BitmapProcessor mBitmapProcessor;
    private final DiskCache mDiskCache;
    private final Handler mHandler;

    //resources for loading images & images with errors
    private final int mLoadingImageResourceId;
    private final int mErrorImageResourceId;

    public static final String TAG = ImageLoader.class.getSimpleName();

    static {
        mExecutorService = new ThreadPoolExecutor(CPU_COUNT,
                CPU_COUNT,
                0,
                TimeUnit.NANOSECONDS,
                new LIFOLinkedBlockingDeque<Runnable>());

    }

    public ImageLoader(@NonNull Context context, int diskCacheSize, int loadingImageResourceId, int errorImageResourceId) {
        mContext = context;
        mMemoryCache = new MemoryCache(context);
        mImageLoaderCallback = new ImageLoaderCallback();
        mRequestsMap = new ConcurrentHashMap<ImageView, String>();
        mDataSource = HttpDataSource.get(context);
        mBitmapProcessor = new BitmapProcessor();
        mDiskCache = new DiskCache(context, diskCacheSize);
        mHandler = new Handler(Looper.myLooper());
        mLoadingImageResourceId = loadingImageResourceId;
        mErrorImageResourceId = errorImageResourceId;
    }

    public void obtainImage(@NonNull ImageView imageView, @NonNull String url) {

        imageView.setImageResource(mLoadingImageResourceId);

        if (mMemoryCache.containsKey(url)) {
            Log.d(TAG, "image " + url + " is obtained from lruCache");
            Bitmap targetBmp = mMemoryCache.get(url);

            //if request isn't added the image won't be displayed
            mRequestsMap.put(imageView, url);
            setBmpToView(targetBmp, url);
        } else {

            synchronized (mRequestsMap) {
                if (mDiskCache.containsKey(url) && !mRequestsMap.containsValue(url)) {

                    //image is in DiskCache -> do loading from DiskCache
                    mExecutorService.execute(new FileLoadingThread(url, mImageLoaderCallback, mHandler, mDiskCache));
                } else {

                    //image isn't in DiskCache -> check for necessity of lading from the network
                    if (!mRequestsMap.containsValue(url)) {
                        //loading of the image from the network is required
                        mExecutorService.execute(new MemoryLoadingThread(url, mImageLoaderCallback, mHandler, mDataSource, mBitmapProcessor));
                    }
                }
            }
            mRequestsMap.put(imageView, url);
        }


    }

    private void setBmpToView(Bitmap bmp, String url) {

        Set<Map.Entry<ImageView, String>> keyValuePair = mRequestsMap.entrySet();

        for (Map.Entry<ImageView, String> pair : keyValuePair) {

            if (pair.getValue().equals(url)) {
                ImageView targetView = pair.getKey();

                if (targetView != null) {
                    targetView.setImageBitmap(bmp);
                    Log.d(TAG, "image " + url + " is laid");
                }

                mRequestsMap.remove(targetView);
                break;
            }
        }

    }

    private void setBmpToView(Integer resourceId, String url) {

        Set<Map.Entry<ImageView, String>> keyValuePair = mRequestsMap.entrySet();

        for (Map.Entry<ImageView, String> pair : keyValuePair) {

            if (pair.getValue().equals(url)) {
                ImageView targetView = pair.getKey();

                if (targetView != null) {
                    targetView.setImageResource(resourceId);
                    Log.d(TAG, "image " + url + " is laid");
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
                if (!mMemoryCache.containsKey(url)) {
                    //if image isn't in MemoryCache, put it there
                    mMemoryCache.put(url, bmp);
                }
            }

            if (!mDiskCache.containsKey(url)) {
                //if image isn't in DiskCache, put it there
                mExecutorService.execute(new FileSavingThread(url, bmp, mImageLoaderCallback, mHandler, mDiskCache));
            }

            setBmpToView(bmp, url);
        }

        @Override
        public void onReceivedError(String url, Exception e) {

            e.printStackTrace();
            //we can't say to user that we didn't manage to write image to file system
            //we need to inform him only when we didn't manage to retrieve an image from file system
            if (!url.startsWith("writing_error")) {
                if (mRequestsMap.containsValue(url)) {

                    //setting an error icon from resources
                    setBmpToView(mErrorImageResourceId, url);
                }
            }


        }
    }

}
