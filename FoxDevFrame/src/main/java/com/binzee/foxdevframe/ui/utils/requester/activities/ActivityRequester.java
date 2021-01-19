package com.binzee.foxdevframe.ui.utils.requester.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.binzee.foxdevframe.ui.utils.requester.BaseRequester;

/**
 * 活动请求工具
 * <p>
 * 专门处理{@link androidx.core.app.ActivityCompat#startActivityForResult}
 *
 * @author tong.xw
 * 2021/01/18 16:32
 */
public class ActivityRequester extends BaseRequester implements ActivityRequestInterface {
    private static final String TAG = "Fox-activity-requester";

    /**
     * 构造器
     *
     * @param fragmentManager 碎片管理器
     */
    public ActivityRequester(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    /**
     * 构造器
     */
    public ActivityRequester(AppCompatActivity activity) {
        this(activity.getSupportFragmentManager());
    }

    /**
     * 构造器
     */
    public ActivityRequester(Fragment fragment) {
        this(fragment.getChildFragmentManager());
    }


    @NonNull
    @Override
    protected String getFragmentTag() {
        return TAG;
    }

    @Override
    protected Fragment createFragment() {
        return new ActivityRequestFragment();
    }

    @Override
    public void request(Intent intent, int requestCode, OnActivityResultCallback callback) {
        request(intent, requestCode, callback, null);
    }

    @Override
    public void request(Intent intent, int requestCode, OnActivityResultCallback callback, Bundle options) {
        if (intent == null) return;
        getFragment().listener = callback;
        if (options == null) getFragment().startActivityForResult(intent, requestCode);
        else getFragment().startActivityForResult(intent, requestCode, options);
    }

    @Override
    protected ActivityRequestFragment getFragment() {
        return (ActivityRequestFragment) super.getFragment();
    }
}
