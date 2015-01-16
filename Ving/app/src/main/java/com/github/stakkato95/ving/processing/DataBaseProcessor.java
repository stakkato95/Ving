package com.github.stakkato95.ving.processing;

import android.database.Cursor;

import com.github.stakkato95.ving.bo.Friend;
import com.github.stakkato95.ving.database.FriendsTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artyom on 15.01.2015.
 */
public class DataBaseProcessor implements Processor<List<Friend>,Cursor> {
    @Override
    public List<Friend> process(Cursor cursor) throws Exception {
        List<Friend> list = new ArrayList<>();

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            int id = (int)cursor.getLong(cursor.getColumnIndex(FriendsTable._ID));
            String firstName = cursor.getString(cursor.getColumnIndex(FriendsTable._FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndex(FriendsTable._LAST_NAME));
            String photo= cursor.getString(cursor.getColumnIndex(FriendsTable._PHOTO_100));

            //Friend friend = new Friend()
            cursor.moveToNext();
        }


        return null;
    }
}
