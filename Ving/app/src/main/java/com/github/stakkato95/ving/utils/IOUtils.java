package com.github.stakkato95.ving.utils;

import android.support.annotation.NonNull;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Artyom on 03.02.2015.
 */
public class IOUtils {

    public static void closeStream(@NonNull final Closeable... closeables) {
        for (Closeable closeable : closeables) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
