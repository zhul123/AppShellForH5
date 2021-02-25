package com.capinfo.fysystem.utils.webview;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.capinfo.fysystem.utils.sp.AppSharedPreferencesHelper;
import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import com.tencent.smtt.sdk.WebView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 所有WebView都需要用到
 * Created by jiahongfei on 16/8/20.
 */
public class JavascriptInterface {

    public static final String JAVASCRIPT_NAME = "android";
    public static final String TOKEN = "token";

    private static final String TAG = "JavascriptInterface";

    /**
     * 锁
     */
    private static final Object mDataLock = new Object();

    private Context mContext;
    private View mShareView;
    private WebView mWebView;
    private Handler mHandler = new Handler(Looper.getMainLooper());


    /**
     * 主要针对H5页面和嵌入页面中的Webview进行设置
     *
     * @return
     */
    public static final JavascriptInterface factoryH5Activity(Context context, WebView webview, View view) {
        return new JavascriptInterface(context, webview, view);
    }

    private JavascriptInterface(Context context, WebView webview, View shareView) {
        mContext = context;
        mShareView = shareView;
        mWebView = webview;
    }

    @android.webkit.JavascriptInterface
    public void setToken(String token) {
        System.out.println("======setTOkennnn");
        AppSharedPreferencesHelper.setToken(token);
    }

    @android.webkit.JavascriptInterface
    public String getToken() {
        return AppSharedPreferencesHelper.getToken();
    }

    @android.webkit.JavascriptInterface
    public void setResult(String result) {
        System.out.println("======setResultsetResult");
        AppSharedPreferencesHelper.setResult(result);
    }

    @android.webkit.JavascriptInterface
    public String getResult() {
        return AppSharedPreferencesHelper.getResult();
    }

}
