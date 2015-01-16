package com.github.stakkato95.ving.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.stakkato95.ving.CoreApplication;
import com.github.stakkato95.ving.bo.Friend;

/**
 * Created by Artyom on 30.12.2014.
 */
public class VkDataBaseHelper extends SQLiteOpenHelper {

    private static final Integer DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "vk_database.db";

    private static final String GET_COUNT_OF = "SELECT * FROM ";
    private SQLiteDatabase mDataBase;

    public static final String KEY = VkDataBaseHelper.class.getSimpleName();


    public static VkDataBaseHelper get(Context context) {return CoreApplication.get(context, KEY);}

    public VkDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        FriendsTable.onCreate(db);
        mDataBase = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        FriendsTable.onUpdate(db);
    }

    public void open() {
        mDataBase = this.getWritableDatabase();
    }

    public SQLiteDatabase getSQLiteDatabase() {
        return mDataBase;
    }


    //@Override
    public long insertFriend(Friend friend) {
        ContentValues values = new ContentValues();
        values.put(FriendsTable._ID, friend.getId());
        values.put(FriendsTable._FULL_NAME, friend.getFirstName());
        values.put(FriendsTable._PHOTO_100, friend.getPhoto());

        long successDetection = 0;

        try {
            //mDataBase = getWritableDatabase();
            mDataBase.beginTransaction();
            //null means we insert whole row, not a certain column
            successDetection = mDataBase.insert(FriendsTable.TABLE_NAME, null, values);
            mDataBase.setTransactionSuccessful();
            mDataBase.endTransaction();
            //mDataBase.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        return successDetection;
    }

    //@Override
    public Cursor findFriendById(int id) {
        String selection = FriendsTable._ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};

        Cursor cursor = null;
        try {
            cursor = mDataBase.query(FriendsTable.TABLE_NAME,null, selection, selectionArgs,null,null,null);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        return cursor;
    }

    public Cursor getCursor() {
        return mDataBase.rawQuery(GET_COUNT_OF + FriendsTable.TABLE_NAME, null);

    }

    //@Override
    public long getCount() {
        mDataBase = getReadableDatabase();

        Cursor cursor;
        long count = 0;
//        if((cursor = mDataBase.rawQuery(DOES_EXIST_TABLE + "'"+FriendsTable.TABLE_NAME+"'", null)) != null) {
//            count = cursor.getCount();
//        }

        mDataBase.close();
        return count;
    }

    public boolean doesTableExist() {
        mDataBase = getReadableDatabase();

        Cursor cursor = mDataBase.rawQuery(GET_COUNT_OF + FriendsTable.TABLE_NAME, null);
        //TODO catch exception
        if(cursor != null) {
            if(cursor.getCount()>0) {
                //mDataBase.close();
                return true;
            }
        }

        mDataBase.close();
        return false;
    }

}
