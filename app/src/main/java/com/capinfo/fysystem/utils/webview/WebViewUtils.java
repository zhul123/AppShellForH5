package com.capinfo.fysystem.utils.webview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebStorage;

import com.capinfo.fysystem.BuildConfig;
import com.capinfo.fysystem.utils.ScreenUtils;
import com.capinfo.fysystem.utils.ToolUtils;
import com.capinfo.fysystem.utils.Utils;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static com.capinfo.fysystem.utils.webview.IntentUtils.gotoPdfActivity;
import static com.capinfo.fysystem.utils.webview.IntentUtils.gotoRefacWebViewActivity;
import static com.capinfo.fysystem.utils.webview.JavascriptInterface.JAVASCRIPT_NAME;


public class WebViewUtils {
    private static final String TAG = "WebViewUtils";

    /**
     * @param activity
     * @param webView
     * @param mainLayout 用于h5调用客户端share分享，传入xml文件最外层Layout
     */
    public static void setX5WebViewProperty(Activity activity, WebView webView, ViewGroup mainLayout) {
        Log.d(TAG,"setX5WebViewProperty activity = " + activity);
        WebSettings setting = webView.getSettings();
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setJavaScriptEnabled(true);
        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        setting.setPluginState(WebSettings.PluginState.ON);
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        //打开本地缓存
        setting.setAppCacheEnabled(true);
        setting.setDomStorageEnabled(true);
        setting.setDatabaseEnabled(true);
        setting.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = activity.getCacheDir().getAbsolutePath();
        setting.setAppCachePath(appCachePath);
        setting.setAllowFileAccess(true);
        setting.setUserAgentString(setting.getUserAgentString() + " Capinfo/" + String.valueOf(Utils.getVersion(activity)));
        // 设置no可以支持缩放
        setting.setSupportZoom(false);
        // 设置no出现缩放工具
        setting.setBuiltInZoomControls(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setting.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.addJavascriptInterface(JavascriptInterface.factoryH5Activity(activity,webView, mainLayout), JAVASCRIPT_NAME);
    }


    public static String mergeTargetUrl(Context context, String intentUrl, String extendParam) {
        if (TextUtils.isEmpty(intentUrl)) {
            return "";
        }
        String targetUrl = "";
        /*
        StringBuilder stringBuilder = new StringBuilder(intentUrl);
        // url与参数之间要使用?, 参数与参数之间要使用 &链接s
        if (intentUrl.contains("?")) {
//            stringBuilder.append("&accessToken=");
            stringBuilder.append("&");
        } else {
//            stringBuilder.append("?accessToken=");
            stringBuilder.append("?");
        }
        stringBuilder
//                .append(AppSharedPreferencesHelper.getCurrentUserToken())
//                .append("&version=1.0&appid=") // follow pmd rule 'ConsecutiveLiteralAppends'
                .append("version=1.0&appid=") // follow pmd rule 'ConsecutiveLiteralAppends'
                .append(SystemFunction.parseAppIdentifier(context, HttpRequestUtil.APP_IDENTIFIER))
                .append("&vuid=")
                .append(AppSharedPreferencesHelper.getCurrentVitalityid())
                .append("&userId=")
                .append(AppSharedPreferencesHelper.getCurrentUid())
                .append("&phone=")
                .append(AppSharedPreferencesHelper.getCurrentUserPhone());
        String channel = Utils.getChannel(context, "UMENG_CHANNEL");
        if (!TextUtils.isEmpty(channel)) {
            stringBuilder.append("&pa_from=").append(channel);
        }

        if (!TextUtils.isEmpty(extendParam)) {
            stringBuilder.append(extendParam);
        }
        targetUrl = stringBuilder.toString();
        stringBuilder.setLength(0);*/
        return targetUrl;
    }
    public static String mergeTargetUrlNoPublicParams(Context context, String intentUrl, String extendParam) {
        if (TextUtils.isEmpty(intentUrl)) {
            return "";
        }
        String targetUrl = "";
        StringBuilder stringBuilder = new StringBuilder(intentUrl);
        // url与参数之间要使用?, 参数与参数之间要使用 &链接s
        if (intentUrl.contains("?")) {
//            stringBuilder.append("&accessToken=");
            stringBuilder.append("&");
        } else {
//            stringBuilder.append("?accessToken=");
            if (!TextUtils.isEmpty(extendParam)) {
                stringBuilder.append("?");
            }
        }

        if (!TextUtils.isEmpty(extendParam)) {
            stringBuilder.append(extendParam);
        }
        targetUrl = stringBuilder.toString();
        stringBuilder.setLength(0);
        return targetUrl;
    }

    private static int mLastWebViewHeight = 0;
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * 产品详情页（保险，服务卡）WebView配置
     * 用的android.webkit.WebView
     *
     */
    public static void productDetailWebViewOption(
            final Activity context,
            final WebView webView,
            final View webLayout,
            final ViewGroup.LayoutParams webLayoutParam,
            final ViewGroup mainLayout) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setCacheMode(android.webkit.WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setUserAgentString(webView.getSettings().getUserAgentString() + " PAHealth/" + String.valueOf(Utils.getVersion(context)));
        webView.addJavascriptInterface(JavascriptInterface.factoryH5Activity(context,webView,mainLayout), JAVASCRIPT_NAME);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                if (null == webView) {
                    return true;
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        String tmpUrl = "";
                        String title = "";
                        if (ToolUtils.isUrlParam(url)) {
                            String[] urls = url.split("\\?");
                            tmpUrl = urls[0];
                            title = ToolUtils.getParamByUrl(url, "title");
                            if (TextUtils.isEmpty(title)) {
                                title = "";
                            } else {
                                try {
                                    title = URLDecoder.decode(title, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (ToolUtils.isUrlPdf(tmpUrl)) {
                                gotoPdfActivity(title, tmpUrl, 0, "", "");
                            } else {
                                gotoRefacWebViewActivity(context, url, title);
                            }
                        } else {
                            tmpUrl = url;
                            IntentUtils.gotoPdfOrWebViewActivity(context, tmpUrl, title);
                        }

                    }
                });

                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (null == webLayout) {
                    return;
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        webLayoutParam.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        if (mLastWebViewHeight >= ScreenUtils.getScreenBounds(context)[1]) {
                            webLayoutParam.height = ScreenUtils.getScreenBounds(context)[1];
                        } else {
                            webLayoutParam.height = mLastWebViewHeight;
                        }

                        webLayout.setLayoutParams(webLayoutParam);
                    }
                });

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (null == webLayout) {
                    return;
                }
                productDetailWebViewHeight(context, webView, webLayout, webLayoutParam, webView.getContentHeight());
            }
        });
    }

    /**
     * 产品详情页（保险，服务卡）WebView设置高度
     * 用的android.webkit.WebView
     *
     */
    public static final void productDetailWebViewHeight(
            final Activity context,
            final WebView webView,
            final View layout,
            final ViewGroup.LayoutParams layoutParams,
            final int height) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (context.isFinishing() || null == layout || null == webView) {
                    return;
                }
                mLastWebViewHeight = (int) (height * webView.getScale());
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = mLastWebViewHeight;
                layout.setLayoutParams(layoutParams);
            }
        });
    }

    public static void onWebViewDestroy(WebView webView) {
        try {
            if (webView != null) {
                webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
                webView.clearHistory();
                ViewParent parent = webView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(webView);
                }
                //start 防止腾讯X5内存泄漏
                try {
                    webView.stopLoading();
                    webView.removeAllViewsInLayout();
                    webView.removeAllViews();
                    webView.setWebViewClient(null);
                    CookieSyncManager.getInstance().stopSync();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                //end

                webView.destroy();
                webView = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除腾讯X5的cookie缓存
     * @param context
     */
    public static void clearTbsX5Cookie(Context context) {
        try {
            //start
            CookieSyncManager.createInstance(context);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.removeSessionCookies(null);
                cookieManager.removeAllCookie();
                cookieManager.flush();
            } else {
                cookieManager.removeAllCookie();
                CookieSyncManager.getInstance().sync();
            }
            WebStorage.getInstance().deleteAllData();
            //end
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}
