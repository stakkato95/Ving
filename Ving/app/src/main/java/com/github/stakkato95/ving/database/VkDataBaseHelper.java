package com.github.stakkato95.ving.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.stakkato95.ving.CoreApplication;

/**
 * Created by Artyom on 30.12.2014.
 */
public class VkDataBaseHelper extends SQLiteOpenHelper {

    private static final Integer DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "vk_database.db";
    private SQLiteDatabase mDataBase;
    public static final String KEY = VkDataBaseHelper.class.getSimpleName();

    public static VkDataBaseHelper get(Context context) {
        return CoreApplication.get(context, KEY);
    }

    public VkDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        FriendsTable.onCreate(db);
        DialogsTable.onCreate(db);
        mDataBase = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        FriendsTable.onUpdate(db);
        DialogsTable.onUpdate(db);
    }

    public void open() {
        mDataBase = this.getWritableDatabase();
    }

    public SQLiteDatabase getSQLiteDatabase() {
        return mDataBase;
    }

}
