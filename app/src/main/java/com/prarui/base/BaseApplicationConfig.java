package com.prarui.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.prarui.utils.common.TagLog;
import com.prarui.utils.common.ToastUtils;
import com.prarui.utils.network.NetWorkUtils;
import com.prarui.utils.network.NetworkConnectChangedReceiver;
import com.prarui.utils.network.OkHttpConfig;
import com.prarui.utils.network.OkHttpManager;
import com.prarui.utils.system.AppTaskManager;
import com.prarui.utils.system.SharePreferenceUtils;
import com.prarui.wigth.HmAlertDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by prarui on 2018/7/4.
 */

public class BaseApplicationConfig extends Application {
    /**
     * 初始进入app的准备；
     */
    @Override
    public void onCreate() {
        super.onCreate();
        NetWorkUtils.build(this);
        SharePreferenceUtils.buid(this);
        TagLog.setLogConfig(this.getPackageName(),true);
        ToastUtils.buid(this);
        Map<String,String> map=new HashMap<>();
        map.put("auth-x","xxxx");
        OkHttpManager.build(map,"tokenName");
        NetWorkUtils.registerReceiver(new NetworkConnectChangedReceiver.OnNewtWorkChangeListener() {
            @Override
            public void onNetWorkChange(Activity activity, NetWorkUtils.NetWorkConnectType type) {
                switch (type){
                    case NO_NET_WORK:
                        showDo(activity,"没有网络");
                        break;
                    case MOBILE_4G:
                        ToastUtils.showToast("连接4G");
                        break;
                    case WIFI:
                        ToastUtils.showToast("连接wifi");
                        break;
                }

            }
        });

    }
    private void showDo(Context context,String message){
        HmAlertDialog.getInstance().showWindow(context, "重要提示", message, new HmAlertDialog.OnAlertDialogListener() {
            @Override
            public void onButtonClick(View view) {
                ToastUtils.showToast("点击了");
            }
        });
    }
}
