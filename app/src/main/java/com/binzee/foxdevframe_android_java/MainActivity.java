package com.binzee.foxdevframe_android_java;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;

import com.binzee.foxdevframe.ui.FoxActivity;
import com.binzee.foxdevframe.ui.tools.ViewUtil;
import com.binzee.foxdevframe.utils.permission.PermissionChecker;
import com.binzee.foxdevframe.utils.permission.PermissionCheckerInterface;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends FoxActivity {

    @Override
    protected int getContentViewResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate() {
        setFullScreen();
        PermissionChecker.with(this)
                .addPermission(Manifest.permission.CAMERA)
                .addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .addPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .checkAndRequest(0x01, (requestCode, failedList, noAskList) -> {
                    // 权限获取回调
                });
    }
}