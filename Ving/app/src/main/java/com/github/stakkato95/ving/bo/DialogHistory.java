package com.github.stakkato95.ving.bo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Artyom on 25.01.2015.
 */
public class DialogHistory extends JSONObjectWrapper {

    private static final String ID = "id";
    private static final String USER_ID = "user_id"; //in dialog your or interlocutor, in chat different people
    private static final String FROM_ID = "from_id";
    private static final String PHOTO = "photo_100";
    private static final String DATE = "date";
    private static final String ROUTE = "out";
    private static final String READ_STATE = "read_state";
    private static final String BODY = "body";

    public DialogHistory(String jsonObject) {
        super(jsonObject);
    }

    public DialogHistory(JSONObject jsonObject) {
        super(jsonObject);
    }

    protected DialogHistory(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<Dialog> CREATOR
            = new Parcelable.Creator<Dialog>() {
        public Dialog createFromParcel(Parcel in) {
            return new Dialog(in);
        }

        public Dialog[] newArray(int size) {
            return new Dialog[size];
        }
    };

    public String getPhoto() {
        return getString(PHOTO);
    }

    public long getId() {
        return getLong(ID);
    }

    public long getUserId() {
        return getLong(USER_ID);
    }

    public long getDate() {
        return getLong(DATE);
    }

    public long getRoute() {
        return getLong(ROUTE);
    }

    public long getReadState() {
        return getLong(READ_STATE);
    }

    public String getBody() {
        return getString(BODY);
    }

    public Long getFromId() {
        return getLong(FROM_ID);
    }

}