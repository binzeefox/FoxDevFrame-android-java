package com.binzee.foxdevframe.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.binzee.foxdevframe.utils.LogUtil;


/**
 * Activity基类
 *
 * @author tong.xw
 * 2021/01/18 12:13
 */
public abstract class FoxActivity extends AppCompatActivity implements UiInterface {
    private final LogUtil mLog = LogUtil.tag("FoxActivity");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView();
        onCreate();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        initContentView();
        onCreate();
    }

    @Override
    public Context getContext() {
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 保护方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 代理onCreate
     * <p>
     * {@link FoxActivity#onCreate(Bundle)}
     * 和
     * {@link FoxActivity#onCreate(Bundle, PersistableBundle)}
     * 方法在调用 {@link FoxActivity#initContentView()} 后都会调用这个方法
     * </p>
     */
    protected void onCreate() {}

    ///////////////////////////////////////////////////////////////////////////
    // 工具方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 设置全屏
     */
    protected void setFullScreen() {
        Window window = getWindow();
        View decorView = window.getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController controller = decorView.getWindowInsetsController();
            controller.hide(WindowInsets.Type.navigationBars());
        } else {
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 私有方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 初始化ContentView
     * <p>
     *     {@link FoxActivity#createContentView()} 优先级高于 {@link FoxActivity#getContentViewResource()}
     * </p>
     */
    private void initContentView() {
        View layout = createContentView();
        if (layout != null) setContentView(layout);
        else if (getContentViewResource() != -1)
            setContentView(getContentViewResource());
    }
}
