package com.github.stakkato95.ving.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.database.DialogHistoryTable;

/**
 * Created by Artyom on 15.02.2015.
 */
public class DialogHistoryRecyclerAdapter extends ZRecyclerCursorAdapter {

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        private ImageView mInterlocutorImage;
        private TextView mInterlocutorMessage;
        private ImageView mUserImage;
        private TextView mUserMessage;
        private TextView mUserDate;
        private TextView mInterlocutorDate;
        private LinearLayout mUserContainer;
        private LinearLayout mInterlocutorContainer;

        public HistoryViewHolder(View view) {
            super(view);
            mInterlocutorImage = (ImageView) view.findViewById(R.id.interlocutor_image);
            mInterlocutorMessage = (TextView) view.findViewById(R.id.interlocutor_message);
            mUserImage = (ImageView) view.findViewById(R.id.user_image);
            mUserMessage = (TextView) view.findViewById(R.id.user_message);
            mUserDate = (TextView) view.findViewById(R.id.user_date);
            mInterlocutorDate = (TextView) view.findViewById(R.id.interlocutor_date);
            mInterlocutorContainer = (LinearLayout) view.findViewById(R.id.interlocutor_container);
            mUserContainer = (LinearLayout) view.findViewById(R.id.user_container);
        }

    }

    private class HeaderViewHolder extends ZRecyclerCursorAdapter.HeaderViewHolder {

        private ProgressBar mProgressBar;
        private TextView mText;

        public HeaderViewHolder(View v) {
            super(v);
            mProgressBar = (ProgressBar) v.findViewById(android.R.id.progress);
            mText = (TextView) v.findViewById(android.R.id.text1);
        }

    }

    private HeaderViewHolder mHeaderViewHolder;

    public DialogHistoryRecyclerAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor, int position) {
        HistoryViewHolder vh = (HistoryViewHolder) viewHolder;
        String messageText = cursor.getString(cursor.getColumnIndex(DialogHistoryTable._BODY));
        String imageUrl = cursor.getString(cursor.getColumnIndex(DialogHistoryTable._PHOTO_100));
        String date = cursor.getString(cursor.getColumnIndex(DialogHistoryTable._DATE_TEXT));
        long route = cursor.getLong(cursor.getColumnIndex(DialogHistoryTable._ROUTE));

        if (route == Api.MESSAGE_ROUTE_OUT) {
            vh.mInterlocutorContainer.setVisibility(View.GONE);
            vh.mInterlocutorImage.setVisibility(View.GONE);
            vh.mUserContainer.setVisibility(View.VISIBLE);
            vh.mUserImage.setVisibility(View.VISIBLE);

            vh.mUserMessage.setText(messageText);
            vh.mUserDate.setText(date);
            if (imageUrl != null) {
                getImageLoader().toView(vh.mUserImage).setCircled(true).byUrl(imageUrl);
            }
        } else {
            vh.mInterlocutorContainer.setVisibility(View.VISIBLE);
            vh.mInterlocutorImage.setVisibility(View.VISIBLE);
            vh.mUserContainer.setVisibility(View.GONE);
            vh.mUserImage.setVisibility(View.GONE);

            vh.mInterlocutorMessage.setText(messageText);
            vh.mInterlocutorDate.setText(date);
            getImageLoader().toView(vh.mInterlocutorImage).setCircled(true).byUrl(imageUrl);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == ITEM_REGULAR) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.adapter_dialog_history, viewGroup, false);
            return new HistoryViewHolder(view);
        } else {
            return getHeaderViewHolder(viewGroup);
        }
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderViewHolder(ViewGroup viewGroup) {
        if (mHeaderViewHolder == null) {
            mHeaderViewHolder = new HeaderViewHolder(mHeader);
        }
        return mHeaderViewHolder;
    }
}
