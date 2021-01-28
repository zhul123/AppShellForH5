package com.capinfo.fysystem.utils.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.capinfo.fysystem.base.BaseApplication;

/**
 * @ClassName: AppSharedPreferencesHelper
 * @Description: (公用类 ， 用于缓存一些key — — value类型的数据)
 */

public class AppSharedPreferencesHelper {
    private static SharedPreferences mSharedPreferences;
    private static Editor mEditor;
    public static final String APP_PREFS = "capinfo_prefs";
    public static final String TOKEN = "token";
    /**
     * Get SharedPreferences
     */
    private static SharedPreferences getSharedPreferences(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }
    public static SharedPreferences getSharedPreferences() {

        if (mSharedPreferences == null) {
            BaseApplication baseApplication = BaseApplication.getInstance();
            if (null != baseApplication) {
                mSharedPreferences = baseApplication.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
            }
        }
        return mSharedPreferences;
    }

    public static Editor getEditor() {

        if (mEditor == null) {
            mEditor = getSharedPreferences().edit();
        }
        return mEditor;
    }

    public static void clear() {
        mEditor = getEditor();
        mEditor.clear();
        mEditor.commit();
    }

    public static String getToken() {
        return getSharedPreferences().getString(TOKEN, "");
    }
    public static void setToken(String token) {
        getEditor().putString(TOKEN,token).commit();
    }
}
