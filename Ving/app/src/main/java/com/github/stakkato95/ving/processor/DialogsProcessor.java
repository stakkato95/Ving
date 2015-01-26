package com.github.stakkato95.ving.processor;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.github.stakkato95.util.MultiValueMap;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.bo.Dialog;
import com.github.stakkato95.ving.bo.JSONArrayWrapper;
import com.github.stakkato95.ving.bo.User;
import com.github.stakkato95.ving.database.DialogsTable;
import com.github.stakkato95.ving.loader.DataLoader;
import com.github.stakkato95.ving.provider.ZContentProvider;
import com.github.stakkato95.ving.source.VkDataSource;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Artyom on 19.01.2015.
 */
public class DialogsProcessor extends DatabaseProcessor {

    public DialogsProcessor(Context context) {
        super(context);
    }

    @Override
    protected void insertDataFrom(JSONArrayWrapper jsonArray) throws Exception {
        ContentResolver resolver = getContext().getContentResolver();
        MultiValueMap<Long,ContentValues> valuesMap = new MultiValueMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Dialog dialog = new Dialog(jsonObject);

            ContentValues value = new ContentValues();
            value.put(DialogsTable._ID, dialog.getId());
            value.put(DialogsTable._ROUTE, dialog.getRoute());
            value.put(DialogsTable._READ_STATE, dialog.getReadState());

            Date date = new Date(dialog.getDate());
            value.put(DialogsTable._DATE, date.toString().substring(0,10));

            String dialogBody = dialog.getBody();
            if (dialog.getBody().contains("\n")) {
                dialogBody = dialogBody.replaceAll("\n"," ");
            }
            value.put(DialogsTable._BODY, dialogBody);

            if (!dialog.getTitle().equals(Api.ONE_INTERLOCUTOR_DIALOG)) {
                value.put(DialogsTable._ID, dialog.getChatId());
                value.put(DialogsTable._DIALOG_NAME, dialog.getTitle());
                value.put(DialogsTable._PHOTO_100, dialog.getPhoto());
            } else {
                value.put(DialogsTable._ID, dialog.getUserId());
            }
            valuesMap.put(dialog.getUserId(),value);
        }

        long id;
        Set<Map.Entry<Long,List<ContentValues>>> keyValuePairs = valuesMap.entrySet();
        StringBuilder idBatch = new StringBuilder();
        for (Map.Entry<Long,List<ContentValues>> pair : keyValuePairs) {
            id = pair.getKey();
            idBatch.append(id).append(',');
        }
        User[] users = DataLoader.getDataDirectly(Api.getUsers() + idBatch, new VkDataSource(), new UserProcessor());

        List<ContentValues> values = new ArrayList<>();
        for (User user : users) {
            List<ContentValues> configuredValues = valuesMap.get(user.getId());

            for (ContentValues value : configuredValues) {
                if (!value.containsKey(DialogsTable._DIALOG_NAME)) {
                    value.put(DialogsTable._DIALOG_NAME, user.getFullName());
                    value.put(DialogsTable._PHOTO_100, user.getPhoto());
                } else {
                    value.put(DialogsTable._LAST_SENDER_PHOTO_100, user.getPhoto());
                }
                values.add(value);
            }
        }
        ContentValues[] insertableValues = new ContentValues[values.size()];
        resolver.bulkInsert(ZContentProvider.DIALOGS_CONTENT_URI, values.toArray(insertableValues));
    }

}