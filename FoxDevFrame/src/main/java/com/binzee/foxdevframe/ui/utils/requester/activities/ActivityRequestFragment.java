package com.binzee.foxdevframe.ui.utils.requester.activities;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * 活动请求业务碎片
 *
 * @author tong.xw
 * 2021/01/18 16:37
 */
public class ActivityRequestFragment extends Fragment {
    volatile ActivityRequestInterface.OnActivityResultCallback listener = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (listener == null) return;
        listener.onResult(requestCode, resultCode, data);
        listener = null;
    }
}
