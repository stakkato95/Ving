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

    public FriendsAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return getLayoutInflater().inflate(R.layout.adapter_friend, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView photoImage = (ImageView) view.findViewById(android.R.id.icon);
        ImageView onlineImage = (ImageView) view.findViewById(android.R.id.icon1);
        TextView fullNameText = (TextView) view.findViewById(android.R.id.text1);

        String fullName = cursor.getString(cursor.getColumnIndex(FriendsTable._FULL_NAME));
        fullNameText.setText(fullName);

        String photoUrl = cursor.getString(cursor.getColumnIndex(FriendsTable._PHOTO_100));
        getImageLoader().obtainImage(photoImage, photoUrl);

        onlineImage.setImageBitmap(null);
        try {
            int online = cursor.getInt(cursor.getColumnIndexOrThrow(FriendsTable._ONLINE));
            if (online == 2) {
                onlineImage.setImageResource(R.drawable.ic_friend_online_mobile);
            } else if (online == 1) {
                onlineImage.setImageResource(R.drawable.ic_friend_online_computer);
            }
        } catch (Exception e) {
            //ignored exception
        }
    }

}