package com.github.stakkato95.loader;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by Artyom on 11.12.2014.
 */
public class MemoryCache {

    private static final int MAX_CACHE_SIZE = (int)(Runtime.getRuntime().maxMemory()) / 1024 / 10;
    private LruCache<String,Bitmap> mLruCache;

    public MemoryCache() {

        mLruCache = new LruCache<String, Bitmap>(MAX_CACHE_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
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

}
