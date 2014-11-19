package com.github.stakkato95.ving.auth;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Artyom on 19.11.2014.
 */
public class Account {

    public static final String ACCESS_TOKEN = "access_token";
    public static final String USER_ID = "user_id";
    public static final String ID_AND_TOKEN = "id_and_token";

    public String access_token;
    public int client_id;

    public void store(Context context) {
        SharedPreferences preference = context.getSharedPreferences(ID_AND_TOKEN, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(ACCESS_TOKEN, access_token);
        editor.putInt(USER_ID, client_id);
        editor.commit();
    }

    public void restore(Context context) {
        SharedPreferences preference = context.getSharedPreferences(ID_AND_TOKEN, context.MODE_PRIVATE);
        access_token = preference.getString(ACCESS_TOKEN, null);
        client_id = preference.getInt(USER_ID, 0);
    }

}
