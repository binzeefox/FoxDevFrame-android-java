package com.binzee.foxdevframe;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.binzee.foxdevframe.utils.LogUtil;


/**
 * Activity生命周期监听器
 *
 * @author tong.xw
 * 2021/01/18 10:56
 */
public class FoxActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        log(activity, "onCreate");
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        log(activity, "onStarted");
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        log(activity, "onResumed");
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        log(activity, "onPaused");
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        log(activity, "onStopped");
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        log(activity, "onSaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        log(activity, "onDestroyed");
    }

    /**
     * 打印日志
     */
    protected void log(Activity activity, String status) {
        LogUtil.tag("FoxLifecycleCallback")
                .message(status + ": " + activity.getClass().getName())
                .v();
    }
}
