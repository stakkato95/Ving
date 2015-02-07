package com.github.stakkato95.ving.bo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Artyom on 06.02.2015.
 */
public class City extends JSONObjectWrapper {

    private static final String ID = "id";
    private static final String TITLE = "title";

    public City(String jsonObject) {
        super(jsonObject);
    }

    public City(JSONObject jsonObject) {
        super(jsonObject);
    }

    protected City(Parcel in) {
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


    public String getName() {
        return getString(TITLE);
    }

    public int getId() {
        return getInt(ID);
    }

}