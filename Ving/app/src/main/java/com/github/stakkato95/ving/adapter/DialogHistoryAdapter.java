package com.github.stakkato95.ving.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.api.Shipper;
import com.github.stakkato95.ving.database.DialogHistoryTable;

/**
 * Created by Artyom on 25.01.2015.
 */
public class DialogHistoryAdapter extends ZCursorAdapter implements Shipper.Callback<Integer> {

    private class ViewHolder {
        public ImageView interlocutorImage;
        public TextView interlocutorMessage;
        public ImageView userImage;
        public TextView userMessage;
        public TextView userDate;
        public TextView interlocutorDate;
        public LinearLayout userContainer;
        public LinearLayout interlocutorContainer;
    }

    public DialogHistoryAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = getLayoutInflater().inflate(R.layout.adapter_dialog_history, parent, false);
        ViewHolder vh = new ViewHolder();
        vh.interlocutorImage = (ImageView) view.findViewById(R.id.interlocutor_image);
        vh.interlocutorMessage = (TextView) view.findViewById(R.id.interlocutor_message);
        vh.userImage = (ImageView) view.findViewById(R.id.user_image);
        vh.userMessage = (TextView) view.findViewById(R.id.user_message);
        vh.userDate = (TextView) view.findViewById(R.id.user_date);
        vh.interlocutorDate = (TextView) view.findViewById(R.id.interlocutor_date);
        vh.interlocutorContainer = (LinearLayout) view.findViewById(R.id.interlocutor_container);
        vh.userContainer = (LinearLayout) view.findViewById(R.id.user_container);
        view.setTag(vh);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder vh = (ViewHolder) view.getTag();

        String messageText = cursor.getString(cursor.getColumnIndex(DialogHistoryTable._BODY));
        String imageUrl = cursor.getString(cursor.getColumnIndex(DialogHistoryTable._PHOTO_100));
        String date = cursor.getString(cursor.getColumnIndex(DialogHistoryTable._DATE_TEXT));
        long route = cursor.getLong(cursor.getColumnIndex(DialogHistoryTable._ROUTE));

        if (route == Api.MESSAGE_ROUTE_OUT) {
            vh.interlocutorContainer.setVisibility(View.GONE);
            vh.interlocutorImage.setVisibility(View.GONE);
            vh.userContainer.setVisibility(View.VISIBLE);
            vh.userImage.setVisibility(View.VISIBLE);

            vh.userMessage.setText(messageText);
            vh.userDate.setText(date);
            if (imageUrl != null) {
                getImageLoader().toView(vh.userImage).setCircled(true).byUrl(imageUrl);
            }
        } else {
            vh.interlocutorContainer.setVisibility(View.VISIBLE);
            vh.interlocutorImage.setVisibility(View.VISIBLE);
            vh.userContainer.setVisibility(View.GONE);
            vh.userImage.setVisibility(View.GONE);

            vh.interlocutorMessage.setText(messageText);
            vh.interlocutorDate.setText(date);
            getImageLoader().toView(vh.interlocutorImage).setCircled(true).byUrl(imageUrl);
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void onShippingPerformed(Integer integer) {
        //todo after training
    }

}