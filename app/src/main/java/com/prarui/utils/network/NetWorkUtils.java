package com.prarui.utils.network;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.prarui.utils.common.ToastUtils;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * NetConUtils(是否网络连接工具类)
 * <p>
 * Created by prarui on 2017/5/26.
 */

public class NetWorkUtils {
    private static Context mContext;

    /**
     * 检查网络连接情况
     *
     * @param
     * @return
     */
    public static void build(Context context) {
        mContext = context;
    }

    public static boolean isOpenNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }
    public static NetWorkConnectType getNetWorkConnectType(){
        /** 获得系统级联网管理员对象 */
        ConnectivityManager manager;
        manager = (ConnectivityManager) mContext
                .getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null) { // 无网情况下
            // 跳转到网络设置页面
        //    mContext.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            return NetWorkConnectType.NO_NET_WORK;
        }else {
            if (info.isAvailable()) { // 网络可用时
           if(info.isConnected()){
                    if(info.getType()==ConnectivityManager.TYPE_MOBILE){
                        String subtypeName = info.getSubtypeName();
                        switch (info.getSubtype()){
                            case TelephonyManager.NETWORK_TYPE_GPRS:
                            case TelephonyManager.NETWORK_TYPE_EDGE:
                            case TelephonyManager.NETWORK_TYPE_CDMA:
                            case TelephonyManager.NETWORK_TYPE_1xRTT:
                            case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                              return NetWorkConnectType.MOBILE_2G;
                            case TelephonyManager.NETWORK_TYPE_UMTS:
                            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                            case TelephonyManager.NETWORK_TYPE_HSDPA:
                            case TelephonyManager.NETWORK_TYPE_HSUPA:
                            case TelephonyManager.NETWORK_TYPE_HSPA:
                            case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                            case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                            case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                               return NetWorkConnectType.MOBILE_3G;
                            case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                              return NetWorkConnectType.MOBILE_4G;
                            default:
                                // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                                if (subtypeName.equalsIgnoreCase("TD-SCDMA") || subtypeName.equalsIgnoreCase("WCDMA") || subtypeName.equalsIgnoreCase("CDMA2000")){
                                    return NetWorkConnectType.MOBILE_3G;
                                }

                        }

                    }else if (info.getType()==ConnectivityManager.TYPE_WIFI){
                        return NetWorkConnectType.WIFI;
                    }

                }else {
                  //  ToastUtils.showToast("网络没有连接");
                    return NetWorkConnectType.NO_NET_WORK;
                }
            }

        }




        return null;
    }
    public static void  registerReceiver(NetworkConnectChangedReceiver.OnNewtWorkChangeListener listener){
        NetworkConnectChangedReceiver receiver=new NetworkConnectChangedReceiver();
        if(listener!=null){
            receiver.setListener(listener);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        mContext.registerReceiver(receiver,filter);
    }
    public static enum NetWorkConnectType{
        WIFI,MOBILE_4G,MOBILE_3G,MOBILE_2G,NULL,NO_NET_WORK;


    }
}
