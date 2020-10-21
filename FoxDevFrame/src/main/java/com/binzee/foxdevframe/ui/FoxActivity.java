package com.binzee.foxdevframe.ui;

import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.binzee.foxdevframe.ui.tools.PopupHelper;
import com.binzee.foxdevframe.ui.tools.rx_recycler.RxHost;
import com.binzee.foxdevframe.ui.tools.rx_recycler.RxRecycler;
import com.binzee.foxdevframe.ui.tools.rx_recycler.RxRecyclerInterface;
import com.binzee.foxdevframe.utils.LogUtil;

import java.util.List;

/**
 * Activity基类
 *
 * @author 狐彻
 * 2020/10/21 9:09
 */
public abstract class FoxActivity extends AppCompatActivity implements UiInterface, RxHost {
    private static final String TAG = "FoxActivity";
    protected RxRecyclerInterface rxRecycler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        rxRecycler = createRxRecycler();

        View layout = createContentView();
        if (layout != null) setContentView(layout);
        else if (getContentViewResource() != -1)
            setContentView(getContentViewResource());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rxRecycler == null || rxRecycler.isDisposed())
            rxRecycler = createRxRecycler();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rxRecycler != null && !rxRecycler.isDisposed()) {
            rxRecycler.dispose();
        }
    }

    @Override
    public void toast(CharSequence text) {
        PopupHelper.get().showToast(text, Toast.LENGTH_LONG);
    }

    @Override
    public void requestPermission(List<String> permissionList) {
        //TODO 待完成
    }

    @Override
    public void navigate(String clsFullName, Bundle params) {
        //TODO 待完成
    }

    @Override
    public RxRecyclerInterface getRxRecycler() {
        return rxRecycler;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 工具方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 设置全屏
     *
     * @author 狐彻 2020/10/21 12:03
     */
    protected void setFullScreen() {
        Window window = getWindow();
        View decorView = window.getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }



    ///////////////////////////////////////////////////////////////////////////
    // 保护方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 创建布局，高优先级
     *
     * @author 狐彻 2020/10/21 11:59
     */
    protected View createContentView() {
        return null;
    }

    /**
     * 通过资源ID加载布局，低优先级
     *
     * @author 狐彻 2020/10/21 11:58
     */
    protected int getContentViewResource() {
        return -1;
    }

    /**
     * 创建Rx回收器
     *
     * @author 狐彻 2020/10/21 11:56
     */
    protected RxRecyclerInterface createRxRecycler() {
        return new RxRecycler();
    }

    ///////////////////////////////////////////////////////////////////////////
    // 内部类
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 生命周期回调
     *
     * @author 狐彻 2020/10/21 9:15
     */
    public static class FoxActivityCallback implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
            LogUtil.v(TAG, "onActivityCreated: " + activity.getClass().getName());
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
            LogUtil.v(TAG, "onActivityStarted: " + activity.getClass().getName());
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
            LogUtil.v(TAG, "onActivityResumed: " + activity.getClass().getName());
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {
            LogUtil.v(TAG, "onActivityPaused: " + activity.getClass().getName());
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
            LogUtil.v(TAG, "onActivityStopped: " + activity.getClass().getName());
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
            LogUtil.v(TAG, "onActivitySaveInstanceState: " + activity.getClass().getName());
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
            LogUtil.v(TAG, "onActivityDestroyed: " + activity.getClass().getName());
        }
    }
}
