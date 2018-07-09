package com.prarui.utils.network;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import com.prarui.utils.common.TagLog;
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
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())){
            //拿到wifi的状态值
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_NEW_STATE,0);
        TagLog.d("wifiState = "+ wifiState);
            switch (wifiState){
                case WifiManager.WIFI_STATE_DISABLED:
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
            }
        }
        //监听wifi的连接状态即是否连接的一个有效的无线路由
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())){
            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (parcelableExtra != null){
                // 获取联网状态的NetWorkInfo对象
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                //获取的State对象则代表着连接成功与否等状态
                NetworkInfo.State state = networkInfo.getState();
                //判断网络是否已经连接
                boolean isConnected = state == NetworkInfo.State.CONNECTED;
               TagLog.d( "isConnected:" + isConnected);
                if (isConnected) {
                } else {
                }
            }
        }

        // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            //获取联网状态的NetworkInfo对象
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info != null) {
                //如果当前的网络连接成功并且网络连接可用
                if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                    if (info.getType() == ConnectivityManager.TYPE_WIFI){
                        if(listener!=null){
                            listener.onNetWorkChange(AppTaskManager.getAppManager().currentActivity(), NetWorkUtils.NetWorkConnectType.WIFI);
                        }
                    }if(info.getType() == ConnectivityManager.TYPE_MOBILE){
                        listener.onNetWorkChange(AppTaskManager.getAppManager().currentActivity(), NetWorkUtils.NetWorkConnectType.MOBILE_4G);
                    }
                              {

                    }
                } else {
                    TagLog.d(getConnectionType(info.getType()) + "断开");
                    if(!NetWorkUtils.isOpenNetwork()) {
                        boolean mobileEnabled = NetWorkUtils.isMobileEnabled(context);
                        TagLog.d("状态:" + mobileEnabled);

                        if (!mobileEnabled) {
                            if (listener != null) {
                                listener.onNetWorkChange(AppTaskManager.getAppManager().currentActivity(), NetWorkUtils.NetWorkConnectType.NO_NET_WORK);
                            }
                        }

                    }

                }
            }
        }
    }
    private String getConnectionType(int type) {
        String connType = "";
        if (type == ConnectivityManager.TYPE_MOBILE) {
            connType = "3G网络数据";
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            connType = "WIFI网络";
        }
        return connType;
    }

    public interface OnNewtWorkChangeListener{
        void onNetWorkChange(Activity activity, NetWorkUtils.NetWorkConnectType type);
    }
}
