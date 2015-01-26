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

import com.github.stakkato95.ving.database.DialogHistoryTable;
import com.github.stakkato95.ving.database.ZBaseColumns;
import com.github.stakkato95.ving.database.ZDataBase;
import com.github.stakkato95.ving.database.DialogsTable;
import com.github.stakkato95.ving.database.FriendsTable;

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

    //TODO go to read about enums :(
    //ordinal
    enum UriType {
        FRIEND(0),
        FRIEND_ID(1),
        DIALOG(2),
        DIALOG_ID(3),
        DIALOG_HISTORY(4),
        DIALOG_HISTORY_ID(5);

        //TODO remove magic
        private int mCode;

        private static final Map<Integer,UriType> sEnumMap;

        //TODO remove magic
        static {
            sEnumMap = new HashMap<>();
            for (UriType uri : UriType.values()) {
                sEnumMap.put(uri.getIntCode(), uri);
            }
        }

        //TODO remove magic
        UriType(int code) {
            mCode = code;
        }

        //TODO remove magic
        //these two methods exists just because UriMatcher detect uri type by its int value
        public int getIntCode() {
            return mCode;
        }

        //TODO remove magic
        public static UriType cast(int index) {
            return sEnumMap.get(index);
        }
    }

    private ZDataBase mDBHelper;
    private static final String AUTHORITY = "com.github.stakkato95.ving.provider";

    public static final Uri FRIENDS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + FriendsTable.NAME);
    public static final Uri DIALOGS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + DialogsTable.NAME);
    public static final Uri DIALOGS_HISTORY_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + DialogHistoryTable.NAME);

    public static final String FRIENDS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTHORITY + "." + FriendsTable.NAME;
    public static final String FRIEND_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + FriendsTable.NAME;
    public static final String DIALOGS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTHORITY + "." + DialogsTable.NAME;
    public static final String DIALOGS_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + DialogsTable.NAME;
    public static final String DIALOGS_HISTORY_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTHORITY + "." + DialogHistoryTable.NAME;
    public static final String DIALOGS_HISTORY_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + DialogHistoryTable.NAME;

    private static final UriMatcher sUriMatcher;
    private static Map<UriType,String> sContentType;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, FriendsTable.NAME, UriType.FRIEND.getIntCode());
        sUriMatcher.addURI(AUTHORITY, FriendsTable.NAME + "/#", UriType.FRIEND_ID.getIntCode());
        sUriMatcher.addURI(AUTHORITY, DialogsTable.NAME, UriType.DIALOG.getIntCode());
        sUriMatcher.addURI(AUTHORITY, DialogsTable.NAME + "/#", UriType.DIALOG_ID.getIntCode());
        sUriMatcher.addURI(AUTHORITY, DialogHistoryTable.NAME, UriType.DIALOG_HISTORY.getIntCode());
        sUriMatcher.addURI(AUTHORITY, DialogHistoryTable.NAME + "/#", UriType.DIALOG_HISTORY_ID.getIntCode());

        sContentType = new HashMap<>();
        sContentType.put(UriType.FRIEND,FRIENDS_CONTENT_TYPE);
        sContentType.put(UriType.FRIEND_ID,FRIEND_CONTENT_ITEM_TYPE);
        sContentType.put(UriType.DIALOG,DIALOGS_CONTENT_TYPE);
        sContentType.put(UriType.DIALOG_ID, DIALOGS_CONTENT_ITEM_TYPE);
        sContentType.put(UriType.DIALOG_HISTORY, DIALOGS_HISTORY_CONTENT_TYPE);
        sContentType.put(UriType.DIALOG_HISTORY_ID, DIALOGS_HISTORY_CONTENT_ITEM_TYPE);
    }

    @Override
    public boolean onCreate() {
        //TODO NPE
        mDBHelper = new ZDataBase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        UriType uriType = UriType.cast(sUriMatcher.match(uri));

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
        UriType uriType = UriType.cast(sUriMatcher.match(uri));
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
        try {
            database.beginTransaction();
            long id = database.insert(tableName, null, values);
            database.setTransactionSuccessful();
            resultUri = ContentUris.withAppendedId(uri, id);
        } finally {
            database.endTransaction();
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return resultUri;
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

    public boolean isProjectionCorrect(UriType uriType, String[] projection) {
        String[] existingColumns = null;

        switch (uriType) {
            case FRIEND:
                existingColumns = FriendsTable.PROJECTION;
                break;
            case FRIEND_ID:
                existingColumns = FriendsTable.PROJECTION;
                break;
            case DIALOG:
                existingColumns = DialogsTable.PROJECTION;
                break;
            case DIALOG_ID:
                existingColumns = DialogsTable.PROJECTION;
                break;
            case DIALOG_HISTORY:
                existingColumns = DialogHistoryTable.PROJECTION;
                break;
            case DIALOG_HISTORY_ID:
                existingColumns = DialogHistoryTable.PROJECTION;
                break;
        }

        Set<String> receivedProjection = new HashSet<>(Arrays.asList(projection));
        Set<String> availableProjection;
        if (existingColumns != null) {
            availableProjection = new HashSet<>(Arrays.asList(existingColumns));
        } else {
            availableProjection = Collections.EMPTY_SET;
        }
        return availableProjection.containsAll(receivedProjection);
    }

}