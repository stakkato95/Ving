package com.github.stakkato95.ving.processor;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.github.stakkato95.ving.CoreApplication;
import com.github.stakkato95.ving.bo.Dialog;
import com.github.stakkato95.ving.bo.Friend;
import com.github.stakkato95.ving.bo.JSONArrayWrapper;
import com.github.stakkato95.ving.database.DialogsTable;
import com.github.stakkato95.ving.database.FriendsTable;
import com.github.stakkato95.ving.provider.VingContentProvider;

import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by Artyom on 19.01.2015.
 */
public class DialogsProcessor implements DatabaseProcessor<InputStream> {

    public static final String ONE_INTERLOCUTOR_DIALOG = " ... ";

    @Override
    public void process(InputStream inputStream) throws Exception {
        if (inputStream != null) {
            String string = new StringProcessor().process(inputStream);
            JSONArrayWrapper jsonArray = new JSONArrayWrapper(string);
            insertDataFrom(jsonArray);
        }
    }

    private void insertDataFrom(JSONArrayWrapper jsonArray) {
        ContentResolver resolver = CoreApplication.getContext().getContentResolver();
        ContentValues[] values = new ContentValues[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getObject(i);
            Dialog dialog = new Dialog(jsonObject);

            ContentValues value = new ContentValues();
            value.put(DialogsTable._ID, dialog.getId());
            value.put(DialogsTable._DATE, dialog.getDate());
            value.put(DialogsTable._ROUTE, dialog.getRoute());
            value.put(DialogsTable._READ_STATE, dialog.getReadState());
            value.put(DialogsTable._BODY, dialog.getBody());

            if (dialog.getTitle().equals(ONE_INTERLOCUTOR_DIALOG)) {
                String[] projection = {FriendsTable._FULL_NAME, FriendsTable._PHOTO_100};
                String selection = FriendsTable._ID + " = ?";
                String[] selectionArgs = {Long.toString(dialog.getUserId())};
                Cursor cursor = resolver.query(VingContentProvider.FRIENDS_CONTENT_URI, projection, selection, selectionArgs, null);

                if (cursor != null) {
                    String dialogName = cursor.getString(cursor.getColumnIndex(FriendsTable._FULL_NAME));
                    String photo = cursor.getString(cursor.getColumnIndex(FriendsTable._PHOTO_100));
                    value.put(DialogsTable._DIALOG_NAME, dialogName);
                    value.put(DialogsTable._PHOTO_100, photo);
                }
            } else {
                value.put(DialogsTable._DIALOG_NAME, dialog.getTitle());
                value.put(DialogsTable._PHOTO_100, dialog.getPhoto());
            }

            values[i] = value;
        }

        //resolver.bulkInsert(VingContentProvider.DIALOGS_CONTENT_URI, values);
    }

}
