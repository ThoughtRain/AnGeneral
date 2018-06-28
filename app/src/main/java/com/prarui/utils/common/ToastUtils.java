package com.prarui.utils.common;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    private static Context context;

    public static void buid(Context context) {
        ToastUtils.context = context;
    }

    private static Toast toast;

    public static void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public static void showToast(int textRes) {
        if (toast == null) {
            toast = Toast.makeText(context, textRes,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(textRes);
        }
        toast.show();
    }
public static void cancel(){
        if(toast!=null){

            toast.cancel();
        }
}
}
