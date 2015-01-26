package com.github.stakkato95.ving.database;

import com.github.stakkato95.util.MultiValueMap;

import java.util.ArrayList;

/**
 * Created by Artyom on 31.12.2014.
 */
public final class FriendsTable extends ZTable {

    public static final String NAME = "friends";

    public static final String _FULL_NAME = "_full_name";
    public static final String _PHOTO_100 = "_photo_100";
    public static final String _ONLINE= "_online";

    private static MultiValueMap<String,String> sDBMap;

    static {
        sDBMap = new MultiValueMap<>();
        sDBMap.put(TYPE_INTEGER, new ArrayList<String>() {{
            add(_ID);
            add(_ONLINE);
        }});
        sDBMap.put(TYPE_TEXT,new ArrayList<String>() {{
            add(_FULL_NAME);
            add(_PHOTO_100);
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
            _ONLINE,
    };

    public static final String[] PROJECTION_OFFLINE = {
            _ID,
            _FULL_NAME,
            _PHOTO_100,
    };

}