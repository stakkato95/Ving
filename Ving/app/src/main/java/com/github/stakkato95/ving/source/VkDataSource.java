package com.github.stakkato95.ving.source;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.github.stakkato95.ving.Api;
import com.github.stakkato95.ving.CoreApplication;
import com.github.stakkato95.ving.auth.VkOAuthHelper;

import java.io.InputStream;

/**
 * Created by Artyom on 19.11.2014.
 */
public class VkDataSource extends HttpDataSource {

    public static final String KEY = "VkDataSource";

    public static VkDataSource get(Context context) {
        return CoreApplication.get(context, KEY);
    }

    @Override
    public InputStream getResult(String p) throws Exception {
        String signUrl = VkOAuthHelper.sign(p);
        String versionValue = Uri.parse(signUrl).getQueryParameter(Api.VERSION_PARAM);
        if (TextUtils.isEmpty(versionValue)) {
            signUrl = signUrl + "&" + Api.VERSION_PARAM + "=" + Api.VERSION_VALUE;
        }
        return super.getResult(signUrl);
    }

}