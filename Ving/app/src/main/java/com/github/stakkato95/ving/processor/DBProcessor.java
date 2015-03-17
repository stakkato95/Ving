package com.github.stakkato95.ving.processor;

import android.content.Context;

import com.github.stakkato95.ving.bo.JSONArrayWrapper;

import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by Artyom on 17.01.2015.
 */
public abstract class DBProcessor {

    private final Context mContext;

    DBProcessor(Context context) {
        mContext = context;
    }

    public void process(InputStream inputStream) throws Exception {
        if (inputStream != null) {
            String string = new StringProcessor().process(inputStream);
            JSONArrayWrapper jsonArray = new JSONArrayWrapper(string);
            insertDataFrom(jsonArray);
        }
    }

    protected void insertDataFrom(JSONArrayWrapper jsonArray) throws Exception { }

    protected void insertDataFrom(JSONObject jsonObject) throws Exception { }

    protected final Context getContext() {
        return mContext;
    }

}
