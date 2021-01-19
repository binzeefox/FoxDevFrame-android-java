package com.binzee.foxdevframe.ui.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;

import androidx.annotation.NonNull;

import com.binzee.foxdevframe.dev.R;
import com.binzee.foxdevframe.dev.utils.ThreadUtils;

/**
 * 视图工具
 *
 * @author tong.xw
 * 2021/01/18 15:11
 */
public class ViewUtil {
    private final View target;  //目标控件

    /**
     * 构造器
     *
     * @param view 目标控件
     */
    public ViewUtil(@NonNull View view) {
        target = view;
    }

    /**
     * 设置防抖点击事件
     *
     * @param skip     防抖超时，毫秒
     * @param listener 点击事件
     */
    public void setOnDebounceClickListener(long skip, View.OnClickListener listener) {
        target.setOnClickListener(new View.OnClickListener() {
            final long _skip = skip;

            @Override
            public void onClick(View v) {
                long curTimeStamp = System.currentTimeMillis();
                Long tagTimeStamp = (Long) v.getTag(R.id.fox_frame_java_view_util_debounce_id);
                if (tagTimeStamp == null || tagTimeStamp + _skip < curTimeStamp) {
                    v.setTag(R.id.fox_frame_java_view_util_debounce_id, curTimeStamp);
                    listener.onClick(v);
                }
            }
        });
    }

    /**
     * 获取View截屏
     */
    public Bitmap captureViewShot() {
        Bitmap bitmap = Bitmap
                .createBitmap(target.getWidth(), target.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        target.layout(target.getLeft(), target.getTop(), target.getRight(), target.getBottom());
        Drawable bg = target.getBackground();
        if (bg != null) bg.draw(canvas);
        else canvas.drawColor(Color.TRANSPARENT);
        target.draw(canvas);
        return bitmap;
    }

    /**
     * 开启disco模式
     *
     * @author 狐彻 2020/11/12 15:06
     */
    public void setDiscoMode() {
        HandlerThread thread = new HandlerThread("view_util_disco");
        thread.start();
        new Handler(thread.getLooper()).post(() -> {
            final int[] r = new int[]{0, 1};
            final int[] g = new int[]{0, 10};
            final int[] b = new int[]{0, 20};

            while (true) {
                try {
                    Thread.sleep(5);
                    discoModeValueFnc(r);
                    discoModeValueFnc(g);
                    discoModeValueFnc(b);

                    ThreadUtils.runOnUiThread(() -> {
                        int colorValue = Color.argb(255, r[0], g[0], b[0]);
                        target.setBackgroundColor(colorValue);
                    });
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////
    // 私有方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * disco模式工具方法
     *
     * @author 狐彻 2020/11/12 15:05
     */
    private void discoModeValueFnc(int[] values) {
        values[0] += values[1];
        if (values[0] > 255) {
            values[0] = 255;
            values[1] *= -1;
        }
        if (values[0] < 0) {
            values[0] = 0;
            values[1] *= -1;
        }
    }
}
