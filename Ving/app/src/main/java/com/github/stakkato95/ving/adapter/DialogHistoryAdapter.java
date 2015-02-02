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
 * Created by Artyom on 25.01.2015.
 */
public class DialogHistoryAdapter extends ZCursorAdapter {

    private class ViewHolder {
        public ImageView interlocutorPhoto;
        public TextView interlocutorMessage;
        public ImageView userPhoto;
        public TextView userMessage;
    }

    public DialogHistoryAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = getLayoutInflater().inflate(R.layout.adapter_dialog_history, parent, false);
        ViewHolder vh = new ViewHolder();
        vh.interlocutorPhoto = (ImageView) view.findViewById(R.id.interlocutor_photo);
        vh.interlocutorMessage = (TextView) view.findViewById(R.id.interlocutor_message);
        vh.userPhoto = (ImageView) view.findViewById(R.id.user_photo);
        vh.userMessage = (TextView) view.findViewById(R.id.user_message);
        view.setTag(vh);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder vh = (ViewHolder) view.getTag();

        String messageText = cursor.getString(cursor.getColumnIndex(DialogTable._BODY));
        String photoUrl = cursor.getString(cursor.getColumnIndex(DialogTable._PHOTO_100));
        long route = cursor.getLong(cursor.getColumnIndex(DialogTable._ROUTE));
        if (route == Api.ROUTE_OUT) {
            vh.interlocutorMessage.setVisibility(View.GONE);
            vh.interlocutorPhoto.setVisibility(View.GONE);
            vh.userMessage.setVisibility(View.VISIBLE);
            vh.userPhoto.setVisibility(View.VISIBLE);

            vh.userMessage.setText(messageText);
            getImageLoader().obtainImage(vh.userPhoto, photoUrl);
        } else {
            vh.interlocutorMessage.setVisibility(View.VISIBLE);
            vh.interlocutorPhoto.setVisibility(View.VISIBLE);
            vh.userMessage.setVisibility(View.GONE);
            vh.userPhoto.setVisibility(View.GONE);

            vh.interlocutorMessage.setText(messageText);
            getImageLoader().obtainImage(vh.interlocutorPhoto, photoUrl);
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

}