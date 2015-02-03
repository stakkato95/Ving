package com.github.stakkato95.ving.processor;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.github.stakkato95.util.MultiValueMap;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.bo.Dialog;
import com.github.stakkato95.ving.bo.JSONArrayWrapper;
import com.github.stakkato95.ving.bo.User;
import com.github.stakkato95.ving.database.DialogTable;
import com.github.stakkato95.ving.loader.DataLoader;
import com.github.stakkato95.ving.provider.ZContentProvider;
import com.github.stakkato95.ving.source.VkDataSource;
import com.github.stakkato95.ving.utils.ProcessingUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
            String stringDate = ProcessingUtils.getDate(dialog.getDate());
            String dialogBody = dialog.getBody();
            if (dialog.getBody().contains("\n")) {
                dialogBody = dialogBody.replaceAll("\n"," ");
            }

            ContentValues value = new ContentValues();
            value.put(DialogTable._ID, dialog.getId());
            value.put(DialogTable._ROUTE, dialog.getRoute());
            value.put(DialogTable._READ_STATE, dialog.getReadState());
            value.put(DialogTable._DATE, stringDate);
            value.put(DialogTable._BODY, dialogBody);

            if (dialog.getTitle().equals(Api.ONE_INTERLOCUTOR_DIALOG)) {
                value.put(DialogTable._ID, dialog.getUserId());
                value.put(DialogTable._TYPE, DialogTable.ONE_INTERLOCUTOR);
            } else {
                value.put(DialogTable._ID, dialog.getChatId());
                value.put(DialogTable._TYPE, DialogTable.CHAT);
                value.put(DialogTable._DIALOG_NAME, dialog.getTitle());
                value.put(DialogTable._PHOTO_100, dialog.getPhoto());
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
                if (!value.containsKey(DialogTable._DIALOG_NAME)) {
                    //means one interlocutor dialog
                    value.put(DialogTable._DIALOG_NAME, user.getFullName());
                    value.put(DialogTable._PHOTO_100, user.getPhoto());
                    value.put(DialogTable._ONLINE, user.getOnlineMode());

                    if (value.getAsInteger(DialogTable._ROUTE) == Api.ROUTE_OUT) {
                        value.put(DialogTable._LAST_SENDER_PHOTO_100, user.getPhoto());
                    }
                } else {
                    value.put(DialogTable._LAST_SENDER_PHOTO_100, user.getPhoto());
                }
                values.add(value);
            }
        }
        ContentValues[] insertableValues = new ContentValues[values.size()];
        resolver.bulkInsert(ZContentProvider.DIALOGS_CONTENT_URI, values.toArray(insertableValues));
    }

}