package com.github.stakkato95.ving.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.database.DialogTable;

/**
 * Created by Artyom on 15.02.2015.
 */
public class DialogsRecyclerAdapter extends ZRecyclerCursorAdapter {

    public class DialogsViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImage;
        public ImageView mLastSenderImage;
        public TextView mTitle;
        public TextView mLastMessage;
        public TextView mDate;
        public ImageView mOnlineImage;
        private String mRequestField;

        public DialogsViewHolder(View view) {
            super(view);
            mImage = (ImageView) view.findViewById(R.id.dialog_image);
            mLastSenderImage = (ImageView) view.findViewById(R.id.last_sender_image);
            mTitle = (TextView) view.findViewById(R.id.dialog_name);
            mLastMessage = (TextView) view.findViewById(R.id.dialog_body);
            mDate = (TextView) view.findViewById(R.id.dialog_date);
            mOnlineImage = (ImageView) view.findViewById(R.id.online_image);
        }

        public void setRequestField(String requestField) {
            mRequestField = requestField;
        }

        public String getRequestField() {
            return mRequestField;
        }

    }

    private class FooterViewHolder extends ZRecyclerCursorAdapter.FooterViewHolder {

        private ProgressBar mProgressBar;
        private TextView mText;

        public FooterViewHolder(View v) {
            super(v);
            mProgressBar = (ProgressBar) v.findViewById(android.R.id.progress);
            mText = (TextView) v.findViewById(android.R.id.text1);
        }

    }

    private FooterViewHolder mFooterViewHolder;

    public DialogsRecyclerAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor, int position) {
        if (position != getRealItemCount()) {
            DialogsViewHolder vh = (DialogsViewHolder) viewHolder;
            String titleText = cursor.getString(cursor.getColumnIndex(DialogTable._DIALOG_NAME));
            String lastMessageText = cursor.getString(cursor.getColumnIndex(DialogTable._BODY));
            String imageUrl = cursor.getString(cursor.getColumnIndex(DialogTable._PHOTO_100));
            String dateText = cursor.getString(cursor.getColumnIndex(DialogTable._DATE));

            vh.mLastMessage.setText(lastMessageText);
            vh.mDate.setText(dateText);
            getImageLoader().toView(vh.mImage).setCircled(true).byUrl(imageUrl);

            int readState = cursor.getInt(cursor.getColumnIndex(DialogTable._READ_STATE));
            int route = cursor.getInt(cursor.getColumnIndex(DialogTable._ROUTE));
            if (readState == Api.MESSAGE_STATE_UNREAD && route == Api.MESSAGE_ROUTE_IN) {
                Spannable titleSpannable = new SpannableString(titleText);
                titleSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, titleSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                vh.mTitle.setText(titleSpannable);
            } else {
                vh.mTitle.setText(titleText);
            }

            String lastSenderImageUrl = cursor.getString(cursor.getColumnIndex(DialogTable._LAST_SENDER_PHOTO_100));
            if (lastSenderImageUrl != null) {
                getImageLoader().toView(vh.mLastSenderImage).setCircled(true).byUrl(lastSenderImageUrl);

                vh.mLastSenderImage.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) vh.mLastMessage.getLayoutParams();
                params.addRule(RelativeLayout.RIGHT_OF, R.id.last_sender_image);
                params.addRule(RelativeLayout.END_OF, R.id.last_sender_image);
                params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.last_sender_image);
                params.setMargins(4, 0, 0, 0);
                params.setMarginStart(4);
                params.removeRule(RelativeLayout.ALIGN_LEFT);
                params.removeRule(RelativeLayout.ALIGN_START);
                vh.mLastMessage.setLayoutParams(params);
            } else {
                vh.mLastSenderImage.setVisibility(View.GONE);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) vh.mLastMessage.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_LEFT, R.id.dialog_name);
                params.addRule(RelativeLayout.ALIGN_START, R.id.dialog_name);
                params.setMargins(0, 0, 0, 0);
                params.setMarginStart(0);
                vh.mLastMessage.setLayoutParams(params);
            }
            //TODO implement cease with user message, not friend's one

            vh.mOnlineImage.setImageBitmap(null);
            try {
                int online = cursor.getInt(cursor.getColumnIndexOrThrow(DialogTable._ONLINE));
                if (online == 2) {
                    vh.mOnlineImage.setImageResource(R.drawable.ic_friend_online_mobile);
                } else if (online == 1) {
                    vh.mOnlineImage.setImageResource(R.drawable.ic_friend_online_computer);
                }
            } catch (Exception e) {
                //ignored exception
            }

            long type = cursor.getLong(cursor.getColumnIndex(DialogTable._TYPE));
            long id = cursor.getLong(cursor.getColumnIndex(DialogTable._ID));
            if (type == DialogTable.ONE_INTERLOCUTOR) {
                vh.setRequestField(Api.FIELD_MESSAGE_USER_ID + id);
            } else {
                vh.setRequestField(Api.FIELD_MESSAGE_CHAT_ID + id);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == ITEM_REGULAR) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.adapter_dialog, viewGroup, false);
            view.setOnClickListener(this);
            return new DialogsViewHolder(view);
        } else {
            return getFooterViewHolder(viewGroup);
        }
    }

    @Override
    protected RecyclerView.ViewHolder getFooterViewHolder(ViewGroup viewGroup) {
        if (mFooterViewHolder == null) {
            mFooterViewHolder = new FooterViewHolder(mFooter);
        }
        return mFooterViewHolder;
    }
}
