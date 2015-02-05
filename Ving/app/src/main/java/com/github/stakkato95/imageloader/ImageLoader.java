package com.github.stakkato95.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.github.stakkato95.imageloader.assist.ImageHelper;
import com.github.stakkato95.imageloader.cache.DiskCache;
import com.github.stakkato95.imageloader.cache.MemoryCache;
import com.github.stakkato95.imageloader.thread.FileLoadingThread;
import com.github.stakkato95.imageloader.thread.FileSavingThread;
import com.github.stakkato95.imageloader.thread.MemoryLoadingThread;
import com.github.stakkato95.ving.CoreApplication;
import com.github.stakkato95.ving.os.ZExecutor;
import com.github.stakkato95.ving.processor.BitmapProcessor;
import com.github.stakkato95.ving.source.HttpDataSource;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Artyom on 11.12.2014.
 */
public class ImageLoader {

    private static ZExecutor sExecutor;
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

    public static final String KEY = ImageLoader.class.getSimpleName();
    public static ImageLoader get(Context context) { return CoreApplication.get(context, KEY); }

    static {
        sExecutor = new ZExecutor();

    }

    public ImageLoader(@NonNull Context context, int diskCacheSize, int loadingImageResourceId, int errorImageResourceId) {
        mContext = context;
        mMemoryCache = new MemoryCache(context);
        mImageLoaderCallback = new ImageLoaderCallback();
        mRequestsMap = new ConcurrentHashMap<>();
        mDataSource = HttpDataSource.get(context);
        mBitmapProcessor = new BitmapProcessor();
        mDiskCache = new DiskCache(context, diskCacheSize);
        mHandler = new Handler(Looper.myLooper());


        mLoadingImageResourceId = loadingImageResourceId;
        mErrorImageResourceId = errorImageResourceId;
    }


    private volatile ImageView mLastImage;

    public void byUrl(/*@NonNull ImageView imageView, */@NonNull String url) {
        mLastImage.setImageResource(mLoadingImageResourceId);
        if (mMemoryCache.containsKey(url)) {
            Bitmap targetBmp = mMemoryCache.get(url);

            //if request isn't added the image won't be displayed
            mRequestsMap.put(mLastImage, url);
            setBmpToView(targetBmp, url);
        } else {

            synchronized (mRequestsMap) {
                if (mDiskCache.containsKey(url) && !mRequestsMap.containsValue(url)) {

                    //image is in DiskCache -> do loading from DiskCache
                    sExecutor.execute(new FileLoadingThread(url, mImageLoaderCallback, mHandler, mDiskCache));
                } else {

                    //image isn't in DiskCache -> check for necessity of lading from the network
                    if (!mRequestsMap.containsValue(url)) {
                        //loading of the image from the network is required
                        sExecutor.execute(new MemoryLoadingThread(url, mImageLoaderCallback, mHandler, mDataSource, mBitmapProcessor));
                    }
                }
            }
            mRequestsMap.put(mLastImage, url);
        }


    }

    public ImageLoader toView(@NonNull ImageView imageView) {
        mLastImage = imageView;
        return this;
    }

    public ImageLoader setCircled(boolean circled) {
        mLastImage.setTag(circled);
        return this;
    }

    private void setBmpToView(Bitmap bmp, String url) {

        Set<Map.Entry<ImageView, String>> keyValuePair = mRequestsMap.entrySet();

        for (Map.Entry<ImageView, String> pair : keyValuePair) {

            if (pair.getValue().equals(url)) {
                ImageView targetView = pair.getKey();

                if (targetView != null) {
                    if ((Boolean)targetView.getTag()) {
                        bmp = ImageHelper.getRounded(bmp);
                    }
                    targetView.setImageBitmap(bmp);
                }

                mRequestsMap.remove(targetView);
                break;
            }
        }

    }

    private void setBmpToView(String url) {

        Set<Map.Entry<ImageView, String>> keyValuePair = mRequestsMap.entrySet();

        for (Map.Entry<ImageView, String> pair : keyValuePair) {

            if (pair.getValue().equals(url)) {
                ImageView targetView = pair.getKey();

                if (targetView != null) {
                    Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), mErrorImageResourceId);
                    if ((boolean)targetView.getTag()) {
                        bmp = ImageHelper.getRounded(bmp);
                    }
                    targetView.setImageBitmap(bmp);
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
                if (url != null && bmp != null && !mMemoryCache.containsKey(url)) {
                    //if image isn't in MemoryCache, put it there
                    mMemoryCache.put(url, bmp);
                }
            }

            if (!mDiskCache.containsKey(url)) {
                //if image isn't in DiskCache, put it there
                sExecutor.execute(new FileSavingThread(url, bmp, mImageLoaderCallback, mHandler, mDiskCache));
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
                    setBmpToView(url);
                }
            }


        }
    }

}
