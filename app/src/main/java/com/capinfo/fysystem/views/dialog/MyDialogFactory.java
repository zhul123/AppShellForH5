package com.capinfo.fysystem.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.capinfo.fysystem.R;

/**
 * @brief JJDatingDialogFactory.class
 */
public class MyDialogFactory {

    private static MyDialogFactory factory = null;
    private TextView title;
    private TextView content;
    private Button btn_ok;
    private Button btn_exit;
    private Button btn_cancel;
    public Dialog dialog;
    public EditText hintContent;

    /**
     * <p/>
     * get the instance of the XingeDialogFactory;
     *
     * @return the singleton of the XingeDialogFactory
     */
    public static synchronized MyDialogFactory getDialogFactory() {

        if (null == factory) {
            factory = new MyDialogFactory();
            return factory;
        } else {
            return factory;
        }

    }

    /**
     * @param context
     * @param view
     * @return
     * @brief 包含view的dialog
     */
    public Dialog showCommonViewDialog(Context context, View view) {
        return showCommonViewDialog(context, view, true, R.style.commonDialog);
    }

    public Dialog showCommonViewDialog(Context context, View view, int theme) {
        return showCommonViewDialog(context, view, true, theme);
    }

    /**
     * @param context
     * @param view
     * @param cancelable
     * @return
     */

    public Dialog showCommonViewDialog(Context context, View view, boolean cancelable) {
        return showCommonViewDialog(context, view, cancelable, R.style.commonDialog);
    }

    /**
     * @param context
     * @param view
     * @return
     * @brief 包含view的dialog
     */
    public Dialog showCommonViewDialog(Context context, boolean cancelable, View view) {
        return showCommonViewDialog(context, view, cancelable, R.style.commonDialog);
    }

    /**
     * @param context
     * @param view
     * @return
     * @brief 包含view的dialog
     */
    public Dialog showCommonViewDialog(Context context, View view, boolean cancelable, int theme) {
        final Dialog customDialog = new Dialog(context, theme);
        WindowManager.LayoutParams localLayoutParams = customDialog.getWindow()
                .getAttributes();
        customDialog.onWindowAttributesChanged(localLayoutParams);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.setCancelable(cancelable);
        customDialog.setContentView(view);
        customDialog.show();
        return customDialog;
    }

    public Dialog showFullDialog(Context context, View view, boolean cancelable, int theme, boolean needSetLocation, int gravity) {
        Dialog dialog = new Dialog(context, theme);
        //自定义布局
        dialog.setContentView(view);

        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //设置宽
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置高
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        if (needSetLocation) {
            dialogWindow.setGravity(gravity);
        }

        dialog.setCanceledOnTouchOutside(cancelable);
        dialog.show();

        return dialog;
    }
}
