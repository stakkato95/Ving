package com.github.stakkato95.ving.processor;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.github.stakkato95.util.MultiValueMap;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.bo.DialogHistory;
import com.github.stakkato95.ving.bo.JSONArrayWrapper;
import com.github.stakkato95.ving.bo.User;
import com.github.stakkato95.ving.database.DialogHistoryTable;
import com.github.stakkato95.ving.loader.DataLoader;
import com.github.stakkato95.ving.provider.ZContentProvider;
import com.github.stakkato95.ving.source.VkDataSource;
import com.github.stakkato95.ving.utils.ProcessingUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Artyom on 25.01.2015.
 */
public class DialogHistoryDBProcessor extends DBProcessor {

    public DialogHistoryDBProcessor(Context context) {
        super(context);
    }

    @Override
    protected void insertDataFrom(JSONArrayWrapper jsonArray) throws Exception {
        ContentResolver resolver = getContext().getContentResolver();
        MultiValueMap<Long, ContentValues> valuesMap = new MultiValueMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            DialogHistory history = new DialogHistory(jsonObject);
            String stringDate = ProcessingUtils.getDate(history.getDate());

            ContentValues value = new ContentValues();
            value.put(DialogHistoryTable._ID, history.getId());
            value.put(DialogHistoryTable._ROUTE, history.getRoute());
            value.put(DialogHistoryTable._READ_STATE, history.getReadState());
            value.put(DialogHistoryTable._BODY, history.getBody());
            value.put(DialogHistoryTable._PHOTO_100, history.getPhoto());
            value.put(DialogHistoryTable._DATE, history.getDate());
            value.put(DialogHistoryTable._DATE_TEXT, stringDate);
            valuesMap.put(history.getFromId(), value);
        }

        long id;
        Set<Map.Entry<Long, List<ContentValues>>> keyValuePairs = valuesMap.entrySet();
        StringBuilder idBatch = new StringBuilder();
        for (Map.Entry<Long, List<ContentValues>> pair : keyValuePairs) {
            id = pair.getKey();
            idBatch.append(id).append(',');
        }
        User[] users = DataLoader.getDataDirectly(Api.getUser() + idBatch + "&" + Api.FIELD_USER_FIELDS + Api._PHOTO_100, new VkDataSource(), new UserProcessor());

        List<ContentValues> values = new ArrayList<>();
        for (User user : users) {
            List<ContentValues> configuredValues = valuesMap.get(user.getId());
            for (ContentValues value : configuredValues) {
                value.put(DialogHistoryTable._FROM_ID, user.getFullName());
                value.put(DialogHistoryTable._PHOTO_100, user.getPhoto100());
                values.add(value);
            }
        }
        resolver.bulkInsert(ZContentProvider.DIALOGS_HISTORY_CONTENT_URI, values.toArray(new ContentValues[values.size()]));
    }

}