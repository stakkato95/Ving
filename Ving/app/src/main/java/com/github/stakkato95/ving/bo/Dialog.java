package com.github.stakkato95.ving.bo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Artyom on 18.01.2015.
 */
public class Dialog extends JSONObjectWrapper {

    private static final String ID = "id";
    private static final String USER_ID = "user_id"; //in dialog your or interlocutor, in chat different people
    private static final String CHAT_ID = "chat_id";
    private static final String PHOTO = "photo_100";
    private static final String DATE = "date";
    private static final String ROUTE = "out"; //1 - from user, 0 - to user
    private static final String READ_STATE = "read_state"; //1 - read, 0 - unread
    private static final String TITLE = "title"; //only for chats, not for dialogs
    private static final String BODY = "body";

    public Dialog(String jsonObject) {
        super(jsonObject);
    }

    public Dialog(JSONObject jsonObject) {
        super(jsonObject);
    }

    protected Dialog(Parcel in) {
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

    public long getChatId() {
        return getLong(CHAT_ID);
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

    public String getTitle() {
        return getString(TITLE);
    }

    public String getBody() {
        return getString(BODY);
    }

}