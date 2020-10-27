package com.binzee.foxdevframe.ui.tools.launcher.target;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

/**
 * 目标为Activity的跳转
 *
 * P.S. 请求跳转请使用Requester.class
 * @author 狐彻
 * 2020/10/22 9:11
 */
public class ActivityTarget implements LauncherTarget {

    @NonNull
    private final Intent intent;  //跳转Intent
    @NonNull
    private final Context ctx;    //承载的ctx
    private Bundle options = null;  //作为startActivity()的第二个参数

    /**
     * 构造器
     *
     * @param context   承载的Context
     * @param intent    目标Intent
     * @author 狐彻 2020/10/22 9:28
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
    public ActivityTarget(@NonNull Context context, @NonNull Intent intent) {
        ctx = context;
        this.intent = intent;
    }

    @Override
    public LauncherTarget putExtra(String key, @NonNull Bundle extra) {
        intent.putExtra(key, extra);
        return this;
    }

    @Override
    public void commit() {
        ctx.startActivity(intent, options);
    }


    /**
     * 设置startActivity()的第二个参数
     *
     * @author 狐彻 2020/10/22 9:37
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
    public void setOptions(Bundle options) {
        this.options = options;
    }
}
