package com.github.stakkato95.ving;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.stakkato95.ving.bo.NoteGsonModel;


public class FriendDetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_details);

        NoteGsonModel note = (NoteGsonModel)getIntent().getSerializableExtra("item");
        ((TextView) findViewById(android.R.id.text1)).setText(note.getTitle());
        ((TextView) findViewById(android.R.id.text2)).setText(note.getContent());

    }
}
