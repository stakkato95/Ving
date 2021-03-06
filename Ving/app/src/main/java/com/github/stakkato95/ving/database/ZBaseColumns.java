package com.github.stakkato95.ving.database;

import android.provider.BaseColumns;

/**
 * Created by Artyom on 31.12.2014.
 */
public interface ZBaseColumns extends BaseColumns {

    String DIVIDER = ", ";

    String TYPE_TEXT = "TEXT";
    String TYPE_INTEGER = "INTEGER";
    String TYPE_REAL = "REAL";
    String TYPE_BLOB = "BLOB";

    String CREATE = "CREATE TABLE IF NOT EXISTS ";
    String DROP = "DROP TABLE IF EXISTS ";

}
