package com.binzee.foxdevframe.ui.tools.rx_recycler;


import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 抽象Rx回收容器
 *
 * @author 狐彻
 * 2020/10/21 11:43
 */
public interface RxRecyclerInterface {

    /**
     * Adds a disposable to this container or disposes it if the
     * container has been disposed.
     * @param d the disposable to add, not null
     * @return true if successful, false if this container has been disposed
     */
    boolean add(Disposable d);

    /**
     * Removes and disposes the given disposable if it is part of this
     * container.
     * @param d the disposable to remove and dispose, not null
     * @return true if the operation was successful
     */
    boolean remove(Disposable d);

    /**
     * Removes (but does not dispose) the given disposable if it is part of this
     * container.
     * @param d the disposable to remove, not null
     * @return true if the operation was successful
     */
    boolean delete(Disposable d);

    /**
     * Dispose the resource, the operation should be idempotent.
     */
    void dispose();

    /**
     * Returns true if this resource has been disposed.
     * @return true if this resource has been disposed
     */
    boolean isDisposed();
}
