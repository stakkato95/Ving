package com.github.stakkato95.ving.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.fragments.assist.FriendsRequest;
import com.github.stakkato95.ving.view.SlidingTabLayout;
import com.github.stakkato95.ving.view.TabsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendsContainerFragment extends Fragment implements ActionBar.TabListener {

    private ViewPager mViewPager;
    private TabsAdapter mTabsAdapter;
    private SlidingTabLayout mSlidingTabLayout;
    private static List<String> mTitles;
    private static Map<Integer,FriendsRequest> mTabs;

    static {
        mTabs = new HashMap<>();
        mTabs.put(0, FriendsRequest.GET_ALL_FRIENDS);
        mTabs.put(1, FriendsRequest.GET_ONLINE_FRIENDS);

        mTitles = new ArrayList<>();
        mTitles.add("Все друзья");
        mTitles.add("Друзья онлайн");
    }

    public FriendsContainerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_container, container, false);

        mTabsAdapter = new TabsAdapter(getActivity().getSupportFragmentManager(), getActivity(), mTabs);
        mViewPager = (ViewPager)view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(mTabsAdapter);
        mSlidingTabLayout = (SlidingTabLayout)view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);

        return view;
    }


    //tab listeners
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}
