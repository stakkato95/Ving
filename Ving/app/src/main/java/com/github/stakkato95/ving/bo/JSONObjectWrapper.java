package com.github.stakkato95.ving.bo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Artyom on 20.11.2014.
 */
public class JSONObjectWrapper implements Parcelable {

    private JSONObject mJO;

    public JSONObjectWrapper(String jsonObject) {
        try {
            mJO = new JSONObject(jsonObject);
        } catch (JSONException e) {
            throw new IllegalArgumentException("invalid json string");
        }
    }

    protected JSONObjectWrapper(Parcel in) {
        readFromParcel(in);
    }

    public JSONObjectWrapper(JSONObject jsonObject) {
        mJO = jsonObject;
    }

    protected String getString(String key) {
        return mJO.optString(key);
    }

    protected Boolean getBoolean(String key) {
        return mJO.optBoolean(key);
    }

    protected Long getLong(String id) {
        return mJO.optLong(id);
    }

    protected void set(String key, String value) {
        try {
            mJO.put(key, value);
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String toString() {
        return mJO.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mJO.toString());
    }

    protected void readFromParcel(final Parcel in) {
        String string = in.readString();
        try {
            mJO = new JSONObject(string);
        } catch (Exception e) {
            throw new IllegalArgumentException("invalid parcel");
        }
    }
}
