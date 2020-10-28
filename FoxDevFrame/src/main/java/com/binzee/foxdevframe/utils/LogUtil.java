package com.binzee.foxdevframe.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Printer;


/**
 * 日志工具类
 * <p>
 * 手动更改{@link #CURRENT_CLASS} 的值来改变当前的打印等级
 *
 * @author binze
 * 2019/11/19 9:31
 */
public class LogUtil {
    public static final int CLASS_NONE = -1;   //CURRENT_CLASS为此时，不打印Log
    public static final int CLASS_E = 0;   //CURRENT_CLASS为此时，只打印E
    public static final int CLASS_W = 1;   //CURRENT_CLASS为此时，只打印E和W
    public static final int CLASS_D = 2;   //CURRENT_CLASS为此时，只打印E,W和D
    public static final int CLASS_I = 3;   //CURRENT_CLASS为此时，只打印E,W,D和I
    public static final int CLASS_V = 4;   //CURRENT_CLASS为此时，全部打印

    public static int CURRENT_CLASS = CLASS_V;   //打印指示器

    //V
    public static void v(CharSequence tag, CharSequence text) {
        if (CURRENT_CLASS >= CLASS_V)
            Log.v(tag.toString(), text.toString());
    }

    public static void v(CharSequence tag, CharSequence text, Throwable throwable) {
        if (CURRENT_CLASS >= CLASS_V)
            Log.v(tag.toString(), text.toString(), throwable);
    }

    //D
    public static void d(CharSequence tag, CharSequence text) {
        if (CURRENT_CLASS >= CLASS_D)
            Log.d(tag.toString(), text.toString());
    }

    public static void d(CharSequence tag, CharSequence text, Throwable throwable) {
        if (CURRENT_CLASS >= CLASS_D)
            Log.d(tag.toString(), text.toString(), throwable);
    }

    //I
    public static void i(CharSequence tag, CharSequence text) {
        if (CURRENT_CLASS >= CLASS_I)
            Log.i(tag.toString(), text.toString());
    }

    public static void i(CharSequence tag, CharSequence text, Throwable throwable) {
        if (CURRENT_CLASS >= CLASS_I)
            Log.i(tag.toString(), text.toString(), throwable);
    }

    //W
    public static void w(CharSequence tag, CharSequence text) {
        if (CURRENT_CLASS >= CLASS_W)
            Log.w(tag.toString(), text.toString());
    }

    public static void w(CharSequence tag, CharSequence text, Throwable throwable) {
        if (CURRENT_CLASS >= CLASS_W)
            Log.w(tag.toString(), text.toString(), throwable);
    }

    //E
    public static void e(CharSequence tag, CharSequence text) {
        if (CURRENT_CLASS >= CLASS_E)
            Log.e(tag.toString(), text.toString());
    }

    public static void e(CharSequence tag, CharSequence text, Throwable throwable) {
        if (CURRENT_CLASS >= CLASS_E)
            Log.e(tag.toString(), text.toString(), throwable);
    }

    /**
     * 设置全局异常日志监控，当主线程执行过长时将会打印错误
     *
     * @author 狐彻 2020/10/27 22:52
     */
    public static void setGlobalExceptionCapture(OnExceptionCapturedListener listener) {
        final String TAG = "FoxGlobalWatcher";
        Looper.getMainLooper().setMessageLogging(new Printer() {
            private long startWorkTimeMillis = 0L;

            @Override
            public void println(String x) {
                if (x.startsWith(">>>>> Dispatching to Handler")) {
                    startWorkTimeMillis = System.currentTimeMillis();
                } else if (x.startsWith("<<<<< Finished to Handler")) {
                    long duration = System.currentTimeMillis() - startWorkTimeMillis;
                    if (duration > 150)
                        w(TAG, "主线程执行耗时过长 -> " + duration + "毫秒\n" + x);
                    if (duration > 1000)
                        e(TAG, "主线程执行耗时过长 -> " + duration + "毫秒\n" + x);
                }
            }
        });

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    Looper.loop();
                } catch (Exception e) {
                    // 主线程崩溃
                    e(TAG, "主线程异常!!", e);
                    listener.onCapture(e);
                }
            }
        });
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
                e(TAG, "异步线程异常!!", e);
                listener.onCapture(e);
        });
    }

    /**
     * 异常捕捉回调
     *
     * @author 狐彻 2020/10/28 8:41
     */
    public interface OnExceptionCapturedListener {
        void onCapture(Throwable e);
    }
}
