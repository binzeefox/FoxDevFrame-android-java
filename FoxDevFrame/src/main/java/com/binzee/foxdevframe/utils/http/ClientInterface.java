package com.binzee.foxdevframe.utils.http;

import java.io.InputStream;

/**
 * 网络工具接口
 *
 * @author 狐彻
 * 2020/10/27 16:46
 */
public interface ClientInterface {

    void get(OnCallListener listener);
    void post(OnCallListener listener);

    interface OnCallListener {
        void onResponse(int responseCode);
        void onSuccess(InputStream stream);
        void onError(Throwable throwable);
        void onComplete();
    }
}
