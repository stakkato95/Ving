package com.github.stakkato95.ving.api;

/**
 * Created by Artyom on 20.11.2014.
 */
public class Api {

    public static final String BASE_PATH = "https://api.vk.com/method/";
    public static final String VERSION_VALUE = "5.8";
    public static final String VERSION_PARAM = "v";

    public static final String JSON_RESPONSE = "response";
    public static final String JSON_ITEMS = "items";

    public static final int GET_COUNT = 30;
    public static final String ONE_INTERLOCUTOR_DIALOG = " ... ";
    public static final int ROUTE_IN = 0;
    public static final int ROUTE_OUT = 1;
    public static final int READ = 1;
    public static final int UNREAD = 0;
    public static final int MESSAGE_PREVIEW_LENGTH = 35;

    public static final String FIELD_PHOTO_100 = "photo_100";
    public static final String FIELD_ONLINE = "online";
    public static final String FIELD_USER_IDS = "user_ids=";
    public static final String FIELD_MESSAGE_CHAT_ID = "chat_id=";
    public static final String FIELD_MESSAGE_USER_ID = "user_id=";
    public static final String FIELD_COUNT = "count=";
    public static final String FIELD_OFFSET = "offset=";
    public static final String FIELD_MESSAGE_PREVIEW = "preview_length=";
    public static final String FIELD_MESSAGE = "message=";

    private static final String FRIENDS_GET = BASE_PATH + "friends.get?fields=" + FIELD_PHOTO_100 + "," + FIELD_ONLINE;
    private static final String MESSAGES_GET_DIALOGS = BASE_PATH + "messages.getDialogs?" + FIELD_MESSAGE_PREVIEW + MESSAGE_PREVIEW_LENGTH;
    private static final String MESSAGES_GET_HISTORY = BASE_PATH + "messages.getHistory?";
    private static final String USERS_GET = BASE_PATH + "users.get?fields=" + FIELD_PHOTO_100 + "," + FIELD_ONLINE + "&" + FIELD_USER_IDS;

    private static final String MESSAGES_SEND = BASE_PATH + "messages.send?";

    public static String getFriends() {
        return FRIENDS_GET;
    }

    public static String getDialogs() {
        return MESSAGES_GET_DIALOGS;
    }

    public static String getUsers() {
        return USERS_GET;
    }

    public static String getDialogHistory() {
        return MESSAGES_GET_HISTORY;

    }

    public static String sendMessage() {
        return MESSAGES_SEND;
    }

}