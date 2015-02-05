package com.github.stakkato95.ving.processor;

import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.bo.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by Artyom on 20.01.2015.
 */
public class UserProcessor implements Processor<InputStream,User[]> {

    @Override
    public User[] process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONObject response = new JSONObject(string);
        JSONArray jsonArray = response.getJSONArray(Api.JSON_RESPONSE);
        User[] users = new User[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            users[i] = new User(jsonArray.getJSONObject(i));
            users[i].createFullName();
        }
        return users;
    }

}
