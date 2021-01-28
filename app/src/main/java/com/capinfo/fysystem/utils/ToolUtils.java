package com.capinfo.fysystem.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.capinfo.fysystem.R;

import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 *
 * @author hongfeijia
 */
public class ToolUtils {

    /**
     * 后缀是pdf
     */
    public static final String SUFFIX_PDF = ".pdf";

    /**
     * 地址是由iobs平台发放
     */
    public static final String SUFFIX_TYPE_IOBS = "iobs";

    /**
     * 设置字体
     *
     * @param context
     * @param textView
     * @param typefaceName 在assets下保存的字体名字
     */
    public static void setTypeface(Context context, TextView textView, String typefaceName) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), typefaceName);
        textView.getPaint().setTypeface(typeface);
    }

    public static void setColorStateList(Context context, TextView textView, int color) {
        ColorStateList csl = (ColorStateList) context.getResources().getColorStateList(color);
        if (csl != null) {
            textView.setTextColor(csl);//设置按钮文字颜色
        }
    }

    /**
     * 其中languag为语言码： zh：汉语 en：英语
     *
     * @return
     */
    public static boolean isLanguage(Context context, String language) {
        Locale locale = context.getResources().getConfiguration().locale;
        String tempLanguage = locale.getLanguage();
        if (tempLanguage.endsWith(language))
            return true;
        else
            return false;
    }

    public static String getNumberFromString(String string) {
        String reg = "[^0-9]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(string);
        return m.replaceAll("").trim();
    }

    public static int intByString(String numberString) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        int result = 0;
        try {
            Number number = numberFormat.parse(numberString);
            result = number.intValue();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 切换语言，只改变当前应用的 android.permission.CHANGE_CONFIGURATION
     * android:configChanges="locale" 调用完一定要更新当前界面的string
     * 例如：调用setContentView(R.layout.main); 从新设置 TextView textView =
     * (TextView)findViewById(R.id.textView);
     * textView.setText(getString(R.string.hello_world));
     *
     * @param context
     * @param locale
     */
    public static void switchLanguage(Context context, Locale locale) {
        Configuration config = context.getResources().getConfiguration();// 获得设置对象
        Resources resources = context.getResources();// 获得res资源对象
        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
        config.locale = locale;
        resources.updateConfiguration(config, dm);
    }

    /**
     * 实现文本复制功能
     *
     * @param content
     */
    public static void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    public static boolean isBankNumber(String bankNumber) {
        char[] cc = bankNumber.toCharArray();
        int[] n = new int[cc.length + 1];
        int j = 1;
        for (int i = cc.length - 1; i >= 0; i--) {
            n[j++] = cc[i] - '0';
        }
        int even = 0;
        int odd = 0;
        for (int i = 1; i < n.length; i++) {
            if (i % 2 == 0) {
                int temp = n[i] * 2;
                if (temp < 10) {
                    even += temp;
                } else {
                    temp = temp - 9;
                    even += temp;
                }
            } else {
                odd += n[i];
            }
        }

        int total = even + odd;
        if (total % 10 == 0)
            return true;
        return false;
    }

//    /**
//     * 判断是否是提供包名的类，clz是否是appPackages中的类
//     * @param clz
//     * @param appPackages 包全名
//     * @return true 自定义的类，false不是package的类
//     */
//    public static boolean isCustomJavaClass(Class<?> clz, Set<String> appPackages) {
//
//        if(clz != null && null != clz.getPackage()){
//
//            return appPackages.contains(clz.getPackage().toString());
//        }
//
//        return  false;
//    }
//
//    public static Set<String> getClassNames(Class paramClass, Set<String> appPackages) {
//        Set<String> classNameTreeSet = new TreeSet<>();
//        if (paramClass.isPrimitive()) {
//            return classNameTreeSet;
//        }
//        classNameTreeSet.add(paramClass.getName());
//        Class whClass = paramClass.getSuperclass();
//        while (null != whClass) {
//            classNameTreeSet.add(whClass.getName());
//            whClass = whClass.getSuperclass();
//        }
//        for (Field field : paramClass.getDeclaredFields()) {
//            //字段类
//            Class clazz = field.getType();
//            classNameTreeSet.add(clazz.getName());
//            //增加这个类的父类
//            whClass = field.getType().getSuperclass();
//            while (null != whClass) {
//                classNameTreeSet.add(whClass.getName());
//                whClass = whClass.getSuperclass();
//            }
//            //如果这个类也是自定义的，递归查找里面的字段
//            if(isCustomJavaClass(clazz,appPackages)){
//                Set<String> tmpSet = getClassNames(clazz,appPackages);
//                if (null != tmpSet && tmpSet.size() > 0) {
//                    classNameTreeSet.addAll(tmpSet);
//                }
//            }
//
//            //容器保存的类
//            if (ArrayList.class.isAssignableFrom(clazz)) {
//                classNameTreeSet.add(ArrayList.class.getName());
//                Type fieldType = field.getGenericType();
//                if (fieldType instanceof ParameterizedType) {
//                    ParameterizedType paramType = (ParameterizedType) fieldType;
//                    Type[] genericTypes = paramType.getActualTypeArguments();
//                    if (genericTypes != null && genericTypes.length > 0) {
//                        if (genericTypes[0] instanceof Class<?>) {
//                            Class<?> subType = (Class<?>) genericTypes[0];
//                            classNameTreeSet.add(subType.getName());
//                            if(isCustomJavaClass(subType,appPackages)){
//                                Set<String> tmp1Set = getClassNames(subType,appPackages);
//                                if (null != tmp1Set && tmp1Set.size() > 0) {
//                                    classNameTreeSet.addAll(tmp1Set);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return classNameTreeSet;
//    }

    public static Uri nativePhotoUri(String photoPathString) {
        if (TextUtils.isEmpty(photoPathString)) {
            return null;
        }
        return Uri.fromFile(new File(photoPathString));
    }

    /**
     * 本地照片路径，返回URI路径
     *
     * @param photoPathString
     * @return
     */
    public static String nativePhotoPath(String photoPathString) {
        if (TextUtils.isEmpty(photoPathString)) {
            return null;
        }
        return Uri.decode(Uri.fromFile(new File(photoPathString)).toString());
    }

    /**
     * 保留几位小数
     *
     * @param newScale
     * @param bigDecimal
     * @return
     */
    public static double bigDecimal(int newScale, double bigDecimal) {
        BigDecimal b = new BigDecimal(bigDecimal);

        return b.setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 在url中根据key获取参数的value（http://www.xxxxx.com?key=value）
     *
     * @param url
     * @param key
     * @return
     */
    public static String getParamByUrl(String url, String key) {
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(key)) {
            String[] urls = url.split("\\?");
            if (urls.length >= 2 && !TextUtils.isEmpty(urls[1])) {
                String[] params = urls[1].split("&");
                if (params.length > 0) {
                    for (String tmpParam : params) {
                        String[] keyParam = tmpParam.split("=");
                        if (keyParam.length >= 2 && key.equals(keyParam[0])) {
                            return keyParam[1];
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 判断url是否是pdf
     *
     * @return
     */
    public static boolean isUrlPdf(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        Uri uri = Uri.parse(url);
        if (null != uri) {
            String lastPath = uri.getLastPathSegment();
            if (null != lastPath) {
                if (lastPath.endsWith(SUFFIX_PDF) || lastPath.endsWith(SUFFIX_PDF.toUpperCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * url中是否有参数
     *
     * @param url
     * @return url为null或者空、没有参数返回false，url有参数?后面有参数返回true
     */
    public static boolean isUrlParam(String url) {
        if (!TextUtils.isEmpty(url)) {
            String[] urls = url.split("\\?");
            if (urls.length >= 2 && !TextUtils.isEmpty(urls[1])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据ImageUrl返回ImageName，如果传入imageUrl为null或者不是Url那么直接以当前时间作为文件名后缀为.jpeg
     *
     * @param imageUrl
     * @return
     */
    public static String getUrlImageName(String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            Uri uri = Uri.parse(imageUrl);
            if (null != uri) {
                String lastPath = uri.getLastPathSegment();
                return lastPath;
            }
        }
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(System.currentTimeMillis()));
        stringBuilder.append(".jpeg");
        return stringBuilder.toString();
    }

    /**
     * 如果字符串全部是空格也是空
     *
     * @param string
     * @return 字符串全部空格或者空true, 否则false
     */
    public static boolean isEmpty(String string) {
        if (TextUtils.isEmpty(string)) {
            return true;
        }
        String tmpString = string.replace(" ", "");
        return TextUtils.isEmpty(tmpString);
    }

    public static String splitPreTypeItemName(String preTypeItemName) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] preItemStrings = preTypeItemName.split("\\|");
        for (int i = 0; i < preItemStrings.length; i++) {
            stringBuilder.append(preItemStrings[i]);
            if (preItemStrings.length - 1 != i) {
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 是否成年18周岁
     *
     * @return
     */
    public static boolean isAdult(long birthday) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(birthday);
        calendar.add(Calendar.YEAR, 18);
        Date update = new Date(calendar.getTimeInMillis());
        Date today = new Date(System.currentTimeMillis());
        return today.after(update);
    }

    /**
     * 打开对应App
     * @param context
     * @param packageName
     * @return
     */
    public static boolean openOtherApp(Context context, String packageName) {
        if (isLive(context) || TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {//防止没有对应app的activity
            Intent lan = context.getPackageManager().getLaunchIntentForPackage(packageName);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(lan.getComponent());
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            ToastUtil.getInstance().makeText(R.string.app_not_installed);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 姓名脱敏
     * @param userName 人名
     * @return
     */
    public static String userNameDesensitization(String userName) {
        if (!TextUtils.isEmpty(userName) && userName.length() > 0) {
            return userName.substring(0, 1) + "**";
        }
        return userName;
    }

    /**
     * 判断当前activity是否还活着
     *
     * @return
     */
    public static boolean isLive(Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            return isLive(activity);
        } else {
            return false;
        }
    }
}