package com.capinfo.fysystem.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.capinfo.fysystem.base.BaseApplication;

import java.io.File;

/**
 * 日志打印工具类，封装到一起，是为了调用时方便
 *
 * @author Administrator
 */
public class Logger {
    private static final String TAG = "Capinfo";
    public static final String PAH_ROOT_FOLDER = "capinfo";
    private static final String HACK_FILE_NAME = ".hack";

    public static void v(String message) {
        if (checkHackFlag())
            Log.v(TAG, message);
    }

    public static void v(String tag, String message) {
        if (checkHackFlag())
            Log.v(tag, message);
    }

    public static void d(String message) {
        if (checkHackFlag())
            Log.d(TAG, message);
    }

    public static void i(String message) {
        if (checkHackFlag())
            Log.i(TAG, message);
    }

    public static void i(String tag, String message) {
        if (checkHackFlag())
            Log.i(tag, message);
    }

    public static void w(String message) {
        if (checkHackFlag())
            Log.w(TAG, message);
    }

    public static void w(String tag, String message) {
        if (checkHackFlag())
            Log.w(tag, message);
    }

    public static void e(String message) {
        if (checkHackFlag())
            Log.e(TAG, message);
    }

    public static void e(String tag, String message) {
        if (checkHackFlag())
            Log.e(tag, message);
    }

    public static void d(String tag, String message) {
        if (!TextUtils.isEmpty(message) && checkHackFlag()) {
            Log.d(TextUtils.isEmpty(tag) ? TAG : tag, message);
        }
    }

    public static boolean checkHackFlag() {
        if (BaseApplication.getInstance().isDebugBuild()) {
            return true;
        } else if (checkDebugHackFlag()) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean checkDebugHackFlag() {
        if (checkDebugFlag(HACK_FILE_NAME)) {
            return true;
        }

        return false;
    }

    private static boolean checkDebugFlag(String flag) {
        File file = new File(getPahPath() + File.separator + flag);
        return file.exists();
    }

    public static String getPahPath() {
        String str = getSdcardPath() + File.separator + PAH_ROOT_FOLDER;
        File file = new File(str);
        if (!file.exists()) {
            file.mkdir();
        }

        return str;
    }
    public static String getSdcardPath() {
        String sdcardPath = Environment.getExternalStorageDirectory().getPath();
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = new File("sdcard-ext");
            if (file.exists() && file.canWrite()) {
                sdcardPath = file.getPath();
            }
        }
        return sdcardPath;
    }
}
