package com.github.stakkato95.ving.source;

import android.content.Context;

import com.github.stakkato95.ving.CoreApplication;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Artyom on 19.11.2014.
 */
public class HttpDataSource implements DataSource<InputStream, String> {

    public static final String KEY = "HttpDataSource";

    public static HttpDataSource get(Context context) {
        return CoreApplication.get(context, KEY);
    }

    @Override
    public InputStream getResult(String p) throws Exception {
        //download data and return
        URL url = new URL(p);
        // Read all the text returned by the server
        return url.openStream();
    }

    public static void close(Closeable in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}