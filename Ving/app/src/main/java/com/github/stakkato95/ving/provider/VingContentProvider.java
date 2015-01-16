package com.github.stakkato95.ving.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
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

    private static final int URI_FRIENDS = 0;
    private static final int URI_FRIEND_ID = 1;

    private static final String AUTHORITY = "com.github.stakkato95.ving.provider";

    public static final Uri FRIENDS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + FriendsTable.NAME);

    public static final String FRIENDS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTHORITY + "." + FriendsTable.NAME;
    public static final String FRIEND_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + FriendsTable.NAME;

    private static final UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, FriendsTable.NAME, URI_FRIENDS);
        sUriMatcher.addURI(AUTHORITY, FriendsTable.NAME + "/#", URI_FRIEND_ID);
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
        queryBuilder.setTables(FriendsTable.NAME);

        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case URI_FRIENDS:
                break;
            case URI_FRIEND_ID:
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
        int uriType = sUriMatcher.match(uri);

        switch (uriType) {
            case URI_FRIEND_ID:
                return FRIEND_CONTENT_ITEM_TYPE;
            case URI_FRIENDS:
                return FRIENDS_CONTENT_TYPE;
        }

        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase database = mVkDataBaseHelper.getSQLiteDatabase();

        long id;
        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case URI_FRIENDS:
                try {
                    database.beginTransaction();
                    id = database.insert(FriendsTable.NAME, null, values);
                    database.setTransactionSuccessful();
                } finally {
                    database.endTransaction();
                }
                break;
            default:
                throw new IllegalArgumentException("Incorrect uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(FRIENDS_CONTENT_URI + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mVkDataBaseHelper.getSQLiteDatabase();
        int deletedRows;
        int uriType = sUriMatcher.match(uri);

        switch (uriType) {
            case URI_FRIENDS:
                deletedRows = database.delete(FriendsTable.NAME, selection, selectionArgs);
                break;
            case URI_FRIEND_ID:
                String id = uri.getLastPathSegment();

                if (!TextUtils.isEmpty(selection)) {
                    deletedRows = database.delete(FriendsTable.NAME,
                            FriendsTable._ID + " = " + id,
                            null);
                } else {
                    deletedRows = database.delete(FriendsTable.NAME,
                            FriendsTable._ID + " = " + id + " and " + selection + " = ?",
                            selectionArgs);
                }

                break;
            default:
                throw new IllegalArgumentException("Incorrect uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mVkDataBaseHelper.getSQLiteDatabase();
        int updatedRows;
        int uriType = sUriMatcher.match(uri);

        switch (uriType) {
            case URI_FRIENDS:
                updatedRows = database.update(FriendsTable.NAME, values, selection, selectionArgs);
                break;
            case URI_FRIEND_ID:
                String id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)) {
                    updatedRows = database.update(FriendsTable.NAME,
                            values,
                            FriendsTable.NAME + " = " + id,
                            selectionArgs);
                } else {
                    updatedRows = database.update(FriendsTable.NAME,
                            values,
                            FriendsTable.NAME + " = " + id + " and " + selection + " = ?",
                            selectionArgs);
                }

                break;
            default:
                throw new IllegalArgumentException("Incorrect uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

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
