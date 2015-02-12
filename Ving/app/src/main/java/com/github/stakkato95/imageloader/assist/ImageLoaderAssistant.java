package com.github.stakkato95.imageloader.assist;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Artyom on 18.12.2014.
 */
public class ImageLoaderAssistant {

    public static void closeStream(@NonNull final Closeable... closeables) {
        for (Closeable closeable : closeables) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeOutputStream(@NonNull final OutputStream... streams) {
        for (OutputStream stream : streams) {
            try {
                stream.flush();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static long setCacheSize(final long cacheSize, final Context context, final long defaultCacheSize) {
        //method for DiskCache

        //5% of free space
        long calculatedСacheSize = context.getFilesDir().getFreeSpace() / 1024 / 1024 / 20;

        //if cacheSize is less than 10 Mb
        if (cacheSize <= 1024 * 1024 * 10) {

            //if calculatedСacheSize is less than defaultCacheSize, setField defaultCacheSize value
            //else setField calculatedСacheSize
            if (calculatedСacheSize <= defaultCacheSize) {
                return defaultCacheSize;
            }
            return calculatedСacheSize;
        }

        return cacheSize;
    }

    public static long setCacheSize(final Context context) {
        //method for MemoryCache

        //20% of free space
        return context.getFilesDir().getFreeSpace() / 5;
    }

    public static String generateFileName(String url) {
        return md5(url);
    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
