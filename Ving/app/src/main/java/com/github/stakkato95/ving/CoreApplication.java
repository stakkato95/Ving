package com.github.stakkato95.ving;

import android.app.Application;
import android.content.Context;

import com.github.stakkato95.loader.ImageLoader;
import com.github.stakkato95.ving.database.VkDataBaseHelper;
import com.github.stakkato95.ving.source.DataBaseSource;
import com.github.stakkato95.ving.source.HttpDataSource;
import com.github.stakkato95.ving.source.VkDataSource;

/**
 * Created by Artyom on 19.11.2014.
 */
public class CoreApplication extends Application {

    private HttpDataSource mHttpDataSource;
    private VkDataSource mVkDataSource;
    private ImageLoader mImageLoader;
    private VkDataBaseHelper mVkDataBaseHelper;
    private DataBaseSource mDataBaseSource;

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
                //TODO                         IS IT CORRECT TO USE APPCONTEXT IN SUCH A WAY???
                mImageLoader = new ImageLoader(getApplicationContext(), 1024 * 1024 * 20, R.drawable.ic_image_loading, R.drawable.ic_image_loading_error);
            }
            return mImageLoader;
        }
        if (VkDataBaseHelper.KEY.equals(name)) {
            if(mVkDataBaseHelper == null) {
                //TODO                                            IS IT CORRECT???
                mVkDataBaseHelper = new VkDataBaseHelper(getApplicationContext());
                mVkDataBaseHelper.open();
            }
            return mVkDataBaseHelper;
        }
        if (DataBaseSource.KEY.equals(name)) {
            if (mDataBaseSource == null) {
                mDataBaseSource = new DataBaseSource(getApplicationContext());
            }
            return mDataBaseSource;
        }
        return super.getSystemService(name);
    }

    public static <T> T get(Context context, String key) {
        if (context == null || key == null){
            throw new IllegalArgumentException("Context and key must not be null");
        }
        T systemService = (T) context.getSystemService(key);
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