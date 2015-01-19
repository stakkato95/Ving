package com.github.stakkato95.ving.api;

/**
 * Created by Artyom on 20.11.2014.
 */
public class Api {

    public static final String BASE_PATH = "https://api.vk.com/method/";
    public static final String VERSION_VALUE = "5.8";
    public static final String VERSION_PARAM = "v";

    public static final int GET_COUNT = 30;

    public static final String GET_FRIENDS = BASE_PATH + "friends.get?fields=photo_100,online";
    public static final String GET_DIALOGS = BASE_PATH + "friends.get?fields=photo_100,online";

    public static String getFriends() {
        return GET_FRIENDS;
    }

    public static String getDialogs() {
        return GET_DIALOGS;
    }

}