package com.github.stakkato95.ving.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.database.FriendsTable;

/**
 * Created by Artyom on 15.02.2015.
 */
public class FriendsRecyclerAdapter extends ZRecyclerCursorAdapter {

    public class FriendsViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImage;
        private ImageView mOnlineImage;
        private TextView mFullNameText;
        private TextView mStatusText;

        public FriendsViewHolder(View view) {
            super(view);
            mImage = (ImageView) view.findViewById(R.id.user_image);
            mOnlineImage = (ImageView) view.findViewById(R.id.online_image);
            mFullNameText = (TextView) view.findViewById(R.id.user_name);
            mStatusText = (TextView) view.findViewById(R.id.user_status);
        }

    }

    public FriendsRecyclerAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor, int position) {
        if (viewHolder instanceof FriendsViewHolder) {
            FriendsViewHolder vh = (FriendsViewHolder) viewHolder;
            String fullName = cursor.getString(cursor.getColumnIndex(FriendsTable._FULL_NAME));
            vh.mFullNameText.setText(fullName);

            String status = cursor.getString(cursor.getColumnIndex(FriendsTable._STATUS));
            if (!status.equals(Api.EMPTY_STRING)) {
                vh.mStatusText.setText(status);
            }

            String imageUrl = cursor.getString(cursor.getColumnIndex(FriendsTable._PHOTO_100));
            getImageLoader().toView(vh.mImage).setCircled(true).byUrl(imageUrl);

            vh.mOnlineImage.setImageBitmap(null);
            try {
                int online = cursor.getInt(cursor.getColumnIndexOrThrow(FriendsTable._ONLINE));
                if (online == Api.USER_ONLINE_MOBILE) {
                    vh.mOnlineImage.setImageResource(R.drawable.ic_friend_online_mobile);
                } else if (online == Api.USER_ONLINE) {
                    vh.mOnlineImage.setImageResource(R.drawable.ic_friend_online_computer);
                }
            } catch (Exception e) {
                //ignored exception
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == ITEM_REGULAR) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.adapter_friend, viewGroup, false);
            view.setOnClickListener(this);
            return new FriendsViewHolder(view);
        } else {
            return getFooterViewHolder();
        }
    }

}
