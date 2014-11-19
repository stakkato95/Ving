package com.github.stakkato95.ving;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.stakkato95.ving.auth.Account;
import com.github.stakkato95.ving.auth.VkOAuthHelper;


public class LoginActivity extends ActionBarActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mWebView = (WebView)findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setWebViewClient(new VkWebViewClient());
        mWebView.loadUrl(VkOAuthHelper.AUTORIZATION_URL);
    }

    private class VkWebViewClient extends WebViewClient {

        public VkWebViewClient() {
            super();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.setVisibility(View.INVISIBLE);
            findViewById(android.R.id.progress).setVisibility(View.VISIBLE);

        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            view.setVisibility(View.VISIBLE);
            findViewById(android.R.id.progress).setVisibility(View.INVISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            view.setVisibility(View.VISIBLE);
            findViewById(android.R.id.progress).setVisibility(View.INVISIBLE);

            parseUrl(url);
        }

    }

    private void parseUrl(String url) {
        try {
            if(url == null)
                return;
            if(url.startsWith(VkOAuthHelper.REDIRECT_URL))
            {
                if(!url.contains("error=")){
                    Uri uri = Uri.parse(url);
                    String fragment = uri.getFragment();
                    Uri parsedFragment = Uri.parse("http://temp.com?" + fragment);
                    String access_token = parsedFragment.getQueryParameter("access_token");

                    String user_id = parsedFragment.getQueryParameter("user_id");

                    Intent intent = getIntent();
                    intent.putExtra(Account.ACCESS_TOKEN, access_token);
                    intent.putExtra(Account.USER_ID, Integer.parseInt(user_id));
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
