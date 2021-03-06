package com.github.stakkato95.ving.source;

import android.content.Context;
import android.util.Log;

import com.github.stakkato95.ving.CoreApplication;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Artyom on 19.11.2014.
 */
public class HttpDataSource implements DataSource<String, InputStream> {

    public static final String KEY = HttpDataSource.class.getSimpleName();

    public static HttpDataSource get(Context context) {
        return CoreApplication.get(context, KEY);
    }

    @Override
    public InputStream getResult(String p) throws Exception {
        Log.d("TAG", p);
        URL url = new URL(p);
        return url.openStream();
    }

}