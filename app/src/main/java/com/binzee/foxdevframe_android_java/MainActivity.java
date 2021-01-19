package com.binzee.foxdevframe_android_java;

import android.Manifest;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.binzee.foxdevframe.ui.FoxActivity;
import com.binzee.foxdevframe.ui.utils.NoticeUtil;
import com.binzee.foxdevframe.ui.utils.launcher.Launcher;
import com.binzee.foxdevframe.utils.LogUtil;
import com.binzee.foxdevframe.utils.ThreadUtils;
import com.binzee.foxdevframe.utils.device.resource.ScopedStorageUtil;
import com.binzee.foxdevframe.utils.net.http.ClientInterface;
import com.binzee.foxdevframe.utils.net.http.ClientUtil;
import com.binzee.foxdevframe.utils.permission.PermissionUtil;
import com.binzee.foxdevframe.utils.device.DeviceStatusUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutionException;

public class MainActivity extends FoxActivity {
    private static final String TAG = "MainActivity";
    private final LogUtil log = LogUtil.tag(TAG);

    @Override
    public int getContentViewResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate() {
//        setFullScreen();
        LogUtil.enableANRLog();
        LogUtil.setGlobalExceptionCapture((isMainThread, t, e) ->
                log.message("onCreate: thread = " + t.getName()).throwable(e).e());
        findViewById(R.id.confirm_button).setOnClickListener(v -> test());
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
        } else NoticeUtil.toast("SDK版本过低").showNow();
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
        new Launcher(this)
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
}