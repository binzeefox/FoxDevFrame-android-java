package com.binzee.foxdevframe.utils.requests;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

import com.binzee.foxdevframe.ui.FoxFragment;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP_PREFIX;

/**
 * 专门用来处理请求的无视图Fragment
 *
 * @author 狐彻
 * 2020/10/21 16:40
 */
@RestrictTo(LIBRARY_GROUP_PREFIX)
public class BaseRequestFragment extends FoxFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
