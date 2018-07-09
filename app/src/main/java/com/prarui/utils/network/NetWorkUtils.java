package com.prarui.utils.network;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.prarui.utils.common.ToastUtils;

import java.lang.reflect.Method;

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

        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NetWorkConnectType.WIFI;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
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
            }
        } else {
            return NetWorkConnectType.NET_WORK_CHANGE;
        }
        return NetWorkConnectType.NET_WORK_CHANGE;
    }


    public static void  registerReceiver(NetworkConnectChangedReceiver.OnNewtWorkChangeListener listener){
        NetworkConnectChangedReceiver receiver=new NetworkConnectChangedReceiver();
        if(listener!=null){
            receiver.setListener(listener);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(receiver,filter);
    }

    /**
     *
     * @param context
     * @return
     */
    public static boolean isMobileEnabled(Context context) {
        try {
            Method getMobileDataEnabledMethod = ConnectivityManager.class.getDeclaredMethod("getMobileDataEnabled");
            getMobileDataEnabledMethod.setAccessible(true);
            return (Boolean) getMobileDataEnabledMethod.invoke( mContext
                    .getSystemService(CONNECTIVITY_SERVICE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 反射失败，默认开启
        return true;
    }
    public static enum NetWorkConnectType{
        WIFI,MOBILE_4G,MOBILE_3G,MOBILE_2G,NO_NET_WORK,NET_WORK_CHANGE;

    }
}
