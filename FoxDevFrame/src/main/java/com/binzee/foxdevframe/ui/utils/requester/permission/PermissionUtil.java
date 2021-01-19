package com.binzee.foxdevframe.ui.utils.requester.permission;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.binzee.foxdevframe.dev.ui.utils.requester.BaseRequester;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 权限工具
 *
 * @author tong.xw
 * 2021/01/18 16:16
 */
public class PermissionUtil extends BaseRequester implements PermissionInterface {
    private static final String TAG = "Fox-Permission-util";
    private final List<String> permissionList = new ArrayList<>();  //容器

    /**
     * 构造器
     *
     * @param fragmentManager 碎片管理器
     */
    public PermissionUtil(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    /**
     * 构造器
     */
    public PermissionUtil(AppCompatActivity activity) {
        this(activity.getSupportFragmentManager());
    }

    /**
     * 构造器
     */
    public PermissionUtil(Fragment fragment) {
        this(fragment.getChildFragmentManager());
    }

    @NonNull
    @Override
    protected String getFragmentTag() {
        return TAG;
    }

    @Override
    protected Fragment createFragment() {
        return new PermissionFragment();
    }

    @Override
    public PermissionInterface addPermission(@NonNull String permission) {
        permissionList.add(permission);
        return this;
    }

    @Override
    public PermissionInterface addPermissions(@NonNull Collection<String> permissionCollection) {
        permissionList.addAll(permissionCollection);
        return this;
    }

    @Override
    public void check(int requestCode, OnPermissionResultListener listener) {
        List<String> failedList = getFragment().checkPermission(permissionList);
        List<String> noAskList = new ArrayList<>();
        for (String permission: failedList) {
            if (getFragment().checkNoAsk(permission))
                noAskList.add(permission);
        }
        if (listener != null) listener.onResult(requestCode, failedList, noAskList);
    }

    @Override
    public void checkAndRequest(int requestCode, OnPermissionResultListener listener) {
        List<String> failedList = getFragment().checkPermission(permissionList);
        if (failedList.isEmpty()) listener.onResult(requestCode, new ArrayList<>(), new ArrayList<>());
        else getFragment().request(requestCode, failedList, listener);
    }

    @Override
    protected PermissionFragment getFragment() {
        return (PermissionFragment) super.getFragment();
    }
}
