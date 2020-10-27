package com.binzee.foxdevframe.utils;

// 线程池基础
//
// corePoolSize: 核心线程数，默认情况下会在线程池中一直存活
// maximumPoolSize: 最大线程数，当活跃线程到达该数目时，后续线程进入队列等待
// keepAliveTime: 非核心线程闲置超时，超时后，闲置的非核心线程将被回收
// workQueue: 任务队列，储存线程
//
// FixedThreadPool: 固定线程数，只有核心线程
// CachedThreadPool: 非固定线程数，只有非核心线程
// ScheduledThreadPool: 核心线程数固定，非核心线程数无限制，常用于执行定时任务和又周期性的任务
// SingleThreadPool: 只有一个核心线程，确保所有任务都在统一线程按顺序执行


import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Thread工具
 *
 * @author 狐彻
 * 2020/10/27 9:15
 */
public class ThreadUtils implements Closeable {
    private static ThreadUtils sInstance;   //单例
    private volatile ExecutorService mIOExecutor;    //IO线程池
    private volatile ExecutorService mComputationExecutor;   //计算线程池
    private final Map<String, ExecutorService> mOtherExecutorsMap
            = new ConcurrentHashMap<>();  //其它线程池

    /**
     * 单例获取
     *
     * @author 狐彻 2020/10/27 9:24
     */
    public static ThreadUtils get() {
        if (sInstance == null) {
            synchronized (ThreadUtils.class) {
                if (sInstance == null)
                    sInstance = new ThreadUtils();
                return sInstance;
            }
        } else return sInstance;
    }

    /**
     * 主线程运行
     *
     * @author 狐彻 2020/10/27 9:28
     */
    public static void runOnUiThread(Runnable runnable) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
    }


    @Override
    public void close() throws IOException {
        synchronized (ThreadUtils.class) {
            shutdownExecutor(mIOExecutor);
            shutdownExecutor(mComputationExecutor);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                mOtherExecutorsMap.values().forEach(this::shutdownExecutor);
            else for (ExecutorService executor : mOtherExecutorsMap.values())
                shutdownExecutor(executor);
            sInstance = null;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 业务方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 在IO线程池内工作
     *
     * @author 狐彻 2020/10/27 9:45
     */
    public void executeIO(Runnable work) {
        if (mIOExecutor == null) initIOExecutor();
        mIOExecutor.execute(work);
    }

    /**
     * IO线程加载数据
     *
     * @author 狐彻 2020/10/27 17:10
     */
    public <T> void invokeIO(Callable<T> callable, OnFutureResultListener<T> listener) {
        executeIO(() -> {
            try {
                T result = mIOExecutor.submit(callable).get();
                listener.onResult(result);
            } catch (ExecutionException | InterruptedException e) {
                listener.onError(e);
            } finally {
                listener.onComplete();
            }
        });
    }

    /**
     * 在计算线程池内工作
     *
     * @author 狐彻 2020/10/27 9:46
     */
    public void executeComputation(Runnable work) {
        if (mComputationExecutor == null) initComputationExecutor();
        mComputationExecutor.execute(work);
    }

    /**
     * Computation线程加载数据
     *
     * @author 狐彻 2020/10/27 17:10
     */
    public <T> void invokeComputation(Callable<T> callable, OnFutureResultListener<T> listener) {
        executeComputation(() -> {
            try {
                T result = mComputationExecutor.submit(callable).get();
                listener.onResult(result);
            } catch (ExecutionException | InterruptedException e) {
                listener.onError(e);
            } finally {
                listener.onComplete();
            }
        });
    }


    /**
     * 在新线程池工作
     *
     * @param defaultExecutor 默认线程池，若tag下无线程池则使用该线程池。若为空则默认为CachedThreadPool
     * @author 狐彻 2020/10/27 9:47
     */
    public void executeOther(String tag, Runnable runnable, ExecutorService defaultExecutor) {
        ExecutorService service = getOtherExecutor(tag, defaultExecutor);
        service.execute(runnable);
    }

    /**
     * 在新线程池工作
     *
     * @author 狐彻 2020/10/27 9:47
     */
    public void executeOther(String tag, Runnable runnable) {
        executeOther(tag, runnable, null);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 私有方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 初始化IO线程池
     *
     * @author 狐彻 2020/10/27 9:36
     */
    protected void initIOExecutor() {
        mIOExecutor = Executors.newCachedThreadPool();  //选用只有非核心线程的线程池
    }

    /**
     * 初始化计算线程池
     *
     * @author 狐彻 2020/10/27 9:36
     */
    protected void initComputationExecutor() {
        mComputationExecutor = Executors.newCachedThreadPool();  //选用只有非核心线程的线程池
    }

    /**
     * 获取/添加新线程
     *
     * @param tag 标签
     * @param defaultExecutor 若该标签下没有线程池实例，则以此创建新线程池。若为空则用CachedThreadPool
     * @author 狐彻 2020/10/27 9:41
     */
    @NonNull
    protected ExecutorService getOtherExecutor(String tag, ExecutorService defaultExecutor) {
        ExecutorService executor = mOtherExecutorsMap.get(tag);
        if (executor != null) return executor;
        if (defaultExecutor == null) executor = Executors.newCachedThreadPool();
        else executor = defaultExecutor;
        mOtherExecutorsMap.put(tag, executor);
        return executor;
    }

    /**
     * 关闭线程池
     *
     * @author 狐彻 2020/10/27 9:32
     */
    private void shutdownExecutor(ExecutorService executor) {
        if (executor != null && !executor.isShutdown())
            executor.shutdownNow();
    }

    ///////////////////////////////////////////////////////////////////////////
    // 接口
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Invoke方法回调
     *
     * @author 狐彻 2020/10/27 17:06
     */
    public interface OnFutureResultListener<T> {
        void onResult(T result);
        void onError(Throwable throwable);
        void onComplete();
    }
}
