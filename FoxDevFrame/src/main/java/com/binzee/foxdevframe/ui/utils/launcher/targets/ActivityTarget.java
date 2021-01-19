package com.binzee.foxdevframe.ui.utils.launcher.targets;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.binzee.foxdevframe.dev.ui.utils.launcher.ILauncherTarget;

/**
 * 目标为Activity的跳转
 *
 * @author tong.xw
 * 2021/01/19 10:23
 */
public class ActivityTarget implements ILauncherTarget {

    @NonNull
    private final Context ctx;  //实现上下文

    @Nullable
    private Intent intent;    //跳转Intent
    private Bundle options = null;  //作为startActivity()的第二个参数


    /**
     * 构造器
     *
     * @param context 实现功能的上下文
     * @param intent  目标Intent
     */
    public ActivityTarget(@NonNull Context context, @NonNull Intent intent, @Nullable Bundle options) {
        ctx = context;
        this.intent = intent;
        this.options = options;
    }



    @Override
    public ILauncherTarget intentInterceptor(@NonNull IntentInterceptor interceptor) {
        intent = interceptor.onIntercept(intent);
        return this;
    }

    @Override
    public void commit() {
        if (intent == null) return;
        if (ctx instanceof Application)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent, options);
    }
}
