package com.github.stakkato95.ving.fragments.assist;

import android.support.v4.app.Fragment;

import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.fragments.FriendsFragment;

/**
 * Created by Artyom on 28.12.2014.
 */
public enum FriendsRequest implements FragmentCreator {

    GET_ALL_FRIENDS(R.string.all_friends, new FragmentCreator() {
        @Override
        public Fragment create() {
            //TODO remove magic numbers
            return FriendsFragment.newInstance(0);
        }
    }),

    GET_ONLINE_FRIENDS(R.string.online_friends, new FragmentCreator() {
        @Override
        public Fragment create() {
            //TODO remove magic numbers
            return FriendsFragment.newInstance(1);
        }
    });


    private FragmentCreator mFragmentCreator;
    private int mTitle;

    FriendsRequest(int title, FragmentCreator fragmentCreator) {
        mTitle = title;
        mFragmentCreator = fragmentCreator;
    }

    public int getTitle() {
        return mTitle;
    }

    @Override
    public Fragment create() {
        return mFragmentCreator.create();
    }
}
