package com.binzee.foxdevframe.utils.phone.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;

import com.binzee.foxdevframe.FoxCore;

/**
 * 低功耗蓝牙工具
 *
 * 需要权限：
 * Manifest.permission.BLUETOOTH
 * Manifest.permission.BLUETOOTH_ADMIN
 * 扫描需要权限：
 * Manifest.permission.ACCESS_COARSE_LOCATION
 *
 * @see com.binzee.foxdevframe.utils.phone.bluetooth.BleInterface
 * @author 狐彻
 * 2020/11/10 8:40
 */
public class BleHelper implements BleInterface {
    private static final String TAG = "BleHelper";

    private final BluetoothManager mManager;
    private final BluetoothAdapter mAdapter;

    /**
     * 获取单例
     *
     * @author 狐彻 2020/11/10 9:03
     */
    public static BleHelper get() {
        return Holder.instance;
    }

    private BleHelper() {
        if (!isSupportBle()) throw new RuntimeException("Ble not supported!!");
        mManager = (BluetoothManager) FoxCore.getApplication()
                .getSystemService(Context.BLUETOOTH_SERVICE);
        mAdapter = mManager.getAdapter();
    }

    @Override
    public boolean isSupportBle() {
        return FoxCore.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    @Override
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public boolean isEnableBle() {
        return mAdapter != null && mAdapter.isEnabled();
    }

    @Override
    public void openBle(@Nullable Context context) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        Activity activity = FoxCore.getTopActivity();

        // 若输入的Context不为空则用该Context
        if (context != null) context.startActivity(intent);
        // 若为空则用栈顶Activity
        else if (activity != null) activity.startActivity(intent);
        // 若栈顶Activity为空则用Application
        else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            FoxCore.getApplication().startActivity(intent);
        }
    }

    @Override
    public BleScanner getScanner(ScanCallback callback) {
        return new FoxBleScanner(mAdapter, callback);
    }

    @Override
    public BleDevice getToolDevice(BluetoothDevice device) {
        return new FoxBleDevice(device);
    }


    ///////////////////////////////////////////////////////////////////////////
    // 单例容器
    ///////////////////////////////////////////////////////////////////////////

    private static class Holder {
        private static final BleHelper instance = new BleHelper();
    }
}
