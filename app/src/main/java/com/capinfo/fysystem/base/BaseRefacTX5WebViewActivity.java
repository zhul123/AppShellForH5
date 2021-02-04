package com.capinfo.fysystem.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.capinfo.fysystem.R;
import com.capinfo.fysystem.event.NetWorkChangedEvent;
import com.capinfo.fysystem.event.PageStatusReloadEvent;
import com.capinfo.fysystem.utils.DoubleUtils;
import com.capinfo.fysystem.utils.ExitingTrigger;
import com.capinfo.fysystem.utils.Logger;
import com.capinfo.fysystem.utils.ToastUtil;
import com.capinfo.fysystem.utils.sp.AppSharedPreferencesHelper;
import com.capinfo.fysystem.utils.webview.WebViewUtils;
import com.capinfo.fysystem.views.PageNullOrErrorView;
import com.capinfo.fysystem.views.WebSystemTitle;
import com.capinfo.fysystem.views.dialog.CommonDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;


/**
 * @desc : 通用WebView界面
 * <p>
 * <p>
 * 该页面支持Uri动态化调用
 * Uri：/app/commonWebView?
 * urlString=""
 * &extend_param=""
 * &force_back_top=false
 * &show_webview_title=true
 * &fix_webview_title=false
 * &title=""
 * &title_right=""
 * &wxpapy_header_referer=""
 * &intent_name_webview_common_parameter=true
 * <p>
 * 具体表述请查看
 * <p>
 * 例子：这里只传入urlString这个参数就可以其他参数都是默认的，由于该页面使用了继承方式，所以需要手动解析参数传入ARouter框架
 * <p>
 * String uriString = "/app/commonWebView?urlString=\"http%3a%2f%2fm.health.pingan.com\""
 * Uri uri = Uri.parse(uriString);
 * Postcard postcard = ARouter.getInstance().build(uri);
 * for (String key : uri.getQueryParameterNames()) {
 * //这里注意参数的格式boolean、int等，这里通用展示了String格式
 * postcard.withString(key,uri.getQueryParameter(key));
 * }
 * postcard.navigation();
 * </p>
 * isShare方法：是否使用H5控制显示
 */

public class BaseRefacTX5WebViewActivity extends BaseActivity {
    private final ExitingTrigger exitingTrigger = new ExitingTrigger();
    public static final String PROTOCOL_KEY_URL = "urlString";
    public static final String TOKENNAME = "yqfk__Access-Token";
    public static final String LOGOUT = "登出";
    public static final String MORE = "更多";
    public static final String HOME = "首页";
    private static final String TAG = BaseRefacTX5WebViewActivity.class.getSimpleName();
    /**
     * loadurl是否拼接公共参数
     */
    public static final String INTENT_NAME_WEBVIEW_COMMON_PARAMETER = "intent_name_webview_common_parameter";
    public static final String URL_JSON_KEY_TITLE = "title";
    public static final String URL_JSON_KEY_HOME = "home_page";
    public static final String URL_JSON_KEY_LOGIN = "login_page";
    public static final String URL_JSON_KEY_TITLE_RIGHT = "title_right";
    public static final String URL_JSON_KEY_SHOW_TITLE = "show_webview_title";
    public static final String URL_JSON_KEY_FORCE_BACK_TOP = "force_back_top";
    public static final String URL_JSON_KEY_FIX_TITLE = "fix_webview_title";
    public static final String URL_WXPAY_HEADER_REFERER = "wxpapy_header_referer";

    protected ViewGroup mMainLayout;
    protected WebView mWebView;
    protected View mContentLayout;
    protected ProgressBar mProgress;
    protected SmartRefreshLayout smartrefreshlayout;
    protected String targetUrl = "";
    protected String currentUrl = "";
    protected boolean isFirstloading; // 控制页面刷新变量
    // webview加载内容更新后的url
    protected String updatedUrl;
    protected boolean isHome = false;
    protected boolean isLogout = false;
    protected boolean x5IsLoad = false;
    protected boolean isback = false;

    /**
     * 用来保存title,主要解决一些系统mWebView.goBack();之后不调用onReceivedTitle监听重新设置title
     */
    private LinkedList<String> mUrlArray = new LinkedList<>();
    private LinkedList<String> mTitleArray = new LinkedList<>();

    private PageNullOrErrorView nullOrErrorView;

    private boolean needClearHistory;

    @Autowired(name = URL_JSON_KEY_HOME, desc = "Home页地址")
    protected String homeUrl;
    @Autowired(name = URL_JSON_KEY_LOGIN, desc = "login页地址")
    protected String loginUrl;
    @Autowired(name = URL_JSON_KEY_TITLE, desc = "默认标题")
    protected String titleContent;
    protected boolean showTitle;
    protected String titleRight;
    protected WebSystemTitle systemTitle;


    private Map<String, String> mHeader = new HashMap<>();
    private long mLastTime = System.currentTimeMillis();
    private ImageView mScreenshotImageView;
    //是否拦截返回键
    private boolean interceptBackPress = false;

    private static final String H5_METHOD_INTERCEPT_BACK_PRESS = "interceptBackPressCallBack";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nullOrErrorView = this.findViewById(R.id.page_status_view);

        mMainLayout = findViewById(R.id.mainLayout);
        mContentLayout = findViewById(R.id.content_layout);
        mProgress = findViewById(R.id.progress);
        smartrefreshlayout = findViewById(R.id.smartrefreshlayout);
        mProgress.setMax(100);
        mWebView = findViewById(R.id.webView1);
        mWebView.setWebViewClient(webViewClient);

        mScreenshotImageView = findViewById(R.id.screenshot_image_view);

        WebViewUtils.setX5WebViewProperty(this, mWebView, mMainLayout);

        IX5WebViewExtension d = mWebView.getX5WebViewExtension();
        if (d == null) {
            print("未加载 X5 ");
        } else {
            x5IsLoad = true;
            print("已加载 X5");
        }
        d = null;

        targetUrl = mergeTargetUrl();

        initTitleBar();
        WebChromeClient webChromeClient = new CusWebChromeClient();
        mWebView.setWebChromeClient(webChromeClient);

        isFirstloading = true;

        smartrefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartrefreshlayout.finishRefresh();
                loadUrl(currentUrl);
            }
        });
    }

    @Override
    protected int getlayoutId() {
        return R.layout.activity_tx5_web_view;
    }

    protected String mergeTargetUrl() {
        String token = AppSharedPreferencesHelper.getToken();
        if(!TextUtils.isEmpty(token)) {
            /*if(mWebView != null){
                String setTokenJS = "javascript:localStorage.setItem('"+TOKENNAME+"','"+token+"')";
                mWebView.evaluateJavascript(setTokenJS,null);
            }*/
            targetUrl = homeUrl;
        }else{
            targetUrl = loginUrl;
        }
        return targetUrl;
    }

    private void initTitleBar() {
        //是否使用自定义标题 TODO:ARouter
        try {
            //为了兼容ARouter用uri传递参数，我们采用String进行解析转换成boolean类型
            String showTitleString = getIntent().getStringExtra(URL_JSON_KEY_SHOW_TITLE);
            if (TextUtils.isEmpty(showTitleString)) {
                showTitle = getIntent().getBooleanExtra(URL_JSON_KEY_SHOW_TITLE, true);
            } else {
                showTitle = Boolean.parseBoolean(showTitleString);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showTitle = getIntent().getBooleanExtra(URL_JSON_KEY_SHOW_TITLE, true);
        }

        if (showTitle) {
            //自定义标题内容
            titleContent = getIntent().getStringExtra(URL_JSON_KEY_TITLE);
            //自定义标题右边按钮文字
            titleRight = getIntent().getStringExtra(URL_JSON_KEY_TITLE_RIGHT);
            systemTitle = findViewById(R.id.system_title);
            setTitle(titleContent);

            systemTitle.setWebView(mWebView);
            //左1的图片设置--back
            systemTitle.setLeftBackBtn(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backWebView();
                }
            });
            systemTitle.setLeftBackBtnVisible(false);
            //标题
            if (!TextUtils.isEmpty(titleContent)) {
                systemTitle.setTitle(titleContent);
            }

        } else {
            findViewById(R.id.system_title).setVisibility(View.GONE);
        }
    }



    protected void showContent(boolean show) {
        if (show) {
            PageNullOrErrorView.hide(nullOrErrorView, mContentLayout);
        } else {
            PageNullOrErrorView.showNoInternetView(nullOrErrorView);
            mContentLayout.setVisibility(View.GONE);
        }
    }

    protected void loadUrl(final String url) {

        if (isNetworkAvailable()) {
            showContent(true);

            if (!TextUtils.isEmpty(url)) {
                String token = AppSharedPreferencesHelper.getToken();
                if(!TextUtils.isEmpty(token)){
                    String js = String.format("javascript:setToken('%s')",token);
                    mWebView.evaluateJavascript(js,null);
                }
                Logger.i("webView load url: " + url);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (TextUtils.isEmpty(mHeader.get("Referer"))) {
                            String referer = getReferer(url);
                            if (!TextUtils.isEmpty(referer)) {
                                mHeader.put("Referer", referer);
                                mWebView.loadUrl(url, mHeader);
                            } else {
                                mWebView.loadUrl(url);
                            }
                        } else {
                            mWebView.loadUrl(url, mHeader);
                        }

                    }
                });
            }
        } else {
            showContent(false);
        }
    }

    private String getReferer(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        Uri uri = Uri.parse(url);
        if (null == uri || TextUtils.isEmpty(uri.getHost())) {
            return "";
        }
        return uri.getScheme() + "://" + uri.getHost();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstloading) {
            loadUrl(targetUrl);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFirstloading = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack() &&
                !(homeUrl != null && homeUrl.equals(mWebView.getUrl()))
        ) {
            print("bakc:"+mWebView.getUrl());
            webViewBack();
        } else {
            if (exitingTrigger.testExpired(System.currentTimeMillis())) {
                ToastUtil.getInstance().makeText(R.string.toast_exit);
            } else {
                finish();
            }
        }
    }

    private final class CusWebChromeClient extends WebChromeClient {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            currentUrl = view.getUrl();
            title = view.getTitle();
            if (!mUrlArray.contains(currentUrl)) {
                mUrlArray.addFirst(currentUrl);
            }
            if (!TextUtils.isEmpty(title) && !mTitleArray.contains(title)) {
                mTitleArray.addFirst(title);
            }

            if(x5IsLoad){
                if(isback) {
                    if (!mUrlArray.isEmpty()) {
                        mUrlArray.removeFirst();
                    }
                    if (!mTitleArray.isEmpty()) {
                        mTitleArray.removeFirst();
                    }
                    isback = false;
                }
            }

            if (systemTitle != null) {
                if(title != null){
                    setTitle(title);
                    titleContent = title;
                    systemTitle.setTitle(titleContent);
                }
            }

            if(isHome || isLogout){
                mTitleArray.clear();
                mUrlArray.clear();
                if(!x5IsLoad) {
                    mTitleArray.addFirst(titleContent);
                    mUrlArray.addFirst(targetUrl);
                }
                mWebView.clearHistory();
                isHome = false;
                isLogout = false;
            }

            changeBarStatus(mTitleArray.size() > 1,mTitleArray.size() < 2);

        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            print(newProgress+"");
            if(newProgress > 10) {
                mProgress.setProgress(newProgress);
            }
        }
    }

    private final WebViewClient webViewClient = new WebViewClient() {

        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onReceivedError(WebView webView, int i, String s, String s1) {
            super.onReceivedError(webView, i, s, s1);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
            print("shouldOverrideUrl: " + webView.getUrl());
            return super.shouldOverrideUrlLoading(webView, webResourceRequest);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            print("url shouldOverrideUrl: " + url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            print("onPageStarted");
            mProgress.setVisibility(View.VISIBLE);
            mProgress.setProgress(10);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            print("finish");
            if(animation == null){
                initAnimation();
            }
            if(mProgress.getVisibility() == View.VISIBLE) {
                mProgress.startAnimation(animation);
                mProgress.setVisibility(View.GONE);
            }
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
            updatedUrl = url;
            if (needClearHistory) {
                needClearHistory = false;
                if (null != mWebView) {
                    mWebView.clearHistory();//清除历史记录
                }
            }
        }
    };
    private AlphaAnimation animation;
    private void initAnimation(){
        animation = new AlphaAnimation(1,0);
        animation.setDuration(500);
    }

    /**
     * 传参数给会页面
     */
    protected void callBackToJs(final String callbackFunc, final String callbackParam) {
        if (TextUtils.isEmpty(callbackFunc)) {
//            Logger.e("callback Func is Null");
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:" + callbackFunc + "(" + callbackParam + ")");
                }
            });
        }
    }


    private void webViewDestroy() {
        WebViewUtils.onWebViewDestroy(mWebView);
        mWebView = null;
    }

    @Override
    protected void onDestroy() {
        webViewDestroy();
        super.onDestroy();
    }

    public String getUpdatedUrl() {
        return updatedUrl;
    }


    public void backWebView() {
        if (mWebView == null) {
        } else {
            if (interceptBackPress) {
                handleBackWebViewSafety();
                return;
            }
            if (mWebView.canGoBack()) {
                webViewBack();
            }
        }
    }

    private void webViewBack() {
        isback = true;
        mWebView.goBack();
        if (!mTitleArray.isEmpty() && !x5IsLoad) {
                mUrlArray.removeFirst();
                mTitleArray.removeFirst();
            print("back:"+mTitleArray.toString());
            if (!mTitleArray.isEmpty()) {
                String title = mTitleArray.getFirst();
                if (!TextUtils.isEmpty(title)) {
                    decodeSystemTitle(title, null);
                }
            } else {
                changeBarStatus(false,true);
            }
        }
    }

    /**
     * 修改左右按钮样式
     * @param leftVisible
     * @param rightVisible
     */
    private void changeBarStatus(boolean leftVisible ,boolean rightVisible){
        if(systemTitle != null) {
            systemTitle.setLeftBackBtnVisible(leftVisible);
            systemTitle.setRightTextBtnVisible(true);
            if(!leftVisible){
                systemTitle.setRightText(MORE);
            }else{
                systemTitle.setRightText(HOME);
//                systemTitle.setRightTextBtnVisible(false);
            }
        }
        if(smartrefreshlayout != null){
            //当没有返回按钮时可刷新（即首页可刷新）
            smartrefreshlayout.setEnableRefresh(!leftVisible);
        }
    }

    private void handleBackWebViewSafety() {
        if (null == mWebView) {
            interceptBackPress = false;
            return;
        }
        callBackToJs(H5_METHOD_INTERCEPT_BACK_PRESS, "");
        mWebView.postDelayed(new Runnable() {
            @Override
            public void run() {
                interceptBackPress = false;
            }
        }, 500);
    }

    @Override
    public void onEventMainThread(Object event) {
        super.onEventMainThread(event);
        if (event instanceof PageStatusReloadEvent) {
            loadUrl(targetUrl);
        } else if (event instanceof NetWorkChangedEvent) {
            if(nullOrErrorView != null && nullOrErrorView.getVisibility() == View.VISIBLE){
                loadUrl(targetUrl);
            }
        }
    }


}
