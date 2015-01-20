package com.github.stakkato95.ving.bo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Artyom on 20.01.2015.
 */
public class User extends JSONObjectWrapper {

    //other fields will be added after
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String PHOTO = "photo_100";
    private static final String ID = "id";
    private static final String FULL_NAME = "full_name";
    private static final String ONLINE = "online";
    private static final String ONLINE_MOBILE = "online_mobile";

    public User(String jsonObject) {
        super(jsonObject);
    }

    public User(JSONObject jsonObject) {
        super(jsonObject);
    }

    protected User(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getFirstName() {
        return getString(FIRST_NAME);
    }

    public String getLastName() {
        return getString(LAST_NAME);
    }

    public String getPhoto() {
        return getString(PHOTO);
    }

    public void createFullName() {
        setField(FULL_NAME, getFirstName() + " " + getLastName());
    }

    public String getFullName() {
        return getString(FULL_NAME);
    }

    public int getOnlineMode() {
        //online_mobile = 2, online = 1, offline = 0
        long isOnlineMobile = getLong(ONLINE_MOBILE);
        if (isOnlineMobile == 1) {
            return 2;
        }
        long isOnline = getLong(ONLINE);
        if (isOnline == 1) {
            return 1;
        }
        return 0;
    }

    public Long getId() {
        return getLong(ID);
    }

}
