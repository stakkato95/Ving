package com.github.stakkato95.ving.database;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.github.stakkato95.ving.bo.Friend;

/**
 * Created by Artyom on 31.12.2014.
 */
public interface VkFriendsSummaryContract extends DataBaseConstants, BaseColumns {

    String TABLE_NAME = "vk_friends_summary";

    String _FIRST_NAME = "_first_name";
    String _LAST_NAME = "_last_name";
    String _PHOTO_50 = "_photo_50";

    String TABLE = TABLE_NAME + " ( " +
            _ID + " " + TYPE_INTEGER + DIVIDER +
            _FIRST_NAME + " " + TYPE_TEXT + DIVIDER +
            _LAST_NAME + " " + TYPE_TEXT + DIVIDER +
            _PHOTO_50 + " " + TYPE_TEXT + " )";

}