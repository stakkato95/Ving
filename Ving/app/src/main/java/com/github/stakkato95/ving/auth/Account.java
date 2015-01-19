package com.github.stakkato95.ving.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.github.stakkato95.ving.auth.secure.EncryptManager;

/**
 * Created by Artyom on 19.11.2014.
 */
public class Account {

    public static final String ACCESS_TOKEN = "access_token";
    public static final String USER_ID = "user_id";
    public static final String ID_AND_TOKEN = "id_and_token";

    public static String access_token;
    public static int client_id;

    public static void store(Context context) {
        String cryptedAccesToken = null;
        String cryptedID = null;
        try {
            cryptedAccesToken = EncryptManager.encrypt(context, access_token);
            cryptedID = EncryptManager.encrypt(context, Integer.toString(client_id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferences preference = context.getSharedPreferences(ID_AND_TOKEN, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(ACCESS_TOKEN, cryptedAccesToken);
        editor.putString(USER_ID, cryptedID);
        editor.commit();
    }

    public static void restore(Context context) {
        SharedPreferences preference = context.getSharedPreferences(ID_AND_TOKEN, context.MODE_PRIVATE);
        String AccessToken = preference.getString(ACCESS_TOKEN, null);
        String ClientID = preference.getString(USER_ID, null);

        try {
            access_token = EncryptManager.decrypt(context, AccessToken);
            client_id = Integer.getInteger(EncryptManager.decrypt(context, ClientID));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
