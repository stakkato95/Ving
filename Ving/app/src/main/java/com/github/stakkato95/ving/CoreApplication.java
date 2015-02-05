package com.github.stakkato95.ving;

import android.app.Application;
import android.content.Context;

import com.github.stakkato95.imageloader.ImageLoader;
import com.github.stakkato95.ving.database.ZDataBase;
import com.github.stakkato95.ving.source.HttpDataSource;
import com.github.stakkato95.ving.source.VkDataSource;

/**
 * Created by Artyom on 19.11.2014.
 */
public class CoreApplication extends Application {

    private HttpDataSource mHttpDataSource;
    private VkDataSource mVkDataSource;
    private ImageLoader mImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        mHttpDataSource = new HttpDataSource();
        mVkDataSource = new VkDataSource();
    }

    @Override
    public Object getSystemService(String name) {
        if (HttpDataSource.KEY.equals(name)) {
            if (mHttpDataSource == null) {
                mHttpDataSource = new HttpDataSource();
            }
            return mHttpDataSource;
        }
        if (VkDataSource.KEY.equals(name)) {
            if (mVkDataSource == null) {
                mVkDataSource = new VkDataSource();
            }
            return mVkDataSource;
        }
        if(ImageLoader.KEY.equals(name)) {
            if(mImageLoader == null) {
                //20 Mb DiskCache
                mImageLoader = new ImageLoader(getApplicationContext(), 1024 * 1024 * 20, R.drawable.ic_image_loading, R.drawable.ic_image_loading_error);
            }
            return mImageLoader;
        }
        return super.getSystemService(name);
    }

    public static <T> T get(Context context, String key) {
        if (context == null || key == null){
            throw new IllegalArgumentException("Context and key must not be null");
        }
        T systemService = (T)context.getSystemService(key);
        if (systemService == null) {
            context = context.getApplicationContext();
            systemService = (T) context.getSystemService(key);
        }
        if (systemService == null) {
            throw new IllegalStateException(key + " not available");
        }
        return systemService;
    }

}