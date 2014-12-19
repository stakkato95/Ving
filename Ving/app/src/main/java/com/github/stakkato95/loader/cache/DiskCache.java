package com.github.stakkato95.loader.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.github.stakkato95.loader.ImageLoader;
import com.github.stakkato95.loader.assist.ImageLoaderAssistant;
import com.github.stakkato95.ving.source.HttpDataSource;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Artyom on 15.12.2014.
 */
public class DiskCache {

    private static final String CACHE_FOLDER = "image_cache";
    private static File CACHE_DIRECTORY;
    private static long DEFAULT_CACHE_SIZE = 1024 * 1024 * 10; //10 Mb
    private static Context mContext;
    private final long mCacheSize;
    private final HttpDataSource mDataSource;
    private final Set<String> mExistingFiles;
    private final Set<String> mLoadingFiles;



    public DiskCache(final Context context, final long cacheSize) {
        mContext = context;
        mDataSource = HttpDataSource.get(context);
        CACHE_DIRECTORY = getCacheDirectory();
        mCacheSize = ImageLoaderAssistant.setCacheSize(cacheSize, mContext, DEFAULT_CACHE_SIZE);
        mLoadingFiles = new LinkedHashSet<String>();

        if (!CACHE_DIRECTORY.exists()) {
            //creates a folder and an empty set of files
            CACHE_DIRECTORY.mkdirs();
            mExistingFiles = new LinkedHashSet<String>();
        } else {
            //gets set of existing files in cache folder
            mExistingFiles = new LinkedHashSet<String>(Arrays.asList(CACHE_DIRECTORY.list()));
        }
    }

    private static File getCacheDirectory() {
        return new File(mContext.getCacheDir(), CACHE_FOLDER);
    }

    public Bitmap get(String url) {
        String targetFileName = ImageLoaderAssistant.generateFileName(url);
        String targetPath = CACHE_DIRECTORY.getAbsolutePath() + File.separator + targetFileName;

        //if image is loading need to wait
        while (mLoadingFiles.contains(targetFileName)) {

        }

        return BitmapFactory.decodeFile(targetPath);
    }

    public void put(String url) {

        String fileName = ImageLoaderAssistant.generateFileName(url);
        mLoadingFiles.add(fileName);

        File file = new File(CACHE_DIRECTORY, fileName);
        FileOutputStream outputStream = null;
        BufferedOutputStream bufferedStream = null;
        InputStream inputStream = null;

        byte[] buffer = new byte[1024*8];
        int numOfReadBytes;

        try {
            file.createNewFile();
            outputStream = new FileOutputStream(file);
            bufferedStream = new BufferedOutputStream(outputStream);

            inputStream = mDataSource.getResult(url);

            while((numOfReadBytes = inputStream.read(buffer)) != -1) {
                bufferedStream.write(buffer, 0, numOfReadBytes);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }finally {
            ImageLoaderAssistant.closeStream(outputStream, bufferedStream);
            ImageLoaderAssistant.closeStream(inputStream);
        }

        //after saving file to disk add it to list of existing files
        mExistingFiles.add(fileName);
        mLoadingFiles.remove(fileName);
    }

    public boolean containsKey(final String url) {
        final String fileName = ImageLoaderAssistant.generateFileName(url);

        return mExistingFiles.contains(fileName);
    }
}
