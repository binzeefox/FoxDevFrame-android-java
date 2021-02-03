package com.binzee.foxdevframe_android_java;

import android.hardware.SensorManager;

import com.binzee.foxdevframe.ui.FoxActivity;
import com.binzee.foxdevframe.utils.LogUtil;
import com.binzee.foxdevframe.utils.device.sensors.SensorDevice;
import com.binzee.foxdevframe.utils.device.sensors.SensorListener;
import com.binzee.foxdevframe.utils.device.sensors.SensorUtil;

/**
 * 测试传感器工具类的Activity
 *
 * @author 狐彻
 * 2020/11/10 14:39
 */
public class SensorActivity extends FoxActivity {
    private static final String TAG = "SensorActivity";

    //传感器回调
    private final SensorListener.AccelerometerListener listener = new SensorListener.AccelerometerListener() {
        @Override
        public void onValueChanged(float x, float y, float z) {
//            LogUtil.d(TAG, "onValueChanged: x " + x);
//            LogUtil.d(TAG, "onValueChanged: y " + y);
//            LogUtil.d(TAG, "onValueChanged: z " + z);
        }
    };

    private SensorDevice sensor; //传感器包装类

    @Override
    protected void onCreate() {
        super.onCreate();
        sensor = SensorUtil.getSensor(SensorUtil.SensorType.ACCELEROMETER, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensor.connect(listener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensor.disconnect(listener);
    }
}
