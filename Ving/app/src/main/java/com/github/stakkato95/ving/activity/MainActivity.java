package com.github.stakkato95.ving.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.fragment.CapFragment;
import com.github.stakkato95.ving.fragment.ZListFragment;
import com.github.stakkato95.ving.fragment.assist.DrawerMenuItem;
import com.github.stakkato95.ving.fragment.assist.FragmentId;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements ZListFragment.ClickCallback {

    //Drawer content
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private static List<DrawerMenuItem> sDrawerMenuItems;

    public static final String KEY_REQUEST_FIELD = "key_request_field";

    static {
        sDrawerMenuItems = new ArrayList<>();
        sDrawerMenuItems.add(DrawerMenuItem.DIALOGS);
        sDrawerMenuItems.add(DrawerMenuItem.FRIENDS);
    }

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);

        //Drawer content
        String[] drawerTitles = new String[sDrawerMenuItems.size()];
        for (int i = 0; i < sDrawerMenuItems.size(); i++) {
            drawerTitles[i] = getResources().getString(sDrawerMenuItems.get(i).getTitle());
        }

        mTitle = getResources().getString(sDrawerMenuItems.get(0).getTitle());
        setTitle(mTitle);
        mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navigation_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, drawerTitles));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
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
        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            mDrawerList.setItemChecked(0, true);

            ZListFragment fragment = sDrawerMenuItems.get(0).getFragment();
            mFragmentManager.beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
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


    //Drawer methods
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    private void selectItem(Integer position) {
        Fragment fragment = null;
        CharSequence title = null;

        switch (position) {
            case 0:
                fragment = sDrawerMenuItems.get(0).getFragment();
                title = getResources().getString(sDrawerMenuItems.get(0).getTitle());
                break;
            case 1:
                fragment = sDrawerMenuItems.get(1).getFragment();
                title = getResources().getString(sDrawerMenuItems.get(1).getTitle());
                break;
            default:
                break;
        }

        if (fragment != null) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();

            mDrawerList.setItemChecked(position, true);
            setTitle(title);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.closeDrawer(mDrawerList);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void showDetails(FragmentId fragmentId, String requestField) {
        Intent intent = null;

        switch (fragmentId) {
            case FRIEND:
                //intent = new Intent(this, UserActivity.class);
                break;
            case DIALOG:
                intent = new Intent(this, DialogHistoryActivity.class);
                break;
            default:
                break;
        }

        intent.putExtra(KEY_REQUEST_FIELD, requestField);
        startActivity(intent);
    }

}
