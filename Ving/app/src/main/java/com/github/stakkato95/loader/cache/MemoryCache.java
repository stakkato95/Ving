package com.github.stakkato95.loader.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.github.stakkato95.loader.assist.ImageLoaderAssistant;

/**
 * Created by Artyom on 11.12.2014.
 */
public class MemoryCache implements Cache<String, Bitmap> {

    private LruCache<String, Bitmap> mLruCache;

    public MemoryCache(Context context) {
        int CACHE_SIZE = ImageLoaderAssistant.setCacheSize(context);
        mLruCache = new LruCache<String, Bitmap>(CACHE_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
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
