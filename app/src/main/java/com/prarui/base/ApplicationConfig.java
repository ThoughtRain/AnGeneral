package com.prarui.base;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.prarui.utils.common.TagLog;
import com.prarui.utils.common.ToastUtils;
import com.prarui.utils.network.NetWorkUtils;
import com.prarui.utils.network.NetworkConnectChangedReceiver;
import com.prarui.utils.network.OkHttpConfig;
import com.prarui.utils.network.OkHttpManager;
import com.prarui.utils.system.AppTaskManager;
import com.prarui.utils.system.SharePreferenceUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by prarui on 2018/7/4.
 */

public class ApplicationConfig extends Application {
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        AlertDialog dialog=builder.setMessage("pc端断开连接，请及时保存编辑文档!")
                                .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                }).create();
                        dialog.getWindow();
                        dialog.setCanceledOnTouchOutside(false);//点击屏幕不消失
                        if (!dialog.isShowing()){//此时提示框未显示
                            dialog.show();
                        }
                        break;
                    case WIFI:
                        ToastUtils.showToast("切换到了Wifi");
                        break;
                    case MOBILE_2G:
                        ToastUtils.showToast("切换到了2G");
                        break;
                    case MOBILE_3G:
                        ToastUtils.showToast("切换到了3G");
                        break;
                    case MOBILE_4G:
                        ToastUtils.showToast("切换到了4G");
                        break;
                }

            }
        });

    }
}
