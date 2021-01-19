package com.binzee.foxdevframe.ui.utils.launcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.binzee.foxdevframe.dev.FoxCore;
import com.binzee.foxdevframe.dev.ui.utils.launcher.targets.ActivityTarget;

/**
 * 页面跳转工具
 *
 * @author tong.xw
 * 2021/01/19 10:12
 */
public class Launcher {

    @NonNull
    private final Context ctx;

    public Launcher(@NonNull Context context) {
        ctx = context;
    }

    /**
     * 用返回栈栈顶Activity
     */
    public static Launcher byTopActivity() {
        return new Launcher(FoxCore.getSimulatedBackStack().peek());
    }

    /**
     * 用Application
     */
    public static Launcher byApplication() {
        return new Launcher(FoxCore.getApplicationContext());
    }

    ///////////////////////////////////////////////////////////////////////////
    // 参数方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 跳转系统页面工具
     */
    public SystemShortCutLauncher systemShortCuts() {
        return new SystemShortCutLauncher(this);
    }

    /**
     * 给定Intent跳转至Activity
     *
     * @param intent 意图
     */
    public ILauncherTarget getActivityTarget(@NonNull Intent intent) {
        return getActivityTarget(intent, null);
    }

    /**
     * 给定Intent跳转至Activity
     *
     * @param intent  意图
     * @param options startActivity()的第二个参数
     */
    public ILauncherTarget getActivityTarget(@NonNull Intent intent, @Nullable Bundle options) {
        return new ActivityTarget(ctx, intent, options);
    }

    /**
     * 跳转Activity
     *
     * @param cls 目标Activity
     */
    public ILauncherTarget getActivityTarget(@NonNull Class<? extends Activity> cls) {
        return getActivityTarget(cls, null);
    }

    /**
     * 跳转Activity
     *
     * @param cls 目标Activity
     * @param options startActivity()的第二个参数
     */
    public ILauncherTarget getActivityTarget(@NonNull Class<? extends Activity> cls, @Nullable Bundle options) {
        Intent intent = new Intent(ctx, cls);
        return new ActivityTarget(ctx, intent, options);
    }
}
