package com.github.stakkato95.ving.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by Artyom on 31.12.2014.
 */
public final class FriendsTable implements DataBaseConstants, BaseColumns {

    public static String NAME = "friends";

    public static String _FULL_NAME = "_last_name";
    public static String _PHOTO_100 = "_photo_100";
    public static String _ONLINE= "_online";

    private static String CREATE_TABLE = CREATE + NAME + " ( " +
            _ID + " " + TYPE_INTEGER + DIVIDER +
            _FULL_NAME + " " + TYPE_TEXT + DIVIDER +
            _PHOTO_100 + " " + TYPE_TEXT + DIVIDER +
            _ONLINE + " " + TYPE_INTEGER + " )";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public static void onUpdate(SQLiteDatabase db) {
        db.execSQL(DROP + NAME);
        db.execSQL(CREATE_TABLE);
    }

}