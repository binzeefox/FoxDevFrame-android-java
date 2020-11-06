package com.binzee.foxdevframe.utils.http;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.binzee.foxdevframe.utils.ThreadUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 网络工具类
 *
 * TODO unfinished
 * @author 狐彻
 * 2020/10/27 16:45。。
 */
public class ClientUtil {
    private volatile String baseUrl = "";   //基本路径

    private ClientUtil(){}

    public static ClientUtil getInstance() {
        return ClientUtilHolder.instance;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 业务方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 设置基本路径
     *
     * @author 狐彻 2020/10/27 17:13
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }




    ///////////////////////////////////////////////////////////////////////////
    // 内部类
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 请求实现类
     *
     * @author 狐彻 2020/10/27 17:27
     */
    public static class Request implements ClientInterface{
        @NonNull
        private final String urlStr;
        private final Map<String, String> params = new Hashtable<>();

        private Request(@NonNull String url) throws IOException {
            this.urlStr = url;
        }

        /**
         * 设置参数
         *
         * @author 狐彻 2020/10/27 17:28
         */
        public void setParams(Map<String, String> params) {
            this.params.clear();
            this.params.putAll(params);
        }

        @Override
        public synchronized void get(OnCallListener listener) {
            try {
                String _urlStr = urlStr;
                if (!params.isEmpty())
                    _urlStr = setGETParams(urlStr);
                HttpURLConnection connection = (HttpURLConnection)
                        new URL(_urlStr).openConnection();
                connection.setRequestMethod("GET");
                ThreadUtils.get().invokeIO(new HttpCallable(connection, listener)
                        , new HttpFutureResultListener(listener));
            } catch (IOException e) {
                listener.onError(e);
            }
        }

        @Override
        public synchronized void post(OnCallListener listener) {
            //TODO 参数设置
            try {
                HttpURLConnection connection = (HttpURLConnection)
                        new URL(urlStr).openConnection();
                connection.setRequestMethod("POST");
                ThreadUtils.get().invokeIO(new HttpCallable(connection, listener)
                        , new HttpFutureResultListener(listener));
            } catch (IOException e) {
                listener.onError(e);
            }
        }

        /**
         * 为GET方法设置参数
         *
         * @author 狐彻 2020/10/27 17:32
         */
        private String setGETParams(final String urlStr) {
            String str = urlStr + "?";
            String[] _params = new String[params.size()];
            int i = 0;
            for (String key: params.keySet()) {
                _params[i] = key + "=" + params.get(key);
                i++;
            }
            str += TextUtils.join("&", _params);
            return str;
        }
    }

    /**
     * 工具
     *
     * @author 狐彻 2020/10/27 17:19
     */
    private static class HttpCallable implements Callable<InputStream>{
        private final HttpURLConnection connection;
        private final ClientInterface.OnCallListener listener;

        HttpCallable(HttpURLConnection connection, ClientInterface.OnCallListener listener) {
            this.connection = connection;
            this.listener = listener;
        }

        @Override
        public InputStream call() throws Exception {
            connection.connect();
            int responseCode = connection.getResponseCode();
            listener.onResponse(responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK)
                return connection.getInputStream();
            return null;
        }
    }

    /**
     * 工具
     *
     * @author 狐彻 2020/10/27 17:26
     */
    private static class HttpFutureResultListener implements ThreadUtils.OnFutureResultListener<InputStream> {
        private ClientInterface.OnCallListener listener;

        public HttpFutureResultListener(ClientInterface.OnCallListener listener) {
            this.listener = listener;
        }

        @Override
        public void onResult(InputStream result) {
            listener.onSuccess(result);
        }

        @Override
        public void onError(Throwable throwable) {
            listener.onError(throwable);
        }

        @Override
        public void onComplete() {
            listener.onComplete();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 单例容器
    ///////////////////////////////////////////////////////////////////////////

    private static class ClientUtilHolder {
        static ClientUtil instance = new ClientUtil();
    }
}
