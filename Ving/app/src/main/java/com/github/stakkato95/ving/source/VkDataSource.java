package com.github.stakkato95.ving.source;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.CoreApplication;
import com.github.stakkato95.ving.auth.VkOAuthHelper;

import java.io.InputStream;

/**
 * Created by Artyom on 19.11.2014.
 */
public class VkDataSource extends HttpDataSource {

    public static final String KEY = VkDataSource.class.getSimpleName();

    public static VkDataSource get(Context context) {
        return CoreApplication.get(context, KEY);
    }

    @Override
    public InputStream getResult(String request) throws Exception {
        String signedUrl = VkOAuthHelper.sign(request);
        String versionValue = Uri.parse(signedUrl).getQueryParameter(Api.VERSION_PARAM);
        if (TextUtils.isEmpty(versionValue)) {
            signedUrl = signedUrl + "&" + Api.VERSION_PARAM + "=" + Api.VERSION_VALUE;
        }
        return super.getResult(signedUrl);
    }

}