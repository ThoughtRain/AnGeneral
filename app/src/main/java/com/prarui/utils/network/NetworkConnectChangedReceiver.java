package com.prarui.utils.network;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.prarui.utils.common.ToastUtils;
import com.prarui.utils.system.AppTaskManager;

/**
 * Created by prarui on 2018/7/6.
 */

public class NetworkConnectChangedReceiver extends BroadcastReceiver {
   private OnNewtWorkChangeListener listener;

    public void setListener(OnNewtWorkChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NetWorkUtils.NetWorkConnectType connectType = NetWorkUtils.getNetWorkConnectType();
        ToastUtils.showToast("连接状态："+connectType+"");
        if(listener!=null){
          listener.onNetWorkChange(AppTaskManager.getAppManager().currentActivity(),connectType);
        }
    }
    public interface OnNewtWorkChangeListener{
        void onNetWorkChange(Activity activity, NetWorkUtils.NetWorkConnectType type);
    }
}
