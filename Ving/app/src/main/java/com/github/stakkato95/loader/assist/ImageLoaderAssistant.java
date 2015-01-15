package com.github.stakkato95.loader.assist;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Artyom on 18.12.2014.
 */
public class ImageLoaderAssistant {

    public static void closeStream(final InputStream inputStream) {
        if(inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeStream(final OutputStream... outputStream) {
        for(OutputStream stream : outputStream) {
            if (outputStream != null) {
                try {
                    stream.flush();
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    public static int setCacheSize(final Context context) {
        //method for MemoryCache

        //20% of free space
        return (int)context.getFilesDir().getFreeSpace() / 5;
    }

    public static String generateFileName(String url) {
        return md5(url);
    }

    public static final String md5(final String s) {
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
