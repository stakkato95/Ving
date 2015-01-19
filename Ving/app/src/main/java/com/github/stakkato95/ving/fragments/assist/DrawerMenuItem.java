package com.github.stakkato95.ving.fragments.assist;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.github.stakkato95.ving.CoreApplication;
import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.adapter.FriendsAdapter;
import com.github.stakkato95.ving.adapter.ZBaseAdapter;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.database.FriendsTable;
import com.github.stakkato95.ving.fragments.FragmentBuilder;
import com.github.stakkato95.ving.fragments.ZListFragment;
import com.github.stakkato95.ving.processing.DatabaseProcessor;
import com.github.stakkato95.ving.processing.FriendsProcessor;
import com.github.stakkato95.ving.provider.VingContentProvider;

/**
 * Created by Artyom on 28.12.2014.
 */
public enum DrawerMenuItem {

    FRIENDS(R.string.friends,
            new FriendsAdapter(CoreApplication.getContext(),null,0),
            new FriendsProcessor(),
            Api.getFriends(),
            VingContentProvider.FRIENDS_CONTENT_URI,
            FriendsTable.PROJECTION,
            FriendsTable.PROJECTION_OFFLINE);


    private CharSequence mTitle;
    private FragmentBuilder mFragmentBuilder;


    DrawerMenuItem(int title,
                   ZBaseAdapter adapter,
                   DatabaseProcessor processor,
                   String url,
                   Uri contentType,
                   String[] projection,
                   String[] projectionOffline) {
        mTitle = CoreApplication.getContext().getResources().getString(title);
        mFragmentBuilder = new FragmentBuilder();
        mFragmentBuilder.setAdapter(adapter);
        mFragmentBuilder.setProcessor(processor);
        mFragmentBuilder.setRequestUrl(url);
        mFragmentBuilder.setContentType(contentType);
        mFragmentBuilder.setProjection(projection);
        mFragmentBuilder.setProjectionOffline(projectionOffline);
    }

    public CharSequence getTitle() {
        return mTitle;
    }

    public ZListFragment create() {
        return mFragmentBuilder.createFragment();
    }
}
