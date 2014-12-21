package com.github.stakkato95.loader.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.github.stakkato95.loader.ImageLoader;
import com.github.stakkato95.loader.assist.ImageLoaderAssistant;

/**
 * Created by Artyom on 11.12.2014.
 */
public class MemoryCache implements Cache<String, Bitmap> {

    private static int CACHE_SIZE; //(int)(Runtime.getRuntime().maxMemory()) / 1024 / 10; //simply magic number
    private LruCache<String, Bitmap> mLruCache;

    public MemoryCache(Context context) {
        CACHE_SIZE = ImageLoaderAssistant.setCacheSize(context);
        mLruCache = new LruCache<String, Bitmap>(CACHE_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //TODO alternative variant of bmp size obtaining
                //return (value.getAllocationByteCount() + key.length());
                return (value.getRowBytes() * value.getHeight() + key.length());
            }
        };
    }

    public Bitmap get(String url) {
        //never check bitmaps for null!!!
        return mLruCache.get(url);
    }

    public void put(String url, Bitmap bmp) {
        mLruCache.put(url, bmp);
    }

    public boolean containsKey(String url) {
        return mLruCache.get(url) != null;
    }

}
