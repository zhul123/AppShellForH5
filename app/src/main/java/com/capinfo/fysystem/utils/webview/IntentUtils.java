package com.capinfo.fysystem.utils.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.capinfo.fysystem.RefacTX5WebViewActivity;
import com.capinfo.fysystem.base.BaseRefacTX5WebViewActivity;
import com.capinfo.fysystem.utils.ToolUtils;


public final class IntentUtils {

    /**
     * 跳转到Webview，跳转显示设置title，加载webview之后获取html标题替换title
     *
     * @param context
     * @param url
     * @param title
     */
    public static void gotoRefacWebViewActivity(Context context,
                                                String url,
                                                String title) {
        gotoRefacWebViewActivity(context, url, true, false, title, "", true);
    }

    /**
     * 跳转到WebView,所有参数
     *
     * @param context
     * @param url               webview url
     * @param showTitleBar      使用自定义标题true；否false
     * @param fixedTitle        无论webview内部如何跳转都使用传入参数title作为标题，否false
     * @param title             自定义标题内容
     * @param isCommonParameter 拼接公共参数和传入的extendParam参数true；否false
     */
    public static void gotoRefacWebViewActivity(Context context,
                                                String url,
                                                boolean showTitleBar,
                                                boolean fixedTitle,
                                                String title,
                                                String rightTitle,
                                                boolean isCommonParameter) {
        Intent intent = new Intent(context, RefacTX5WebViewActivity.class);
        intent.putExtra(BaseRefacTX5WebViewActivity.URL_JSON_KEY_TITLE, title);
        intent.putExtra(BaseRefacTX5WebViewActivity.URL_JSON_KEY_SHOW_TITLE, showTitleBar);
        intent.putExtra(BaseRefacTX5WebViewActivity.URL_JSON_KEY_FIX_TITLE, fixedTitle);
        intent.putExtra(BaseRefacTX5WebViewActivity.INTENT_NAME_WEBVIEW_COMMON_PARAMETER, isCommonParameter);
        intent.putExtra(BaseRefacTX5WebViewActivity.URL_JSON_KEY_TITLE_RIGHT, rightTitle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    //---------------WebView 相关end---------------

    /**
     * 一般pdf的显示
     * 按钮不显示
     */
    public static void gotoPdfActivity(String title, String url, int contentLenght, String buttonString, String docuno) {
        gotoPdfActivity(title, url, contentLenght, buttonString, docuno, 0, "");
    }

    /**
     * 一般pdf的显示
     * 按钮不显示
     */
    public static void gotoPdfActivity(String title, String url, int contentLenght, String buttonString, String docuno, int type, String orderNo) {
//        Bundle bundle = new Bundle();
//        bundle.putString(BaseConstantDef.PARAM_PDF_URL, url);
//        bundle.putString(BaseConstantDef.PARAM_TITLE, title);
//        bundle.putLong(BaseConstantDef.PARAM_CONTENT_LENGTH, contentLenght);
//        bundle.putString(BaseConstantDef.PARAM_BUTTON_STRING, buttonString);
//        bundle.putString(BaseConstantDef.PARAM_DOCUNO, docuno);
//        bundle.putInt(BaseConstantDef.PARAM_TYPE, type);
//        bundle.putString(BaseConstantDef.PARAM_ORDERNO, orderNo);
//        ARouter.getInstance().build(PATH_PDF).with(bundle).navigation();
    }

    /**
     * 判断链接的后缀，跳转到pdf或者WebView
     *
     * @param context
     * @param url
     * @param title
     */
    public static void gotoPdfOrWebViewActivity(Context context, String url, String title) {
//        if (url.endsWith(SUFFIX_PDF) || url.endsWith(SUFFIX_PDF.toUpperCase())) {
        if (ToolUtils.isUrlPdf(url)) {
            gotoPdfActivity(title, url, 0, "", "");
        } else {
            gotoRefacWebViewActivity(context, url, title);
        }
    }
}
