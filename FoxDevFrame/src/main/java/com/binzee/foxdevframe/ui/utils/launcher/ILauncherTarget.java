package com.binzee.foxdevframe.ui.utils.launcher;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 跳转目标接口
 *
 * @author tong.xw
 * 2021/01/19 10:17
 */
public interface ILauncherTarget {

    /**
     * intent拦截器，用于在跳转前对intent进行最终操作，比如填入Extra等
     *
     * @param interceptor 拦截器
     */
    ILauncherTarget intentInterceptor(@NonNull IntentInterceptor interceptor);

    /**
     * 开始跳转
     */
    void commit();

    /**
     * 拦截器
     */
    interface IntentInterceptor {

        /**
         * 拦截回调，返回的Intent将用于跳转
         *
         * @return 最终用于跳转的Intent，若返回null则终止跳转
         */
        @Nullable
        Intent onIntercept(@Nullable Intent intent);
    }
}
