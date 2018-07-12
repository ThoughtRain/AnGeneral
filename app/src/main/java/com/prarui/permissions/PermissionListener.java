package com.prarui.permissions;

import java.util.List;

/**
 * Created by prarui on 2018/7/12.
 * 请求、、
 *
 */

public interface PermissionListener {
    void onGranted();

    void onDenied(List<String> deniedPermission);

    void onShouldShowRationale(List<String> deniedPermission);

}
