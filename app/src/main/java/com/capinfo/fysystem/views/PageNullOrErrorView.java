package com.capinfo.fysystem.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.capinfo.fysystem.event.PageStatusReloadEvent;
import com.capinfo.fysystem.R;

import de.greenrobot.event.EventBus;


/**
 * @brief 处理空页面 网络出错等
 */
public class PageNullOrErrorView extends FrameLayout {
    private Context mContext;
    protected static final int PAGE_STATUS_NO_INTERNET = 1001;
    protected static final int PAGE_STATUS_NULL_DATA = 1002;
    protected static final int PAGE_STATUS_RESPONSE_ERROR = 1003;
    protected static final int PAGE_STATUS_WHITE_LIST_INTERCEPT_ERROR = 1004;// 白名单拦截展示
    private TextView pageStatusTv;
    private ImageView pageStatusIv;
    private TextView pageExplainTv;
    private TextView btnReload;

    private final OnClickListener mReloadClickListener = new ReloadClickListener();

    private static class ReloadClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new PageStatusReloadEvent());
        }
    }

    public PageNullOrErrorView(Context context) {
        super(context);
        init(context);
    }

    public PageNullOrErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * @param context
     * @brief 初始化标题
     */
    private void init(Context context) {
        mContext = context;
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(lp);
        LayoutInflater mInflater = LayoutInflater.from(context);
        View v = mInflater.inflate(R.layout.layout_response_error_null, null);
        pageStatusIv = (ImageView) v.findViewById(R.id.status_image);
        pageStatusTv = (TextView) v.findViewById(R.id.status_tv);
        pageExplainTv = (TextView) v.findViewById(R.id.explain_tv);
        btnReload = (TextView) v.findViewById(R.id.reload_btn);
        btnReload.setOnClickListener(mReloadClickListener);
        addView(v);
    }

    /**
     * 显示返回失败，错误等页面
     */
    private void showResponseErrorView() {
        showResponseStatusView(PAGE_STATUS_RESPONSE_ERROR, mContext.getString(R.string.label_response_error));
        show();
    }

    /**
     * 显示返回数据为空页面
     *
     * @param label
     */
    private void showResponseNullDataView(String label) {
        if (TextUtils.isEmpty(label)) {
            showResponseStatusView(PAGE_STATUS_NULL_DATA, mContext.getString(R.string.label_null_data));
        } else {
            showResponseStatusView(PAGE_STATUS_NULL_DATA, label);
        }
        show();
    }

    private void show() {
        show(this);
    }

    private static void show(View view) {
        if (null != view) {
            view.setVisibility(VISIBLE);
        }
    }

    //    private void hide() {
    //        hide(this);
    //    }

    private static void hide(View view) {
        if (null != view) {
            view.setVisibility(GONE);
        }
    }


    /**
     * 显示无网络
     */
    private void showNoInternetView() {
        showResponseStatusView(PAGE_STATUS_NO_INTERNET, mContext.getString(R.string.label_no_internet));
        show();
    }

    /**
     * 显示无网络
     */
    private void showNoInternetViewWithWarn(String warn) {
        showResponseStatusView(PAGE_STATUS_NO_INTERNET, warn);
        show();
    }

    /**
     * 显示页面 无网络状态， 无数据状态，获取失败状态
     *
     * @param pageStatus
     */
    private void showResponseStatusView(int pageStatus, String statusStr) {
        if (PAGE_STATUS_NO_INTERNET == pageStatus) {
            pageStatusIv.setImageResource(R.drawable.icon_no_internet);
            btnReload.setVisibility(View.VISIBLE);
        } else if (PAGE_STATUS_NULL_DATA == pageStatus) {
            pageStatusIv.setImageResource(R.drawable.icon_null_data);
            btnReload.setVisibility(View.GONE);
        } else if (PAGE_STATUS_RESPONSE_ERROR == pageStatus) {
            pageStatusIv.setImageResource(R.drawable.icon_response_error);
            pageExplainTv.setVisibility(View.GONE);
            pageExplainTv.setText(R.string.label_explain);
            btnReload.setVisibility(View.VISIBLE);
        } else if (PAGE_STATUS_WHITE_LIST_INTERCEPT_ERROR == pageStatus) {
            pageStatusIv.setImageResource(R.drawable.icon_no_internet);
            btnReload.setVisibility(View.GONE);
        }
        pageStatusTv.setText(statusStr);
    }

    public static void showResponseNullDataView(PageNullOrErrorView nullOrErrorView, String prompt) {
        if (nullOrErrorView != null) {
            nullOrErrorView.showResponseNullDataView(prompt);
        }
    }

    public static void showResponseErrorView(PageNullOrErrorView nullOrErrorView) {
        if (nullOrErrorView != null) {
            nullOrErrorView.showResponseErrorView();
        }
    }

    public static void showNoInternetViewWithWarn(PageNullOrErrorView nullOrErrorView, String warn) {
        if (nullOrErrorView != null) {
            nullOrErrorView.showNoInternetViewWithWarn(warn);
        }
    }

    public static void showNoInternetView(PageNullOrErrorView nullOrErrorView) {
        if (nullOrErrorView != null) {
            nullOrErrorView.showNoInternetView();
        }
    }

    public static void hide(PageNullOrErrorView nullOrErrorView, View alterView) {
        hide(nullOrErrorView);
        show(alterView);
    }

    public void setReloadClickListener(OnClickListener onClickListener) {
        btnReload.setOnClickListener(onClickListener);
    }

    /**
     * 获取是否有重新加载按钮
     * @return
     */
    public boolean hasReloadBtn(){
        return btnReload.getVisibility() == VISIBLE ? true : false ;
    }


}
