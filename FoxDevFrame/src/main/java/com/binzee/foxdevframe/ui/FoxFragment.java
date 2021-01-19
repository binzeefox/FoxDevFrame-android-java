package com.binzee.foxdevframe.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.binzee.foxdevframe.FoxCore;


/**
 * 碎片基类
 *
 * @author tong.xw
 * 2021/01/18 14:55
 */
public abstract class FoxFragment extends Fragment implements UiInterface {

    @NonNull
    @Override
    public Context getContext() {
        Context ctx = super.getContext();
        if (ctx == null) ctx = FoxCore.getApplicationContext();
        return ctx;
    }
}
