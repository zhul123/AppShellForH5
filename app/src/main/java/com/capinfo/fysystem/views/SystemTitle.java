package com.capinfo.fysystem.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.capinfo.fysystem.R;


/**
 * @brief 标题栏
 * <p/>
 * 标题栏，包含标题，左按钮和右按钮，要显示按钮时首先要设置按钮的按钮的visibility为visiable，也可以设置按钮点击事件、
 * 按钮的背景图片、按钮是否可以点击
 */
public class SystemTitle extends LinearLayout {

    private TextView right = null;
    private TextView rightBadge = null;
    private TextView title = null;
    private TextView left = null;
    private ImageView rightImage = null;
    private TextView secTitle = null;
    private View title_ll = null;

    private OnClickListener tClick = null;

    private View mTitleLl;
    private View mNetworkView;
    private Context mContext;
    private boolean mShowPrompt = false;
    private View mBottomSpanLine;

    public SystemTitle(Context context) {
        super(context);
        init(context);
    }

    public SystemTitle(Context context, AttributeSet attrs) {
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
        View v = mInflater.inflate(R.layout.system_title, null);
        mTitleLl = v.findViewById(R.id.title_parent_ll);
        left = (TextView) v.findViewById(R.id.system_title_left);
        right = (TextView) v.findViewById(R.id.system_title_right);
        rightBadge = (TextView) v.findViewById(R.id.system_title_right_badge);
        title = (TextView) v.findViewById(R.id.tv_title);
        rightImage = (ImageView) v.findViewById(R.id.system_title_right_image);
        secTitle = (TextView) v.findViewById(R.id.tv_sec_title);
        title_ll = v.findViewById(R.id.title_ll);
        mNetworkView = v.findViewById(R.id.no_network_prompt);
        mBottomSpanLine = v.findViewById(R.id.bottom_span_line);
        addView(v);

    }

    public void setShowPrompt(boolean showPrompt) {
        mShowPrompt = showPrompt;
        if (showPrompt) {
            ConnectivityManager connManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();
            if (null != activeNetwork) {
                showNetWorkDisconnected(true);
            } else {
                showNetWorkDisconnected(false);
            }
            mNetworkView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public void showNetWorkDisconnected(boolean isConnected) {
        if (mShowPrompt && (null != mNetworkView)) {
            if (isConnected) {
                mNetworkView.setVisibility(View.GONE);
            } else {
                mNetworkView.setVisibility(View.VISIBLE);
            }
        }
    }

    //设置title字体颜色
    public void setTitleTextColor(int color) {
        if (title != null) {
            title.setTextColor(color);
        }
    }

    //设置title背景
    public void setTitleBg(int res) {
        if (mTitleLl != null) {
            mTitleLl.setBackgroundResource(res);
        }
    }

    /**
     * 获取title信息
     *
     * @return
     */
    public CharSequence getTitleMessage() {
        if (title != null)
            return title.getText();
        else
            return "";

    }

    /**
     * 设置title
     *
     * @param
     */
    public void setTitle(String message) {
        if (title != null) {
            title.setText(message);

        }
    }

    /**
     * @param click
     * @brief 设置title的点击事件
     */
    public void setTitleClick(OnClickListener click) {
        tClick = click;
        if (title != null) {
            title.setOnClickListener(tClick);
        }
    }

    /*
     * private OnClickListener titleClick = new OnClickListener() {
     *
     * @Override public void onClick(View v) { if(tClick != null) {
     * tClick.onClick(v); } } };
     */

    /**
     * @param text
     * @param click
     * @brief 设置左边按钮文本和和点击事件
     */
    public void setLeftBtn(String text, OnClickListener click) {
        if (left != null) {
            left.setVisibility(View.VISIBLE);
            left.setText(text);
            left.setText("");
            left.setOnClickListener(click);
        }
    }

    /**
     * @param text
     * @param click
     * @brief 设置左边按钮文本和和点击事件
     */
    public void setLeftBtn1(String text, OnClickListener click) {
        if (left != null) {
            left.setVisibility(View.VISIBLE);
            left.setText(text);
            left.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//            left.setText("");
            left.setOnClickListener(click);
        }
    }

    /**
     * @param drawableResId
     * @param click
     * @brief 设置左边按钮背景图片和和点击事件
     */
    public void setLeftBtn(int drawableResId, OnClickListener click) {
        if (left != null) {
            left.setVisibility(View.VISIBLE);
            left.setText("");
            left.setCompoundDrawablesWithIntrinsicBounds(drawableResId, 0, 0, 0);
            left.setOnClickListener(click);
        }
    }

    /**
     * 左侧标题图片
     * @param drawableResId
     */
    public void setLeftBtnDrawable(int drawableResId){
        if (left != null) {
            left.setVisibility(View.VISIBLE);
            left.setCompoundDrawablesWithIntrinsicBounds(drawableResId, 0, 0, 0);
        }
    }

    /**
     * 设置左侧返回箭头距离屏幕的距离
     * @param paddingLeft
     */
    public void setLeftBtnLeftPadding(int paddingLeft){
        if (left != null) {
            left.setPadding(paddingLeft, left.getPaddingTop(), left.getPaddingRight(), left.getPaddingBottom());
        }
    }


    /**
     * @param text
     * @param click
     * @brief 设置右边按钮文本和和点击事件
     */
    public void setRightBtn(String text, OnClickListener click) {
        setRightBtn(text, true, click);
    }

    public void setRightBtn(String text, boolean enable, OnClickListener click) {
        if (right != null) {
            right.setVisibility(View.VISIBLE);
            right.setText(text);
            right.setEnabled(enable);
            if (click != null) {
                right.setOnClickListener(click);
            }
        }
    }

    public void setRightBtnEnable(boolean flag) {
        if (right != null) {
            right.setEnabled(flag);
        }
    }


    /**
     * @param colorRes
     * @brief 设置左边文本颜色
     */
    public void setLeftTextColor(int colorRes) {
        if (left != null) {
            left.setTextColor(colorRes);
        }
    }

    /**
     * @param colorRes
     * @brief 设置右边文本颜色
     */
    public void setRightTextColor(int colorRes) {
        if (right != null) {
            right.setTextColor(colorRes);
        }
    }

    public void setRightTextSize(int size) {
        if (right != null) {
            right.setTextSize(size);
        }
    }

    public TextView getRightTextView() {
        return right;
    }

    public void setRightBadge(String text) {
        if (rightBadge != null) {
            rightBadge.setVisibility(View.VISIBLE);
            rightBadge.setText(text);
        }
    }

    public void hideRightBadge() {
        if (rightBadge != null) {
            rightBadge.setVisibility(View.GONE);
        }
    }

    /**
     * @param res
     * @param click
     * @brief 设置右边图片和点击事件
     */
    public void setRightImage(int res, OnClickListener click) {
        if (rightImage != null) {
            rightImage.setVisibility(View.VISIBLE);
            rightImage.setBackgroundResource(res);
            rightImage.setOnClickListener(click);
        }
    }

    public View getRightImage() {
        return rightImage;
    }

    public void setTitleColor(int color) {
        Log.i("Huskar", "setTitleColor");


    }

    /**
     * @param text
     * @param click
     * @brief 设置二级title文本和点击事件
     */
    public void setSecTitle(String text, OnClickListener click) {
        if (secTitle != null) {
            secTitle.setVisibility(View.VISIBLE);
            secTitle.setText(text);
            secTitle.setOnClickListener(click);
        }
    }

    /**
     * @param text
     * @brief 设置二级title的文本
     */
    public void setSecTitleText(String text) {
        if (secTitle != null) {
            secTitle.setVisibility(View.VISIBLE);
            secTitle.setText(text);
        }
    }

    public void setSecondTitleTxtColor(int color) {
        if (secTitle != null) {
            secTitle.setTextColor(color);
        }
    }

    public void setSecondTitleTxtSize(int size) {
        if (secTitle != null) {
            secTitle.setTextSize(size);
        }
    }

    /**
     * @param click
     * @brief 设置title整个点击事件
     */
    public void setAllTitleClick(OnClickListener click) {
        if (title_ll != null) {
            title_ll.setOnClickListener(click);
        }
    }

    /**
     * @brief 隐藏二级title
     */
    public void hideSecTitle() {
        if (secTitle != null) {
            secTitle.setVisibility(View.GONE);
        }
    }

    /**
     * @param res
     * @brief 设置二级title背景
     */
    public void setSecTitleBg(int res) {
        if (secTitle != null) {
            secTitle.setVisibility(View.VISIBLE);
            secTitle.setBackgroundResource(res);
        }
    }

    /**
     * @param right
     * @brief 设置二级title右边图片
     */
    public void setSecTitleRightDrawable(Drawable right) {
        if (secTitle != null) {
            secTitle.setVisibility(View.VISIBLE);
            secTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, right, null);
        }
    }

    /**
     * @param isVisible
     * @brief 隐藏右边的按钮
     */
    public void setVisiableToRightButton(boolean isVisible) {
        if (right != null) {
            right.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * @param isVisible
     * @brief 隐藏右边的按钮
     */
    public void setVisiableToLeftButton(boolean isVisible) {
        if (left != null) {
            left.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * @param right
     * @brief 设置title右边的图标
     */
    public void setTitleRightDrawable(Drawable right) {
        if (title != null) {
            title.setVisibility(View.VISIBLE);
            title.setCompoundDrawablesWithIntrinsicBounds(null, null, right, null);
        }
    }

    /**
     * @brief 隐藏title右边的图标
     */
    public void hideTitleRightDrawable() {
        if (title != null) {
            title.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    public void setBottomSpanLineColor(int resId) {
        if (mBottomSpanLine == null) {
        } else {
            mBottomSpanLine.setBackgroundResource(resId);
        }
    }

    public void setBottomSpanLineVisible(int visible) {
        if (mBottomSpanLine != null) {
            mBottomSpanLine.setVisibility(visible);
        }
    }

}
