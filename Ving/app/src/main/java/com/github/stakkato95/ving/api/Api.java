package com.github.stakkato95.ving.api;

import com.github.stakkato95.ving.R;

/**
 * Created by Artyom on 20.11.2014.
 */
public class Api {

    public static final String BASE_PATH = "https://api.vk.com/method/";
    public static final String VERSION_VALUE = "5.8";
    public static final String VERSION_PARAM = "v";

    public static final String JSON_RESPONSE = "response";
    public static final String JSON_ITEMS = "items";

    public static final String ONE_INTERLOCUTOR_DIALOG = " ... ";
    public static final String SORT_ORDER_HINTS = "hints";
    public static final String EMPTY_STRING = "";

    public static final int GET_COUNT = 30;
    public static final int MESSAGE_ROUTE_IN = 0;
    public static final int MESSAGE_ROUTE_OUT = 1;
    public static final int MESSAGE_STATE_READ = 1;
    public static final int MESSAGE_STATE_UNREAD = 0;
    public static final int MESSAGE_PREVIEW_LENGTH = 35;

    public static final int USER_OFFLINE = 0;
    public static final int USER_ONLINE = 1;
    public static final int USER_ONLINE_MOBILE = 2;
    public static final int USER_SEX_MAN = 2;
    public static final int USER_SEX_WOMAN = 1;
    public static final int USER_SEX_WTF = 0;
    public static final int USER_ONLINE_STRING = R.string.user_online;
    public static final int USER_STATE = R.string.user_state;

    public static final int PHOTOS_SORT_CHRONOLOGICAL = 0;
    public static final int PHOTOS_SORT_ANTICHRONOLOGICAL = 1;
    public static final int PHOTOS_SPECIAL_SIZES = 1;

    public static final String FIELD_USER_IDS = "user_ids=";
    public static final String FIELD_MESSAGE_CHAT_ID = "chat_id=";
    public static final String FIELD_MESSAGE_USER_ID = "user_id=";
    public static final String FIELD_OWNER_ID = "owner_id=";
    public static final String FIELD_ALBUM_ID = "album_id=";

    public static final String FIELD_USER_FIELDS = "fields=";
    public static final String FIELD_FRIENDS_SORT_ORDER = "order=";
    public static final String FIELD_PHOTOS_SORT_ORDER = "rev=";
    public static final String FIELD_MESSAGE_PREVIEW = "preview_length=";
    public static final String FIELD_COUNT = "count=";
    public static final String FIELD_OFFSET = "offset=";
    public static final String FIELD_MESSAGE = "message=";
    public static final String FIELD_PHOTO_SIZES = "photo_sizes=";

    public static final String _PHOTO_100 = "photo_100";
    public static final String _PHOTO_200 = "photo_200";
    public static final String _PHOTO_MAX = "photo_max";
    public static final String _LAST_SEEN = "last_seen";
    public static final String _ONLINE = "online";
    public static final String _SEX = "sex";
    public static final String _BIRTHDAY = "bdate";
    public static final String _CITY = "city";
    public static final String _COUNTRY = "country";
    public static final String _HOME_TOWN = "home_town";
    public static final String _UNIVERSITIES = "universities";
    public static final String _SCHOOLS = "schools";
    public static final String _OCCUPATION = "occupation";
    public static final String _RELATIVES = "relatives";
    public static final String _RELATION = "relation";
    public static final String _PERSONAL = "personal";
    public static final String _PHONE = "home_phone";
    public static final String _PHONE_MOBILE = "mobile_phone";
    public static final String _SKYPE = "skype";
    public static final String _SITE = "site";
    public static final String _STATUS = "status";
    public static final String _COUNTERS = "counters";

    //PROFILE

    public static final String _ACTIVITIES = "activities";
    public static final String _INTERESTS = "interests";
    public static final String _MUSIC = "music";
    public static final String _MOVIES = "movies";
    public static final String _TV = "tv";
    public static final String _BOOKS = "books";
    public static final String _GAMES = "games";
    public static final String _ABOUT = "about";
    public static final String _QUOTES = "quotes";

    public static final String _ALBUM_WALL = "wall";
    public static final String _ALBUM_PROFILE = "profile";
    public static final String _ALBUM_SAVED = "saved";

    private static final String FRIENDS_GET = BASE_PATH + "friends.get?" + FIELD_FRIENDS_SORT_ORDER + SORT_ORDER_HINTS + "&" + FIELD_USER_FIELDS + _PHOTO_100 + "," + _ONLINE;
    private static final String MESSAGES_GET_DIALOGS = BASE_PATH + "messages.getDialogs?" + FIELD_MESSAGE_PREVIEW + MESSAGE_PREVIEW_LENGTH;
    private static final String MESSAGES_GET_HISTORY = BASE_PATH + "messages.getHistory?";
    private static final String USERS_GET = BASE_PATH + "users.get?" + FIELD_USER_IDS;
    private static final String ALBUMS_GET = BASE_PATH + "photos.getAlbums?" + FIELD_OWNER_ID;
    private static final String PHOTOS_GET = BASE_PATH + "photos.get?" + FIELD_OWNER_ID;

    private static final String MESSAGES_SEND = BASE_PATH + "messages.send?";

    public static String getFriends() {
        return FRIENDS_GET;
    }

    public static String getDialogs() {
        return MESSAGES_GET_DIALOGS;
    }

    public static String getUser() {
        return USERS_GET;
    }

    public static String getDialogHistory() {
        return MESSAGES_GET_HISTORY;
    }

    public static String getAlbums() {
        return ALBUMS_GET;
    }

    public static String getPhotos() {
        return PHOTOS_GET;
    }


    public static String sendMessage() {
        return MESSAGES_SEND;
    }

}