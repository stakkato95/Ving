package com.github.stakkato95.ving.auth;

/**
 * Created by Artyom on 19.11.2014.
 */
public class VkOAuthHelper {

    public static final String PERMISSION = "notify,friends,photos,audio,video,docs,status,notes,pages,wall,groups,messages,offline,notifications";
    public static final String CLIENT_ID = "4616591";


    public static final String REDIRECT_URL = "https://oauth.vk.com/blank.html";
    public static final String AUTORIZATION_URL = "https://oauth.vk.com/authorize?client_id=" + CLIENT_ID +"&scope=" + PERMISSION + "&redirect_uri=" + REDIRECT_URL + "&display=touch&response_type=token";

    public static String sign(String request) {
        if (request.contains("?")) {
            return request + "&access_token=" + Account.access_token;
        } else {
            return request + "?access_token=" + Account.access_token;
        }
    }




}
