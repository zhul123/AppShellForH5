package com.capinfo.fysystem.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.capinfo.fysystem.utils.ScreenUtils;
import com.tencent.smtt.sdk.WebView;
import com.capinfo.fysystem.R;

import androidx.annotation.DrawableRes;

public class WebSystemTitle extends LinearLayout {

    private TextView righTv;
    private TextView title;
    private ImageView leftBack;
    private ImageView leftFinish;
    private ImageView rightImage;
    private View mNetworkView;
    protected WebView mWebView;

    public WebSystemTitle(Context context) {
        super(context);
        init(context);
    }

    public WebSystemTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * @param context
     * @brief 初始化标题
     */
    private void init(Context context) {
        setLayoutParams(new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        LayoutInflater.from(context).inflate(R.layout.system_title_web, this, true);
        leftBack = findViewById(R.id.system_title_left);
        leftFinish = findViewById(R.id.system_title_left2);
        righTv = findViewById(R.id.system_title_right);
        title = findViewById(R.id.tv_title);
        rightImage = findViewById(R.id.system_title_right_image);
        mNetworkView = findViewById(R.id.no_network_prompt);

    }

    /**
     * 设置title
     *
     * @param
     */
    public void setTitle(final String message) {
        //动态控制TextView的宽度，目的是，让title一直居中显示.这里拿了屏宽
        if (title != null) {

            int marginLeft = 0;
            if (leftBack.getVisibility() == View.VISIBLE) {
                marginLeft += leftBack.getWidth();
            }
            if (leftFinish.getVisibility() == View.VISIBLE) {
                marginLeft += leftFinish.getWidth();
            }
            int marginRight = 0;
            if (righTv.getVisibility() == View.VISIBLE) {
                marginRight += righTv.getWidth();
            }
            if (rightImage.getVisibility() == View.VISIBLE) {
                marginRight += rightImage.getWidth();
            }

            int margin = Math.max(marginLeft, marginRight) * 2;
            int screenWidth = ScreenUtils.getWindowWidth(getContext());
            int textViewWidth = screenWidth - margin;
            if (textViewWidth != title.getWidth()) {
                //设置了宽需要刷新，不然不起效
                title.getLayoutParams().width = textViewWidth;
                title.requestLayout();
                //y不变
//                ValueAnimator valueAnimator = ValueAnimator.ofInt(title.getWidth(), textViewWidth).setDuration(600);
//                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        title.getLayoutParams().width = (int) animation.getAnimatedValue();
//                        title.requestLayout();
//                    }
//                });
//                valueAnimator.start();
            }
            if (!TextUtils.equals(message, getTitle())) {
                title.setText(message);
            }
        }
    }

    public CharSequence getTitle() {
        if (title != null)
            return title.getText();
        else
            return "";

    }

    public void setLeftBackBtnVisible(boolean visible){
        if(leftBack != null){
            leftBack.setVisibility(visible ? VISIBLE : INVISIBLE);
        }
    }

    /**
     * @param click
     * @brief 设置左边按钮文本和和点击事件
     */


    public void setLeftBackBtn(OnClickListener click) {
        if (leftBack != null) {
            leftBack.setVisibility(View.VISIBLE);
            leftBack.setImageResource(R.drawable.btn_navbar_back);
            leftBack.setOnClickListener(click);
        }
    }


    public void setWebView(WebView webView) {
        this.mWebView = webView;
    }

    public void setLeftFinishBtn(OnClickListener click) {
        if (leftFinish == null) return;
        if (mWebView != null) {
            if (!mWebView.canGoBack()) {
                if (leftFinish.getVisibility() != View.GONE) {
                    leftFinish.setVisibility(View.GONE);
                    leftFinish.post(new Runnable() {
                        @Override
                        public void run() {
                            setTitle(getTitle().toString());
                        }
                    });
                }
            } else {
                if (leftFinish.getVisibility() != View.VISIBLE) {
                    leftFinish.setVisibility(View.VISIBLE);
                    leftFinish.setOnClickListener(click);
                    leftFinish.setImageResource(R.drawable.web_page_close);
                    leftFinish.post(new Runnable() {
                        @Override
                        public void run() {
                            setTitle(getTitle().toString());
                        }
                    });

                }
            }
        }
    }

    //-----------------------------------------------
    public void setRightTextColor(int colorRes) {
        if (righTv != null) {
            righTv.setTextColor(colorRes);
        }
    }

    public void setRightTextSize(float size) {
        if (righTv != null) {
            righTv.setTextSize(size);
        }
    }
    public void setRightText(String text) {
        if (righTv != null) {
            righTv.setText(text);
        }
    }

    public String getRightText(){
        if (righTv != null) {
            return righTv.getText().toString();
        }
        return "";
    }

    public void setRightTextBtnVisible(boolean isVisible) {
        if (righTv != null) {
            righTv.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
        }
    }
    public void setRightTextBtn(String text, OnClickListener click) {
        setRightTextBtn(text, true, click);
    }

    public void setRightTextBtn(String text, boolean enable, OnClickListener click) {
        if (righTv != null) {
            if (rightImage != null) {
                rightImage.setVisibility(View.GONE);
            }
            righTv.setVisibility(View.VISIBLE);
            righTv.setText(text);
            righTv.setEnabled(enable);
            if (click != null) {
                righTv.setOnClickListener(click);
            }
            setTitle(getTitle().toString());
            righTv.requestLayout();
        }
    }

    //---------------------------------------------
    public View getRightImageBtn() {
        return rightImage;
    }

    public void setRightImage(@DrawableRes int drawableResId, OnClickListener click) {
        if (rightImage != null) {
            if (righTv != null) {
                righTv.setVisibility(View.GONE);
            }
            rightImage.setVisibility(View.VISIBLE);
            rightImage.setImageResource(drawableResId);
            rightImage.setOnClickListener(click);
            setTitle(getTitle().toString());
            rightImage.requestLayout();
        }
    }

    private boolean mShowPrompt = true;

    public void showNetWorkDisconnected(boolean isConnected) {
        if (mShowPrompt && (null != mNetworkView)) {
            if (isConnected) {
                mNetworkView.setVisibility(View.GONE);
            } else {
                mNetworkView.setVisibility(View.VISIBLE);
            }
        }
    }
}
