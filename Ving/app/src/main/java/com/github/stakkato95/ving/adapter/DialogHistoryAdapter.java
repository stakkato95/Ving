package com.github.stakkato95.ving.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.activity.MainActivity;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.database.DialogTable;

/**
 * Created by Artyom on 25.01.2015.
 */
public class DialogHistoryAdapter extends ZCursorAdapter {

    public DialogHistoryAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return getLayoutInflater().inflate(R.layout.adapter_dialog_history, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Cursor cursor = getItem(position);

        if (convertView == null) {
            convertView = getLayoutInflater().inflate(R.layout.adapter_dialog_history, parent, false);
        }
        ImageView interlocutorPhoto = (ImageView) convertView.findViewById(R.id.interlocutor_photo);
        TextView interlocutorMessage = (TextView)convertView.findViewById(R.id.interlocutor_message);
        ImageView userPhoto = (ImageView) convertView.findViewById(R.id.user_photo);
        TextView userMessage = (TextView)convertView.findViewById(R.id.user_message);

        String messageText = cursor.getString(cursor.getColumnIndex(DialogTable._BODY));
        String photoUrl = cursor.getString(cursor.getColumnIndex(DialogTable._PHOTO_100));
        long route = cursor.getLong(cursor.getColumnIndex(DialogTable._ROUTE));
        if (route == Api.ROUTE_OUT) {
            interlocutorMessage.setVisibility(View.GONE);
            interlocutorPhoto.setVisibility(View.GONE);
            userMessage.setVisibility(View.VISIBLE);
            userPhoto.setVisibility(View.VISIBLE);

            userMessage.setText(messageText);
            getImageLoader().obtainImage(userPhoto, photoUrl);
        } else {
            interlocutorMessage.setVisibility(View.VISIBLE);
            interlocutorPhoto.setVisibility(View.VISIBLE);
            userMessage.setVisibility(View.GONE);
            userPhoto.setVisibility(View.GONE);

            interlocutorMessage.setText(messageText);
            getImageLoader().obtainImage(interlocutorPhoto, photoUrl);
        }

        return convertView;
    }

    @Override
    public Cursor getItem(int position) {
        return (Cursor) super.getItem(getCount() - 1 - position);
    }
}