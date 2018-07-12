package com.prarui.permissions;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by prarui on 2018/7/9.
 */

public class AndroidPermission {
    public  class Builder{
        protected  final String TAG = AndroidPermission.class.getSimpleName();
        private PermissionFragment fragment;
        public Builder(@NonNull FragmentActivity activity) {
            fragment = getPermissionsFragment(activity);
        }

        public PermissionFragment getPermissionsFragment(@NonNull FragmentActivity activity) {
            PermissionFragment fragment = (PermissionFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG);
            boolean isNewInstance = fragment == null;
            if (isNewInstance) {
                fragment = new PermissionFragment();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                fragmentManager.beginTransaction().add(fragment, TAG).commit();
                fragmentManager.executePendingTransactions();
            }
            return fragment;


        }
        public Builder requestPermissions(String... permissions){

        return this;
        }
        public void requestPermissions(PermissionListener listener, String... permissions) {
            fragment.setListener(listener);
            fragment.requestPermissions(permissions);

        }
    }

    private static final String TAG = AndroidPermission.class.getSimpleName();
    private PermissionFragment fragment;


    /**
     * 外部调用申请权限
     *
     * @param permissions 申请的权限
     * @param listener    监听权限接口
     */



}
