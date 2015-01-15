package com.github.stakkato95.ving.processing;

import com.github.stakkato95.ving.bo.Friend;
import com.github.stakkato95.ving.bo.JSONArrayWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artyom on 21.11.2014.
 */
public class FriendArrayProcessor implements Processor<List<Friend>,InputStream>{

    @Override
    public List<Friend> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);

        //raw json array
        JSONArrayWrapper jsonArray = new JSONArrayWrapper(string);
        List<Friend> friendArray = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getObject(i);
            Friend friend = new Friend(jsonObject);
            friend.createFullName();
            friendArray.add(friend);
        }
        return friendArray;
    }

}