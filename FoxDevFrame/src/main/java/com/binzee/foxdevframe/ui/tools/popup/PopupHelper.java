package com.binzee.foxdevframe.ui.tools.popup;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.binzee.foxdevframe.FoxCore;
import com.binzee.foxdevframe.ui.views.CustomDialogFragment;
import com.binzee.foxdevframe.utils.phone.DimenUtil;

/**
 * 弹窗、提示工具
 *
 * @author 狐彻
 * 2020/10/21 9:43
 */
public class PopupHelper {
    private static final String TAG = "PopupHelper";
    private Toast mToast;   //Toast实例
    private volatile DialogFragment mDialog; //弹窗实例

    // 私有化构造器
    // @author 狐彻 2020/10/21 9:56
    private PopupHelper(){
        //private constructor
    }

    /**
     * 静态获取
     *
     * @author 狐彻 2020/10/27 9:03
     */
    public static PopupHelper get() {
        return PopupHelperHolder.instance;
    }

    /**
     * 系统弹窗工具
     *
     * @author 狐彻 2020/10/27 9:03
     */
    public static SystemShortcutPopup systemPopup() {
        return new SystemShortcutPopup();
    }

    /**
     * 显示提示
     *
     * @author 狐彻 2020/10/21 10:10
     */
    public void showToast(CharSequence text, int duration) {
        mToast = Toast.makeText(getContext(), text, duration);
        mToast.show();
    }

    /**
     * 立即显示提示
     *
     * @author 狐彻 2020/10/21 10:09
     */
    public void showToastNow(CharSequence text, int duration) {
        if (mToast != null) mToast.cancel();
        showToast(text, duration);
    }

    /**
     * 将Dialog放进DialogFragment中显示
     *
     * 该方法将会注销当前已显示的弹窗
     * @author 狐彻 2020/10/21 10:37
     */
    public DialogFragment getDialogFragment(Dialog dialog) {
        if (dialog != null)
            dismissDialog();
        mDialog = new CustomDialogFragment(dialog);
        return mDialog;
    }

    /**
     * 创建简单弹窗
     *
     * @author 狐彻 2020/10/21 11:09
     */
    public SimpleDialogHelper getSimpleDialogHelper() {
        return new SimpleDialogHelper();
    }

    /**
     * 显示网络加载弹窗
     *
     * @author 狐彻 2020/10/21 11:10
     */
    public DialogFragment getLoadingDialog(String title, CharSequence cancelText, DialogInterface.OnDismissListener listener) {
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        final int DP16 = DimenUtil.dipToPx(16);
        ProgressBar bar = new ProgressBar(getContext());
        bar.setPadding(0, DP16, 0, DP16);
        bar.setLayoutParams(params);
        bar.setIndeterminate(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title)
                .setView(bar)
                .setCancelable(false);
        if (listener != null)
            builder.setOnDismissListener(listener);
        if (!TextUtils.isEmpty(cancelText))
            builder.setNegativeButton(cancelText, (dialog, which) -> {
                if (listener != null)
                    listener.onDismiss(dialog);
            });
        return getDialogFragment(builder.create());
    }

    /**
     * 注销弹窗
     *
     * 该方法将会注销当前已显示的弹窗
     * @author 狐彻 2020/10/21 10:39
     */
    public void dismissDialog() {
        if (mDialog != null && mDialog.getFragmentManager() != null)
            mDialog.dismiss();
        mDialog = null;
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
     * 简单弹窗工具类
     *
     * @author 狐彻 2020/10/21 10:43
     */
    public class SimpleDialogHelper {
        private final AlertDialog.Builder builder;

        private SimpleDialogHelper() {
            builder = new AlertDialog.Builder(getContext());
        }

        /**
         * 设置标题
         *
         * @author 狐彻 2020/10/21 10:46
         */
        public SimpleDialogHelper setTitle(CharSequence text) {
            builder.setTitle(text);
            return this;
        }

        /**
         * 设置内容
         *
         * @author 狐彻 2020/10/21 10:46
         */
        public SimpleDialogHelper setMessage(CharSequence message) {
            builder.setMessage(message);
            return this;
        }

        /**
         * 设置图标
         *
         * @author 狐彻 2020/10/21 10:47
         */
        public SimpleDialogHelper setIcon(@DrawableRes int iconId) {
            builder.setIcon(iconId);
            return this;
        }

        /**
         * 设置图标
         *
         * @author 狐彻 2020/10/21 10:48
         */
        public SimpleDialogHelper setIcon(Drawable drawable) {
            builder.setIcon(drawable);
            return this;
        }

        /**
         * 设置积极按钮
         *
         * @author 狐彻 2020/10/21 10:48
         */
        public SimpleDialogHelper setPositiveButton(CharSequence text, final DialogInterface.OnClickListener listener) {
            builder.setPositiveButton(text, listener);
            return this;
        }

        /**
         * 设置消极按钮
         *
         * @author 狐彻 2020/10/21 10:48
         */
        public SimpleDialogHelper setNegativeButton(CharSequence text, final DialogInterface.OnClickListener listener) {
            builder.setNegativeButton(text, listener);
            return this;
        }

        /**
         * 创建
         *
         * @author 狐彻 2020/10/21 10:51
         */
        public DialogFragment create() {
            return getDialogFragment(builder.create());
        }
    }

    /**
     * 单例容器
     *
     * @author 狐彻 2020/10/21 9:52
     */
    private static class PopupHelperHolder {
        private static final PopupHelper instance = new PopupHelper();
    }
}
