package com.binzee.foxdevframe.ui.tools;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import java.util.Hashtable;

/**
 * 视图工具
 *
 * @author 狐彻
 * 2020/10/24 10:06
 */
public class ViewUtil {
    //目标View
    @NonNull
    private final View target;
    //工作Handler
    private static WorkHandler workHandler;

    /**
     * 构造器
     *
     * @author 狐彻 2020/10/24 10:08
     */
    protected ViewUtil(@NonNull View view) {
        this.target = view;
        if (workHandler == null) initWorkHandler();
    }

    /**
     * 静态获取
     *
     * @author 狐彻 2020/10/24 10:09
     */
    public static ViewUtil with(@NonNull View view) {
        return new ViewUtil(view);
    }

    /**
     * 通过activity和targetID获取
     *
     * @author 狐彻 2020/10/24 10:38
     */
    public static ViewUtil with(@NonNull Activity activity, @IdRes int targetId) {
        View view = activity.findViewById(targetId);
        if (view == null) throw new TargetIsNullException();
        return with(view);
    }

    /**
     * 初始化工作Handler
     *
     * @author 狐彻 2020/10/24 10:12
     */
    private void initWorkHandler() {
        HandlerThread thread = new HandlerThread("view_util_handler");
        thread.start();
        workHandler = new WorkHandler(thread.getLooper());
    }

    ///////////////////////////////////////////////////////////////////////////
    // 工具方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 返回目标View
     *
     * @author 狐彻 2020/10/24 10:31
     */
    @NonNull
    public View getTarget() {
        return target;
    }

    /**
     * 将当前目标转为其子节点
     *
     * @author 狐彻 2020/10/24 10:30
     */
    public ViewUtil switchToChild(@IdRes int viewId) {
        View view = target.findViewById(viewId);
        if (view == null) throw new TargetIsNullException();
        return with(view);
    }

    /**
     * 添加防抖点击监听
     *
     * @author 狐彻 2020/10/24 10:07
     */
    public void setOnClickListenerDebounce(View.OnClickListener listener, long skip) {
        long timeStamp = System.currentTimeMillis();
        target.setOnClickListener(v -> {
            if (workHandler.debounceFlag.get(timeStamp)) return;
            Message message = new Message();
            message.what = WorkHandler.DEBOUNCE_START;
            message.obj = new long[]{timeStamp, skip};
            workHandler.sendMessage(message);
            listener.onClick(v);
        });
    }

    ///////////////////////////////////////////////////////////////////////////
    // TextView及其子类相关
    ///////////////////////////////////////////////////////////////////////////

    public void setError(CharSequence error) {
        setError(error, null);
    }

    /**
     * 设置错误状态
     *
     * @author 狐彻 2020/10/24 10:42
     */
    public void setError(CharSequence error, Drawable errorIcon) {
        if (!isTextView()) return;
        if (errorIcon != null)
            ((TextView) target).setError(error, errorIcon);
        else ((TextView) target).setError(error);
    }

    /**
     * 获取文字
     *
     * @return 若非TextView子类则返回空字符串
     * @author 狐彻 2020/10/24 10:41
     */
    public CharSequence getText() {
        if (!isTextView()) return "";
        return ((TextView) target).getText();
    }

    ///////////////////////////////////////////////////////////////////////////
    // 类型判断
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 判断是否是TextView子类
     *
     * @author 狐彻 2020/10/24 10:44
     */
    public boolean isTextView() {
        return target instanceof TextView;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 内部类
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 工作Handler
     *
     * @author 狐彻 2020/10/24 10:15
     */
    private static class WorkHandler extends Handler {
        static final int DEBOUNCE_START = 0;

        //防抖标志，key为点击时间，flag为是否应跳过
        private final Hashtable<Long, Boolean> debounceFlag = new Hashtable<>();

        public WorkHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case DEBOUNCE_START:    //开始防抖
                    startDebounce((long[]) msg.obj);
                    break;
                default:
                    break;
            }
        }

        /**
         * 开始防抖标记
         *
         * @param args [首次点击时间, 防抖间隔时间毫秒]
         * @author 狐彻 2020/10/24 10:21
         */
        private void startDebounce(long[] args) {
            debounceFlag.put(args[0], true);
            postDelayed(() -> debounceFlag.put(args[0], false), args[1]);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 内部类
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 空目标异常
     *
     * @author 狐彻 2020/10/24 10:36
     */
    private static class TargetIsNullException extends RuntimeException {
        TargetIsNullException() {
            super("目标View为空");
        }
    }
}
