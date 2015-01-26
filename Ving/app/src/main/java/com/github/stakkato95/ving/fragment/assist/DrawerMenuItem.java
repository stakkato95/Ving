package com.github.stakkato95.ving.fragment.assist;

import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.fragment.DialogsFragment;
import com.github.stakkato95.ving.fragment.FriendsFragment;
import com.github.stakkato95.ving.fragment.ZListFragment;

/**
 * Created by Artyom on 28.12.2014.
 */
public enum DrawerMenuItem {

    //TODO remove magic about fragment, need to be fragment creator orr something else
    FRIENDS(R.string.friends, new FriendsFragment()),
    //TODO remove magic about fragment
    DIALOGS(R.string.dialogs, new DialogsFragment());


    private int mTitle;
    private ZListFragment mZListFragment;

    DrawerMenuItem(int title, ZListFragment fragment) {
        mTitle = title;
        mZListFragment = fragment;
    }

    public int getTitle() {
        return mTitle;
    }
    public ZListFragment getFragment() {
        return mZListFragment;
    }

}
