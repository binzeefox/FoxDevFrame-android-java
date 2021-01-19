package com.binzee.foxdevframe.ui;

/**
 * 懒加载
 *
 * @author tong.xw
 * 2021/01/18 14:59
 */
public abstract class FoxLazyFoxFragment extends FoxFragment {
    private boolean isLoaded = false;   //是否已加载

    @Override
    public void onResume() {
        super.onResume();
        checkLoad();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        checkLoad();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isLoaded = false;
    }

    /**
     * 手动设置该Fragment的加载状态
     *
     * @author 狐彻 2020/10/21 11:35
     */
    public void setLoaded(boolean isLoaded) {
        this.isLoaded = isLoaded;
    }

    protected void checkLoad() {
        if (isVisible() && !isLoaded) {
            onLoad();
            isLoaded = true;
        }
    }

    /**
     * 懒加载
     */
    protected abstract void onLoad();
}
