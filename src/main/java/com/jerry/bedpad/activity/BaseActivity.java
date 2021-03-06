package com.jerry.bedpad.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jerry on 2016/12/29.
 */

public class BaseActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_RESULT = 1;

    private static PermissionListener mListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    /**
     * 申请权限
     * @param permissions 所需要的权限数组
     * @param listener 申请权限结果回调
     */
    public static void requestRuntimePermission(String[] permissions, PermissionListener listener) {
        // 获取应用程序栈顶的Activity，用来申请权限
        Activity topActivity = ActivityCollector.getTopActivity();
        if (topActivity == null) {
            return;
        }
        mListener = listener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            // 如果没有这个权限，就添加到集合中
            if (ContextCompat.checkSelfPermission(topActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        // 如果拒绝权限列表不空，就开始申请权限
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(topActivity,
                    permissionList.toArray(new String[permissionList.size()]), REQUEST_PERMISSION_RESULT);
        } else {
            mListener.onGranted();
        }
    }

    /**
     * 请求权限的回调
     * @param requestCode 请求码
     * @param permissions 请求权限列表数组
     * @param grantResults 请求结果数组
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_RESULT:
                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    // 遍历所有请求结果，如果有被拒绝的，添加到拒绝集合中
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                           deniedPermissions.add(permission);
                        }
                    }
                    if (deniedPermissions.isEmpty()) {
                        mListener.onGranted();
                    } else {
                        mListener.onDenied(deniedPermissions);
                    }
                }
                break;
            default:
                break;
        }
    }

}
