package com.prarui.utils.system;

import android.content.Context;
import android.net.ConnectivityManager;

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
        ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }
}
