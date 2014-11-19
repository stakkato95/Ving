package com.github.stakkato95.ving.auth;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.auth.AuthenticationException;

/**
 * Created by Artyom on 19.11.2014.
 */
public class VkOAuthHelper {

    private static String sToken;

    public static final String REDIRECT_URL = "https://oauth.vk.com/blank.html";
    public static final String AUTORIZATION_URL = "https://oauth.vk.com/authorize?client_id=4616591&scope=offline,wall,photos,status&redirect_uri=" + REDIRECT_URL + "&display=touch&response_type=token";

    public static String sign(String url) {
        if (url.contains("?")) {
            return url + "&access_token="+sToken;
        } else {
            return url + "?access_token="+sToken;
        }
    }

}
