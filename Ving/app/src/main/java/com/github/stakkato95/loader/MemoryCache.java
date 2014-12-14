package com.github.stakkato95.loader;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by Artyom on 11.12.2014.
 */
public class MemoryCache {

    private static final int MAX_CACHE_SIZE = 1024 * 1024; //(int)(Runtime.getRuntime().maxMemory()) / 1024 / 10; //simply magic number
    private LruCache<String,Bitmap> mLruCache;

    public MemoryCache() {

        mLruCache = new LruCache<String, Bitmap>(MAX_CACHE_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //TODO alternative variant of bmp size obtaining
                //return (value.getAllocationByteCount() + key.length());
                return (value.getRowBytes() * value.getHeight() + key.length());
            }
        };

    }

    public Bitmap get(String url) {

        Bitmap bmp = mLruCache.get(url);

        if (bmp != null) {
            return bmp;
        } else {
            return null;
        }

    }

    public void put(String url, Bitmap bmp) {
        if (mLruCache.get(url) == null) {
            mLruCache.put(url, bmp);
        }

    }

    public boolean containsKey(String url) {
        return mLruCache.get(url) != null;
    }
}
