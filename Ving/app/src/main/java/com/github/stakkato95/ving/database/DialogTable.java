package com.github.stakkato95.ving.database;

import com.github.stakkato95.util.MultiValueMap;

import java.util.ArrayList;

/**
 * Created by Artyom on 19.01.2015.
 */
public final class DialogTable extends ZTable {

    public static final String NAME = "dialogs";

    public static final String _DIALOG_NAME = "_dialog_name"; //identified by friend's full_name, or dialog's title
    public static final String _LAST_SENDER_PHOTO_100 = "_last_sender_photo_100";
    public static final String _PHOTO_100 = "_photo_100";
    public static final String _DATE = "_date";
    public static final String _ROUTE = "_route";
    public static final String _READ_STATE = "_read_state";
    public static final String _BODY = "_body";
    public static final String _ONLINE = "_online";
    public static final String _TYPE = "_type";

    public static final int CHAT = 1;
    public static final int ONE_INTERLOCUTOR = 0;

    private static MultiValueMap<String,String> sDBMap;

    static {
        sDBMap = new MultiValueMap<>();
        sDBMap.put(TYPE_INTEGER, new ArrayList<String>() {{
            add(_ID);
            add(_READ_STATE);
            add(_ROUTE);
            add(_ONLINE);
            add(_TYPE);
        }});
        sDBMap.put(TYPE_TEXT, new ArrayList<String>() {{
            add(_DIALOG_NAME);
            add(_LAST_SENDER_PHOTO_100);
            add(_PHOTO_100);
            add(_DATE);
            add(_BODY);
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
            _DIALOG_NAME,
            _LAST_SENDER_PHOTO_100,
            _PHOTO_100,
            _DATE,
            _ROUTE,
            _READ_STATE,
            _BODY,
            _ONLINE,
            _TYPE
    };

    public static final String[] PROJECTION_OFFLINE = {
            _ID,
            _DIALOG_NAME,
            _LAST_SENDER_PHOTO_100,
            _PHOTO_100,
            _DATE,
            _ROUTE,
            _READ_STATE,
            _BODY,
            _TYPE
    };

}