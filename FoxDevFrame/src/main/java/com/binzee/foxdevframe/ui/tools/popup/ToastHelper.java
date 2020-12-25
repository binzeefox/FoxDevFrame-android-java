package com.binzee.foxdevframe.ui.tools.popup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.binzee.foxdevframe.FoxCore;


/**
 * 提示工具
 *
 * @author 狐彻
 * 2020/10/21 9:43
 */
@SuppressLint("ShowToast")
public class ToastHelper {
    private static final String TAG = "ToastHelper";
    private Toast mToast;   //Toast实例

    // 私有化构造器
    // @author 狐彻 2020/10/21 9:56
    private ToastHelper() {
        //private constructor
    }

    /**
     * 静态获取
     *
     * @author 狐彻 2020/10/27 9:03
     */
    public static ToastHelper get() {
        return PopupHelperHolder.instance;
    }

    /**
     * 显示提示
     *
     * @author 狐彻 2020/10/21 10:10
     */
    public Operation toast(CharSequence text, int duration) {
        Toast toast = Toast.makeText(getContext(), text, duration);
        return new Operation(toast);
    }

    /**
     * 立即显示提示
     *
     * @author 狐彻 2020/10/21 10:09
     */
    public Operation toast(@StringRes int strId, int duration) {
        Toast toast = Toast.makeText(getContext(), strId, duration);
        return new Operation(toast);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 私有方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 获取上下文实例
     *
     * @return 若存在栈顶Activity优先使用栈顶Activity，否则使用ApplicationContext
     * @author 狐彻 2020/10/21 9:56
     */
    private Context getContext() {
        if (FoxCore.getTopActivity() != null)
            return FoxCore.getTopActivity();
        else return FoxCore.getApplication();
    }

    ///////////////////////////////////////////////////////////////////////////
    // 内部类
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 操作
     *
     * @author tong.xw 2020/12/25 11:20
     */
    public class Operation {
        private final Toast toast;

        private Operation(@NonNull Toast toast) {
            this.toast = toast;
        }

        /**
         * 当上一个Toast结束时显示
         *
         * @author tong.xw 2020/12/25 11:26
         */
        void showOnLastHide() {
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
        void showNow() {
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
     * 单例容器
     *
     * @author tong.xw 2020/12/25 11:27
     */
    private static class PopupHelperHolder {
        private static final ToastHelper instance = new ToastHelper();
    }
}
