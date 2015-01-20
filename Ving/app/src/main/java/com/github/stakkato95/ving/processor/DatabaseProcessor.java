package com.github.stakkato95.ving.processor;

import android.content.Context;

import com.github.stakkato95.ving.bo.JSONArrayWrapper;

import java.io.InputStream;

/**
 * Created by Artyom on 17.01.2015.
 */
public abstract class DatabaseProcessor {

    private final Context mContext;

    DatabaseProcessor(Context context) {
        mContext = context;
    }

    public void process(InputStream inputStream) throws Exception {
        if (inputStream != null) {
            String string = new StringProcessor().process(inputStream);
            JSONArrayWrapper jsonArray = new JSONArrayWrapper(string);
            insertDataFrom(jsonArray);
        }
    }

    protected abstract void insertDataFrom(JSONArrayWrapper jsonArray);

    protected final Context getContext() {
        return mContext;
    }

}
