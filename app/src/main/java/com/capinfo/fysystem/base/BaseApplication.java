package com.capinfo.fysystem.base;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.alibaba.android.arouter.launcher.ARouter;
import com.capinfo.fysystem.BuildConfig;
import com.capinfo.fysystem.event.NetWorkChangedEvent;
import com.capinfo.fysystem.utils.EventBusUtils;
import com.capinfo.fysystem.utils.Logger;
import com.capinfo.fysystem.utils.StartUpUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;

public class BaseApplication extends Application {

    private static BaseApplication mInstance = null;
    private ConnectionChangeReceiver mNetworkStateReceiver;
    private NetworkInfo.State mCurrentNetWorkState;

    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.init(this);
        registerNetworkState();
        StartUpUtils.get().initSDK();
        mInstance = this;
        CrashReport.initCrashReport(getApplicationContext(),BuildConfig.BUGLY_APP_ID,isDebugBuild());
    }

    public static BaseApplication getInstance() {
        return mInstance;
    }

    public boolean isDebugBuild() {
        return BuildConfig.DEBUG;
    }

    private void registerNetworkState() {
        // 注册网络监听
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mNetworkStateReceiver = new ConnectionChangeReceiver();
        registerReceiver(mNetworkStateReceiver, filter, "permission.ALLOW_BROADCAST", null);
    }

    public class ConnectionChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                ConnectivityManager connManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();
                if (null != activeNetwork) {
                    mCurrentNetWorkState = activeNetwork.getState();
                } else {
                    mCurrentNetWorkState = NetworkInfo.State.DISCONNECTED;
                }
                if (NetworkInfo.State.CONNECTED == mCurrentNetWorkState) {
                    EventBusUtils.postEventBus(new NetWorkChangedEvent(true));
                } else {
                    EventBusUtils.postEventBus(new NetWorkChangedEvent(false));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.i("ConnectionChangeReceiver, meet exception : " + ex.getMessage());
            }
        }
    }
}
