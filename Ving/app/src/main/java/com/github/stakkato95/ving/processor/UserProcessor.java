package com.github.stakkato95.ving.processor;

import com.github.stakkato95.ving.bo.JSONArrayWrapper;
import com.github.stakkato95.ving.bo.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by Artyom on 20.01.2015.
 */
public class UserProcessor implements Processor<InputStream,User> {

    @Override
    public User process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONArrayWrapper jsonArray = new JSONArrayWrapper(string);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        User user = new User(jsonObject);
        user.createFullName();
        return user;
    }

}
