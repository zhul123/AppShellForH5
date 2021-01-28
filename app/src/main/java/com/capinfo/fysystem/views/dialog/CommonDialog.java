package com.capinfo.fysystem.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.capinfo.fysystem.R;
import com.capinfo.fysystem.utils.DoubleUtils;


/**
 * 两个按钮的公用弹出框
 */

public class CommonDialog {

    private Dialog mDialog;
    private Context mContext;

    private View.OnClickListener okClickListener;
    private View.OnClickListener cancelClickListener;
    private String dialogText, dialogOkText, dialogCancelText;
    private TextView tvDialogTitle;
    private TextView tvDialogContent;
    private int type;

    public CommonDialog(Context context) {
        this.mContext = context;
    }

    public CommonDialog(Context context, View.OnClickListener listener,int type) {
        this.mContext = context;
        this.okClickListener = listener;
        this.type = type;
    }

    public void setDialogText(String dialogText) {
        this.dialogText = dialogText;
    }

    public void setDialogOkText(String dialogOkText) {
        this.dialogOkText = dialogOkText;
    }

    public void setDialogCancelText(String dialogCancelText) {
        this.dialogCancelText = dialogCancelText;
    }

    public void showDialog() {
        if (mDialog == null) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_common, null, false);
            boolean cancelable = false;

            mDialog = MyDialogFactory.getDialogFactory().showCommonViewDialog(mContext, cancelable, view);

            tvDialogContent = view.findViewById(R.id.tv_dialog_content);
            TextView tvDialogCancel = view.findViewById(R.id.tv_dialog_cancel);
            TextView tvDialogOk = view.findViewById(R.id.tv_dialog_ok);
            tvDialogTitle = view.findViewById(R.id.tv_dialog_title);
            if (1 == type) {
                tvDialogContent.setVisibility(View.VISIBLE);
                tvDialogOk.setVisibility(View.VISIBLE);
                tvDialogCancel.setVisibility(View.VISIBLE);

                tvDialogOk.setBackgroundResource(R.drawable.solid_theme_r18_bg);
                tvDialogCancel.setBackgroundResource(R.drawable.hollow_black_r18_bg);

                tvDialogContent.setText(dialogText);
                tvDialogOk.setText(dialogOkText);
                tvDialogCancel.setText(dialogCancelText);

                tvDialogOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (okClickListener != null) {
                            if (!DoubleUtils.isFastDoubleClick()) {
                                okClickListener.onClick(v);
                            }
                        }

                    }
                });

                tvDialogCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });

            }
        } else {
            mDialog.show();
        }
    }

    public void setDialogTitle(String dialogTitle) {
        if (!TextUtils.isEmpty(dialogTitle) && tvDialogTitle != null) {
            tvDialogTitle.setVisibility(View.VISIBLE);
            tvDialogTitle.setText(dialogTitle);
        }
    }

    public void setContentGravity(int gravity) {
        if (tvDialogContent != null) {
            tvDialogContent.setGravity(gravity);
        }
    }

    public void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

}
