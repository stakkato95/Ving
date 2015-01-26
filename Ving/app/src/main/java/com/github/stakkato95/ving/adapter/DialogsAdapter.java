package com.github.stakkato95.ving.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.database.DialogsTable;

/**
 * Created by Artyom on 20.01.2015.
 */
public class DialogsAdapter extends ZCursorAdapter {

    public DialogsAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return getLayoutInflater().inflate(R.layout.adapter_dialog, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView photo = (ImageView) view.findViewById(R.id.dialog_photo);
        ImageView lastSenderPhoto = (ImageView) view.findViewById(R.id.dialog_last_sender);
        TextView title = (TextView) view.findViewById(R.id.dialog_name);
        TextView lastMessage = (TextView)view.findViewById(R.id.dialog_body);
        TextView date = (TextView)view.findViewById(R.id.dialog_date);

        String titleText = cursor.getString(cursor.getColumnIndex(DialogsTable._DIALOG_NAME));
        String lastMessageText = cursor.getString(cursor.getColumnIndex(DialogsTable._BODY));
        String photoUrl = cursor.getString(cursor.getColumnIndex(DialogsTable._PHOTO_100));
        String dateText = cursor.getString(cursor.getColumnIndex(DialogsTable._DATE));

        title.setText(titleText);
        lastMessage.setText(lastMessageText);
        date.setText(dateText);
        getImageLoader().obtainImage(photo, photoUrl);

        String lastSenderPhotoUrl = null;
        if (cursor.getString(cursor.getColumnIndex(DialogsTable._LAST_SENDER_PHOTO_100)) != null) {
            lastSenderPhotoUrl = cursor.getString(cursor.getColumnIndex(DialogsTable._LAST_SENDER_PHOTO_100));
            view.setTag(Api.FIELD_DIALOG_HISTORY_CHAT_ID);
        } else {
            long route = cursor.getLong(cursor.getColumnIndex(DialogsTable._ROUTE));
            if (route == Api.ROUTE_IN) {
                lastSenderPhotoUrl = cursor.getString(cursor.getColumnIndex(DialogsTable._PHOTO_100));
            }
            view.setTag(Api.FIELD_DIALOG_HISTORY_USER_ID);
        }
        //TODO implement cease with user message, not friend's one
        if (lastSenderPhotoUrl != null) {
            getImageLoader().obtainImage(lastSenderPhoto, lastSenderPhotoUrl);
        }
    }

}