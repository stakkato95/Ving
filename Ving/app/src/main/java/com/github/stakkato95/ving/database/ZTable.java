package com.github.stakkato95.ving.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.github.stakkato95.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Artyom on 25.01.2015.
 */
public abstract class ZTable implements ZBaseColumns {

    private static String CREATE_TABLE;

    public ZTable() {
        StringBuilder dbQueryBuilder = new StringBuilder();
        dbQueryBuilder.append(ZBaseColumns.CREATE).append(getName()).append(" ( ");

        MultiValueMap<String,String> dbMap = getDbMap();
        Set<Map.Entry<String,List<String>>> keyValuePairs = dbMap.entrySet();

        for (Map.Entry<String,List<String>> pair : keyValuePairs) {
            List<String> columns = pair.getValue();
            for (String column : columns) {
                dbQueryBuilder.append(column).append(" ").append(pair.getKey()).append(DIVIDER);
            }
        }
        dbQueryBuilder.deleteCharAt(dbQueryBuilder.length() - 2).append(" )");
        CREATE_TABLE = dbQueryBuilder.toString();
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db) {
        db.execSQL(DROP + getName());
        db.execSQL(CREATE_TABLE);
    }

    public abstract MultiValueMap<String,String> getDbMap();

    public abstract String getName();

}
