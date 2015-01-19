package com.github.stakkato95.ving.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.github.stakkato95.ving.database.DialogsTable;
import com.github.stakkato95.ving.database.FriendsTable;
import com.github.stakkato95.ving.database.VkDataBaseHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Artyom on 16.01.2015.
 */
public class VingContentProvider extends ContentProvider {

    private VkDataBaseHelper mVkDataBaseHelper;

    //TODO enums
    private static final int URI_FRIENDS = 0;
    private static final int URI_FRIEND_ID = 1;
    private static final int URI_DIALOGS = 2;
    private static final int URI_DIALOGS_ID = 3;

    private static final String AUTHORITY = "com.github.stakkato95.ving.provider";

    public static final Uri FRIENDS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + FriendsTable.NAME);
    public static final Uri DIALOGS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + DialogsTable.NAME);

    public static final String FRIENDS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTHORITY + "." + FriendsTable.NAME;
    public static final String FRIEND_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + FriendsTable.NAME;
    public static final String DIALOGS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTHORITY + "." + DialogsTable.NAME;
    public static final String DIALOGS_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + DialogsTable.NAME;

    private static final UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, FriendsTable.NAME, URI_FRIENDS);
        sUriMatcher.addURI(AUTHORITY, FriendsTable.NAME + "/#", URI_FRIEND_ID);
        sUriMatcher.addURI(AUTHORITY, DialogsTable.NAME, URI_DIALOGS);
        sUriMatcher.addURI(AUTHORITY, DialogsTable.NAME + "/#", URI_DIALOGS_ID);
    }

    @Override
    public boolean onCreate() {
        //TODO NPE
        mVkDataBaseHelper = VkDataBaseHelper.get(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriType = sUriMatcher.match(uri);

        if (projection != null) {
            if (!isProjectionCorrect(uriType, projection)) {
                throw new IllegalArgumentException("Incorrect projection column(s)");
            }
        }

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(FriendsTable.NAME);

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
        Uri resultUri;
        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case URI_FRIENDS:
                try {
                    database.beginTransaction();
                    id = database.insert(FriendsTable.NAME, null, values);
                    database.setTransactionSuccessful();
                    resultUri = ContentUris.withAppendedId(FRIENDS_CONTENT_URI, id);
                } finally {
                    database.endTransaction();
                }
                break;
            default:
                throw new IllegalArgumentException("Incorrect uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //TODO writable/readable
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

    public boolean isProjectionCorrect(int uriType, String[] projection) {
        String[] existingColumns = null;

        switch (uriType) {
            case URI_FRIENDS:
                existingColumns = FriendsTable.PROJECTION;
                break;
            case URI_FRIEND_ID:
                existingColumns = FriendsTable.PROJECTION;
                break;
        }


        Set<String> receivedProjection = new HashSet<>(Arrays.asList(projection));
        Set<String> availableProjection = new HashSet<>(Arrays.asList(existingColumns));

        return availableProjection.containsAll(receivedProjection);
    }

}