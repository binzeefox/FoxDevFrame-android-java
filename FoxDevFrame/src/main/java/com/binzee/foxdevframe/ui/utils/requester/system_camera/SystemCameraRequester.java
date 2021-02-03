package com.binzee.foxdevframe.ui.utils.requester.system_camera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.binzee.foxdevframe.FoxCore;
import com.binzee.foxdevframe.ui.utils.requester.activities.ActivityRequestInterface;
import com.binzee.foxdevframe.ui.utils.requester.activities.ActivityRequester;


/**
 * 调用系统相机功能请求器
 *
 * @author tong.xw
 * 2021/01/18 16:42
 */
public class SystemCameraRequester {
    private final ActivityRequester requester;
    
    /**
     * 构造器
     */
    public SystemCameraRequester(ActivityRequester requester) {
        this.requester = requester;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 业务方法
    ///////////////////////////////////////////////////////////////////////////
    
    /**
     * 开启相册
     */
    public void openGallery(int requestCode, OnResultCallback callback) {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        if (intent.resolveActivity(FoxCore.getPackageManager()) != null) {
            try {
                requester.request(intent, requestCode, createCallback(requestCode, callback));
            } catch (Exception e) {
                callback.onError(e);
            } finally {
                callback.onComplete();
            }
        }
    }

    /**
     * 开启相机
     *
     * @param requestCode 请求码
     * @param cacheUri    缓存路径  若为空则将bitmap返回至data里
     * @param callback    获取回调
     */
    public void openCamera(int requestCode, Uri cacheUri, OnResultCallback callback) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cacheUri != null)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cacheUri);
        try {
            requester.request(intent, requestCode, createCallback(requestCode, callback));
        } catch (Exception e) {
            callback.onError(e);
        } finally {
            callback.onComplete();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 私有方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 创建监听器
     *
     * @author 狐彻 2020/10/30 17:24
     */
    private ActivityRequestInterface.OnActivityResultCallback createCallback(int requestCode, OnResultCallback callback) {
        return (requestCode1, resultCode, resultData) -> {
            if (resultCode == Activity.RESULT_CANCELED)
                callback.onError(new RequestCancelException());
            else if (resultData == null)
                callback.onResult(requestCode1, null, null);
            else {
                Uri uri = resultData.getData();
                callback.onResult(requestCode1, resultData, uri);
            }
        };
    }


    ///////////////////////////////////////////////////////////////////////////
    // 异常
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 获取结果回调
     *
     * @author 狐彻 2020/10/30 11:20
     */
    public interface OnResultCallback {
        void onResult(int requestCode, Intent resultData, Uri uri);

        /**
         * 错误回调
         *
         * @param e RequestCancelException 请求取消会抛出该异常
         * @author tong.xw 2020/12/4 14:04
         */
        void onError(Throwable e);

        void onComplete();
    }

    /**
     * 用户取消请求
     *
     * @author 狐彻 2020/10/30 12:02
     */
    public static class RequestCancelException extends Exception {
        public RequestCancelException() {
            super("获取媒体失败，用户取消");
        }
    }
}
