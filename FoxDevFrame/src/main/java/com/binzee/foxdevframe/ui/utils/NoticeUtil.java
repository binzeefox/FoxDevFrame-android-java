package com.binzee.foxdevframe.ui.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.binzee.foxdevframe.FoxCore;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

/**
 * 提醒工具
 *
 * @author tong.xw
 * 2021/01/18 15:31
 */
@SuppressLint("ShowToast")
public class NoticeUtil {
    private Toast mToast;
    private Snackbar mSnackbar;

    // 私有化构造器
    private NoticeUtil() {
        //private constructor
    }

    public static NoticeUtil get() {
        return PopupHelperHolder.instance;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Toast
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 自定义toast
     *
     * @author tong.xw 2020/12/25 14:22
     */
    public static Operation toast(@NonNull Toast toast) {
        return get().createOperation(toast);
    }

    /**
     * 自定义toast
     *
     * @author tong.xw 2020/12/25 14:22
     */
    public static Operation toast(String text) {
        return get().createOperation(Toast.makeText(get().getContext(), text, Toast.LENGTH_SHORT));
    }

    /**
     * 自定义toast
     *
     * @author tong.xw 2020/12/25 14:22
     */
    public static Operation toast(@StringRes int textRes) {
        return get().createOperation(Toast.makeText(get().getContext(), textRes, Toast.LENGTH_SHORT));
    }

    /**
     * 无内容空Toast
     */
    public static Operation toast() {
        return toast(Toast.makeText(get().getContext(), "", Toast.LENGTH_SHORT));
    }

    ///////////////////////////////////////////////////////////////////////////
    // Snackbar
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 自定义Snackbar
     */
    public static Operation snackbar(@NonNull Snackbar snackbar) {
        return get().createOperation(snackbar);
    }

    /**
     * 无内容空Snackbar
     */
    public static Operation snackbar(@NonNull View view) {
        return snackbar(Snackbar.make(view, "", Snackbar.LENGTH_SHORT));
    }

    /**
     * 带Action的Snackbar
     */
    public static Operation snackbar(@NonNull View view, @NonNull String actionText, @Nullable View.OnClickListener listener) {
        Snackbar snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT);
        snackbar.setAction(actionText, listener);
        return snackbar(snackbar);
    }

    /**
     * 带Action的Snackbar
     */
    public static Operation snackbar(@NonNull View view, @StringRes int resourceId, @Nullable View.OnClickListener listener) {
        Snackbar snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT);
        snackbar.setAction(resourceId, listener);
        return snackbar(snackbar);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 私有方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 创建Operation
     */
    private Operation createOperation(@NonNull Toast toast) {
        return new ToastOperation(toast);
    }

    /**
     * 创建Operation
     */
    private Operation createOperation(@NonNull Snackbar snackbar) {
        return new SnackbarOperation(snackbar);
    }


    /**
     * 获取上下文实例
     *
     * @return 若存在栈顶Activity优先使用栈顶Activity，否则使用ApplicationContext
     * @author 狐彻 2020/10/21 9:56
     */
    private Context getContext() {
        if (FoxCore.getSimulatedBackStack().peek() != null)
            return FoxCore.getSimulatedBackStack().peek();
        else return FoxCore.getApplicationContext();
    }

    ///////////////////////////////////////////////////////////////////////////
    // 内部类
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 操作
     *
     * @author tong.xw 2020/12/25 11:20
     */
    public class ToastOperation implements Operation {
        private final Toast toast;

        private ToastOperation(@NonNull Toast toast) {
            this.toast = toast;
        }

        public Operation setDuration(int duration) {
            toast.setDuration(duration);
            return this;
        }

        @Override
        public Operation setMessage(@NonNull String message) {
            toast.setText(message);
            return this;
        }

        @Override
        public Operation setMessage(int resourceId) {
            toast.setText(resourceId);
            return this;
        }

        /**
         * 当上一个Toast结束时显示
         *
         * @author tong.xw 2020/12/25 11:26
         */
        public void showOnLastHide() {
            if (mToast == null) {
                mToast = toast;
                mToast.show();
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                mToast.addCallback(new Toast.Callback() {
                    @Override
                    public void onToastHidden() {
                        mToast = toast;
                        mToast.show();
                    }
                });
            } else {
                mToast = toast;
                mToast.show();
            }
        }

        /**
         * 取消上一个toast立即显示
         *
         * @author tong.xw 2020/12/25 11:26
         */
        public void showNow() {
            if (mToast == null) {
                mToast = toast;
                mToast.show();
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                mToast.addCallback(new Toast.Callback() {
                    @Override
                    public void onToastHidden() {
                        mToast = toast;
                        mToast.show();
                    }
                });
                mToast.cancel();
            } else {
                mToast.cancel();
                mToast = toast;
                mToast.show();
            }
        }
    }

    /**
     * 操作
     *
     * @author tong.xw 2020/12/25 11:20
     */
    public class SnackbarOperation implements Operation {
        private final Snackbar snackbar;

        private SnackbarOperation(@NonNull Snackbar snackbar) {
            this.snackbar = snackbar;
        }

        public Operation setDuration(int duration) {
            snackbar.setDuration(duration);
            return this;
        }

        @Override
        public Operation setMessage(@NonNull String message) {
            snackbar.setText(message);
            return this;
        }

        @Override
        public Operation setMessage(int resourceId) {
            snackbar.setText(resourceId);
            return this;
        }

        /**
         * 当上一个Toast结束时显示
         *
         * @author tong.xw 2020/12/25 11:26
         */
        public void showOnLastHide() {
            if (mSnackbar == null) {
                mSnackbar = snackbar;
                mSnackbar.show();
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                mSnackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        mSnackbar = snackbar;
                        snackbar.show();
                    }
                });
            } else {
                mSnackbar = snackbar;
                snackbar.show();
            }
        }

        /**
         * 取消上一个toast立即显示
         *
         * @author tong.xw 2020/12/25 11:26
         */
        public void showNow() {
            if (mSnackbar == null) {
                mSnackbar = snackbar;
                snackbar.show();
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                mSnackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        mSnackbar = snackbar;
                        snackbar.show();
                    }
                });
                mSnackbar.dismiss();
            } else {
                mSnackbar.dismiss();
                mSnackbar = snackbar;
                snackbar.show();
            }
        }
    }

    /**
     * 单例容器
     *
     * @author tong.xw 2020/12/25 11:27
     */
    private static class PopupHelperHolder {
        private static final NoticeUtil instance = new NoticeUtil();
    }

    /**
     * 操作
     */
    public interface Operation {

        /**
         * 设置时长
         */
        Operation setDuration(int duration);

        /**
         * 设置内容
         */
        Operation setMessage(@NonNull String message);

        /**
         * 设置内容
         */
        Operation setMessage(@StringRes int resourceId);

        /**
         * 立即显示
         */
        void showNow();

        /**
         * 上一个消失后显示
         */
        void showOnLastHide();
    }
}
