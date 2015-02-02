package com.github.stakkato95.ving.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.database.DialogTable;

/**
 * Created by Artyom on 20.01.2015.
 */
public class DialogsAdapter extends ZCursorAdapter {

    private class ViewHolder {
        public ImageView photo;
        public ImageView lastSenderPhoto;
        public TextView title;
        public TextView lastMessage;
        public TextView date;
    }

    public static final int ID_KEY = R.string.key_id;

    public DialogsAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = getLayoutInflater().inflate(R.layout.adapter_dialog, parent, false);
        ViewHolder vh = new ViewHolder();
        vh.photo = (ImageView) view.findViewById(R.id.dialog_photo);
        vh.lastSenderPhoto = (ImageView) view.findViewById(R.id.dialog_last_sender);
        vh.title = (TextView) view.findViewById(R.id.dialog_name);
        vh.lastMessage = (TextView)view.findViewById(R.id.dialog_body);
        vh.date = (TextView)view.findViewById(R.id.dialog_date);
        view.setTag(vh);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder vh = (ViewHolder)view.getTag();

        String titleText = cursor.getString(cursor.getColumnIndex(DialogTable._DIALOG_NAME));
        String lastMessageText = cursor.getString(cursor.getColumnIndex(DialogTable._BODY));
        String photoUrl = cursor.getString(cursor.getColumnIndex(DialogTable._PHOTO_100));
        String dateText = cursor.getString(cursor.getColumnIndex(DialogTable._DATE));

        vh.title.setText(titleText);
        vh.lastMessage.setText(lastMessageText);
        vh.date.setText(dateText);
        getImageLoader().obtainImage(vh.photo, photoUrl);

        String lastSenderPhotoUrl = null;
        if (cursor.getString(cursor.getColumnIndex(DialogTable._LAST_SENDER_PHOTO_100)) != null) {
            lastSenderPhotoUrl = cursor.getString(cursor.getColumnIndex(DialogTable._LAST_SENDER_PHOTO_100));
            view.setTag(ID_KEY, Api.FIELD_DIALOG_HISTORY_CHAT_ID);
        } else {
            long route = cursor.getLong(cursor.getColumnIndex(DialogTable._ROUTE));
            if (route == Api.ROUTE_IN) {
                lastSenderPhotoUrl = cursor.getString(cursor.getColumnIndex(DialogTable._PHOTO_100));
            }
            view.setTag(ID_KEY, Api.FIELD_DIALOG_HISTORY_USER_ID);
        }
        //TODO implement cease with user message, not friend's one
        if (lastSenderPhotoUrl != null) {
            getImageLoader().obtainImage(vh.lastSenderPhoto, lastSenderPhotoUrl);
        }
    }

}