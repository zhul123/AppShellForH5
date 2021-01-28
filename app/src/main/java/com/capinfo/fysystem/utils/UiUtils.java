package com.capinfo.fysystem.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.capinfo.fysystem.R;
import com.capinfo.fysystem.views.SystemTitle;

/**
 * Created by hetao
 */
public class UiUtils {
    private static final String TAG = UiUtils.class.getSimpleName();

    private static final String BACK_LABEL = "BACK_LABEL";
    private static final String TITLE_LABEL = "TITLE_LABEL";

    public static Intent encodeSystemTitle(Intent intent, String backLabel, String titleLabel) {
        // always use "BACK" instead of customized text
//        if (!TextUtils.isEmpty(backLabel)) {
//            intent.putExtra(BACK_LABEL, backLabel);
//        }

        if (!TextUtils.isEmpty(TITLE_LABEL)) {
            intent.putExtra(TITLE_LABEL, titleLabel);
        }

        return intent;
    }

    public static void decodeSystemTitle(SystemTitle systemTitle, Intent intent, View.OnClickListener listener) {
        decodeSystemTitle(systemTitle, intent, null, null, listener);
    }

    public static void decodeSystemTitle(SystemTitle systemTitle, Intent intent, String backHolder,
                                         String titleHolder, View.OnClickListener listener) {
        if (null == systemTitle) {
            return;
        }

        String backLabel = null;
        String titleLabel = null;
        if (null != intent) {
            backLabel = intent.getStringExtra(BACK_LABEL);
            titleLabel = intent.getStringExtra(TITLE_LABEL);
        }
        if (TextUtils.isEmpty(backLabel)) {
            backLabel = backHolder;
        }
        if (TextUtils.isEmpty(titleLabel)) {
            titleLabel = titleHolder;
        }

        if (null != backLabel) {
            systemTitle.setLeftBtn(backLabel, listener);
        }
        if (null != titleLabel) {
            systemTitle.setTitle(titleLabel);
        }
    }


    private static final String DESCRIPTION_LABEL = "DESCRIPTION_LABEL";

    public static Intent setDescription(Intent intent, String description) {
        intent.putExtra(DESCRIPTION_LABEL, description);
        return intent;
    }

    public static String getDescription(Intent intent) {
        return null == intent ? null : intent.getStringExtra(DESCRIPTION_LABEL);
    }

    public static final String REQUEST_TYPE_QR_CODE = "request_type_qr_code";
    public static final String REQUEST_TYPE_BAR_CODE = "request_type_bar_code";
    private static final String REQUEST_TYPE = "request_type";

    public static Intent setRequestType(Intent intent, String requestType) {
        intent.putExtra(REQUEST_TYPE, requestType);
        return intent;
    }

    public static String getRequestType(Intent intent) {
        return null == intent ? null : intent.getStringExtra(REQUEST_TYPE);
    }

    private static final String GUID = "GUID";

    public static Intent setKeyword(Intent intent, String guid) {
        if (!TextUtils.isEmpty(guid)) {
            intent.putExtra(GUID, guid);
        }
        return intent;
    }

    public static String getKeyword(Intent intent) {
        return intent.getStringExtra(GUID);
    }

    private static String MISSING_KEYWORD = "MISSING_KEYWORD";

    public static Intent setMissingKeywordError(Intent intent, String error) {
        intent.putExtra(MISSING_KEYWORD, error);
        return intent;
    }

    public static String getMissingKeywordError(Intent intent) {
        return intent.getStringExtra(MISSING_KEYWORD);
    }

    private static class QrScanResult {
        private String user_guid; // 用户id
        private String type; // 用户type

        public String getUser_guid() {
            return user_guid;
        }

        public void setUser_guid(String user_guid) {
            this.user_guid = user_guid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    /*public static boolean checkValidQrScanning(Intent intent, String resultString) {
        if (!TextUtils.isEmpty(resultString)) {
            QrScanResult qrScanResult = JSonUtils.parseObjectWithoutException(resultString, QrScanResult.class);
            if (null != qrScanResult) {
                if (!TextUtils.isEmpty(qrScanResult.getUser_guid())) {
                    return true;
                }
            } else {
                Log.e(TAG, "checkValidQrScanning, invalid scan code data = " + resultString);
            }
        }

        return false;
    }*/

    /**
     * @param context
     * @param intent
     * @param packageName 设置webView scheme 通过那个package打开intent
     */
    public static void preferPackageForIntent(Context context, Intent intent, String packageName) {
        PackageManager pm = context.getPackageManager();
        for (ResolveInfo resolveInfo : pm.queryIntentActivities(intent, 0)) {
            if (resolveInfo.activityInfo.packageName.equals(packageName)) {
                intent.setPackage(packageName);
                break;
            }
        }
    }

    public static void emitCalling(Context context, String number) {
        try {
            Intent callIntent = new Intent();
            callIntent.setAction(Intent.ACTION_VIEW);
            callIntent.setData(Uri.parse("tel:" + number));
            context.startActivity(callIntent);
        } catch (Exception ex) {
            ex.printStackTrace();
//            Logger.Loggere("callCustomerServiceTelephone, number " + number + ", exception = " + ex.getMessage());
        }
    }

    public static void setUnreadCount(TextView textView, int count) {
        if (null == textView) {
            return;
        }

        if (count > 0) {
            String label = count < 100 ? String.valueOf(count) : "...";
            textView.setText(label);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    public static String getAppName(Context context) {
        PackageManager pm = context.getPackageManager();
        String name = null;
        try {
            name = pm.getApplicationLabel(
                    pm.getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
        }
        return name;
    }

    public static void moveCursorEndForEditText(EditText edit) {
        if (edit != null) {
            String text = edit.getText().toString();
            edit.setSelection(text.length());
        } else {
//            Logger.e(TAG, "editext ifs null");
        }
    }

    public static int getDefaultAvatarForUser() {
        return R.drawable.pro_default_160;
    }


}
