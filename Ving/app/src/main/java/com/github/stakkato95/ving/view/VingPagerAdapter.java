package com.github.stakkato95.ving.view;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.stakkato95.ving.bo.Friend;
import com.github.stakkato95.ving.fragments.FriendsFragment;

/**
 * Created by Artyom on 23.12.2014.
 */
public class VingPagerAdapter extends FragmentPagerAdapter {

    private final int NUMBER_OF_ITEMS;

    public VingPagerAdapter(FragmentManager fragmentManager, int numOfItems) {
        super(fragmentManager);
        NUMBER_OF_ITEMS = numOfItems;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                //fragment = FriendsFragment.newInstance(); //HOW TO SEND ARGS HERE??? ALL FRIENDS
                break;
            case 1:
                //fragment = FriendsFragment.newInstance(); //ONLINE FIENDS
                break;
        }
        return fragment;

        //IT WOULD BE IDEAL TO USE THIS CONSTRUCTION, THAT WOULD DIFFER JUT IN ARGS
        //return FriendsFragment.newInstance(ARRAY_OF_FRIENS__ALL_OR_ONLINE);
    }

    @Override
    public int getCount() {
        return NUMBER_OF_ITEMS;
    }
}
