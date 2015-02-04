package com.github.stakkato95.ving.fragment;

import android.net.Uri;
import android.view.View;
import android.widget.ListView;

import com.github.stakkato95.ving.adapter.DialogsAdapter;
import com.github.stakkato95.ving.adapter.ZCursorAdapter;
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
    public void onListItemClick(ListView l, View v, int position, long id) {
        String requestField = (String) v.getTag(DialogsAdapter.ID_KEY) + id;
        ClickCallback callback = getCallback();
        if (callback != null) {
            callback.showDetails(getFragmentId(), requestField);
        }
    }

    @Override
    public ZCursorAdapter getAdapter() {
        return new DialogsAdapter(getActivity(), null, 0);
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