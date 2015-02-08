package com.github.stakkato95.ving.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.fragment.DialogHistoryFragment;
import com.github.stakkato95.ving.fragment.UserFragment;

/**
 * Created by Artyom on 04.02.2015.
 */
public class UserActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String userId = getIntent().getStringExtra(MainActivity.KEY_REQUEST_FIELD);
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            UserFragment fragment = UserFragment.newInstance(userId);
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                //FLAG_ACTIVITY_CLEAR_TOP - clear activities above this one
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
