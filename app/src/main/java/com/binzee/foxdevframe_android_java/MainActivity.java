package com.binzee.foxdevframe_android_java;

import android.Manifest;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.binzee.foxdevframe.ui.FoxActivity;
import com.binzee.foxdevframe.ui.tools.launcher.Launcher;
import com.binzee.foxdevframe.ui.tools.popup.PopupHelper;
import com.binzee.foxdevframe.utils.permission.PermissionChecker;

public class MainActivity extends FoxActivity {

    @Override
    protected int getContentViewResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate() {
//        setFullScreen();

        findViewById(R.id.confirm_button).setOnClickListener(v -> test());
    }

    private void test() {
//        permissionTest();
//        systemSettingTest();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            systemSettingPopupTest();
        }
    }

    private void permissionTest() {
        PermissionChecker.with(this)
                .addPermission(Manifest.permission.CAMERA)
                .addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .addPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .checkAndRequest(0x01, (requestCode, failedList, noAskList) -> {
                    // 权限获取回调
                });
    }

    private void systemSettingTest() {
        Launcher.with(this)
                .systemShortCuts()
//                        .launchApplicationDetails());
//                        .launchConnectionSetting());
//                        .launchWirelessSettings());
//                        .launchBluetoothSettings());
//                        .launchNetworkSettings());
//                        .launchLocationSetting());
//                        .launchSoundSetting());
                .launchWifiSetting();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void systemSettingPopupTest() {
        PopupHelper.systemPopup()
                .showWifiSetting();
//                .showVolumeSetting();
//                .showNFCSetting();
//                .showInternetSetting();
    }
}