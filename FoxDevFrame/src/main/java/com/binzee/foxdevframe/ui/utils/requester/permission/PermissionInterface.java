package com.binzee.foxdevframe.ui.utils.requester.permission;

import androidx.annotation.NonNull;

import java.util.Collection;
import java.util.List;

/**
 * 权限请求相关接口
 *
 * @author tong.xw
 * 2021/01/18 16:11
 */
public interface PermissionInterface {

    /**
     * 权限请求回调
     */
    interface OnPermissionResultListener {

        /**
         * 权限获取回调
         *
         * @param requestCode 请求码
         * @param failedList  失败权限，包含不再询问的权限
         * @param noAskList   不在询问的权限
         */
        void onResult(int requestCode, @NonNull List<String> failedList, @NonNull List<String> noAskList);
    }

    /**
     * 添加权限
     */
    PermissionInterface addPermission(@NonNull String permission);

    /**
     * 批量添加权限
     */
    PermissionInterface addPermissions(@NonNull Collection<String> permissionCollection);

    /**
     * 仅检查权限
     */
    void check(int requestCode, OnPermissionResultListener listener);

    /**
     * 检查并请求权限
     */
    void checkAndRequest(int requestCode, OnPermissionResultListener listener);
}
