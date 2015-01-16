package com.github.stakkato95.ving.processing;

import android.content.ContentValues;

import com.github.stakkato95.ving.bo.Friend;
import com.github.stakkato95.ving.bo.JSONArrayWrapper;
import com.github.stakkato95.ving.database.FriendsTable;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artyom on 21.11.2014.
 */
public class FriendArrayProcessor implements Processor<List<Friend>,InputStream>{

    //private final VkDataBaseHelper mDataBaseHelper;

//    public FriendArrayProcessor(VkDataBaseHelper helper) {
//        mDataBaseHelper = helper;
//    }

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

            ContentValues values = new ContentValues();
            values.put(FriendsTable._ID, friend.getId());
            values.put(FriendsTable._FIRST_NAME, friend.getFirstName());
            values.put(FriendsTable._LAST_NAME, friend.getLastName());
            values.put(FriendsTable._PHOTO_100, friend.getPhoto());


            //mDataBaseHelper.insertFriend(friend);
            friendArray.add(friend);
        }
        //TODO add _IS_ONLINE field
        //TODO add update method to VkDataBaseHelper
        //Cursor cursor = mDataBaseHelper.getCursor();

        return friendArray;
    }

}