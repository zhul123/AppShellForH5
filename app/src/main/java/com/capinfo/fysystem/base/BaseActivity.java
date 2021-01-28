package com.capinfo.fysystem.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.alibaba.android.arouter.launcher.ARouter;
import com.capinfo.fysystem.BuildConfig;
import com.capinfo.fysystem.event.NetWorkChangedEvent;
import com.capinfo.fysystem.utils.StatusBarUtil;
import com.capinfo.fysystem.utils.ToastUtil;
import com.capinfo.fysystem.utils.UiUtils;
import com.capinfo.fysystem.utils.Utils;
import com.capinfo.fysystem.views.SystemTitle;
import com.capinfo.fysystem.views.WebSystemTitle;
import com.capinfo.fysystem.R;

import androidx.fragment.app.FragmentActivity;
import de.greenrobot.event.EventBus;

abstract public class BaseActivity extends FragmentActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    protected Context mContext = null;
    private Dialog dialog;
    private BaseApplication baseApplication;

    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }

    public <T extends View> T findById(int resId) {
//        return ButterKnife.findById(this, resId);
        return findViewById(resId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Logger.e("onCreate", this.getClass().getSimpleName());
        mContext = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getlayoutId() != 0) {
            setContentView(getlayoutId());
        }

        ARouter.getInstance().inject(this);
        EventBus.getDefault().register(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setStatusBarColor(this, R.color.white);
            StatusBarUtil.StatusBarLightMode(this);
        }
        baseApplication = (BaseApplication) getApplication();

    }

    protected abstract int getlayoutId();

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public Resources getResources() {
        // 不根据系统字体的大小来改变
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public interface OnMessageDialogBtnClickListener {
        public void onClick(int index, String name);
    }

    protected void startActivityBase(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setClass(this, cls);
        startActivity(intent);
    }

    protected void startActivityBase(Intent intent) {
        startActivity(intent);
    }

    protected void startActivityForResultBase(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        if (bundle != null)
            intent.putExtras(bundle);
        intent.setClass(this, cls);
        startActivityForResult(intent, requestCode);
    }

    protected void backForResultBase(int resultCode, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null)
            intent.putExtras(bundle);
        setResult(resultCode, intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void checkCommonError(int errorCode, String msg) {
        ToastUtil.getInstance().makeText(msg);
    }

    protected View loadingView;
    protected ImageView loadingAnimView;
    protected TextView loadingText;
    private Animation mLoadingAnim;

    public void showLoadingView() {
        showLoadingView("");
    }

    protected void showLoadingView(String msg) {
        if (loadingView == null) {
            loadingView = findViewById(R.id.loading_rl);
            loadingAnimView = (ImageView) findViewById(R.id.loading_ani_iv);
            loadingText = (TextView) findViewById(R.id.loading_tv);
        }
        loadingView.setVisibility(View.VISIBLE);
        //当msg不为空时， 需要显示背景
        if (!TextUtils.isEmpty(msg)) {
            findViewById(R.id.toast_ll).setBackgroundResource(R.drawable.custom_toast_bg);
            loadingText.setVisibility(View.VISIBLE);
            loadingText.setText(msg);
        }
        startLoadingAnim();
    }

    protected void showLoadingView(View v) {
        showLoadingView(v, "");
    }

    protected void showLoadingView(View v, String msg) {
        if (loadingView == null) {
            loadingView = v.findViewById(R.id.loading_rl);
            loadingAnimView = (ImageView) v.findViewById(R.id.loading_ani_iv);
            loadingText = (TextView) v.findViewById(R.id.loading_tv);
        }
        loadingView.setVisibility(View.VISIBLE);
        //当msg不为空时， 需要显示背景
        if (!TextUtils.isEmpty(msg)) {
            v.findViewById(R.id.toast_ll).setBackgroundResource(R.drawable.custom_toast_bg);
            loadingText.setVisibility(View.VISIBLE);
            loadingText.setText(msg);
        }
        startLoadingAnim();
    }

    public void dismissLoadingView() {
        if (loadingView != null) {
            stopLoadingAnim();
            loadingView.setVisibility(View.GONE);
        }
    }

    private void stopLoadingAnim() {
        if (null != mLoadingAnim/* && loadingPlayAni.isRunning()*/) {
            mLoadingAnim.cancel();
        }
    }

    private void startLoadingAnim() {
        if (mLoadingAnim == null) {
            mLoadingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_loading);
        }

        loadingAnimView.setAnimation(mLoadingAnim);
        loadingAnimView.startAnimation(mLoadingAnim);

    }

    protected void decodeSystemTitle(int titleId, OnClickListener listener) {
        String title = titleId > 0 ? getString(titleId) : "";
        decodeSystemTitle(title, listener);
    }

    protected void decodeSystemTitle(String title, OnClickListener listener) {
        View systemTitle = findViewById(R.id.system_title);
        if (systemTitle != null) {
            if (systemTitle instanceof SystemTitle) {
                UiUtils.decodeSystemTitle((SystemTitle) systemTitle, getIntent(),
                        getString(R.string.title_back), title, listener);
            } else if (systemTitle instanceof WebSystemTitle) {
                ((WebSystemTitle) systemTitle).setTitle(title);
            }
        }

    }

    private CharSequence getTitleMessage() {
        View systemTitle = findViewById(R.id.system_title);
        if (systemTitle != null) {
            if (systemTitle instanceof SystemTitle) {
                return ((SystemTitle) systemTitle).getTitleMessage();
            } else if (systemTitle instanceof WebSystemTitle) {
                return ((WebSystemTitle) systemTitle).getTitle();
            }
        }
        return super.getTitle();

    }

    public static Intent encodeSystemTitle(Activity activity, Intent intent) {
        if (activity instanceof BaseActivity) {
            return ((BaseActivity) activity).encodeSystemTitle(intent);
        }
        return intent;
    }

    protected Intent encodeSystemTitle(Intent intent) {
        return encodeSystemTitle(intent, 0, 0);
    }

    protected Intent encodeSystemTitle(Intent intent, int titleId) {
        return encodeSystemTitle(intent, 0, titleId);
    }

    protected Intent encodeSystemTitle(Intent intent, int backId, int titleId) {
        CharSequence backLabel = backId > 0 ? getString(backId) : getTitleMessage();
        String titleLabel = titleId > 0 ? getString(titleId) : "";
        return UiUtils.encodeSystemTitle(intent, (backLabel == null ? "" : backLabel.toString()), titleLabel);
    }

    protected void overrideTitleActionBtn(int labelId, OnClickListener listener) {
        overrideTitleActionBtn(getString(labelId), listener);
    }

    protected void overrideTitleActionBtn(String rightlabel, OnClickListener listener) {
        View systemTitle = findViewById(R.id.system_title);
        if (systemTitle != null) {
            if (systemTitle instanceof SystemTitle) {
                ((SystemTitle) systemTitle).setRightBtn(rightlabel, listener);
            } else if (systemTitle instanceof WebSystemTitle) {
                ((WebSystemTitle) systemTitle).setRightTextBtn(rightlabel, listener);
            }
        }
    }

    protected void overrideTitleActionBtn(String rightlabel, int textColor, OnClickListener listener) {
        View systemTitle = findViewById(R.id.system_title);
        if (systemTitle != null) {
            if (systemTitle instanceof SystemTitle) {
                ((SystemTitle) systemTitle).setRightBtn(rightlabel, listener);
                ((SystemTitle) systemTitle).setRightTextColor(textColor);
            } else if (systemTitle instanceof WebSystemTitle) {
                ((WebSystemTitle) systemTitle).setRightTextBtn(rightlabel, listener);
                ((WebSystemTitle) systemTitle).setRightTextColor(textColor);
            }
        }

    }

    protected void overrideTitleActionBtn(String rightlabel, boolean enable, OnClickListener listener) {
        SystemTitle systemTitle = (SystemTitle) findViewById(R.id.system_title);
        if (null != systemTitle) {
            systemTitle.setRightBtn(rightlabel, enable, listener);
        }
    }

    protected void overrideRightImageBtn(int labelId, OnClickListener listener) {
        SystemTitle systemTitle = (SystemTitle) findViewById(R.id.system_title);
        if (null != systemTitle) {
            systemTitle.setRightImage(labelId, listener);
        }
    }

    protected void overrideRightActionImageBtn(int imageId, OnClickListener listener) {
        View systemTitle = findViewById(R.id.system_title);
        if (null != systemTitle) {
            if (systemTitle instanceof SystemTitle) {
                ((SystemTitle) systemTitle).setRightImage(imageId, listener);
            } else if (systemTitle instanceof WebSystemTitle) {
                ((WebSystemTitle) systemTitle).setRightImage(imageId, listener);
            }
        }


    }

    protected void overrideLeftBtnDrawable(int drawableResId) {
        SystemTitle systemTitle = (SystemTitle) findViewById(R.id.system_title);
        if (null != systemTitle) {
            systemTitle.setLeftBtnDrawable(drawableResId);
        }
    }

    protected void setShowTitlePrompt(boolean showPrompt) {
        SystemTitle systemTitle = (SystemTitle) findViewById(R.id.system_title);
        if (null != systemTitle) {
            systemTitle.setShowPrompt(showPrompt);
        }
    }

    protected void setHighlightTitle(String title) {
        SystemTitle systemTitle = (SystemTitle) findViewById(R.id.system_title);
        if (null != systemTitle) {
            systemTitle.setTitle(title);
            systemTitle.setTitleBg(R.color.primary);
            systemTitle.setTitleTextColor(getResources().getColor(R.color.white));
        }
    }

    protected void setHighlightTitle(String title, int bottomSpanLineColor) {
        SystemTitle systemTitle = (SystemTitle) findViewById(R.id.system_title);
        if (null != systemTitle) {
            systemTitle.setTitle(title);
            systemTitle.setTitleBg(R.color.primary);
            systemTitle.setTitleTextColor(getResources().getColor(R.color.white));
            systemTitle.setBottomSpanLineColor(bottomSpanLineColor);
        }
    }

    protected void backToActivityBase(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setClass(this, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    /**
     * 隐藏软键盘
     */
    protected void hideSoftKeyBoard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
//            Logger.e(TAG, "hideSoftKeyBoard exception: " + e.getMessage());
        }
    }

    /**
     * 显示软键盘
     */
    protected void showSoftKeyBoard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
//            Logger.e(TAG, "showSoftKeyBoard exception: " + e.getMessage());
        }
    }

    /**
     * 判断当前activity是否还活着
     *
     * @return
     */
    public boolean isLive() {
        if (isFinishing()) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (isDestroyed()) {
                return false;
            }
        }
        return true;
    }

    protected boolean isNetworkAvailable() {
        if (!isFinishing()) {
            return Utils.isNetworkAvailable(this);
        }
        return false;
    }

    /**
     * eventbus统一处理
     */
    public void onEventMainThread(Object event) {
        if (event instanceof NetWorkChangedEvent) {
            NetWorkChangedEvent dEvent = (NetWorkChangedEvent) event;
            boolean isNetWorkConnected = dEvent.mNetWorkConnected;
            View systemTitleView = findViewById(R.id.system_title);
            if (null != systemTitleView && systemTitleView instanceof WebSystemTitle) {
                WebSystemTitle webSystemTitle = (WebSystemTitle) systemTitleView;
                webSystemTitle.showNetWorkDisconnected(isNetWorkConnected);
            }
        }
    }

    public void print(String str){
        if(BuildConfig.DEBUG){
            System.out.println("=========print:"+str);
        }
    }

}
