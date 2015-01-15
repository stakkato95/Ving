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
public class VkDataBaseOpenHelper extends SQLiteOpenHelper {

    private static final Integer DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "vk_database.db";

    private static final String GET_COUNT_OF = "SELECT * FROM ";
    private static final String DROP = "DROP TABLE IF EXISTS ";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    private SQLiteDatabase mDataBase;

    public static final String KEY = VkDataBaseOpenHelper.class.getSimpleName();


    public static VkDataBaseOpenHelper get(Context context) {return CoreApplication.get(context, KEY);}

    public VkDataBaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE + VkFriendsSummaryContract.TABLE);
        mDataBase = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP + VkFriendsSummaryContract.TABLE_NAME);
        onCreate(db);
    }

    public void open() {
        mDataBase = this.getWritableDatabase();
    }




    //@Override
    public long insertFriend(Friend friend) {
        ContentValues values = new ContentValues();
        values.put(VkFriendsSummaryContract._ID, friend.getId());
        values.put(VkFriendsSummaryContract._FIRST_NAME, friend.getFirstName());
        values.put(VkFriendsSummaryContract._LAST_NAME, friend.getLastName());
        values.put(VkFriendsSummaryContract._PHOTO_50, friend.getPhoto());

        long successDetection = 0;

        try {
            //mDataBase = getWritableDatabase();
            mDataBase.beginTransaction();
            //null means we insert whole row, not a certain column
            successDetection = mDataBase.insert(VkFriendsSummaryContract.TABLE_NAME, null, values);
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
        String selection = VkFriendsSummaryContract._ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};

        Cursor cursor = null;
        try {
            cursor = mDataBase.query(VkFriendsSummaryContract.TABLE_NAME,null, selection, selectionArgs,null,null,null);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        return cursor;
    }

    //@Override
    public long getCount() {
        mDataBase = getReadableDatabase();

        Cursor cursor;
        long count = 0;
//        if((cursor = mDataBase.rawQuery(DOES_EXIST_TABLE + "'"+VkFriendsSummaryContract.TABLE_NAME+"'", null)) != null) {
//            count = cursor.getCount();
//        }

        mDataBase.close();
        return count;
    }

    public boolean doesTableExist() {
        mDataBase = getReadableDatabase();

        Cursor cursor = mDataBase.rawQuery(GET_COUNT_OF + VkFriendsSummaryContract.TABLE_NAME, null);
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

    public void dropTable() {
        mDataBase = getWritableDatabase();
        mDataBase.execSQL(DROP + VkFriendsSummaryContract.TABLE_NAME);
        mDataBase.close();
    }

    public void addTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_TABLE + VkFriendsSummaryContract.TABLE);
    }
}
