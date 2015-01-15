package com.github.stakkato95.ving.bo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Artyom on 29.12.2014.
 */
public class JSONArrayWrapper {

    private JSONArray mJSONArray;

    public JSONArrayWrapper(String string) {
        try {
            mJSONArray = new JSONObject(string).getJSONObject("response").getJSONArray("items");
        } catch (JSONException e) {
            throw new IllegalArgumentException("invalid json string");
        }
    }

    public JSONObject getObject(int index) {
        try {
            return mJSONArray.getJSONObject(index);
        } catch (JSONException e) {
            throw new IllegalArgumentException("the mapping doesn't exist or is not a JSONObject");
        }
    }

    public int length() {
        return mJSONArray.length();
    }
}
