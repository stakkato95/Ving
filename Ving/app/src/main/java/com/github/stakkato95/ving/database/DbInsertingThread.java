package com.github.stakkato95.ving.database;

import android.content.Context;
import android.database.Cursor;

import com.github.stakkato95.ving.bo.Friend;

import java.util.List;

/**
 * Created by Artyom on 15.01.2015.
 */
public class DbInsertingThread extends Thread{

    private final VkDataBaseHelper mDataBase;
    private final List<Friend> mList;

    public DbInsertingThread(Context context, List<Friend> list) {
        mList = list;
        mDataBase = VkDataBaseHelper.get(context);
    }

    public void start() {
        run();
    }

    @Override
    public void run() {
        long count = 0;
        for(Friend friend : mList) {
            count += mDataBase.insertFriend(friend);
        }


        long id = mList.get(0).getId();
        int id2 = (int)id;
        Cursor cursor = mDataBase.findFriendById(id2);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {

            //cursor.getLong(cursor.getColumnIndex(FriendsTable._ID));
            cursor.getString(cursor.getColumnIndex(FriendsTable._LAST_NAME));
            cursor.moveToNext();
        }
    }
}
