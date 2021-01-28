package com.capinfo.fysystem.views.popwindows;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.capinfo.fysystem.utils.ScreenUtils;

import androidx.annotation.LayoutRes;
import androidx.core.widget.PopupWindowCompat;


/**
 * @Description: 通用气泡菜单
 */
public class PopWindowMenu extends PopupWindow {
    private Context mContext;
private  View popView;
    public PopWindowMenu(Context context, @LayoutRes int layoutId) {
        this.mContext = context;
        popView = LayoutInflater.from(mContext).inflate(layoutId, null);
        setPopView(popView);
    }

    public void setOnLayoutCompletedLsn(OnLayoutCompletedListener listener){
        if (listener != null){
            listener.onBindView(popView);
        }
    }

    public interface OnLayoutCompletedListener{
        void onBindView(View v);
    }

    private void setPopView(View mView) {
        // 设置外部可点击
        this.setOutsideTouchable(false);

        this.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置视图
        this.setContentView(mView);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable();

        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    public void showPop(final View view){
        int offsetX = 0;
        int offsetY = 0;
        PopupWindowCompat.showAsDropDown(PopWindowMenu.this, view, offsetX, offsetY, Gravity.BOTTOM);
    }
}
