package com.binzee.foxdevframe.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.LayoutRes;

/**
 * 页面接口
 *
 * @author tong.xw
 * 2021/01/18 14:07
 */
public interface UiInterface {

    Context getContext();

    default void runOnUiThread(Runnable action) {
        Handler handler = new Handler(Looper.getMainLooper());

        if (Thread.currentThread() != handler.getLooper().getThread()) {
            handler.post(action);
        } else {
            action.run();
        }
    }

    default View createContentView() {
        return null;
    }

    default @LayoutRes int getContentViewResource() {
        return -1;
    }
}
