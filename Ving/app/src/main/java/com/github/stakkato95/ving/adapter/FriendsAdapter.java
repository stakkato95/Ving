package com.github.stakkato95.ving.adapter;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.stakkato95.imageloader.ImageLoader;
import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.database.FriendsTable;

/**
 * Created by Artyom on 16.01.2015.
 */
public class FriendsAdapter extends CursorAdapter {

    public interface Callback {
        void onPageLimitReached(int cursorPosition);
    }

    private final LayoutInflater mLayoutInflater;
    private final ImageLoader mImageLoader;
    private final Callback mCallback;
    private final Handler mHandler;
    private static final int DEFAULT_IMAGE_RESOURCE = -1;

    public FriendsAdapter(Context context, Cursor cursor, int flags, Callback callback) {
        super(context, cursor, flags);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageLoader = ImageLoader.get(context);
        mCallback = callback;
        mHandler = new Handler();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mLayoutInflater.inflate(R.layout.adapter_friend, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView photoImage = (ImageView) view.findViewById(android.R.id.icon);
        ImageView onlineImage = (ImageView) view.findViewById(android.R.id.icon1);
        TextView fullNameText = (TextView) view.findViewById(android.R.id.text1);

        String fullName = cursor.getString(cursor.getColumnIndex(FriendsTable._FULL_NAME));
        fullNameText.setText(fullName);

        String photoUrl = cursor.getString(cursor.getColumnIndex(FriendsTable._PHOTO_100));
        mImageLoader.obtainImage(photoImage, photoUrl);

        onlineImage.setImageResource(DEFAULT_IMAGE_RESOURCE);
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


        final int cursorPosition = cursor.getPosition();
        if (cursorPosition == Api.GET_COUNT) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    //TODO like gr
                    mCallback.onPageLimitReached(cursorPosition);
                }
            });
        }

    }

}
