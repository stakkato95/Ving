package com.github.stakkato95.ving.processor;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.github.stakkato95.ving.bo.Friend;
import com.github.stakkato95.ving.bo.JSONArrayWrapper;
import com.github.stakkato95.ving.database.FriendsTable;
import com.github.stakkato95.ving.provider.ZContentProvider;

import org.json.JSONObject;

/**
 * Created by Artyom on 04.02.2015.
 */
public class UserDBProcessor  extends DBProcessor {

    public UserDBProcessor(Context context) {
        super(context);
    }

    @Override
    protected void insertDataFrom(JSONArrayWrapper jsonArray) throws Exception {
        ContentResolver resolver = getContext().getContentResolver();
        ContentValues[] values = new ContentValues[jsonArray.length()];


        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Friend friend = new Friend(jsonObject);
            friend.createFullName();

            ContentValues value = new ContentValues();
            value.put(FriendsTable._ID, friend.getId());
            value.put(FriendsTable._FULL_NAME, friend.getFullName());
            value.put(FriendsTable._PHOTO_100, friend.getPhoto());
            value.put(FriendsTable._ONLINE, friend.getOnlineMode());
            values[i] = value;
        }

        resolver.bulkInsert(ZContentProvider.FRIENDS_CONTENT_URI, values);
    }

}
