package com.github.stakkato95.ving.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.github.stakkato95.ving.database.FriendsTable;
import com.github.stakkato95.ving.database.VkDataBaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Artyom on 16.01.2015.
 */
public class VingContentProvider extends ContentProvider {

    private VkDataBaseHelper mVkDataBaseHelper;

    private static final int FRIENDS = 0;
    private static final int FRIEND_ID = 1;

    private static final String AUTHORITY = "com.github.stakkato95.ving.provider";
    private static final String BASE_PATH = "friends";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + BASE_PATH;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/friend";

    private static final UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH, FRIENDS);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", FRIEND_ID);
    }

    @Override
    public boolean onCreate() {
        mVkDataBaseHelper = VkDataBaseHelper.get(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        if (!isProjectionCorrect(projection)) {
            throw new IllegalArgumentException("Incorrect projection column(s)");
        }

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(FriendsTable.TABLE_NAME);

        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case FRIENDS:
                break;
            case FRIEND_ID:
                queryBuilder.appendWhere(FriendsTable._ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Incorrect uri: " + uri);
        }

        SQLiteDatabase database = mVkDataBaseHelper.getSQLiteDatabase();
        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase database = mVkDataBaseHelper.getSQLiteDatabase();

        long id;
        int uriType = sUriMatcher.match(uri);
        switch(uriType) {
            case FRIENDS:
                database.beginTransaction();
                id = database.insert(FriendsTable.TABLE_NAME, null, values);
                database.setTransactionSuccessful();
                database.endTransaction();
                break;
            default:
                throw new IllegalArgumentException("Incorrect uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return Uri.parse(CONTENT_URI + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mVkDataBaseHelper.getSQLiteDatabase();
        int deletedRows;
        int uriType = sUriMatcher.match(uri);

        switch(uriType) {
            case FRIENDS:
                deletedRows = database.delete(FriendsTable.TABLE_NAME, selection,selectionArgs);
                break;
            case FRIEND_ID:
                String id = uri.getLastPathSegment();

                if(!TextUtils.isEmpty(selection)) {
                    deletedRows = database.delete(FriendsTable.TABLE_NAME,
                            FriendsTable._ID + " = " + id,
                            null);
                } else {
                    deletedRows = database.delete(FriendsTable.TABLE_NAME,
                            FriendsTable._ID + " = " + id + " and " + selection + " = ?",
                            selectionArgs);
                }

                break;
            default:
                throw new IllegalArgumentException("Incorrect uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);

        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mVkDataBaseHelper.getSQLiteDatabase();
        int updatedRows;
        int uriType = sUriMatcher.match(uri);

        switch(uriType) {
            case FRIENDS:
                updatedRows = database.update(FriendsTable.TABLE_NAME,values,selection,selectionArgs);
                break;
            case FRIEND_ID:
                String id = uri.getLastPathSegment();

                if(TextUtils.isEmpty(selection)) {
                    updatedRows = database.update(FriendsTable.TABLE_NAME,
                            values,
                            FriendsTable.TABLE_NAME + " = " + id,
                            selectionArgs);
                } else {
                    updatedRows = database.update(FriendsTable.TABLE_NAME,
                            values,
                            FriendsTable.TABLE_NAME + " = " + id + " and " + selection + " = ?",
                            selectionArgs);
                }

                break;
            default:
                throw new IllegalArgumentException("Incorrect uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);

        return updatedRows;
    }

    public boolean isProjectionCorrect(String[] projection) {
        String[] existing = {
                FriendsTable._ID,
                FriendsTable._FULL_NAME,
                FriendsTable._PHOTO_100,
                FriendsTable._ONLINE,
        };

        List<String> receivedColumns = new ArrayList<>(Arrays.asList(projection));
        List<String> existingColumns = new ArrayList<>(Arrays.asList(existing));

        if (!existingColumns.containsAll(existingColumns)) {
            return false;
        }
        return true;
    }
}
