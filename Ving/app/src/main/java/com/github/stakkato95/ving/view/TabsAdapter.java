package com.github.stakkato95.ving.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.stakkato95.ving.fragments.assist.FriendsRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Artyom on 23.12.2014.
 */
public class TabsAdapter extends FragmentPagerAdapter {

    private Map<Integer,FriendsRequest> mTabs;
    private Context mContext;

    public TabsAdapter(FragmentManager fragmentManager, Context context, Map<Integer,FriendsRequest> tabs) {
        super(fragmentManager);
        mTabs = new HashMap<>();
        mTabs.putAll(tabs);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mTabs.get(position).create();
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(mTabs.get(position).getTitle());
    }
}
