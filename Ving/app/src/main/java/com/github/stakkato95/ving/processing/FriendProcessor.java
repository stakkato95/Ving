package com.github.stakkato95.ving.processing;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.github.stakkato95.ving.bo.Friend;
import com.github.stakkato95.ving.bo.JSONArrayWrapper;
import com.github.stakkato95.ving.database.FriendsTable;
import com.github.stakkato95.ving.provider.VingContentProvider;

import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by Artyom on 21.11.2014.
 */
public class FriendProcessor implements DatabaseProcessor<InputStream> {

    private final Context mContext;
    private final ContentResolver mContentResolver;
    private int mInsertionCount;

    public FriendProcessor(Context context) {
        mContext = context;
        mContentResolver = mContext.getContentResolver();

    }

    @Override
    public void process(InputStream inputStream) throws Exception {

        if (inputStream != null) {
            String string = new StringProcessor().process(inputStream);
            JSONArrayWrapper jsonArray = new JSONArrayWrapper(string);
            Cursor cursor = mContentResolver.query(VingContentProvider.FRIENDS_CONTENT_URI, new String[]{FriendsTable._ID}, null, null, null);

            insertDataFrom(jsonArray);
        }
    }

    private void insertDataFrom(JSONArrayWrapper jsonArray) {
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
        mInsertionCount = mContentResolver.bulkInsert(VingContentProvider.FRIENDS_CONTENT_URI, values);
    }

    public int getInsertionCount() {
        return mInsertionCount;
    }

}