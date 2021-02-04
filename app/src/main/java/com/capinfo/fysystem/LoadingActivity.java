package com.capinfo.fysystem;

import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.andrjhf.okpermission.OKPermissionListener;
import com.andrjhf.okpermission.OKPermissionManager;
import com.andrjhf.okpermission.PermissionItem;
import com.capinfo.fysystem.base.BaseActivity;
import com.capinfo.fysystem.utils.sp.AppSharedPreferencesHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LoadingActivity extends BaseActivity {
    public static final PermissionItem[] PERMISSION_ALL = new PermissionItem[]{
            new PermissionItem(android.Manifest.permission.READ_EXTERNAL_STORAGE, R.string.comp_pedometer_launcher_tips_permission_storage, R.drawable.comp_pedometer_launcher_ic_launcher),
            new PermissionItem(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, R.string.comp_pedometer_launcher_tips_permission_storage, R.drawable.comp_pedometer_launcher_ic_launcher)
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(permission()){
            goNext();
        }
    }

    private void goNext(){
        String urlRevisePwd = "http://yqfk.bgosp.com/app/#/main/revisePwd";
        String urlHome = "http://yqfk.bgosp.com/app/#/main/homeUserMsg";
        String urlLogin = "http://yqfk.bgosp.com/app/#/";
        String title = "疫情防疫";

        ARouter.getInstance().build("/comm/commonWebview")
                .withString(RefacTX5WebViewActivity.URL_JSON_KEY_HOME,urlHome)
                .withString(RefacTX5WebViewActivity.URL_JSON_KEY_LOGIN,urlLogin)
                .withString(RefacTX5WebViewActivity.URL_JSON_KEY_TITLE,title)
                .withString(RefacTX5WebViewActivity.URL_JSON_KEY_REVISEPSD,urlRevisePwd)
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

    private boolean permission() {
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
    }
}
