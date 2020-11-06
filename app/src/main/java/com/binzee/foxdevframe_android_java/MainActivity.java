package com.binzee.foxdevframe_android_java;

import android.Manifest;
import android.os.Build;

import com.binzee.foxdevframe.ui.FoxActivity;
import com.binzee.foxdevframe.ui.tools.launcher.Launcher;
import com.binzee.foxdevframe.ui.tools.popup.PopupHelper;
import com.binzee.foxdevframe.utils.LogUtil;
import com.binzee.foxdevframe.utils.TextTools;
import com.binzee.foxdevframe.utils.permission.PermissionUtil;

public class MainActivity extends FoxActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected int getContentViewResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate() {
//        setFullScreen();
        LogUtil.enableANRLog();
        LogUtil.setGlobalExceptionCapture(e ->
                LogUtil.e(TAG, "onCreate: ", e));

        findViewById(R.id.confirm_button).setOnClickListener(v -> test());

        TextTools.IDCardUtil util = TextTools.get("210682199403110018").getIDCardUtil();
        String city = "身份证地址：" + util.getProvinceName();
        String birthDay = " 出生日期：" + util.getBirthDay();
        String gender = " 性别：" + (util.isMale() ? "男" : "女");
        LogUtil.d(TAG, "onCreate: idcard  " + city + birthDay + gender);

        //设置二次返回键点击验证
        setBackPressTwiceCheck(new OnPressTwiceListener() {
            @Override
            public void onFirstPress() {
                toast("两秒内第二次点击返回键返回");
            }

            @Override
            public void onSecondPress() {
                superOnBackPressed();
            }
        }, 2000);
    }

    private void test() {
        permissionTest();
//        systemSettingTest();
//        systemSettingPopupTest();
//        ThreadUtils.get().executeIO(() -> {
//            throw new RuntimeException("这是一个子线程内运行时异常");
//        });
//        throw new RuntimeException("这是一个运行时异常");
    }

    private void permissionTest() {
        PermissionUtil.with(this)
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

    private void systemSettingPopupTest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            PopupHelper.systemPopup()
                    .showWifiSetting();
//                .showVolumeSetting();
//                .showNFCSetting();
//                .showInternetSetting();
        }
    }
}