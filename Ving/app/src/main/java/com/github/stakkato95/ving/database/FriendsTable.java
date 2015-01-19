package com.github.stakkato95.ving.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by Artyom on 31.12.2014.
 */
public final class FriendsTable implements DataBaseConstants {

    public static final String NAME = "friends";

    public static final String _FULL_NAME = "_full_name";
    public static final String _PHOTO_100 = "_photo_100";
    public static final String _ONLINE= "_online";

    private static final String CREATE_TABLE = CREATE + NAME + " ( " +
            _ID + " " + TYPE_INTEGER + DIVIDER +
            _FULL_NAME + " " + TYPE_TEXT + DIVIDER +
            _PHOTO_100 + " " + TYPE_TEXT + DIVIDER +
            _ONLINE + " " + TYPE_INTEGER + " )";

    public static final String[] PROJECTION = {
            _ID,
            _FULL_NAME,
            _PHOTO_100,
            _ONLINE,
    };

    public static final String[] PROJECTION_OFFLINE = {
            _ID,
            _FULL_NAME,
            _PHOTO_100,
    };

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public static void onUpdate(SQLiteDatabase db) {
        db.execSQL(DROP + NAME);
        db.execSQL(CREATE_TABLE);
    }

}