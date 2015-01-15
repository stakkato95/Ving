package com.github.stakkato95.ving.bo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Artyom on 20.11.2014.
 */
public class JSONObjectWrapper implements Parcelable {

    private JSONObject mJSONObject;

    public JSONObjectWrapper(String jsonObject) {
        try {
            mJSONObject = new JSONObject(jsonObject);
        } catch (JSONException e) {
            throw new IllegalArgumentException("invalid json string");
        }
    }

    protected JSONObjectWrapper(Parcel in) {
        readFromParcel(in);
    }

    public JSONObjectWrapper(JSONObject jsonObject) {
        mJSONObject = jsonObject;
    }

    protected String getString(String key) {
        return mJSONObject.optString(key);
    }

    protected Boolean getBoolean(String key) {
        return mJSONObject.optBoolean(key);
    }

    protected Long getLong(String id) {
        return mJSONObject.optLong(id);
    }

    protected void setField(String key, String value) {
        try {
            mJSONObject.put(key, value);
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String toString() {
        return mJSONObject.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mJSONObject.toString());
    }

    protected void readFromParcel(final Parcel in) {
        String string = in.readString();
        try {
            mJSONObject = new JSONObject(string);
        } catch (Exception e) {
            throw new IllegalArgumentException("invalid parcel");
        }
    }
}
