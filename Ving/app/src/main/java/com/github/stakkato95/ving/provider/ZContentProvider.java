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

import com.github.stakkato95.ving.bo.Dialog;
import com.github.stakkato95.ving.database.DBHelper;
import com.github.stakkato95.ving.database.DialogsDBHelper;
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

    enum UriType {
        FRIENDS(0),
        FRIEND_ID(1),
        DIALOGS(2),
        DIALOGS_ID(3);

        private int mCode;
        private static final Map<Integer,UriType> sEnumMap;

        static {
            sEnumMap = new HashMap<>();
            for (UriType uri : UriType.values()) {
                sEnumMap.put(uri.getIntCode(), uri);
            }
        }

        UriType(int code) {
            mCode = code;
        }

        //these two methods exists just because UriMatcher detect uri type by its int value
        public int getIntCode() {
            return mCode;
        }

        public static UriType castToUri(int index) {
            return sEnumMap.get(index);
        }
    }

    private DBHelper mDBHelper;
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
        sUriMatcher.addURI(AUTHORITY, FriendsTable.NAME, UriType.FRIENDS.getIntCode());
        sUriMatcher.addURI(AUTHORITY, FriendsTable.NAME + "/#", UriType.FRIEND_ID.getIntCode());
        sUriMatcher.addURI(AUTHORITY, DialogsTable.NAME, UriType.DIALOGS.getIntCode());
        sUriMatcher.addURI(AUTHORITY, DialogsTable.NAME + "/#", UriType.DIALOGS_ID.getIntCode());
    }

    @Override
    public boolean onCreate() {
        //TODO NPE
        mDBHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        UriType uriType = UriType.castToUri(sUriMatcher.match(uri));

        if (projection != null) {
            if (!isProjectionCorrect(uriType, projection)) {
                throw new IllegalArgumentException("Incorrect projection column(s)");
            }
        }

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (uriType) {
            case FRIENDS:
                queryBuilder.setTables(FriendsTable.NAME);
                break;
            case FRIEND_ID:
                queryBuilder.appendWhere(FriendsTable._ID + " = " + uri.getLastPathSegment());
                queryBuilder.setTables(FriendsTable.NAME);
                break;
            case DIALOGS:
                queryBuilder.setTables(DialogsTable.NAME);
                break;
            case DIALOGS_ID:
                queryBuilder.setTables(DialogsTable.NAME);
                queryBuilder.appendWhere(DialogsTable._ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Incorrect uri: " + uri);
        }

        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        UriType uriType = UriType.castToUri(sUriMatcher.match(uri));

        switch (uriType) {
            case FRIENDS:
                return FRIENDS_CONTENT_TYPE;
            case FRIEND_ID:
                return FRIEND_CONTENT_ITEM_TYPE;
            case DIALOGS:
                return DIALOGS_CONTENT_TYPE;
            case DIALOGS_ID:
                return DIALOGS_CONTENT_ITEM_TYPE;
        }

        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tableName;
        Uri contentUri;
        Uri resultUri;
        UriType uriType = UriType.castToUri(sUriMatcher.match(uri));
        switch (uriType) {
            case FRIENDS:
                tableName = FriendsTable.NAME;
                contentUri = FRIENDS_CONTENT_URI;
                break;
            case DIALOGS:
                tableName = DialogsTable.NAME;
                contentUri = DIALOGS_CONTENT_URI;
                break;
            default:
                throw new IllegalArgumentException("Incorrect uri: " + uri);
        }

        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        try {
            database.beginTransaction();
            long id = database.insert(tableName, null, values);
            database.setTransactionSuccessful();
            resultUri = ContentUris.withAppendedId(contentUri, id);
        } finally {
            database.endTransaction();
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deletedRows;
        String tableName = null;
        String id;
        UriType uriType = UriType.castToUri(sUriMatcher.match(uri));

        switch (uriType) {
            case FRIENDS:
                tableName = FriendsTable.NAME;
                break;
            case FRIEND_ID:
                id = uri.getLastPathSegment();
                if (!TextUtils.isEmpty(selection)) {
                    selection = FriendsTable._ID + " = " + id;
                    selectionArgs = null;
                } else {
                    selection = FriendsTable._ID + " = " + id + " and " + selection + " = ?";
                }
                break;
            case DIALOGS:
                tableName = DialogsTable.NAME;
                break;
            case DIALOGS_ID:
                id = uri.getLastPathSegment();
                if (!TextUtils.isEmpty(selection)) {
                    selection = DialogsTable._ID + " = " + id;
                    selectionArgs = null;
                } else {
                    selection = DialogsTable._ID + " = " + id + " and " + selection + " = ?";
                }
                break;
            default:
                throw new IllegalArgumentException("Incorrect uri: " + uri);
        }

        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        deletedRows = database.delete(tableName, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updatedRows;
        String tableName = null;
        String id;
        UriType uriType = UriType.castToUri(sUriMatcher.match(uri));

        switch (uriType) {
            case FRIENDS:
                tableName = FriendsTable.NAME;
                break;
            case FRIEND_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = FriendsTable.NAME + " = " + id;
                } else {
                    selection = FriendsTable.NAME + " = " + id + " and " + selection + " = ?";
                }
                break;
            case DIALOGS:
                tableName = DialogsTable.NAME;
                break;
            case DIALOGS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = DialogsTable.NAME + " = " + id;
                } else {
                    selection = DialogsTable.NAME + " = " + id + " and " + selection + " = ?";
                }
                break;
            default:
                throw new IllegalArgumentException("Incorrect uri: " + uri);
        }

        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        updatedRows = database.update(tableName, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return updatedRows;
    }

    public boolean isProjectionCorrect(UriType uriType, String[] projection) {
        String[] existingColumns = null;

        switch (uriType) {
            case FRIENDS:
                existingColumns = FriendsTable.PROJECTION;
                break;
            case FRIEND_ID:
                existingColumns = FriendsTable.PROJECTION;
                break;
            case DIALOGS:
                existingColumns = DialogsTable.PROJECTION;
                break;
            case DIALOGS_ID:
                existingColumns = DialogsTable.PROJECTION;
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