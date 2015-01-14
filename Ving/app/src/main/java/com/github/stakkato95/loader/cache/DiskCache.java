package com.github.stakkato95.loader.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.github.stakkato95.loader.assist.ImageLoaderAssistant;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Artyom on 15.12.2014.
 */
public class DiskCache implements Cache<String, Bitmap> {

    private static final String CACHE_FOLDER = "image_cache";
    private static File CACHE_DIRECTORY;
    private static long DEFAULT_CACHE_SIZE = 1024 * 1024 * 50; //50 Mb
    private static Context mContext;

    //TODO write methods for siz control
    private final long mCacheSize;
    private final Set<String> mExistingFiles;
    private final Set<String> mLoadingFiles;


    public DiskCache(final Context context, final long cacheSize) {
        mContext = context;
        CACHE_DIRECTORY = getCacheDirectory();
        mCacheSize = ImageLoaderAssistant.setCacheSize(cacheSize, mContext, DEFAULT_CACHE_SIZE);
        mLoadingFiles = new LinkedHashSet<String>();

        if (!CACHE_DIRECTORY.exists()) {
            //creates a folder and an empty set of files
            CACHE_DIRECTORY.mkdirs();
            mExistingFiles = new LinkedHashSet<String>();
        } else {
            //gets set of existing files in cache folder
            //TODO move to thread
            mExistingFiles = new LinkedHashSet<String>(Arrays.asList(CACHE_DIRECTORY.list()));
        }
    }

    private static File getCacheDirectory() {
        return new File(mContext.getCacheDir(), CACHE_FOLDER);
    }

    public Bitmap get(String url) throws Exception {

        String targetFileName = ImageLoaderAssistant.generateFileName(url);
        String targetPath = CACHE_DIRECTORY.getAbsolutePath() + File.separator + targetFileName;

        //if image is loading need to wait
        while (mLoadingFiles.contains(targetFileName)) {
        }

        Bitmap bmp = BitmapFactory.decodeFile(targetPath);

        if (bmp == null) {
            throw new Exception("The image on " + url + " link is null");
        }

        return bmp;
    }

    public void put(String url, Bitmap bmp) throws Exception {

        String fileName = ImageLoaderAssistant.generateFileName(url);
        mLoadingFiles.add(fileName);

        File file = new File(CACHE_DIRECTORY, fileName);
        FileOutputStream outputStream = null;
        BufferedOutputStream bufferedStream = null;

        //if set doesn't contain such a file we add it
        if (!mExistingFiles.contains(fileName)) {

            try {
                file.createNewFile();
                outputStream = new FileOutputStream(file);
                bufferedStream = new BufferedOutputStream(outputStream);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, bufferedStream);
            } catch (FileNotFoundException e) {
                throw new Exception("File " + fileName + " not found");
            } catch (IOException e) {
                throw new Exception("Error while writing file " + fileName);
            } catch (Exception e) {
                throw new Exception("Unexpected error with file" + fileName);
            } finally {
                ImageLoaderAssistant.closeStream(outputStream, bufferedStream);
            }
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
