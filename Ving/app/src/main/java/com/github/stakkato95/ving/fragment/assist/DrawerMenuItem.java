package com.github.stakkato95.ving.fragment.assist;

import android.net.Uri;

import com.github.stakkato95.ving.CoreApplication;
import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.adapter.FriendsAdapter;
import com.github.stakkato95.ving.adapter.ZCursorAdapter;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.database.DialogsTable;
import com.github.stakkato95.ving.database.FriendsTable;
import com.github.stakkato95.ving.fragment.FragmentBuilder;
import com.github.stakkato95.ving.fragment.ZListFragment;
import com.github.stakkato95.ving.processor.DatabaseProcessor;
import com.github.stakkato95.ving.processor.DialogsProcessor;
import com.github.stakkato95.ving.processor.FriendsProcessor;
import com.github.stakkato95.ving.provider.VingContentProvider;

/**
 * Created by Artyom on 28.12.2014.
 */
public enum DrawerMenuItem {

    FRIENDS(R.string.friends,
            new FriendsAdapter(CoreApplication.getContext(), null, 0),
            new FriendsProcessor(),
            Api.getFriends(),
            VingContentProvider.FRIENDS_CONTENT_URI,
            FriendsTable.PROJECTION,
            FriendsTable.PROJECTION_OFFLINE);

//    DIALOGS(R.string.dialogs,
//            new DialogsAdapter(CoreApplication.getContext(), null, 0),
//            new DialogsProcessor(),
//            Api.getDialogs(),
//            VingContentProvider.DIALOGS_CONTENT_URI,
//            DialogsTable.PROJECTION,
//            DialogsTable.PROJECTION);


    private CharSequence mTitle;
    private ZListFragment mZListFragment;

    DrawerMenuItem(int title,
                   ZCursorAdapter adapter,
                   DatabaseProcessor processor,
                   String url,
                   Uri contentType,
                   String[] projection,
                   String[] projectionOffline) {
        mTitle = CoreApplication.getContext().getResources().getString(title);
        FragmentBuilder builder = new FragmentBuilder();
        mZListFragment = builder.setAdapter(adapter)
                .setProcessor(processor)
                .setRequestUrl(url)
                .setContentType(contentType)
                .setProjection(projection)
                .setProjectionOffline(projectionOffline)
                .createFragment();
    }

    public CharSequence getTitle() {
        return mTitle;
    }

    public ZListFragment create() {
        return mZListFragment;
    }

}
