package com.binzee.foxdevframe.ui.tools.rx_recycler;


import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 基于RxJava2的回收器
 *
 * @author 狐彻
 * 2020/10/21 11:48
 */
public class RxRecycler implements RxRecyclerInterface {
    private final CompositeDisposable dContainer = new CompositeDisposable();


    @Override
    public boolean add(Disposable d) {
        return dContainer.add(d);
    }

    @Override
    public boolean remove(Disposable d) {
        return dContainer.remove(d);
    }

    @Override
    public boolean delete(Disposable d) {
        return dContainer.delete(d);
    }

    @Override
    public void dispose() {
        dContainer.dispose();
    }

    @Override
    public boolean isDisposed() {
        return dContainer.isDisposed();
    }
}
