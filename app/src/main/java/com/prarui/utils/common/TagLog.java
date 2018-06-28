package com.prarui.utils.common;

import android.util.Log;

/**
 * Created by Prarui on 2017/9/27.
 */

public class TagLog {
    private static String TAG = "prarui";
    private static boolean isOpenLog=true;

    /**
     *
     * @param TAG
     * @param isOpenLog 关闭
     */
    public static void setLogConfig(String TAG,boolean isOpenLog){
     TagLog.TAG=TAG;
     TagLog.isOpenLog=isOpenLog;
    }

    /**
     *
     * @param isOpenLog 默认关闭
     */
    public static void setLogConfig(boolean isOpenLog){
        TagLog.isOpenLog=isOpenLog;
    }
    public static void d(String... name) {
        if(!isOpenLog){
            return;
        }
        StringBuffer buffer = new StringBuffer();
        for (int index = 0; index < name.length; index++) {
            buffer.append(name[index] + "\n");
        }
        Log.d(TAG, buffer.toString());
    }
    public static void i(String... name) {
        if(!isOpenLog){
            return;
        }
        StringBuffer buffer = new StringBuffer();
        for (int index = 0; index < name.length; index++) {
            buffer.append(name[index] + "\n");
        }
        Log.i(TAG, buffer.toString());
    }
    public static void e(String... name) {
        if(!isOpenLog){
            return;
        }
        StringBuffer buffer = new StringBuffer();
        for (int index = 0; index < name.length; index++) {
            buffer.append(name[index] + "\n");
        }
        Log.e(TAG, buffer.toString());
    }
    public static void v(String... name) {
        if(!isOpenLog){
            return;
        }
        StringBuffer buffer = new StringBuffer();
        for (int index = 0; index < name.length; index++) {
            buffer.append(name[index] + "\n");
        }
        Log.v(TAG, buffer.toString());
    }
    public static void w(String... name) {
        if(!isOpenLog){
            return;
        }
        StringBuffer buffer = new StringBuffer();
        for (int index = 0; index < name.length; index++) {
            buffer.append(name[index] + "\n");
        }
        Log.w(TAG, buffer.toString());
    }
}
