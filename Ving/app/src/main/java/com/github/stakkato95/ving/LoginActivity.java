package com.github.stakkato95.ving;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.github.stakkato95.ving.auth.Account;
import com.github.stakkato95.ving.auth.VkOAuthHelper;


public class LoginActivity extends ActionBarActivity {

    private WebView mWebView;
    private boolean isErrorRecieved;
    private Button mRetryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mRetryButton = (Button)findViewById(R.id.retry_update_button);
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl(VkOAuthHelper.AUTORIZATION_URL);
            }
        });

        getSupportActionBar().hide();
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
            isErrorRecieved = false;
            findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
            view.setVisibility(View.INVISIBLE);
            findViewById(R.id.on_error_text).setVisibility(View.INVISIBLE);
            mRetryButton.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            isErrorRecieved = true;
            findViewById(android.R.id.progress).setVisibility(View.GONE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if(!isErrorRecieved) {
                view.setVisibility(View.VISIBLE);
                findViewById(android.R.id.progress).setVisibility(View.GONE);
                parseUrl(url);
            } else {
                findViewById(R.id.on_error_text).setVisibility(View.VISIBLE);
                view.setVisibility(View.INVISIBLE);
                mRetryButton.setVisibility(View.VISIBLE);
            }
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
