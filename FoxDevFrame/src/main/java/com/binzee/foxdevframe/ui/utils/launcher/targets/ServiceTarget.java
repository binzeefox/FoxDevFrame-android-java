package com.binzee.foxdevframe.ui.utils.launcher.targets;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.binzee.foxdevframe.ui.utils.launcher.ILauncherTarget;

import java.util.concurrent.Executor;

/**
 * TODO 服务目标
 *
 * @author tong.xw
 * 2021/01/19 10:32
 */
public class ServiceTarget implements ILauncherTarget {

    @NonNull
    private final Context ctx;  //实现上下文

    @Nullable
    private Intent intent;    //跳转Intent
    private boolean isBind;   //是否bind

    //下列为Bind参数
    private int flags;
    private Executor executor;
    private ServiceConnection conn;

    /**
     * 构造器
     *
     * @param context 上下文
     * @param intent 目标意图
     * @param isBind 是否绑定服务
     *
     */
    public ServiceTarget(@NonNull Context context, @NonNull Intent intent, boolean isBind
            , int flags, @Nullable Executor executor, @Nullable ServiceConnection conn) {
        ctx = context;
        this.intent = intent;
        this.isBind = isBind;
    }


    @Override
    public ILauncherTarget intentInterceptor(@NonNull IntentInterceptor interceptor) {
        return null;
    }

    @Override
    public void commit() {

    }
}
