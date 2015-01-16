package com.github.stakkato95.ving.bo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Artyom on 29.12.2014.
 */
public class Friend extends JSONObjectWrapper {

    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String PHOTO = "photo_100";
    private static final String ID = "id";
    private static final String FULL_NAME = "full_name";
    private static final String ONLINE = "online";
    private static final String ONLINE_MOBILE = "online_mobile";


    public static final Parcelable.Creator<Friend> CREATOR
            = new Parcelable.Creator<Friend>() {
        public Friend createFromParcel(Parcel in) {
            return new Friend(in);
        }

        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };

    public Friend(String jsonObject) {
        super(jsonObject);
    }

    public Friend(JSONObject jsonObject) {
        super(jsonObject);
    }

    protected Friend(Parcel in) {
        super(in);
    }


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

    public boolean isOnline() {
        Long isOnline = getLong(ONLINE);
        return isOnline == 1;
    }

    public boolean isOnlineMobile() {
        Long isOnline = getLong(ONLINE_MOBILE);
        return isOnline == 1;
    }

    public Long getId() {
        return getLong(ID);
    }
}
