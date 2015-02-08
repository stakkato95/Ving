package com.github.stakkato95.ving.bo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Artyom on 05.02.2015.
 */
public class Photo extends JSONObjectWrapper {

    private static final String PHOTO_75 = "photo_75";
    private static final String PHOTO_130 = "photo_130";
    private static final String PHOTO_604 = "photo_604";
    private static final String PHOTO_807 = "photo_807";
    private static final String PHOTO_1280 = "photo_1280";
    private static final String DATE = "date";
    private static final String SIZES = "sizes";
    private static final String SRC = "src";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String ID = "id";


    public Photo(String jsonObject) {
        super(jsonObject);
    }

    public Photo(JSONObject jsonObject) {
        super(jsonObject);
    }

    protected Photo(Parcel in) {
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

    public String getPhoto75() {
        return getString(PHOTO_75);
    }

    public String getPhoto130() {
        return getString(PHOTO_130);
    }

    public String getPhoto200() {
        try {
            return getJSONArray(SIZES).optJSONObject(7).optString(SRC);
        } catch (Exception e) {
            return getJSONArray(SIZES).optJSONObject(2).optString(SRC);
        }
    }

    public int getPhoto200Width() {
        try {
            return getJSONArray(SIZES).optJSONObject(7).optInt(WIDTH);
        } catch (Exception e) {
            return getJSONArray(SIZES).optJSONObject(2).optInt(WIDTH);
        }
    }

    public int getPhoto200Height() {
        try {
            return getJSONArray(SIZES).optJSONObject(7).optInt(HEIGHT);
        } catch (Exception e) {
            return getJSONArray(SIZES).optJSONObject(2).optInt(HEIGHT);
        }
    }

    public String getPhoto604() {
        return getString(PHOTO_604);
    }

    public String getPhoto807() {
        return getString(PHOTO_807);
    }

    public String getPhoto1280() {
        return getString(PHOTO_1280);
    }

    public Long getId() {
        return getLong(ID);
    }

    public Long getDate() {
        return getLong(DATE);
    }


}