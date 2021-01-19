package com.binzee.foxdevframe.ui.utils.requester.activities;

import android.content.Intent;
import android.os.Bundle;

/**
 * 活动请求接口
 *
 * @author tong.xw
 * 2021/01/18 16:33
 */
public interface ActivityRequestInterface {

    interface OnActivityResultCallback {

        /**
         * 回调
         *
         * @param requestCode   请求码
         * @param resultCode    结果码
         * @param resultData    返回结果
         * @author 狐彻 2020/10/30 11:37
         */
        void onResult(int requestCode, int resultCode, Intent resultData);
    }

    /**
     * 请求
     *
     * @author 狐彻 2020/10/30 11:39
     */
    void request(Intent intent, int requestCode, OnActivityResultCallback callback);
    void request(Intent intent, int requestCode, OnActivityResultCallback callback, Bundle options);
}
