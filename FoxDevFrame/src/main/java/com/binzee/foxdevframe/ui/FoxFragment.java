package com.binzee.foxdevframe.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.binzee.foxdevframe.ui.tools.PopupHelper;

import java.util.List;

/**
 * 碎片基类
 *
 * @author 狐彻
 * 2020/10/21 11:27
 */
public abstract class FoxFragment extends Fragment implements UiInterface {

    @Override
    public void toast(CharSequence text) {
        PopupHelper.get().showToast(text, Toast.LENGTH_LONG);
    }

    @Override
    public void requestPermission(List<String> permissionList) {
        //TODO
    }

    @Override
    public void navigate(String clsFullName, Bundle params) {
        //TODO
    }

    @Override
    public void runOnUiThread(Runnable runnable) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
    }
}