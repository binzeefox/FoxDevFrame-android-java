package com.binzee.foxdevframe.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.binzee.foxdevframe.FoxCore;


/**
 * 碎片基类
 *
 * @author tong.xw
 * 2021/01/18 14:55
 */
public abstract class FoxFragment extends Fragment implements UiInterface {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = createContentView();
        if (view != null) {
            return view;
        } else if (getContentViewResource() != -1) {
            return inflater.inflate(getContentViewResource(), container, false);
        } else return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Context getContext() {
        Context ctx = super.getContext();
        if (ctx == null) ctx = FoxCore.getApplicationContext();
        return ctx;
    }
}
