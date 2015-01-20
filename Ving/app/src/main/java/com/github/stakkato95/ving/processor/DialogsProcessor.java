package com.github.stakkato95.ving.processor;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.bo.Dialog;
import com.github.stakkato95.ving.bo.JSONArrayWrapper;
import com.github.stakkato95.ving.bo.User;
import com.github.stakkato95.ving.database.DialogsTable;
import com.github.stakkato95.ving.loader.DataLoader;
import com.github.stakkato95.ving.provider.ZContentProvider;
import com.github.stakkato95.ving.source.VkDataSource;

import org.json.JSONObject;

/**
 * Created by Artyom on 19.01.2015.
 */
public class DialogsProcessor extends DatabaseProcessor {

    public static final String ONE_INTERLOCUTOR_DIALOG = " ... ";

    public DialogsProcessor(Context context) {
        super(context);
    }

    @Override
    protected void insertDataFrom(JSONArrayWrapper jsonArray) {
        ContentResolver resolver = getContext().getContentResolver();
        ContentValues[] values = new ContentValues[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Dialog dialog = new Dialog(jsonObject);

            ContentValues value = new ContentValues();
            value.put(DialogsTable._ID, dialog.getId());
            value.put(DialogsTable._DATE, dialog.getDate());
            value.put(DialogsTable._ROUTE, dialog.getRoute());
            value.put(DialogsTable._READ_STATE, dialog.getReadState());
            value.put(DialogsTable._BODY, dialog.getBody());

            if (dialog.getTitle().equals(ONE_INTERLOCUTOR_DIALOG)) {
                User user = null;
                try {
                    //TODO exception is thrown, because we get not a json OBJECT, but a json ARRAY
                    user = DataLoader.getDataDirectly(Api.getUsers() + dialog.getUserId(), new VkDataSource(), new UserProcessor());
                } catch (Exception e) {
                    //TODO throw this one and make method throwable
                }
                if (user != null) {
                    String dialogName = user.getFullName();
                    String photo = user.getPhoto();
                    value.put(DialogsTable._DIALOG_NAME, dialogName);
                    value.put(DialogsTable._PHOTO_100, photo);
                }
            } else {
                value.put(DialogsTable._DIALOG_NAME, dialog.getTitle());
                value.put(DialogsTable._PHOTO_100, dialog.getPhoto());
            }
            values[i] = value;
        }
        resolver.bulkInsert(ZContentProvider.DIALOGS_CONTENT_URI, values);
    }

}