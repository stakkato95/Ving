package com.github.stakkato95.ving.fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;

import com.github.stakkato95.ving.adapter.DialogsRecyclerAdapter;
import com.github.stakkato95.ving.adapter.ZRecyclerCursorAdapter;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.database.DialogTable;
import com.github.stakkato95.ving.fragment.assist.FragmentId;
import com.github.stakkato95.ving.processor.DBProcessor;
import com.github.stakkato95.ving.processor.DialogsDBProcessor;
import com.github.stakkato95.ving.provider.ZContentProvider;

/**
 * Created by Artyom on 18.01.2015.
 */
public class DialogsFragment extends ZQueueFragment {

    @Override
    public ZRecyclerCursorAdapter.OnRecyclerClickListener getOnClickListener() {
        return new ZRecyclerCursorAdapter.OnRecyclerClickListener() {
            @Override
            public void onRecyclerItemClicked(View view) {
                //todo way to determine whether it is a chat or user
                DialogsRecyclerAdapter.DialogsViewHolder vh = (DialogsRecyclerAdapter.DialogsViewHolder)mRecyclerView.getChildViewHolder(view);
                String requestField = vh.getRequestField();
                ClickCallback callback = getCallback();
                if (callback != null) {
                    callback.showDetails(getFragmentId(), requestField);
                }
            }
        };
    }

    @Override
    public ZRecyclerCursorAdapter getAdapter(Context context, Cursor cursor) {
        return new DialogsRecyclerAdapter(context, cursor);
    }

    @Override
    public DBProcessor getProcessor() {
        return new DialogsDBProcessor(getActivity());
    }

    @Override
    public String getRequestUrl() {
        return Api.getDialogs();
    }

    @Override
    public Uri getUri() {
        return ZContentProvider.DIALOGS_CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
        return DialogTable.PROJECTION;
    }

    @Override
    public String[] getProjectionOffline() {
        return DialogTable.PROJECTION_OFFLINE;
    }

    @Override
    public FragmentId getFragmentId() {
        return FragmentId.DIALOG;
    }

}