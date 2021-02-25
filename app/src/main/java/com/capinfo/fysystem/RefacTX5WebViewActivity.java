package com.capinfo.fysystem;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.capinfo.fysystem.base.BaseRefacTX5WebViewActivity;
import com.capinfo.fysystem.utils.DoubleUtils;
import com.capinfo.fysystem.utils.ToastUtil;
import com.capinfo.fysystem.utils.sp.AppSharedPreferencesHelper;
import com.capinfo.fysystem.utils.webview.WebViewUtils;
import com.capinfo.fysystem.views.dialog.CommonDialog;
import com.capinfo.fysystem.views.popwindows.PopWinForList;
import com.capinfo.fysystem.views.popwindows.PopWindowMenu;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.core.widget.PopupWindowCompat;

/**
 * @desc : 通用WebView界面
 * <p>
 * <pre>
 * 该页面支持Uri动态化调用
 * Uri：/app/commonWebView?
 * urlString=""
 * &extend_param=""
 * &intent_name_notice=false
 * &intent_name_splash=false
 * &force_back_top=false
 * &show_webview_title=true
 * &fix_webview_title=false
 * &title=""
 * &title_right=""
 * &intent_name_webview_common_parameter=true
 *
 * 具体表述请查看
 *
 * 例子：这里只传入urlString这个参数就可以其他参数都是默认的，由于该页面使用了继承方式，所以需要手动解析参数传入ARouter框架
 * String uriString = "/app/commonWebView?urlString=\"http%3a%2f%2fm.health.pingan.com\""
 * Uri uri = Uri.parse(uriString);
 * Postcard postcard = ARouter.getInstance().build(uri);
 * for (String key : uri.getQueryParameterNames()) {
 * //这里注意参数的格式boolean、int等，这里通用展示了String格式
 * postcard.withString(key,uri.getQueryParameter(key));
 * }
 * postcard.navigation();
 * </pre>
 * </p>
 */
@Route(path = "/comm/commonWebview", name = "通用WebView界面")
public class RefacTX5WebViewActivity extends BaseRefacTX5WebViewActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = RefacTX5WebViewActivity.class.getSimpleName();
    private final String REVISEPSD = "修改密码";
    private final String LOGOUTSTR = "退出登录";
    private final String CLEAR = "清除缓存";
    private final String UPLOAD = "版本更新";
    public static final String URL_JSON_KEY_REVISEPSD = "revise_psd";

    private CommonDialog mCommonDialog;
    private PopWinForList mPopWin;
    @Autowired(name=URL_JSON_KEY_REVISEPSD,desc = "修改密码地址")
    protected String revise_psd = "";
    private List<String> moreDatas = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //闪烁避免
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        initDialog();
        moreDatas.add(REVISEPSD);
        moreDatas.add(UPLOAD);
        moreDatas.add(CLEAR);
        moreDatas.add(LOGOUTSTR);
        if (showTitle) {
            titleRight = "V"+BuildConfig.VERSION_NAME;
            //右侧文字的设置
            if (!TextUtils.isEmpty(titleRight)) {
                systemTitle.setRightTextBtn(titleRight, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Beta.checkUpgrade();
                    }
                });
            }
            systemTitle.setRightImageLsn(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tag = (String) v.getTag();
                    setRightOnClick(v,tag);

                }
            });

        }
    }

    @Override
    protected void onResume() {
        if(mWebView != null){
            mWebView.post(mRunnable);
        }
        super.onResume();
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            UpgradeInfo upgradeInfo = Beta.getAppUpgradeInfo();
            if(upgradeInfo != null && BuildConfig.VERSION_CODE < upgradeInfo.versionCode){
                if(systemTitle != null){
                    systemTitle.setRightTextDrawable(0,0,R.mipmap.icon_new,0);
                }
            }
        }
    };
    public void setRightOnClick(View view,String tag) {
        if (!isLive())
            return;
        //防止多次点击
        if (!DoubleUtils.isFastDoubleClick() && isNetworkAvailable()) {
            print(mWebView.getUrl());
            if (MORE.equals(tag)) {
                if(mWebView != null && loginUrl.equals(mWebView.getUrl())){
                    moreDatas.remove(REVISEPSD);
                    moreDatas.remove(LOGOUTSTR);
                }else{
                    if(!moreDatas.contains(REVISEPSD)){
                        moreDatas.add(REVISEPSD);
                    }
                    if(!moreDatas.contains(LOGOUTSTR)){
                        moreDatas.add(LOGOUTSTR);
                    }
                }

                mPopWin = new PopWinForList(this);
                mPopWin.setDatas(moreDatas);
                mPopWin.setOnItemClick(this);
                mPopWin.showPop(view);
            } else if (HOME.equals(tag)) {
                loadUrl(TextUtils.isEmpty(homeUrl) ? targetUrl : homeUrl);
                isHome = true;
            } else if (REVISEPSD.equals(tag)) {
                loadUrl(revise_psd);
            }
        }
    }

    private void initDialog() {
        mCommonDialog = new CommonDialog(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(loginUrl)) {
                    WebViewUtils.clearTbsX5Cookie(RefacTX5WebViewActivity.this);
                    String setTokenJS = "javascript:localStorage.clear()";
                    AppSharedPreferencesHelper.setToken(null);
                    AppSharedPreferencesHelper.setResult(null);
                    mWebView.evaluateJavascript(setTokenJS, null);
                    loadUrl(loginUrl);
                } else {
                    smartrefreshlayout.autoRefresh();
                }
                isLogout = true;
                mCommonDialog.dismiss();
            }
        }, 1);
        mCommonDialog.setDialogOkText("登出");
        mCommonDialog.setDialogCancelText("取消");
        mCommonDialog.setDialogText("是否确认退出登录？");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mPopWin.dismiss();
        switch (moreDatas.get(position)) {
            case LOGOUTSTR:
                if (mCommonDialog != null) {
                    mCommonDialog.showDialog();
                }
                break;
            case CLEAR:
                if (mWebView != null) {
                    WebViewUtils.clearTbsX5Cookie(this);
                    ToastUtil.getInstance().makeText("清除成功");
                    AppSharedPreferencesHelper.setToken(null);
                    AppSharedPreferencesHelper.setResult(null);
                    mWebView.reload();
                }
                break;
            case REVISEPSD:
                loadUrl(revise_psd);
                break;
            case UPLOAD:
                Beta.checkUpgrade();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
