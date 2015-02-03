package com.github.stakkato95.ving.database;

import com.github.stakkato95.util.MultiValueMap;

import java.util.ArrayList;

/**
 * Created by Artyom on 25.01.2015.
 */
public final class DialogHistoryTable extends ZTable {

    public static final String NAME = "dialog_history";

    public static final String _USER_ID = "_user_id";
    public static final String _FROM_ID = "_from_id";
    public static final String _PHOTO_100 = "_photo_100";
    public static final String _DATE = "_date";
    public static final String _DATE_TEXT = "_date_text";
    public static final String _ROUTE = "_route";
    public static final String _READ_STATE = "_read_state";
    public static final String _BODY = "_body";

    private static MultiValueMap<String,String> sDBMap;

    static {
        sDBMap = new MultiValueMap<>();
        sDBMap.put(TYPE_INTEGER, new ArrayList<String>() {{
            add(_ID);
            add(_USER_ID);
            add(_FROM_ID);
            add(_ROUTE);
            add(_READ_STATE);
            add(_DATE);
        }});
        sDBMap.put(TYPE_TEXT, new ArrayList<String>() {{
            add(_PHOTO_100);
            add(_BODY);
            add(_DATE_TEXT);
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
            _FROM_ID,
            _PHOTO_100,
            _DATE,
            _DATE_TEXT,
            _ROUTE,
            _BODY
    };

}