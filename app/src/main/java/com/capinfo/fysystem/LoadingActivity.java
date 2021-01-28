package com.capinfo.fysystem;

import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.capinfo.fysystem.base.BaseActivity;
import com.capinfo.fysystem.utils.sp.AppSharedPreferencesHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LoadingActivity extends BaseActivity {
//    public static final PermissionItem[] PERMISSION_ALL = new PermissionItem[]{
//            new PermissionItem(android.Manifest.permission.READ_EXTERNAL_STORAGE, R.string.comp_pedometer_launcher_tips_permission_storage, R.drawable.comp_pedometer_launcher_ic_launcher),
//            new PermissionItem(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, R.string.comp_pedometer_launcher_tips_permission_storage, R.drawable.comp_pedometer_launcher_ic_launcher)
//    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goNext();
        /*if(permission()){
            goNext();
        }*/
    }

    private void goNext(){
//        String url = "http://219.232.207.218:9100/approval?appType=cap&code=DZ67Rg&username=%22%E6%9C%B1%E7%A3%8A%22";
        String url = "http://192.168.1.158:3000/";
        String urlHome = "http://219.232.207.218:9100/approval?appType=cap&code=DZ67Rg&username=%22%E6%9C%B1%E7%A3%8A%22";
        String urlLogin = "http://219.232.207.218:9100/approval?appType=cap&code=DZ67Rg&username=%22%E6%9C%B1%E7%A3%8A%22";
//        String url = "https://hr.capinfo.com.cn/templates/index/hcmlogon.jsp";
        String title = "防疫系统";
        if(!TextUtils.isEmpty(AppSharedPreferencesHelper.getToken())){
            url = urlHome;
        }
        ARouter.getInstance().build("/comm/commonWebview")
                .withString(RefacTX5WebViewActivity.PROTOCOL_KEY_URL,url)
                .withString(RefacTX5WebViewActivity.URL_JSON_KEY_HOME,urlHome)
                .withString(RefacTX5WebViewActivity.URL_JSON_KEY_LOGIN,urlLogin)
                .withString(RefacTX5WebViewActivity.URL_JSON_KEY_TITLE,title)
                .navigation(this, new NavCallback() {
                    @Override
                    public void onArrival(Postcard postcard) {
                        finish();
                    }
                });
    }

    @Override
    protected int getlayoutId() {
        return 0;
    }

    /*private boolean permission() {
        return OKPermissionManager.applyPermissionNoDialog(this, false, PERMISSION_ALL, new OKPermissionListener() {

            @Override
            public void onOKPermission(@NonNull String[] permissions, @NonNull int[] grantResults, boolean success) {
                goNext();
            }

            @Override
            public void onRefusePermission() {
            }

            @Override
            public void onAppSettingsSuccess() {
            }
        });
    }*/
}
