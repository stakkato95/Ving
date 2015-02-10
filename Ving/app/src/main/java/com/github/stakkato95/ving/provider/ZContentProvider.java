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
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.github.stakkato95.ving.database.DialogHistoryTable;
import com.github.stakkato95.ving.database.DialogTable;
import com.github.stakkato95.ving.database.FriendsTable;
import com.github.stakkato95.ving.database.ZBaseColumns;
import com.github.stakkato95.ving.database.ZDataBase;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Artyom on 16.01.2015.
 */
public class ZContentProvider extends ContentProvider {

    enum UriType {
        FRIEND,
        FRIEND_ID,
        DIALOG,
        DIALOG_ID,
        DIALOG_HISTORY,
        DIALOG_HISTORY_ID
    }

    private ZDataBase mDBHelper;
    private static final String AUTHORITY = "com.github.stakkato95.ving.provider";

    public static final Uri FRIENDS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + FriendsTable.NAME);
    public static final Uri DIALOGS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + DialogTable.NAME);
    public static final Uri DIALOGS_HISTORY_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + DialogHistoryTable.NAME);

    public static final String FRIENDS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTHORITY + "." + FriendsTable.NAME;
    public static final String FRIEND_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + FriendsTable.NAME;
    public static final String DIALOGS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTHORITY + "." + DialogTable.NAME;
    public static final String DIALOGS_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + DialogTable.NAME;
    public static final String DIALOGS_HISTORY_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTHORITY + "." + DialogHistoryTable.NAME;
    public static final String DIALOGS_HISTORY_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + DialogHistoryTable.NAME;

    private static final UriMatcher sUriMatcher;
    private static Map<Integer, String> sContentType;
    private static Map<Integer, String[]> sProjections;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, FriendsTable.NAME, UriType.FRIEND.ordinal());
        sUriMatcher.addURI(AUTHORITY, FriendsTable.NAME + "/#", UriType.FRIEND_ID.ordinal());
        sUriMatcher.addURI(AUTHORITY, DialogTable.NAME, UriType.DIALOG.ordinal());
        sUriMatcher.addURI(AUTHORITY, DialogTable.NAME + "/#", UriType.DIALOG_ID.ordinal());
        sUriMatcher.addURI(AUTHORITY, DialogHistoryTable.NAME, UriType.DIALOG_HISTORY.ordinal());
        sUriMatcher.addURI(AUTHORITY, DialogHistoryTable.NAME + "/#", UriType.DIALOG_HISTORY_ID.ordinal());

        sContentType = new HashMap<>();
        sContentType.put(UriType.FRIEND.ordinal(), FRIENDS_CONTENT_TYPE);
        sContentType.put(UriType.FRIEND_ID.ordinal(), FRIEND_CONTENT_ITEM_TYPE);
        sContentType.put(UriType.DIALOG.ordinal(), DIALOGS_CONTENT_TYPE);
        sContentType.put(UriType.DIALOG_ID.ordinal(), DIALOGS_CONTENT_ITEM_TYPE);
        sContentType.put(UriType.DIALOG_HISTORY.ordinal(), DIALOGS_HISTORY_CONTENT_TYPE);
        sContentType.put(UriType.DIALOG_HISTORY_ID.ordinal(), DIALOGS_HISTORY_CONTENT_ITEM_TYPE);

        sProjections = new HashMap<>();
        sProjections.put(UriType.FRIEND.ordinal(), FriendsTable.PROJECTION);
        sProjections.put(UriType.FRIEND_ID.ordinal(), FriendsTable.PROJECTION);
        sProjections.put(UriType.DIALOG.ordinal(), DialogTable.PROJECTION);
        sProjections.put(UriType.DIALOG_ID.ordinal(), DialogTable.PROJECTION);
        sProjections.put(UriType.DIALOG_HISTORY.ordinal(), DialogHistoryTable.PROJECTION);
        sProjections.put(UriType.DIALOG_HISTORY_ID.ordinal(), DialogHistoryTable.PROJECTION);
    }

    @Override
    public boolean onCreate() {
        //TODO NPE
        mDBHelper = new ZDataBase(getContext());
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

        String tableName = uri.getLastPathSegment();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(tableName);

        if (uri.getPathSegments().size() != 1) {
            queryBuilder.appendWhere(ZBaseColumns._ID + " = " + uri.getLastPathSegment());
        }

        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        int uriType = sUriMatcher.match(uri);
        return sContentType.get(uriType);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri resultUri;

        if (uri.getPathSegments().size() != 1) {
            throw new IllegalArgumentException("Incorrect uri: " + uri);
        }

        String tableName = uri.getLastPathSegment();
        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        long id = database.insert(tableName, null, values);
        resultUri = ContentUris.withAppendedId(uri, id);
        return resultUri;
    }

    @Override
    public int bulkInsert(Uri uri, @NonNull ContentValues[] values) {
        int numValues = values.length;
        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        try {
            database.beginTransaction();
            for (int i = 0; i < numValues; i++) {
                if (i == numValues - 1) {
                    insert(uri, values[i]);
                    getContext().getContentResolver().notifyChange(uri, null);
                } else {

                    insert(uri, values[i]);
                }
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
        return numValues;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deletedRows;
        String id;
        String tableName = uri.getLastPathSegment();

        //TODO handle situation with incorrect uri
        if (uri.getPathSegments().size() != 1) {
            id = uri.getLastPathSegment();
            if (!TextUtils.isEmpty(selection)) {
                selection = ZBaseColumns._ID + " = " + id;
                selectionArgs = null;
            } else {
                selection = ZBaseColumns._ID + " = " + id + " and " + selection + " = ?";
            }
        }

        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        deletedRows = database.delete(tableName, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updatedRows;
        String id;
        String tableName = uri.getLastPathSegment();

        if (uri.getPathSegments().size() != 1) {
            id = uri.getLastPathSegment();
            if (!TextUtils.isEmpty(selection)) {
                selection = ZBaseColumns._ID + " = " + id;
                selectionArgs = null;
            } else {
                selection = ZBaseColumns._ID + " = " + id + " and " + selection + " = ?";
            }
        }

        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        updatedRows = database.update(tableName, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return updatedRows;
    }

    public boolean isProjectionCorrect(int uriType, String[] projection) {
        String[] existingColumns = sProjections.get(uriType);
        Set<String> availableProjection;
        Set<String> receivedProjection = new HashSet<>(Arrays.asList(projection));
        if (existingColumns != null) {
            availableProjection = new HashSet<>(Arrays.asList(existingColumns));
        } else {
            availableProjection = Collections.EMPTY_SET;
        }
        return availableProjection.containsAll(receivedProjection);
    }

}