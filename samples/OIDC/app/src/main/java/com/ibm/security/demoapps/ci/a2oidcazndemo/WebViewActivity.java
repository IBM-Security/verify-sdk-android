package com.ibm.security.demoapps.ci.a2oidcazndemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ibm.security.verifysdk.ContextHelper;
import com.ibm.security.verifysdk.NetworkHandler;
import com.ibm.security.verifysdk.OAuthContext;

import java.util.HashMap;

import okhttp3.logging.HttpLoggingInterceptor;

public class WebViewActivity extends AppCompatActivity {

    private final String TAG = "WebViewActivity";

    private WebView webView;
    private String clientId;
    private String clientSecret;
    private String hostname;

    private String authorizeUrl;
    private String tokenUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final boolean ignoreSsl = false;
        webView = new WebView(ContextHelper.sharedInstance().getContext());

        clientId = getIntent().getStringExtra("clientId");
        clientSecret = getIntent().getStringExtra("clientSecret");
        hostname = getIntent().getStringExtra("hostname");

        authorizeUrl = "https://" + hostname + "/oidc/endpoint/default/authorize";
        tokenUrl =  "https://" + hostname + "/oidc/endpoint/default/token";
        Log.d(TAG, "Url: " + authorizeUrl + "?client_id=" + clientId + "&response_type=code&scope=openid&redirect_uri=oidc://callback");

        CookieManager.getInstance().removeAllCookies(null);
        webView.loadUrl(authorizeUrl + "?client_id=" + clientId + "&response_type=code&scope=openid&redirect_uri=oidc://callback");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                Log.d(TAG, "Url: " + request.getUrl());
                Log.d(TAG, "Scheme: " + request.getUrl().getScheme());

                if (!"oidc".equals(request.getUrl().getScheme())) {
                    return false;
                }

                String code = request.getUrl().getQueryParameter("code");

                NetworkHandler.sharedInstance().setLoggingInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

                OAuthContext.sharedInstance().authorize(tokenUrl, clientId, code, new HashMap<String, Object>() {{
                    put("redirect_uri", "oidc://callback");
                    put("client_secret", clientSecret);
                }}, ignoreSsl, (oAuthToken, e) -> {

                    if (e != null)  {
                        setResult(RESULT_OK,  new Intent().putExtra("error", e));
                    } else {
                        setResult(RESULT_OK,  new Intent().putExtra("token", oAuthToken));
                    }
                    if (!isFinishing()) {
                        finish();
                    }
                });

                return true;
            }
        });

        setContentView(webView);
    }
}
