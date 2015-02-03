package com.github.stakkato95.ving.processor;

import com.github.stakkato95.ving.api.Api;

import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by Artyom on 03.02.2015.
 */
public class MessageSendProcessor implements Processor<InputStream,Integer> {


    @Override
    public Integer process(InputStream inputStream) throws Exception {
        int messageId = 0;
        if (inputStream != null) {
            String string = new StringProcessor().process(inputStream);
            messageId = new JSONObject(string).getInt(Api.JSON_RESPONSE);
        }
        return messageId;
    }
}
