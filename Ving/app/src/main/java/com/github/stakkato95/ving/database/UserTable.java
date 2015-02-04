package com.github.stakkato95.ving.database;

import com.github.stakkato95.util.MultiValueMap;

import java.util.ArrayList;

/**
 * Created by Artyom on 04.02.2015.
 */
public final class UserTable extends ZTable {

    public static final String NAME = "user";

    public static final String _FULL_NAME = "_full_name";
    public static final String _PHOTO_100 = "_photo_100";
    public static final String _ONLINE= "_online";
    public static final String _SEX = "_sex";
    public static final String _BIRTHDAY = "_bdate";
    public static final String _PHONE = "_home_phone";
    public static final String _PHONE_MOBILE = "_mobile_phone";
    public static final String _SKYPE = "_skype";
    public static final String _SITE = "_site";
    public static final String _STATUS = "_status";

    private static MultiValueMap<String,String> sDBMap;

    static {
        sDBMap = new MultiValueMap<>();
        sDBMap.put(TYPE_INTEGER, new ArrayList<String>() {{
            add(_ID);
            add(_ONLINE);
            add(_SEX);
            add(_PHONE);
            add(_PHONE_MOBILE);
        }});
        sDBMap.put(TYPE_TEXT,new ArrayList<String>() {{
            add(_FULL_NAME);
            add(_PHOTO_100);
            add(_BIRTHDAY);
            add(_SKYPE);
            add(_SITE);
            add(_STATUS);
        }});
    }

    @Override
    public MultiValueMap<String, String> getDbMap() {
        return sDBMap;
    }

    @Override
    public String getName() {
        return NAME;
    }

    public static final String[] PROJECTION = {
            _ID,
            _FULL_NAME,
            _PHOTO_100,
            _SEX,
            _BIRTHDAY,
            _PHONE,
            _PHONE_MOBILE,
            _SKYPE,
            _SITE,
            _STATUS,
            _ONLINE
    };

    public static final String[] PROJECTION_OFFLINE = {
            _ID,
            _FULL_NAME,
            _PHOTO_100,
            _SEX,
            _BIRTHDAY,
            _PHONE,
            _PHONE_MOBILE,
            _SKYPE,
            _SITE,
            _STATUS
    };

}