package com.github.stakkato95.ving.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.stakkato95.loader.ImageLoader;
import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.database.FriendsTable;

/**
 * Created by Artyom on 16.01.2015.
 */
public class FriendsAdapter extends CursorAdapter {

    class ViewHolder {
        ImageView photoImage;
        ImageView onlineImage;
        TextView fullNameText;
    }

    private final LayoutInflater mLayoutInflater;
    private final ImageLoader mImageLoader;
    private static final int DEFAULT_IMAGE_RESOURCE = -1;

    public FriendsAdapter(Context context, Cursor cursor, int flags) {
        super(context,cursor,flags);
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageLoader = ImageLoader.get(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mLayoutInflater.inflate(R.layout.adapter_friend,parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder;
        if (cursor.getPosition() == 0) {
            viewHolder = new ViewHolder();
            viewHolder.photoImage = (ImageView)view.findViewById(android.R.id.icon);
            viewHolder.onlineImage = (ImageView)view.findViewById(android.R.id.icon1);
            viewHolder.fullNameText = (TextView)view.findViewById(android.R.id.text1);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }

        String fullName = cursor.getString(cursor.getColumnIndex(FriendsTable._FULL_NAME));
        viewHolder.fullNameText.setText(fullName);
        String photoUrl = cursor.getString(cursor.getColumnIndex(FriendsTable._PHOTO_100));
        mImageLoader.obtainImage(viewHolder.photoImage, photoUrl);


        viewHolder.onlineImage.setImageResource(DEFAULT_IMAGE_RESOURCE);
        int online = cursor.getInt(cursor.getColumnIndex(FriendsTable._ONLINE));

        //online_mobile = 2, online = 1, offline = 0
        if(online == 2) {
            viewHolder.onlineImage.setImageResource(R.drawable.ic_friend_online_mobile);
        } else if (online == 1){
            viewHolder.onlineImage.setImageResource(R.drawable.ic_friend_online_computer);
        }
    }

}
