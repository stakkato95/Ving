package com.github.stakkato95.ving;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.github.stakkato95.ving.auth.Account;


public class StartActivity extends ActionBarActivity {

    public static final int LOGIN_ACTIVITY_RESULT = 0;
    private Account mAccount = new Account();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mAccount.restore(this);

        if(mAccount.access_token == null) {
            startActivityForResult(new Intent(this, LoginActivity.class), LOGIN_ACTIVITY_RESULT);
        }
    }

    private void update() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == LOGIN_ACTIVITY_RESULT && resultCode == RESULT_OK) {
            mAccount.access_token = data.getStringExtra(Account.ACCESS_TOKEN);
            mAccount.client_id = data.getIntExtra(Account.USER_ID, 0);
            mAccount.store(this);
        }
    }
}
