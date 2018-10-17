package com.software.tempe.simplenews;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import dmax.dialog.SpotsDialog;

public class NewsDetailActivity extends AppCompatActivity {

    private WebView article_webview;

    private AlertDialog alertDialog;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        alertDialog = new SpotsDialog.Builder()
                .setContext(NewsDetailActivity.this)
                .build();

        alertDialog.show();

        article_webview = findViewById(R.id.article_webview);
        article_webview.getSettings().setJavaScriptEnabled(true);
        article_webview.setWebChromeClient(new WebChromeClient());
        article_webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                alertDialog.dismiss();
            }
        });

        if (getIntent() != null)    {
            if(!getIntent().getStringExtra("web_url").isEmpty()) {
                article_webview.loadUrl(getIntent().getStringExtra("web_url"));
            }
        }
    }
}
