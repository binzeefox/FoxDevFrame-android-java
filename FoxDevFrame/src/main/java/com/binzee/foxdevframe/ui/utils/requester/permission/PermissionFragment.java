package com.binzee.foxdevframe.ui.utils.requester.permission;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.binzee.foxdevframe.dev.FoxCore;
import com.binzee.foxdevframe.dev.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static androidx.annotation.RestrictTo.Scope.LIBRARY;

/**
 * 请求权限专用
 *
 * @author tong.xw
 * 2021/01/18 16:19
 */
@RestrictTo(LIBRARY)
public class PermissionFragment extends Fragment {
    private final LogUtil log = LogUtil.tag("PermissionFragment");
    private volatile PermissionInterface.OnPermissionResultListener listener = null;

    /**
     * 检查权限
     */
    List<String> checkPermission(@NonNull List<String> permissionList) {
        log.message("checkPermission: 检查权限 => " + permissionList);

        final List<String> failedList = new ArrayList<>();
        for (String permission: permissionList) {
            int result = ActivityCompat.checkSelfPermission(getContext(), permission);
            if (result != PERMISSION_GRANTED)
                failedList.add(permission);
        }

        return failedList;
    }

    /**
     * 请求权限
     */
    void request(int requestCode, @NonNull List<String> permissionList, PermissionInterface.OnPermissionResultListener listener) {
        // 若当前监听不为空，则上一次请求未完成，忽略此次请求
        if (this.listener != null) return;
        this.listener = listener;
        requestPermissions(permissionList.toArray(new String[0]), requestCode);
    }

    /**
     * 检查是否不在询问
     *
     * @author 狐彻 2020/10/21 17:13
     */
    boolean checkNoAsk(String permission) {
        return !shouldShowRequestPermissionRationale(permission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        final List<String> failedList = new ArrayList<>();
        final List<String> noAskList = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++){
            if (grantResults[i] != PERMISSION_GRANTED) {
                failedList.add(permissions[i]);
                if (checkNoAsk(permissions[i]))
                    noAskList.add(permissions[i]);
            }
        }

        if (!failedList.isEmpty())
            log.message("startCheckAndRequest: 权限未通过 => " + failedList).v();
        if (!failedList.isEmpty())
            log.message("startCheckAndRequest: 权限不再询问 => " + noAskList).v();

        if (listener != null) listener.onResult(requestCode, failedList, noAskList);
        listener = null;
    }

    @NonNull
    @Override
    public Context getContext() {
        Context context = super.getContext();
        if (context == null)
            context = FoxCore.getApplicationContext();
        return context;
    }
}
