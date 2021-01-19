package com.binzee.foxdevframe.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Printer;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 日志工具
 *
 * @author tong.xw
 * 2021/01/18 11:00
 */
public class LogUtil {
    public static final int CLASS_NONE = -1;   //CURRENT_CLASS为此时，不打印Log
    public static final int CLASS_E = 0;   //CURRENT_CLASS为此时，只打印E
    public static final int CLASS_W = 1;   //CURRENT_CLASS为此时，只打印E和W
    public static final int CLASS_D = 2;   //CURRENT_CLASS为此时，只打印E,W和D
    public static final int CLASS_I = 3;   //CURRENT_CLASS为此时，只打印E,W,D和I
    public static final int CLASS_V = 4;   //CURRENT_CLASS为此时，全部打印

    private static int CURRENT_CLASS = CLASS_V;   //打印指示器

    private final ExecutorService logExecutor = Executors.newSingleThreadExecutor();

    private static FoxLog firstLog = null;
    private static FoxLog lastLog = null;

    // 非静态全局变量 START
    @NonNull
    private final String tag;
    @NonNull
    private String message = "";
    private Throwable t = null;
    // 非静态全局变量 FINAL

    /**
     * 私有化构造器
     *
     * @param tag 标签
     */
    private LogUtil(@NonNull String tag) {
        this.tag = tag;
    }

    /**
     * 设置log级别
     *
     * @see LogUtil#CLASS_NONE
     * @see LogUtil#CLASS_E
     * @see LogUtil#CLASS_W
     * @see LogUtil#CLASS_D
     * @see LogUtil#CLASS_I
     * @see LogUtil#CLASS_V
     */
    public static void setLogFilter(int filterClass) {
        CURRENT_CLASS = filterClass;
    }

    /**
     * 开启Anr日志,当主线程执行过长时将会打印错误
     *
     * @author 狐彻 2020/11/06 14:17
     */
    public static void enableANRLog() {
        Looper.getMainLooper().setMessageLogging(new Printer() {
            private long startWorkTimeMillis = 0L;

            @Override
            public void println(String x) {
                if (x.startsWith(">>>>> Dispatching to Handler")) {
                    startWorkTimeMillis = System.currentTimeMillis();
                } else if (x.startsWith("<<<<< Finished to Handler")) {
                    LogUtil log = LogUtil.tag("ANRLogger");
                    long duration = System.currentTimeMillis() - startWorkTimeMillis;
                    if (duration > 1000)
                        log.message("主线程执行耗时过长 -> " + duration + "毫秒\n" + x).e();
                    else if (duration > 150)
                        log.message("主线程执行耗时过长 -> " + duration + "毫秒\n" + x).w();

                }
            }
        });
    }

    /**
     * 设置全局异常日志监控
     *
     * @author 狐彻 2020/10/27 22:52
     */
    public static void setGlobalExceptionCapture(OnExceptionCapturedListener listener) {
        LogUtil log = LogUtil.tag("GlobalExceptionCapture");
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    Looper.loop();
                } catch (Exception e) {
                    // 主线程崩溃
                    log.message("主线程异常!!").throwable(e).e();
                    if (listener != null)
                        listener.onCapture(true, Thread.currentThread(), e);
                }
            }
        });
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            log.message("异步线程异常!!").throwable(e).e();
            if (!Objects.equals(t, Looper.getMainLooper().getThread()) && listener != null)
                listener.onCapture(false, t, e);
        });
    }

    /**
     * 同步方法 ！！主线程警告！！ <p>
     * 获取利用该类打印的所有可见日志的字符串
     */
    public static String getLogRecordText() {
        synchronized (LogUtil.class) {
            FoxLog log = firstLog;
            StringBuilder sb = new StringBuilder();
            while (log != null) {
                sb.append(log.toString());
                log = log.next;
            }
            return sb.toString();
        }
    }

    /**
     * tag
     */
    public static LogUtil tag(@NonNull String tag) {
        return new LogUtil(tag);
    }

    /**
     * 日志内容
     */
    public LogUtil message(@NonNull String msg) {
        this.message = msg;
        return this;
    }

    /**
     * 异常捕获
     */
    public LogUtil throwable(Throwable throwable) {
        this.t = throwable;
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 打印方法
    ///////////////////////////////////////////////////////////////////////////

    // v
    public void v() {
        if (CURRENT_CLASS >= CLASS_V) {
            Log.v(tag, message, t);
            recordLog("V", tag, message, t);
        }
    }

    // d
    public void d() {
        if (CURRENT_CLASS >= CLASS_D) {
            Log.d(tag, message, t);
            recordLog("D", tag, message, t);
        }
    }

    // i
    public void i() {
        if (CURRENT_CLASS >= CLASS_I) {
            Log.i(tag, message, t);
            recordLog("I", tag, message, t);
        }
    }

    // w
    public void w() {
        if (CURRENT_CLASS >= CLASS_W) {
            Log.w(tag, message, t);
            recordLog("W", tag, message, t);
        }
    }

    // e
    public void e() {
        if (CURRENT_CLASS >= CLASS_E) {
            Log.e(tag, message, t);
            recordLog("E", tag, message, t);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 私有方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 记录本地化
     */
    private void recordLog(String level, String tag, String message, Throwable t) {
        // 读写锁在该方法的调用方法中实现
        logExecutor.execute(() -> {
            synchronized (LogUtil.class) {
                FoxLog log = new FoxLog(level, tag, message, t);
                if (firstLog == null) {
                    firstLog = log;
                    return;
                }
                if (lastLog == null) {
                    firstLog.next = log;
                    lastLog = log;
                    return;
                }
                lastLog.next = log;
                lastLog = log;
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////
    // 内部类
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 日志数据类
     *
     * @author 狐彻 2020/11/06 14:32
     */
    private static class FoxLog {
        final String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS", Locale.CHINA).format(new Date());
        final String level;
        final String tag;
        final String msg;
        final Throwable e;

        FoxLog next;

        FoxLog(String level, String tag, String msg, Throwable e) {
            this.level = level;
            this.tag = tag;
            this.msg = msg;
            this.e = e;
        }

        @NonNull
        @Override
        public String toString() {
            String log = timeStamp + " " + level + "/" + tag + ": " + msg;
            if (e != null) log += (". throw -> " + e.toString());
            log += "\n";
            return log;
        }
    }

    /**
     * 异常捕捉回调
     *
     * @author 狐彻 2020/10/28 8:41
     */
    public interface OnExceptionCapturedListener {
        void onCapture(boolean isMainThread, Thread t, Throwable e);
    }
}
