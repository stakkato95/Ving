package com.github.stakkato95.ving.source;

import android.content.Context;
import android.database.Cursor;

import com.github.stakkato95.ving.CoreApplication;
import com.github.stakkato95.ving.database.VkDataBaseHelper;
import com.github.stakkato95.ving.database.FriendsTable;

/**
 * Created by Artyom on 15.01.2015.
 */
public class DataBaseSource implements DataSource<Cursor,String[]> {

    public static final String KEY = DataBaseSource.class.getSimpleName();
    private final VkDataBaseHelper mVkDataBaseHelper;

    public static DataBaseSource get(Context context) {
        return CoreApplication.get(context, KEY);
    }

    public DataBaseSource(Context context) {
        mVkDataBaseHelper = VkDataBaseHelper.get(context);
    }

    @Override
    public Cursor getResult(String[] columns) throws Exception {
        Cursor cursor = null;

        cursor = mVkDataBaseHelper.getSQLiteDatabase().query(FriendsTable.NAME, columns, null, null, null, null, null, null);
        return cursor;
    }

}
