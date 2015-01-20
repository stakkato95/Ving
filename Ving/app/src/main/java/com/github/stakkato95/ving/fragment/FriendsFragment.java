package com.github.stakkato95.ving.fragment;

import android.net.Uri;

import com.github.stakkato95.ving.adapter.FriendsAdapter;
import com.github.stakkato95.ving.adapter.ZCursorAdapter;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.database.FriendsTable;
import com.github.stakkato95.ving.processor.DatabaseProcessor;
import com.github.stakkato95.ving.processor.FriendsProcessor;
import com.github.stakkato95.ving.provider.ZContentProvider;

/**
 * Created by Artyom on 19.01.2015.
 */
public class FriendsFragment extends ZListFragment {

    @Override
    public ZCursorAdapter getAdapter() {
        return new FriendsAdapter(getActivity(), null, 0);
    }

    @Override
    public DatabaseProcessor getProcessor() {
        return new FriendsProcessor(getActivity());
    }

    @Override
    public String getRequestUrl() {
        return Api.getFriends();
    }

    @Override
    public Uri getContentUri() {
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

}
