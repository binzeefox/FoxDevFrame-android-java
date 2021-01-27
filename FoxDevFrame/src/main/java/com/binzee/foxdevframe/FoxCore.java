package com.binzee.foxdevframe;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.binzee.foxdevframe.utils.LogUtil;

import java.util.Locale;
import java.util.Stack;

/**
 * 核心类<p>
 * <p>
 * - 提供静态Activity单例<p>
 * - 提供应用信息<p>
 * - 注册LifeCycle
 *
 * @author tong.xw
 * 2021/01/18 10:38
 */
public class FoxCore {
    private static final String TAG = "FoxCore";

    // 配置
    private final FoxConfigs configs = new FoxConfigs();

    // ApplicationContext，全局获取
    private Context appContext;

    // 返回栈
    private final ActivityStack mBackStack = new ActivityStack();

    // 私有化构造器
    private FoxCore() {
        // private constructor
    }

    /**
     * 获取单例
     */
    @NonNull
    private static FoxCore get() {
        FoxCore core = Holder.instance;
        if (core.appContext == null)
            throw new UnInitializedException();
        return core;
    }

    /**
     * 初始化
     */
    public static FoxCore init(Context ctx) {
        Holder.instance.appContext = ctx.getApplicationContext();
        Holder.instance.registerActivityCallback();

        // 本地化多语言设置
        String languageTag = Holder.instance.configs.readLanguageTag();
        Locale locale = Locale.forLanguageTag(languageTag);
        get().setLocale(locale, false);
        return get();
    }

    /**
     * 设置语言
     */
    public static void setLocal(@NonNull Locale local) {
        get().setLocale(local, true);
    }

    /**
     * 获取ApplicationContext
     */
    public static Context getApplicationContext() {
        return get().appContext;
    }

    /**
     * 获取模拟返回栈
     */
    public static ActivityStack getSimulatedBackStack() {
        return get().mBackStack;
    }

    /**
     * 获取资源
     */
    public static Resources getResources() {
        return getApplicationContext().getResources();
    }

    /**
     * 获取包名
     */
    public static String getPackageName() {
        return getApplicationContext().getPackageName();
    }

    /**
     * 获取PackageManager
     */
    @NonNull
    public static PackageManager getPackageManager() {
        return getApplicationContext().getPackageManager();
    }

    /**
     * 获取该App包信息
     */
    @NonNull
    public static PackageInfo getPackageInfo() throws PackageManager.NameNotFoundException {
        return getPackageManager().getPackageInfo(getPackageName(), 0);
    }

    /**
     * 获取版本名
     */
    @NonNull
    public static String getVersionName() {
        try {
            return getPackageInfo().versionName;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.tag(TAG).message("获取版本名失败").throwable(e).e();
            return "";
        }
    }

    /**
     * 获取版本号，兼容Android P
     *
     * @return 若获取失败则返回 -1
     */
    public static long getVersionCode() {
        try {
            PackageInfo info = getPackageInfo();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return info.getLongVersionCode();
            } else return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.tag(TAG).message("获取版本号失败").throwable(e).e();
            return -1;
        }
    }

    /**
     * 设置语言
     */
    public void setLocale(@NonNull Locale locale, boolean localize) {
        Resources r = FoxCore.getResources();
        DisplayMetrics m = r.getDisplayMetrics();
        Configuration c = r.getConfiguration();

        c.setLocale(locale);
        r.updateConfiguration(c, m);
        if (localize) configs.writeLanguageTag(locale.toLanguageTag());
    }

    ///////////////////////////////////////////////////////////////////////////
    // 私有方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 注册Activity回调
     */
    private void registerActivityCallback() {
        ((Application) getApplicationContext())
                .registerActivityLifecycleCallbacks(new FoxActivityLifecycleCallback() {
                    @Override
                    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                        super.onActivityCreated(activity, savedInstanceState);
                        // 入栈
                        mBackStack.push(activity);
                    }

                    @Override
                    public void onActivityResumed(@NonNull Activity activity) {
                        super.onActivityResumed(activity);
                        // 状态可见，转入栈顶
                        mBackStack.moveToTop(activity);
                    }

                    @Override
                    public void onActivityDestroyed(@NonNull Activity activity) {
                        super.onActivityDestroyed(activity);
                        // 回收
                        mBackStack.superRemove(activity);
                    }
                });
    }

    ///////////////////////////////////////////////////////////////////////////
    // 内部类
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 返回栈
     *
     * @author 狐彻 2020/10/21 9:19
     */
    public static class ActivityStack extends Stack<Activity> {
        @Override
        public synchronized Activity pop() {
            Activity activity = super.pop();
            activity.finish();
            return activity;
        }

        @Override
        public synchronized Activity remove(int index) {
            Activity activity = super.remove(index);
            activity.finish();
            return activity;
        }

        @Override
        public boolean remove(@Nullable Object o) {
            if (o instanceof Activity) ((Activity) o).finish();
            return super.remove(o);
        }

        ///////////////////////////////////////////////////////////////////////////
        // 私有方法
        ///////////////////////////////////////////////////////////////////////////

        /**
         * 代理父类移除方法
         *
         * @author 狐彻 2020/10/21 9:23
         */
        private void superRemove(@NonNull Object o) {
            super.remove(o);
        }

        /**
         * 把站内某值移至栈顶
         *
         * @author 狐彻 2020/10/21 9:20
         */
        private void moveToTop(Activity o) {
            super.remove(o);
            push(o);
        }
    }

    /**
     * 单例容器
     *
     * @author 狐彻 2020/10/21 8:16
     */
    private static class Holder {
        private static final FoxCore instance = new FoxCore();
    }

    /**
     * 未初始化异常
     *
     * @author 狐彻 2020/10/21 8:21
     */
    private static class UnInitializedException extends RuntimeException {
        private UnInitializedException() {
            super("未初始化，请调用 FoxCore.init(Context) 进行初始化");
        }
    }
}
