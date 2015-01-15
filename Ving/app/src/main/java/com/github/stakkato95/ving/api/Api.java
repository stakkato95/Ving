package com.github.stakkato95.ving.api;

/**
 * Created by Artyom on 20.11.2014.
 */
public class Api {

    public static final String BASE_PATH = "https://api.vk.com/method/";
    public static final String VERSION_VALUE = "5.8";
    public static final String VERSION_PARAM = "v";

    public static final int FRIENDS_GET_COUNT = 20;
    public static final int FRIENDS_GET_DEFAULT_OFFSET = 0;

    public static final String FRIENDS_GET = BASE_PATH + "friends.get?fields=photo_100,online";
}