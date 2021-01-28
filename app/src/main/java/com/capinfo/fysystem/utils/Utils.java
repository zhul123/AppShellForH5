package com.capinfo.fysystem.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.capinfo.fysystem.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    private static StringBuffer mStrBuf = new StringBuffer();
    private static SimpleDateFormat mFormat = new SimpleDateFormat(
            "dd/MM/yyyy HH:mm:ss");

    /**
     * 对给定的字符串进行MD5加密
     *
     * @param str
     * @return
     */
    public static String MD5(String str) {
        if (!TextUtils.isEmpty(str)) {
            MessageDigest messageDigest = null;
            try {
                messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.reset();
                messageDigest.update(str.getBytes("UTF-8"));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            byte[] byteArray = messageDigest.digest();
            StringBuffer md5StrBuff = new StringBuffer();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(
                            Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
            return md5StrBuff.toString();
        } else {
            return null;
        }
    }

    /**
     * 打印日志
     *
     * @param msg
     */
    public static void log(String msg) {
        log(TAG, msg);
    }

    /**
     * 打印日志
     *
     * @param tag
     * @param msg
     */
    public static void log(String tag, String msg) {
        if (!TextUtils.isEmpty(msg)) {
//            Logger.d(TAG, msg);
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        return ConnectNetwork.checkNetwork(context);
    }

    private static class ConnectNetwork {
        /**
         * 检测是否有可用的网络
         *
         * @param context
         * @return
         */
        public static boolean checkNetwork(final Context context) {
            boolean flag = false;
            if (context == null) {
                return flag;
            }
            ConnectivityManager cwjManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cwjManager.getActiveNetworkInfo() != null) {
                NetworkInfo networkInfo = cwjManager.getActiveNetworkInfo();
                if(null != networkInfo){
                    flag = networkInfo.isAvailable();
                }
            }

            return flag;
        }
    }

    /**
     * 序列化一个对象
     *
     * @param context
     * @param fileName
     * @param object
     * @return
     */
    public static synchronized boolean persistObject(Context context,
                                                     String fileName, Object object) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(context.openFileOutput(fileName,
                    Context.MODE_PRIVATE));
            oos.writeObject(object);
            oos.flush();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                oos = null;
            }
        }
        return false;
    }

    /**
     * 验证字符串是否为手机号
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        if (!TextUtils.isEmpty(mobile)) {
            mobile = mobile.trim();
            String pattern = "^[1]\\d{10}$";
            return mobile.matches(pattern);
        }
        return false;
    }

    /**
     * 获取外部存储的路径
     *
     * @return null 没有外部存储
     */
    public static String getExtStorePath() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            return Environment.getExternalStorageDirectory().getPath();
        }
        return null;
    }

    /**
     * 显示提示信息
     *
     * @param context
     * @param msg
     */
    public static void showMsg(Context context, String msg) {
        if (context != null) {
//            Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            ToastUtil.getInstance().makeText(msg);
        }
    }

    /**
     * 显示提示信息
     *
     * @param context
     * @param resId   资源id
     */
    public static void showMsg(Context context, int resId) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isFinishing()) {
//                Logger.d(TAG, "showMsg, skip while host activity is finishing.");
            }
        }
        if (context != null) {
//            Toast.makeText(context.getApplicationContext(), resId, Toast.LENGTH_SHORT).show();
            ToastUtil.getInstance().makeText(resId);
        }
    }

    /**
     * 获取版本name
     *
     * @return 当前应用的版本号(versionName)
     */
    public static String getVersion(Context context) {
        if (context != null) {
            try {
                PackageManager manager = context.getPackageManager();
                if(null == manager){
                    return "1.0.0";
                }
                PackageInfo info = manager.getPackageInfo(
                        context.getPackageName(), 0);
                if (null == info){
                    return "1.0.0";
                }
                return info.versionName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "1.0.0";
    }

    /**
     * 获取版本Code
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        if (context != null) {
            try {
                PackageManager manager = context.getPackageManager();
                if(null == manager){
                    return 0;
                }
                PackageInfo info = manager.getPackageInfo(
                        context.getPackageName(), 0);
                if (null == info){
                    return 0;
                }
                return info.versionCode;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }


            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return resultData;
    }

    public static class StringUitls {
        /**
         * 设置textview某一区间显示不同颜色的文字
         *
         * @param startIndex
         * @param endIndex
         * @param color
         * @param textView
         */
        public static void setTextIntervalColor(int startIndex, int endIndex,
                                                int color, TextView textView) {
            SpannableString sp = new SpannableString(textView.getText()
                    .toString());
            sp.setSpan(new ForegroundColorSpan(color), startIndex, endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(sp);

        }

        /**
         * 把包含文本信息的输入流转化为字符串
         *
         * @param inputStream
         * @return
         * @throws UnsupportedEncodingException
         */
        public static String getStringFromInputStream(InputStream inputStream)
                throws UnsupportedEncodingException {
            // BufferedReader br = new BufferedReader(new InputStreamReader(
            // inputStream));

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    inputStream, "GB2312")); // 在此设置编码格式，可以消除乱码

            StringBuffer sb = new StringBuffer();
            String line = null;
            try {
                while ((line = br.readLine()) != null) {
                    sb.append(line);

                }
            } catch (IOException e) {

                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
            return sb.toString();
        }

        /**
         * 判断字符串不为空
         *
         * @param str
         * @return
         */
        public static boolean isNotBlank(String str) {
            if (str != null && !"".equals(str) && !".".equals(str)
                    && !"0".equals(str))
                return true;
            return false;
        }

        public static String saveNDecimalPlaces(Float formatNum,
                                                Integer decimalPoint) {
            StringBuilder sb = new StringBuilder("0.00");
            if (decimalPoint != null) {
                sb.delete(0, sb.length());
                sb.append("0.");
                for (int i = 0; i < decimalPoint; i++) {
                    sb.append("0");
                }
            }
            DecimalFormat format = new DecimalFormat(sb.toString());
            return format.format(formatNum);
        }
    }

    public static int dp2px(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * zhangsiqi 传入一个ListView，根据他的Item的高度设置他的高度 *
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 保存数据到sharePreference
     *
     * @param context
     * @param key
     * @return
     */
    public static void saveStringData(Context context, String projectName,
                                      String key, String value) {
        SharedPreferences preference = context.getSharedPreferences(
                projectName, Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 从sharePreference中取数据
     *
     * @param context
     * @param key
     * @return
     */
    public static String getStringData(Context context, String projectName,
                                       String key) {
        SharedPreferences preference = context.getSharedPreferences(
                projectName, Context.MODE_PRIVATE);
        String value = preference.getString(key, "");
        return value;
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间字符串 格式(dd/MM/yyyy HH:mm:ss)
     */
    public static String getCurFormatDate() {
        return mFormat.format(new Date());
    }

    /**
     * 正常返回manifest中的meta-data为channel的value 若没有或为空，则返回null
     *
     * @param context packageName包名
     * @return
     */
    public static String getChannel(Context context, String channelKey) {
        ApplicationInfo ai;
        try {
            ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            // Integer param = bundle.getInt(channelKey);
            Object param = bundle.get(channelKey);
            if (param.getClass() == Integer.class) {
                return ((Integer) param).toString();
            } else {
                return param.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static boolean checkPwd(Context context, String firstPwd, String secPwd) {
//        boolean flag = true;
//        if (StringUtil.isEmpty(firstPwd)) {
//            flag = false;
//            ToastUtil.getInstance(context).promptInputNewPassword();
//        } else if (StringUtil.isEmpty(secPwd)) {
//            flag = false;
//            ToastUtil.getInstance(context).promptInputConfirmPassword();
//        } else if (firstPwd.length() < 6) {
//            flag = false;
//            ToastUtil.getInstance(context).promptInvalidPassword();
//        } else if (!firstPwd.equals(secPwd)) {
//            flag = false;
//            ToastUtil.getInstance(context).promptDiffForTwoPassword();
//        }
//        return flag;
//    }

    public static boolean checkShowInvalidNetworkMsg(Activity activity) {
        if (isNetworkAvailable(activity)) {
            return true;
        }

        if (!activity.isFinishing()) {
            showMsg(activity, R.string.tip_no_network);
        }
        return false;
    }

    @SuppressLint("NewApi")
    public static void enableStrictMode(Class cls) {
        if (Utils.hasGingerbread()) {
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
                    new StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyLog();
            StrictMode.VmPolicy.Builder vmPolicyBuilder =
                    new StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog();

            if (Utils.hasHoneycomb()) {
                threadPolicyBuilder.penaltyFlashScreen();
                vmPolicyBuilder.setClassInstanceLimit(cls, 1);
            }
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }


    }

    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;

    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= 19;
    }

    public static List<Camera.Size> getResolutionList(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        return previewSizes;
    }

    public static class ResolutionComparator implements Comparator<Camera.Size> {

        @Override
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            if (lhs.height != rhs.height)
                return lhs.height - rhs.height;
            else
                return lhs.width - rhs.width;
        }

    }

    public static boolean isAppRunning(Context context) {
        boolean isAppRunning = false;
        String packageName = context.getPackageName();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = activityManager.getRunningTasks(100);

        for (ActivityManager.RunningTaskInfo runningTaskInfo : taskList) {
            if (runningTaskInfo.topActivity.getPackageName().equals(packageName) ||
                    runningTaskInfo.baseActivity.getPackageName().equals(packageName)) {
                isAppRunning = true;
            }
        }

        return isAppRunning;
    }

    public static String getTopActivityName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = activityManager.getRunningTasks(100);

        for (ActivityManager.RunningTaskInfo runningTaskInfo : taskList) {
            if (runningTaskInfo.topActivity.getPackageName().equals(context.getPackageName())) {
                return runningTaskInfo.topActivity.getClassName();
            }
        }
        return "";
    }

    public static String getAppName(Context context) {
        String appName = null;
        if (context != null) {
            PackageManager packageManager = null;
            ApplicationInfo applicationInfo = null;
            try {
                packageManager = context.getPackageManager();
                applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
                appName = (String) packageManager.getApplicationLabel(applicationInfo);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }
        return appName;
    }


    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        int i;
        int j;
        if (bmp.getHeight() > bmp.getWidth()) {
            i = bmp.getWidth();
            j = bmp.getWidth();
        } else {
            i = bmp.getHeight();
            j = bmp.getHeight();
        }

        Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);

        Canvas localCanvas = new Canvas(localBitmap);

        while (true) {
            localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0, i, j), null);
            if (needRecycle)
                bmp.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                // F.out(e);
            }
            i = bmp.getHeight();
            j = bmp.getHeight();
        }
    }


    /**
     * 判断应用是否已经启动
     *
     * @param context     一个context
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public static boolean isAppAlive(Context context, String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
//                Logger.i(TAG, String.format("the %s is running, isAppAlive return true", packageName));
                return true;
            }
        }
//        Logger.i(TAG, String.format("the %s is not running, isAppAlive return false", packageName));
        return false;
    }

    //获取栈顶Activity
    public static ComponentName getTopActivity(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            return tasks.get(0).topActivity;
        }

        return null;
    }

    public static String transformThousand(int number) {
        String thousand = "";
        if (number <= 999) {
            thousand = String.valueOf(number);
        } else {
            int remainder = number % 1000;
            if (0 == remainder) {
                thousand = number / 1000 + "k";
            } else {
                NumberFormat format = NumberFormat.getInstance();
                format.setRoundingMode(RoundingMode.HALF_UP);
                format.setMinimumFractionDigits(1);
                format.setMaximumFractionDigits(1);
                thousand = format.format(number / 1000d) + "k";
            }
        }
        return thousand;
    }

    /**
     * 获取单个文件的MD5值！
     *
     * @param file
     * @return
     */

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    public static boolean checkFileMD5(File file, String fileMD5) {
        boolean result = false;
        if (TextUtils.isEmpty(fileMD5)) {
//            Logger.d(TAG, "MD5  is empty!");
            result = false;
        } else {
            String localMD5 = Utils.getFileMD5(file);
            if (TextUtils.equals(fileMD5, localMD5)) {
                result = true;
            } else {
                result = false;
            }
        }

        return result;
    }

    // 公里计算公式
    public static String getDistanceByStep(int steps) {
        return String.format("%.2f", steps * 0.6f / 1000);
    }

    // 千卡路里计算公式
    public static String getCalorieByStep(int steps) {
        return String.format("%.1f", steps * 0.6f * 60 * 1.036f / 1000);
    }

    public static String getResolution(Context context) {
        DisplayMetrics dm = new DisplayMetrics();

        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(dm);

        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        return screenWidth + "x" + screenHeight;
    }

    public static String getTimestamp() {
        Calendar calendar = Calendar.getInstance();//获取当前日历对象
        long localTime = calendar.getTimeInMillis() / 1000;//获取当前时区下日期时间对应的时间戳, 服务器仅支持10位时间戳,精确到秒
        return localTime + "";
    }

    public static String getLanguage(Context context) {
        final String language = Utils.getChannel(context, "LANGUAGE");
        return language;
    }

    public static String getChannel(Context context) {
        final String channel = Utils.getChannel(context, "UMENG_CHANNEL");
        return channel;
    }

    public static String getDeviceid(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 开屏true，否则false
     * @param context
     * @return
     */
    public static boolean isScreenOn(Context context){
        // If the screen is off then the device has been locked
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            isScreenOn = powerManager.isInteractive();
        } else {
            isScreenOn = powerManager.isScreenOn();
        }
        Log.e("Screen","isScreenOn : " + isScreenOn);
        return isScreenOn;
    }

    public static String asteriskUserName(String userName){
        // 最后一位不替换为星号
        if (!TextUtils.isEmpty(userName)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < userName.length(); i++) {
                char c = userName.charAt(i);
                if (i!=userName.length()-1) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        } else {
            return userName;
        }
    }

    /**
     * 将手机号脱敏处理
     * @param phoneNumber
     * @return
     */
    public static String asteriskPhone(String phoneNumber) {

        // 前面3位和后面4位不替换为星号
        if (!TextUtils.isEmpty(phoneNumber) && phoneNumber.length() > 6) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < phoneNumber.length(); i++) {
                char c = phoneNumber.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        } else {
            return phoneNumber;
        }
    }

    /**
     * 将证件号码脱敏处理
     * @param idNumber
     * @return
     */
    public static String asteriskID(String idNumber) {

        // 前面3位和后面4位不替换为星号
        if (!TextUtils.isEmpty(idNumber) && idNumber.length() > 7) {

            return idNumber.replaceAll("(?<=[\\d]{3})\\d(?=[\\d]{4})", "*");
        } else {
            return idNumber;
        }
    }

    /**
     * 将用户姓名脱敏处理
     * @param userName
     * @return
     */
    public static String asteriskName(String userName) {
        //两个字的名字 例如张三 修正为 *三
        //三个字以上的名字 例如张三三 修正为 张*三
        if (!TextUtils.isEmpty(userName)) {
            if(userName.length()>2){
                return userName.replaceAll("(?<=[\\w])\\w(?=[\\w])", "*");
            }else{
                return userName.replaceAll("\\w(?=[\\w])", "*");
            }
        } else {
        return userName;
        }
    }


    /**
     * 银行卡号脱敏规则：显示前4、后4位，其余位均用*遮蔽
     *
     * @param cardNo
     * @return
     */
    public static String desensitizationBankCardNo(String cardNo) {
        if (cardNo.length() >= 8) {
            StringBuilder sb = new StringBuilder();
            String first = cardNo.substring(0, 4);
            String last = cardNo.substring(cardNo.length() - 4);
            sb.append(first);
            sb.append(" **** **** ");
            sb.append(last);
            return sb.toString();
        }
        return cardNo;
    }

    public static boolean isMainProcess(Application application) {
        return application.getApplicationContext().getPackageName().equals
                (getProcessName(application));
    }

    public static boolean isStepProcess(Application application){
        return (application.getApplicationContext().getPackageName() + ":todaystep").equals
                (getProcessName(application));
    }

    /**
     * 通过ActivityManager获取进程名时，有null存在的话，才通过反射ActivityThread的getProcessName方法来获取进程名字
     * @param application
     * @return
     */
    public static String getProcessName(Application application) {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) application.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = manager.getRunningAppProcesses();
        if (appProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo process : appProcesses) {
                if (process != null && process.pid == pid) {
                    processName = process.processName;
                }
            }
        } else {
            try {
                Class<?> clazz = Class.forName("android.app.ActivityThread");
                Method currentActivityThreadMethod = clazz.getDeclaredMethod("currentActivityThread");
                currentActivityThreadMethod.setAccessible(true);
                Object tCurrentActivityThread = currentActivityThreadMethod.invoke(null);

                Method tGetProcessNameMethod = clazz.getDeclaredMethod("getProcessName");
                tGetProcessNameMethod.setAccessible(true);
                processName = (String) tGetProcessNameMethod.invoke(tCurrentActivityThread);
            } catch (Throwable e) {
            } finally {
                if (processName == null) {
                    processName = "";
                }
            }
        }
        return processName;
    }

    public static final String IMAGE_SUFFIX_PNG = ".png";
    public static final String IMAGE_SUFFIX_PNG_ = ".PNG";
    public static final String IMAGE_SUFFIX_JPG = ".jpg";
    public static final String IMAGE_SUFFIX_JPG_ = ".JPG";
    public static final String IMAGE_SUFFIX_JPEG = ".jpeg";
    public static final String IMAGE_SUFFIX_JPEG_ = ".JPEG";

    public static String imgSuffix(String imgUrl) {
        String suffix = "";
        if (imgUrl.contains(IMAGE_SUFFIX_PNG) || imgUrl.contains(IMAGE_SUFFIX_PNG_)) {
            suffix = IMAGE_SUFFIX_PNG;
        } else if (imgUrl.contains(IMAGE_SUFFIX_JPG) || imgUrl.contains(IMAGE_SUFFIX_JPG_)) {
            suffix = IMAGE_SUFFIX_JPG;
        } else if (imgUrl.contains(IMAGE_SUFFIX_JPEG) || imgUrl.contains(IMAGE_SUFFIX_JPEG_)) {
            suffix = IMAGE_SUFFIX_JPEG;
        } else {

        }

        return suffix;
    }
    /**
    * wcd 获取栈中现有的activity实例
     * @return 异常时，返回null
    * */
    public static List<Activity> getActivitiesByApplication(Application application) {
        List<Activity> list = new ArrayList<>();
        try {
            Class<Application> applicationClass = Application.class;
            Field mLoadedApkField = applicationClass.getDeclaredField("mLoadedApk");
            mLoadedApkField.setAccessible(true);
            Object mLoadedApk = mLoadedApkField.get(application);
            Class<?> mLoadedApkClass = mLoadedApk.getClass();
            Field mActivityThreadField = mLoadedApkClass.getDeclaredField("mActivityThread");
            mActivityThreadField.setAccessible(true);
            Object mActivityThread = mActivityThreadField.get(mLoadedApk);
            Class<?> mActivityThreadClass = mActivityThread.getClass();
            Field mActivitiesField = mActivityThreadClass.getDeclaredField("mActivities");
            mActivitiesField.setAccessible(true);
            Object mActivities = mActivitiesField.get(mActivityThread);
            if (mActivities instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<Object, Object> arrayMap = (Map<Object, Object>) mActivities;
                for (Map.Entry<Object, Object> entry : arrayMap.entrySet()) {
                    Object value = entry.getValue();
                    Class<?> activityClientRecordClass = value.getClass();
                    Field activityField = activityClientRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Object o = activityField.get(value);
                    list.add((Activity) o);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            list = null;
        }
        return list;

    }

    private static List<Activity> mDetailActivityList = Collections.synchronizedList(new LinkedList<Activity>());
    private static List<Activity> mConfirmActivityList = Collections.synchronizedList(new LinkedList<Activity>());
    private static Map<String, List<Activity>> mActivityMap = new HashMap<>();

    /**
     * 根据tag定义任务栈，存放activity实例
     *
     * @param tag      页面属性，相当于key，扩展用
     * @param activity 入栈实例
     */
    public static void addActivityByTag(String tag, Activity activity) {
        if (activity != null && mDetailActivityList != null && mActivityMap != null) {
            mDetailActivityList.add(activity);
            mActivityMap.put(tag, mDetailActivityList);
        }
    }

    /**
     * 移除特定tag对应的任务栈中某个activity
     *
     * @param tag
     * @param activity
     */
    public static void removeActivityByTag(String tag, Activity activity) {
        if (activity != null && mDetailActivityList != null && mActivityMap != null && mDetailActivityList.size() > 0) {
            mDetailActivityList.remove(activity);
            mActivityMap.put(tag, mDetailActivityList);
        }
    }


    /**
     * 根据tag定义任务栈，存放activity实例
     *
     * @param tag      页面属性，相当于key，扩展用
     * @param activity 入栈实例
     */
    public static void addConfirmActivityByTag(String tag, Activity activity) {
        if (activity != null && mConfirmActivityList != null && mActivityMap != null) {
            mConfirmActivityList.add(activity);
            mActivityMap.put(tag, mConfirmActivityList);
        }
    }

    /**
     * 移除特定tag对应的任务栈中某个activity
     *
     * @param tag
     * @param activity
     */
    public static void removeConfirmActivityByTag(String tag, Activity activity) {
        if (activity != null && mConfirmActivityList != null && mActivityMap != null && mConfirmActivityList.size() > 0) {
            mConfirmActivityList.remove(activity);
            mActivityMap.put(tag, mConfirmActivityList);
        }
    }

    /**
     * 获取特定任务栈列表
     *
     * @param tag key
     * @return list
     */
    public static List<Activity> getActivityListByTag(String tag) {
        if (mActivityMap != null)
            return mActivityMap.get(tag);
        else
            return null;
    }

    private static long lastClickTime;
    private final static long TIME = 1000;

    public static boolean isFastRequest() {
        long time = System.currentTimeMillis();
        if (Math.abs(time - lastClickTime) < TIME) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 根据比例计算宽高
     *
     * @param width
     * @param scale
     * @return
     */
    public static final int[] getWidthAndHeightByScale(int width, float scale) {
        int viewHeight = Math.round(width / scale);
        return new int[]{width, viewHeight};
    }
}
