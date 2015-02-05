package com.github.stakkato95.ving.bo;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.stakkato95.ving.R;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Artyom on 20.01.2015.
 */
public class User extends JSONObjectWrapper {

    //other fields will be added after
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String PHOTO_100 = "photo_100";
    private static final String PHOTO_200 = "photo_200";
    private static final String PHOTO_MAX = "photo_max";
    private static final String LAST_SEEN = "last_seen";
    private static final String LAST_SEEN_TIME = "time";
    private static final String ID = "id";
    private static final String SEX = "sex";
    private static final String BIRTHDAY = "bdate";



    private static final String FULL_NAME = "full_name";
    private static final String STATUS = "status";
    private static final String ONLINE = "online";
    private static final String ONLINE_MOBILE = "online_mobile";

    private static final String COUNTERS = "counters";
    private static final String COUNTER_ALBUMS = "albums";
    private static final String COUNTER_VIDEOS = "videos";
    private static final String COUNTER_AUDIOS = "audios";
    private static final String COUNTER_PHOTOS = "photos";
    private static final String COUNTER_FRIENDS = "friends";
    private static final String COUNTER_MUTUAL_FRIENDS = "mutual_friends";
    private static final String COUNTER_FOLLOWERS = "followers";
    private static final String COUNTER_GIFTS = "gifts";

    private static final String HOME_TOWN = "home_town";
    private static final String TOWN = "town";
    private static final String COUNTRY = "country";
    private static final String PHONE_MOBILE = "mobile_phone";
    private static final String PHONE = "home_phone";
    private static final String SKYPE = "skype";
    private static final String SITE = "site";

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

    public String getPhoto100() {
        return getString(PHOTO_100);
    }

    public String getPhoto200() {
        return getString(PHOTO_200);
    }

    public String getPhotoMax() {
        return getString(PHOTO_MAX);
    }

    public void createFullName() {
        setField(FULL_NAME, getFirstName() + " " + getLastName());
    }

    public String getFullName() {
        return getString(FULL_NAME);
    }

    public int getOnlineMode() {
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

    public Long getSex() {
        return getLong(SEX);
    }

    public String getBirthday() {
        return getString(BIRTHDAY);
    }







    public String getStatus() {
        return getString(STATUS);
    }

    public Long getLastSeen() {
        return getJSONObject(LAST_SEEN).optLong(LAST_SEEN_TIME);
    }

    public LinkedHashMap<Integer, String> getCountersMap() {
        JSONObject object = getJSONObject(COUNTERS);
        Map<Integer, String> countersMap = new LinkedHashMap<>();
        countersMap.put(R.string.counter_friends, object.optString(COUNTER_FRIENDS));
        countersMap.put(R.string.counter_mutual_friends, object.optString(COUNTER_MUTUAL_FRIENDS));
        countersMap.put(R.string.counter_followers, object.optString(COUNTER_FOLLOWERS));
        countersMap.put(R.string.counter_photos, object.optString(COUNTER_PHOTOS));
        countersMap.put(R.string.counter_albums, object.optString(COUNTER_ALBUMS));
        countersMap.put(R.string.counter_audios, object.optString(COUNTER_AUDIOS));
        countersMap.put(R.string.counter_videos, object.optString(COUNTER_VIDEOS));
        countersMap.put(R.string.counter_gifts, object.optString(COUNTER_GIFTS));
        return (LinkedHashMap<Integer, String>)countersMap;
    }


    //PROFILE
    public String getHomeTown() {
        return getString(HOME_TOWN);
    }

    public String getTown() {
        return getString(TOWN);
    }

    public String getCountry() {
        return getString(COUNTRY);
    }

    public Long getPhoneMobile() {
        return getLong(PHONE_MOBILE);
    }

    public Long getPhone() {
        return getLong(PHONE);
    }

    public String getSkype() {
        return getString(SKYPE);
    }

    public String getSite() {
        return getString(SITE);
    }

}
