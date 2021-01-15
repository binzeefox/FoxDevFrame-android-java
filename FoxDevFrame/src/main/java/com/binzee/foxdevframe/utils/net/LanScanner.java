package com.binzee.foxdevframe.utils.net;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 局域网扫描器
 *
 * @author tong.xw
 * 2021/01/13 13:48
 */
public class LanScanner {
    private static final String TAG = "Scanner";

    private final String myAddress;   //本地ip
    private final ExecutorService workThreadPool = Executors.newCachedThreadPool();
    private final ExecutorService loopThreadPool = Executors.newSingleThreadExecutor();

    private OnResponseListener listener = null;
    private boolean recycled = false;

    public LanScanner(String localAddress) {
        myAddress = localAddress;
    }

    /**
     * 开始扫描
     *
     * @author tong.xw 2021/01/14 09:58
     */
    public void scan() {
        String addressPre = myAddress.substring(0, myAddress.lastIndexOf("."));
        Log.v(TAG, "scan: " + addressPre);
        int i = 1;
        while (i <= 255 && !recycled) {
            String ip = addressPre + "." + i;
            if (TextUtils.equals(myAddress, ip)) continue;
            pingIp(ip, listener);
            i++;
        }
    }

    /**
     * 回收
     *
     * @author tong.xw 2021/01/14 09:58
     */
    public void recycle() {
        recycled = true;
        workThreadPool.shutdown();
        loopThreadPool.shutdown();
    }

    public boolean isRecycled() {
        return recycled;
    }

    /**
     * 设置监听
     *
     * @author tong.xw 2021/01/14 09:58
     */
    public void setOnResponseListener(OnResponseListener listener) {
        this.listener = listener;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 内部方法
    ///////////////////////////////////////////////////////////////////////////

    private void pingIp(final String ip, final OnResponseListener listener) {
        workThreadPool.execute(() -> {
            try {
                InetAddress address = InetAddress.getByName(ip);
                boolean reachable = address.isReachable(3000);
                if (reachable && listener != null) {
                    listener.response(address);
                }

                logger(ip, reachable);
            } catch (IOException e) {
                Log.e(TAG, "pingIp: ", e);
            }
        });
    }

    private synchronized void logger(String ip, boolean reachable) {
        Log.v(TAG, "pingIp: ----------------------------------------");
        Log.v(TAG, "pingIp: ip = " + ip);
        Log.v(TAG, "pingIp: result = " + reachable);
        Log.v(TAG, "pingIp: ----------------------------------------");
    }

    public interface OnResponseListener{
        void response(InetAddress address);
    }
}
