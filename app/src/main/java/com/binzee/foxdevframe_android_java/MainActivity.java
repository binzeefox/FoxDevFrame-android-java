package com.binzee.foxdevframe_android_java;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.hardware.camera2.CameraDevice;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.binzee.foxdevframe.ui.FoxActivity;
import com.binzee.foxdevframe.ui.tools.launcher.Launcher;
import com.binzee.foxdevframe.ui.tools.popup.ToastUtil;
import com.binzee.foxdevframe.ui.tools.popup.dialog.SystemDialogHelper;
import com.binzee.foxdevframe.ui.tools.requests.ActivityRequester;
import com.binzee.foxdevframe.utils.LogUtil;
import com.binzee.foxdevframe.utils.ThreadUtils;
import com.binzee.foxdevframe.utils.device.resource.ScopedStorageUtil;
import com.binzee.foxdevframe.utils.http.ClientInterface;
import com.binzee.foxdevframe.utils.http.ClientUtil;
import com.binzee.foxdevframe.utils.permission.PermissionUtil;
import com.binzee.foxdevframe.utils.device.DeviceStatusUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
                ToastUtil.toast("再次点击关闭").setDuration(Toast.LENGTH_LONG).showNow();
            }

            @Override
            public void onSecondPress() {
                superOnBackPressed();
            }
        }, 2000);
    }

    private void test() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ThreadUtils.get().executeIO(() -> {
                Log.d(TAG, "test: start");
                try {
                    Uri uri = ScopedStorageUtil.get().openFilePicker(getSupportFragmentManager()).get();
                    Log.d(TAG, "test: uri = " + uri);
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, "test: ", e);
                }
            });
        } else ToastUtil.toast("SDK版本过低").showNow();
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
            SystemDialogHelper helper = new SystemDialogHelper();
            helper.showWifiSetting();
//                .showVolumeSetting();
//                .showNFCSetting();
//                .showInternetSetting();
        }
    }

    private void netWorkStateTest() {
        LogUtil.d(TAG, "netWorkStateTest: isNetworkMetered " + DeviceStatusUtil.get().isNetworkNotMetered());
    }
}