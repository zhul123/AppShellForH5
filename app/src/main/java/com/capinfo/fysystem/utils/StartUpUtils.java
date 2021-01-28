package com.capinfo.fysystem.utils;

import android.app.Application;
import android.text.TextUtils;

import com.capinfo.fysystem.base.BaseApplication;
import com.tencent.smtt.sdk.QbSdk;

import java.security.GeneralSecurityException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//import com.pa.health.lib.photo.utils.PhotoUtil;

/**
 * @author :  jiahongfei
 * @email : jiahongfeinew@163.com
 * @date : 2019/4/4
 * @desc : 启动预加载
 */
public class StartUpUtils {

    private static StartUpUtils sStartUpUtils;
    private Application mContext;
    private ExecutorService mExecutors = Executors.newFixedThreadPool(5);

    public static StartUpUtils get() {
        if (null == sStartUpUtils) {
            synchronized (StartUpUtils.class) {
                if (null == sStartUpUtils) {
                    sStartUpUtils = new StartUpUtils();
                }
            }
        }
        return sStartUpUtils;
    }

    private StartUpUtils() {
        mContext = BaseApplication.getInstance();
    }

    //---------------------------------开机异步初始化SDK Start---------------------------------

    public void initSDK() {
        mExecutors.execute(new Runnable() {
            @Override
            public void run() {
                initTx5WebView();

            }
        });
    }

    /**
     * 初始化腾讯X5浏览器
     */
    private void initTx5WebView() {
        QbSdk.setDownloadWithoutWifi(true);
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Logger.e(" onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                Logger.e("onCoreInitFinished");
            }
        };
        //x5内核初始化接口
        try {
            QbSdk.initX5Environment(BaseApplication.getInstance(), cb);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
