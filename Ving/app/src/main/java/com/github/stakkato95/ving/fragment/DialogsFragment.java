package com.github.stakkato95.ving.fragment;

import android.net.Uri;

import com.github.stakkato95.ving.adapter.DialogsAdapter;
import com.github.stakkato95.ving.adapter.ZCursorAdapter;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.database.DialogTable;
import com.github.stakkato95.ving.fragment.assist.FragmentId;
import com.github.stakkato95.ving.processor.DatabaseProcessor;
import com.github.stakkato95.ving.processor.DialogsProcessor;
import com.github.stakkato95.ving.provider.ZContentProvider;

/**
 * Created by Artyom on 18.01.2015.
 */
public class DialogsFragment extends ZListFragment {
    @Override
    public ZCursorAdapter getAdapter() {
        return new DialogsAdapter(getActivity(), null, 0);
    }

    @Override
    public DatabaseProcessor getProcessor() {
        return new DialogsProcessor(getActivity());
    }

    @Override
    public String getRequestUrl() {
        return Api.getDialogs();
    }

    @Override
    public Uri getContentUri() {
        return ZContentProvider.DIALOGS_CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
        return DialogTable.PROJECTION;
    }

    @Override
    public String[] getProjectionOffline() {
        return DialogTable.PROJECTION;
    }

    @Override
    public FragmentId getFragmentId() {
        return FragmentId.DIALOG;
    }
}