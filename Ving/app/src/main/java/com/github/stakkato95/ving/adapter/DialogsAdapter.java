package com.github.stakkato95.ving.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
        public ImageView onlineImage;
    }

    public static final int ID_KEY = R.string.key_id;

    public DialogsAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = getLayoutInflater().inflate(R.layout.adapter_dialog, parent, false);
        ViewHolder vh = new ViewHolder();
        vh.photo = (ImageView) view.findViewById(R.id.dialog_image);
        vh.lastSenderPhoto = (ImageView) view.findViewById(R.id.last_sender_image);
        vh.title = (TextView) view.findViewById(R.id.dialog_name);
        vh.lastMessage = (TextView)view.findViewById(R.id.dialog_body);
        vh.date = (TextView)view.findViewById(R.id.dialog_date);
        vh.onlineImage = (ImageView)view.findViewById(R.id.online_image);
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

        vh.lastMessage.setText(lastMessageText);
        vh.date.setText(dateText);
        getImageLoader().obtainImage(vh.photo, photoUrl);

        int readState = cursor.getInt(cursor.getColumnIndex(DialogTable._READ_STATE));
        int route = cursor.getInt(cursor.getColumnIndex(DialogTable._ROUTE));
        if (readState == Api.MESSAGE_STATE_UNREAD && route == Api.MESSAGE_ROUTE_IN) {
            Spannable titleSpannable = new SpannableString(titleText);
            titleSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, titleText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            vh.title.setText(titleSpannable);
        } else {
            vh.title.setText(titleText);
        }

        String lastSenderPhotoUrl = cursor.getString(cursor.getColumnIndex(DialogTable._LAST_SENDER_PHOTO_100));
        if (lastSenderPhotoUrl != null) {
            getImageLoader().obtainImage(vh.lastSenderPhoto, lastSenderPhotoUrl);

            vh.lastSenderPhoto.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)vh.lastMessage.getLayoutParams();
            params.addRule(RelativeLayout.RIGHT_OF, R.id.last_sender_image);
            params.addRule(RelativeLayout.END_OF, R.id.last_sender_image);
            params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.last_sender_image);
            params.setMargins(4,0,0,0);
            params.setMarginStart(4);
            params.removeRule(RelativeLayout.ALIGN_LEFT);
            params.removeRule(RelativeLayout.ALIGN_START);
            vh.lastMessage.setLayoutParams(params);
        } else {
            vh.lastSenderPhoto.setVisibility(View.GONE);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) vh.lastMessage.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_LEFT, R.id.dialog_name);
            params.addRule(RelativeLayout.ALIGN_START, R.id.dialog_name);
            params.setMargins(0, 0, 0, 0);
            params.setMarginStart(0);
            vh.lastMessage.setLayoutParams(params);
        }
        //TODO implement cease with user message, not friend's one

        vh.onlineImage.setImageBitmap(null);
        try {
            int online = cursor.getInt(cursor.getColumnIndexOrThrow(DialogTable._ONLINE));
            if (online == 2) {
                vh.onlineImage.setImageResource(R.drawable.ic_friend_online_mobile);
            } else if (online == 1) {
                vh.onlineImage.setImageResource(R.drawable.ic_friend_online_computer);
            }
        } catch (Exception e) {
            //ignored exception
        }

        long type = cursor.getLong(cursor.getColumnIndex(DialogTable._TYPE));
        if (type == DialogTable.ONE_INTERLOCUTOR) {
            view.setTag(ID_KEY, Api.FIELD_MESSAGE_USER_ID);
        } else {
            view.setTag(ID_KEY, Api.FIELD_MESSAGE_CHAT_ID);
        }
    }

}