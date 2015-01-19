package com.github.stakkato95.ving.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.auth.Account;


public class StartActivity extends ActionBarActivity {

    public static final int LOGIN_ACTIVITY_RESULT = 0;
    private Account mAccount = new Account();

    private static Button mAuthButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //TODO remove if after will be use NonActionBar theme
        //getSupportActionBar().hide();

        mAccount.restore(this);

        if(mAccount.access_token == null) {
            mAuthButton = (Button)findViewById(R.id.auth_button);
            mAuthButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(StartActivity.this, LoginActivity.class), LOGIN_ACTIVITY_RESULT);
                }
            });
        } else {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == LOGIN_ACTIVITY_RESULT) {
            if(resultCode == RESULT_OK) {
                mAccount.access_token = data.getStringExtra(Account.ACCESS_TOKEN);
                mAccount.client_id = data.getIntExtra(Account.USER_ID, 0);
                mAccount.store(this);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }
    }
}
