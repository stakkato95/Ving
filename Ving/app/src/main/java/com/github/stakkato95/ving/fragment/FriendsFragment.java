package com.github.stakkato95.ving.fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.widget.ListView;

import com.github.stakkato95.ving.adapter.FriendsAdapter;
import com.github.stakkato95.ving.adapter.FriendsRecyclerAdapter;
import com.github.stakkato95.ving.adapter.ZCursorAdapter;
import com.github.stakkato95.ving.adapter.ZRecyclerCursorAdapter;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.database.FriendsTable;
import com.github.stakkato95.ving.fragment.assist.FragmentId;
import com.github.stakkato95.ving.processor.DBProcessor;
import com.github.stakkato95.ving.processor.FriendsDBProcessor;
import com.github.stakkato95.ving.provider.ZContentProvider;

/**
 * Created by Artyom on 19.01.2015.
 */
public class FriendsFragment extends ZQueueFragment {

    @Override
    public ZRecyclerCursorAdapter.OnRecyclerClickListener getOnClickListener() {
        return new ZRecyclerCursorAdapter.OnRecyclerClickListener() {
            @Override
            public void onRecyclerItemClicked(View view) {
                long id = mRecyclerView.getChildItemId(view);
                String friendId = Long.toString(id);
                ClickCallback callback = getCallback();
                if (callback != null) {
                    callback.showDetails(getFragmentId(), friendId);
                }
            }
        };
    }

    @Override
    public ZRecyclerCursorAdapter getAdapter(Context context, Cursor cursor) {
        return new FriendsRecyclerAdapter(context, cursor);
    }

    @Override
    public DBProcessor getProcessor() {
        return new FriendsDBProcessor(getActivity());
    }

    @Override
    public String getRequestUrl() {
        return Api.getFriends();
    }

    @Override
    public Uri getUri() {
        return ZContentProvider.FRIENDS_CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
        return FriendsTable.PROJECTION;
    }

    @Override
    public String[] getProjectionOffline() {
        return FriendsTable.PROJECTION_OFFLINE;
    }

    @Override
    public FragmentId getFragmentId() {
        return FragmentId.FRIEND;
    }

}
