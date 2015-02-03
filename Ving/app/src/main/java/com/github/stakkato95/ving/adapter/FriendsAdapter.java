package com.github.stakkato95.ving.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.database.FriendsTable;

/**
 * Created by Artyom on 16.01.2015.
 */
public class FriendsAdapter extends ZCursorAdapter {

    private class ViewHolder {
        public ImageView photo;
        public ImageView onlineImage;
        public TextView fullNameText;
    }

    public FriendsAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = getLayoutInflater().inflate(R.layout.adapter_friend, parent, false);
        ViewHolder vh = new ViewHolder();
        vh.photo = (ImageView) view.findViewById(R.id.user_image);
        vh.onlineImage = (ImageView) view.findViewById(R.id.online_image);
        vh.fullNameText = (TextView) view.findViewById(R.id.user_name);
        view.setTag(vh);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder vh = (ViewHolder) view.getTag();


        String fullName = cursor.getString(cursor.getColumnIndex(FriendsTable._FULL_NAME));
        vh.fullNameText.setText(fullName);

        String photoUrl = cursor.getString(cursor.getColumnIndex(FriendsTable._PHOTO_100));
        getImageLoader().obtainImage(vh.photo, photoUrl);

        vh.onlineImage.setImageBitmap(null);
        try {
            int online = cursor.getInt(cursor.getColumnIndexOrThrow(FriendsTable._ONLINE));
            if (online == 2) {
                vh.onlineImage.setImageResource(R.drawable.ic_friend_online_mobile);
            } else if (online == 1) {
                vh.onlineImage.setImageResource(R.drawable.ic_friend_online_computer);
            }
        } catch (Exception e) {
            //ignored exception
        }
    }

}