package com.capinfo.fysystem.utils;

/**
 * @author :  wcd
 * @email : WANGCHUNDONG719@pingan.com.cn
 * @date : 2019-5-28 16:12
 * @desc :
 * @modify： wangshaobo，时间差加绝对值才会更完美
 */

public class DoubleUtils {
    private static long lastClickTime;
    private final static long TIME = 800;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if (Math.abs(time - lastClickTime) < TIME) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
