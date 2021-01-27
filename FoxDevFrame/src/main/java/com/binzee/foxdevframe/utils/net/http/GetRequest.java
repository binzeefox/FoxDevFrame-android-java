package com.binzee.foxdevframe.utils.net.http;

import com.binzee.foxdevframe.utils.LogUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * GET请求
 *
 * @author 狐彻
 * 2020/11/09 9:19
 */
class GetRequest implements ClientInterface {
    private static final String TAG = "GetRequest";
    private final HttpURLConnection connection;

    /**
     * 构造器
     *
     * @author 狐彻 2020/11/09 9:19
     */
    GetRequest(URL url) throws IOException {
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
    }

    @Override
    public void request(OnCallListener listener) {
        try {
            listener.onStart(connection);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200)
                listener.onSuccess(connection, responseCode, connection.getInputStream());
            else listener.onSuccess(connection, responseCode, null);
        } catch (Exception e) {
            LogUtil.tag(TAG).message("request: 请求失败").throwable(e).e();
            listener.onError(e);
            connection.disconnect();
        }
    }

    @Override
    public ClientInterface setConnectTimeout(int miles) {
        connection.setConnectTimeout(miles);
        return this;
    }

    @Override
    public ClientInterface setReadTimeout(int miles) {
        connection.setReadTimeout(miles);
        return this;
    }
}