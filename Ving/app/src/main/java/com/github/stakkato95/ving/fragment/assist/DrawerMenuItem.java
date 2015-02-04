package com.github.stakkato95.ving.fragment.assist;

import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.fragment.DialogsFragment;
import com.github.stakkato95.ving.fragment.FriendsFragment;
import com.github.stakkato95.ving.fragment.ZQueueFragment;

/**
 * Created by Artyom on 28.12.2014.
 */
public enum DrawerMenuItem implements FragmentCreator {

    DIALOGS(R.string.dialogs, new FragmentCreator() {
        @Override
        public ZQueueFragment createFragment() {
            return new DialogsFragment();
        }
    }),
    FRIENDS(R.string.friends, new FragmentCreator() {
        @Override
        public ZQueueFragment createFragment() {
            return new FriendsFragment();
        }
    });


    private int mTitle;
    private FragmentCreator mFragmentCreator;

    DrawerMenuItem(int title, FragmentCreator fragmentCreator) {
        mTitle = title;
        mFragmentCreator = fragmentCreator;
    }

    public int getTitleResource() {
        return mTitle;
    }

    @Override
    public ZQueueFragment createFragment() {
        return mFragmentCreator.createFragment();
    }
}
