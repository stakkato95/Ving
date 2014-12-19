package com.github.stakkato95.ving;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StatFs;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.stakkato95.ving.auth.VkOAuthHelper;
import com.github.stakkato95.ving.bo.Friend;
import com.github.stakkato95.ving.fragments.CapFragment;
import com.github.stakkato95.ving.fragments.FriendsFragment;
import com.github.stakkato95.ving.manager.DataManager;
import com.github.stakkato95.ving.processing.FriendArrayProcessor;
import com.github.stakkato95.ving.source.HttpDataSource;
import com.github.stakkato95.ving.source.VkDataSource;

import java.io.File;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements DataManager.Callback<List<Friend>> {

    private FriendArrayProcessor mFriendArrayProcessor;
    private VkDataSource mVkDataSource;
    private ProgressBar mProgressBar;
    private TextView mErrorTextView;
    private Button mRetryButton;


    //Drawer content
    private String[] mScreenTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private Fragment mLastFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFriendArrayProcessor = new FriendArrayProcessor();
        mVkDataSource = new VkDataSource();
        mProgressBar = (ProgressBar)findViewById(android.R.id.progress);
        mErrorTextView = (TextView)findViewById(R.id.error_text);
        mRetryButton = (Button)findViewById(R.id.retry_update_button);
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem(0);
            }
        });

        mLastFragment = new Fragment();

        //Drawer content
        mTitle = mDrawerTitle = getTitle();
        mScreenTitles = getResources().getStringArray(R.array.drawer_items_array);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.navigation_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mScreenTitles));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //adds arrow
        getSupportActionBar().setHomeButtonEnabled(true); //app icon is touchable

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                getSupportActionBar().setTitle(mDrawerTitle);
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                getSupportActionBar().setTitle(mTitle);
                supportInvalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            refreshListView(mVkDataSource, mFriendArrayProcessor);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //DataManager methods & callbacks
    private void refreshListView(HttpDataSource dataSource, FriendArrayProcessor processor) {
        DataManager.loadData(MainActivity.this, getRequestUrl(), dataSource, processor);
    }

    private String getRequestUrl() {
        return Api.FRIENDS_GET;
    }

    @Override
    public void onDataLoadStart() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDone(List<Friend> data) {
        mProgressBar.setVisibility(View.GONE);

        FriendsFragment fragment = FriendsFragment.newInstance(new ArrayList<Friend>(data));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        //TODO swipe-refresh logic
//        if(mArrayAdapter == null) {
//            mRefreshData = data;
//
//        } else {
//            mRefreshData.clear();
//            mRefreshData.addAll(data);
//            mArrayAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    public void onError(Exception e) {
        mProgressBar.setVisibility(View.GONE);

        if (mLastFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(mLastFragment)
                    .commit();
        }

        if(UnknownHostException.class.isInstance(e)) {
            mErrorTextView.setTextSize(22);
            mErrorTextView.setText("Проверьте подключение и\nповторите попытку");
            mRetryButton.setVisibility(View.VISIBLE);
        } else {
            mErrorTextView.setText("ERROR\n" + e.getLocalizedMessage());
        }
    }


    //Drawer methods
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    private void selectItem(Integer position) {
        Fragment fragment = null;

        switch(position) {
            case 0:
                refreshListView(mVkDataSource, mFriendArrayProcessor);
                mDrawerList.setItemChecked(position, true);
                setTitle(mScreenTitles[position]);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 1:
                fragment = CapFragment.newInstance("Сообщения в разработка");
                break;
            case 2:
                fragment = CapFragment.newInstance("Фотографии в разработка");
                break;
            case 3:
                fragment = CapFragment.newInstance("Видео в разработка");
                break;
            default:
                break;
        }

        if(fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();

            mDrawerList.setItemChecked(position, true);
            setTitle(mScreenTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
            mLastFragment = fragment;
        } else {
            getSupportFragmentManager().beginTransaction()
                    .remove(mLastFragment)
                    .commit();
        }

        mErrorTextView.setText("");
        mRetryButton.setVisibility(View.INVISIBLE);
    }

}
