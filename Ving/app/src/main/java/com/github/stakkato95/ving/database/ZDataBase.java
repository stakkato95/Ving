package com.github.stakkato95.ving.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Artyom on 30.12.2014.
 */
public class ZDataBase extends SQLiteOpenHelper {

    private static final Integer DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ving_database.db";
    private static Set<Class> sTables;

    static {
        sTables = new LinkedHashSet<>();
        sTables.add(FriendsTable.class);
        sTables.add(DialogsTable.class);
        sTables.add(DialogHistoryTable.class);
    }

    public ZDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (Class tableClass : sTables) {
            createTable(tableClass,db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (Class tableClass : sTables) {
            upgradeTable(tableClass, db);
        }
    }

    public void createTable(Class tableClass, SQLiteDatabase db) {
        try {
            ZTable table = (ZTable)tableClass.newInstance();
            table.onCreate(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void upgradeTable(Class tableClass, SQLiteDatabase db) {
        try {
            ZTable table = (ZTable)tableClass.newInstance();
            table.onUpgrade(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}