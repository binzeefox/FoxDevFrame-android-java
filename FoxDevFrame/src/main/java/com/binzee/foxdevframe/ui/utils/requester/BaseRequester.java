package com.binzee.foxdevframe.ui.utils.requester;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * 利用无页面Fragment请求数据的基类
 *
 * @author tong.xw
 * 2021/01/18 16:07
 */
public abstract class BaseRequester {

    private final Fragment mFragment;

    /**
     * 构造器
     *
     * @param fragmentManager 碎片管理器
     */
    public BaseRequester(FragmentManager fragmentManager) {
        Fragment fragment = fragmentManager.findFragmentByTag(getFragmentTag());
        if (fragment != null) {
            mFragment = fragment;
            return;
        }

        //Fragment不存在，创建Fragment
        mFragment = createFragment();
        fragmentManager.beginTransaction()
                .add(mFragment, getFragmentTag())
                .commitNow();
    }

    /**
     * 业务Fragment标签
     */
    @NonNull
    protected abstract String getFragmentTag();

    /**
     * 创建Fragment
     */
    protected abstract Fragment createFragment();

    /**
     * 获取Fragment
     */
    protected Fragment getFragment() {
        return mFragment;
    }
}
