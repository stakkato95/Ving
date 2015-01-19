package com.github.stakkato95.ving.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Artyom on 19.01.2015.
 */
public final class DialogsTable implements DataBaseConstants {

    public static final String NAME = "dialogs";

    public static final String _DIALOG_NAME = "_dialog_name"; //identified by friend's full_name, or dialog's title
    public static final String _PHOTO_100 = "_photo_100";
    public static final String _DATE = "_date";
    public static final String _ROUTE = "_route";
    public static final String _READ_STATE = "_read_state";
    public static final String _BODY = "_body";

    private static final String CREATE_TABLE = CREATE + NAME + " ( " +
            _ID + " " + TYPE_INTEGER + DIVIDER +
            _DIALOG_NAME + " " + TYPE_TEXT + DIVIDER +
            _PHOTO_100 + " " + TYPE_TEXT + DIVIDER +
            _DATE + " " + TYPE_INTEGER + DIVIDER +
            _ROUTE + " " + TYPE_INTEGER + DIVIDER +
            _READ_STATE + " " + TYPE_INTEGER + DIVIDER +
            _BODY + " " + TYPE_TEXT + " )";

    public static final String[] PROJECTION = {
            _ID,
            _DIALOG_NAME,
            _PHOTO_100,
            _DATE,
            _ROUTE,
            _READ_STATE,
            _BODY
    };

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public static void onUpdate(SQLiteDatabase db) {
        db.execSQL(DROP + NAME);
        db.execSQL(CREATE_TABLE);
    }

}
