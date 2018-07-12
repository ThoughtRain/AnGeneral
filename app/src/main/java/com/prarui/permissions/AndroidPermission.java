package com.prarui.permissions;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by prarui on 2018/7/9.
 * 使用构建者模式生成的权限请求
 *
 *
 *
 */

public class AndroidPermission {
    public static class Builder {
        private PermissionListener listener;
        private String[] permissions;
        private FragmentActivity activity;
        protected final String TAG = AndroidPermission.class.getSimpleName();
        private PermissionFragment fragment;
        public Builder init(@NonNull FragmentActivity activity) {
            this.activity = activity;
            return this;
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

        public Builder requestPermissions(String... permissions) {
            this.permissions = permissions;
            return this;
        }

        public Builder addListener(@NonNull PermissionListener listener) {
            this.listener = listener;
            return this;
        }

        public void build() {
            fragment = getPermissionsFragment(activity);
            fragment.setListener(listener);
            fragment.requestPermissions(permissions);
        }
    }




}
