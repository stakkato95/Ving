package com.github.stakkato95.ving.processor;

import android.content.Context;

import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.bo.JSONArrayWrapper;
import com.github.stakkato95.ving.source.HttpDataSource;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Artyom on 02.03.2015.
 */
public class DialogHistoryLongPollProcessor extends DBProcessor {

    private final HttpDataSource mHttpSource;

    public DialogHistoryLongPollProcessor(Context context) {
        super(context);
        mHttpSource = HttpDataSource.get(context);
    }

    @Override
    public void process(InputStream inputStream) throws Exception {
        if (inputStream != null) {
            String string = new StringProcessor().process(inputStream);
            JSONObject jsonObject = new JSONObject(string);

            String longPollRequest = Api.getLongPoll(jsonObject);
            InputStream longPollStream = mHttpSource.getResult(longPollRequest);
            BufferedReader reader = new BufferedReader(new InputStreamReader(longPollStream));

            String longPollResponse;
            boolean needRestartConnection = true;
            while (needRestartConnection) {
                while ((longPollResponse = reader.readLine()).equals(Api.EMPTY_STRING)) {}
                if (longPollResponse == null) {
                    needRestartConnection = true;
                    //TODO restart connection here
                } else {
                    needRestartConnection = false;
                }
            }
            insertDataFrom(jsonObject);
        }
    }

    @Override
    protected void insertDataFrom(JSONObject jsonObject) throws Exception {

    }
}
