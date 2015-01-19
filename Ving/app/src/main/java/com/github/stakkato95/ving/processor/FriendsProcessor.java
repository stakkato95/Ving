package com.github.stakkato95.ving.processor;

import android.content.ContentResolver;
import android.content.ContentValues;

import com.github.stakkato95.ving.CoreApplication;
import com.github.stakkato95.ving.bo.Friend;
import com.github.stakkato95.ving.bo.JSONArrayWrapper;
import com.github.stakkato95.ving.database.FriendsTable;
import com.github.stakkato95.ving.provider.VingContentProvider;

import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by Artyom on 21.11.2014.
 */
public class FriendsProcessor implements DatabaseProcessor<InputStream> {

    @Override
    public void process(InputStream inputStream) throws Exception {
        if (inputStream != null) {
            String string = new StringProcessor().process(inputStream);
            JSONArrayWrapper jsonArray = new JSONArrayWrapper(string);
            insertDataFrom(jsonArray);
        }
    }

    private void insertDataFrom(JSONArrayWrapper jsonArray) {
        ContentResolver resolver = CoreApplication.getContext().getContentResolver();
        ContentValues[] values = new ContentValues[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getObject(i);
            Friend friend = new Friend(jsonObject);
            friend.createFullName();

            ContentValues value = new ContentValues();
            value.put(FriendsTable._ID, friend.getId());
            value.put(FriendsTable._FULL_NAME, friend.getFullName());
            value.put(FriendsTable._PHOTO_100, friend.getPhoto());
            value.put(FriendsTable._ONLINE, friend.getOnlineMode());
            values[i] = value;
        }

        resolver.bulkInsert(VingContentProvider.FRIENDS_CONTENT_URI, values);
    }

}