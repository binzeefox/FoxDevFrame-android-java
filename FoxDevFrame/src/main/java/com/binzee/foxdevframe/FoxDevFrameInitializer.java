package com.binzee.foxdevframe;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import java.util.Collections;
import java.util.List;

/**
 * 初始化工具
 *
 * @author tong.xw
 * 2021/01/18 12:09
 */
public class FoxDevFrameInitializer implements Initializer<FoxCore> {
    @NonNull
    @Override
    public FoxCore create(@NonNull Context context) {
        return FoxCore.init(context);
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }
}
