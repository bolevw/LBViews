package com.test.lbviews.androidview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.test.lbviews.R;

import java.util.HashMap;

/**
 * webView开车指南
 * Created by liubo on 2017/3/8.
 */

public class SWebView extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swebview);
        webView = (WebView) findViewById(R.id.webview);  // 一般不推荐webview直接写在布局里，一般是addview添加

        webView.loadUrl(""); //webview加载url

        HashMap<String, String> userHeader = new HashMap<>();
        userHeader.put("User-Agent", "android");
        webView.loadUrl("www.baidu.com", userHeader); //加载URL 并添加请求头

        String html = "Html 数据";
        webView.loadData(html, "text/html", "utf-8"); // webview加载html片段，但是可能会导致加载乱码 ,推荐下面等方法
        webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);

        //支持js
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JSBridge(), "JS"); //为了安全起见，js只能调用JavascriptInterface注释的方法，js调用方式："window:JS:showToast()";


        webView.loadUrl("javascript:actionFromNativeWithParam(" + "'come from Native'" + ")");


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {//设置webview内进行跳转
                view.loadUrl(url);
                return true;

            }

            @Override
            public void onLoadResource(WebView view, String url) {
                //加载资源的时候
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //加载页面时
                super.onPageStarted(view, url, favicon);
            }


        });

    }

    class JSBridge {
        @JavascriptInterface
        public void showToast() {
            Toast.makeText(SWebView.this, "js - > android toast", Toast.LENGTH_SHORT).show();
        }
    }
}
