package com.github.stakkato95.loader.thread;

import com.github.stakkato95.loader.cache.DiskCache;
import com.github.stakkato95.ving.source.HttpDataSource;

/**
 * Created by Artyom on 18.12.2014.
 */
public class FileSavingThread extends Thread {

    private final String mUrl;
    private final HttpDataSource mDataSource;
    private final DiskCache mDiskCache;

    public FileSavingThread(String url, HttpDataSource httpDataSource, DiskCache diskCache) {
        mUrl = url;
        mDataSource = httpDataSource;
        mDiskCache = diskCache;
    }

    @Override
    public void run() {
        mDiskCache.put(mUrl);
    }
}
