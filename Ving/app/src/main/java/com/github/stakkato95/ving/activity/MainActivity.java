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
    private String[] mDrawerTitles;

    public static final String KEY_REQUEST_FIELD = "key_request_field";


    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);

        //Drawer content
        mDrawerTitles = new String[DrawerMenuItem.values().length];
        for (int i = 0; i < DrawerMenuItem.values().length; i++) {
            mDrawerTitles[i] = getResources().getString(DrawerMenuItem.values()[i].getTitleResource());
        }

        setTitle(mTitle = mDrawerTitles[0]);
        mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navigation_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, mDrawerTitles));
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

            ZListFragment fragment = DrawerMenuItem.values()[0].createFragment();
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
                fragment = DrawerMenuItem.values()[0].createFragment();
                title = mDrawerTitles[0];
                break;
            case 1:
                fragment = DrawerMenuItem.values()[1].createFragment();
                title = mDrawerTitles[1];
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

        if (intent != null) {
            intent.putExtra(KEY_REQUEST_FIELD, requestField);
            startActivity(intent);
        }
    }


}
