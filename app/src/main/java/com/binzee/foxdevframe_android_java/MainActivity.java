package com.binzee.foxdevframe_android_java;

import android.Manifest;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.binzee.foxdevframe.FoxCore;
import com.binzee.foxdevframe.ui.FoxActivity;
import com.binzee.foxdevframe.ui.tools.launcher.Launcher;
import com.binzee.foxdevframe.ui.tools.popup.PopupHelper;
import com.binzee.foxdevframe.utils.LogUtil;
import com.binzee.foxdevframe.utils.TextTools;
import com.binzee.foxdevframe.utils.ThreadUtils;
import com.binzee.foxdevframe.utils.http.ClientInterface;
import com.binzee.foxdevframe.utils.http.ClientUtil;
import com.binzee.foxdevframe.utils.permission.PermissionUtil;
import com.binzee.foxdevframe.utils.phone.ADBTools;
import com.binzee.foxdevframe.utils.phone.PhoneStatusUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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

//        netWorkTest();
//        permissionTest();
//        systemSettingTest();
//        systemSettingPopupTest();
//        ThreadUtils.get().executeIO(() -> {
//            throw new RuntimeException("这是一个子线程内运行时异常");
//        });
//        throw new RuntimeException("这是一个运行时异常");
    }

    private void netWorkTest() {
        ThreadUtils.get().executeIO(() -> {
            try {
                ClientUtil.get().GET("https://www.baidu.com")
                        .request(new ClientInterface.OnCallListener() {

                            @Override
                            public void onStart(HttpURLConnection connection) {
                                LogUtil.d(TAG, "onResponse: " + connection);
                            }

                            @Override
                            public void onSuccess(HttpURLConnection connection, int responseCode, InputStream stream) {
                                String s = ClientUtil.getStringFromInputStream(stream);
                                LogUtil.d(TAG, "onSuccess: " + s);
                                connection.disconnect();
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                LogUtil.e(TAG, "onError: ", throwable);
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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

    private void netWorkStateTest() {
        LogUtil.d(TAG, "netWorkStateTest: isNetworkMetered " + PhoneStatusUtil.get().isNetworkNotMetered());
    }
}