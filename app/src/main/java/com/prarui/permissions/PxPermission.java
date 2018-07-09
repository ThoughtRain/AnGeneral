package com.prarui.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by prarui on 2018/7/9.
 */

public class PxPermission {
    private static Activity context;
    public static void build(Activity context){
    PxPermission.context=context;
   }

    /**
     * 权限请求
     * @param permission
     */
   public void requestPermissions(OnPxPermissionListener listener,int code,String ...permission){
       if(null==listener){
            return;
       }
       /**
        * 当大于Android的版本是M时进行判断；
        */
       if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
           for(String px:permission){
               int checkSelfPermission = ActivityCompat.checkSelfPermission(context, px);
               boolean isChecked = checkSelfPermission == PackageManager.PERMISSION_DENIED;
               if(isChecked){
                listener.onSucceed(px,code);
               }else {
                 // listener.onFailed(px,code);
                   listener.onFailed(px,code);

               }


           }
           final int[] grantResults = new int[permission.length];

           PackageManager packageManager = context.getPackageManager();
           String packageName = context.getPackageName();

           final int permissionCount = permission.length;
           for (int i = 0; i < permissionCount; i++) {
               grantResults[i] = packageManager.checkPermission(
                       permission[i], packageName);
           }

           ((ActivityCompat.OnRequestPermissionsResultCallback) context).onRequestPermissionsResult(
                   code, permission, grantResults);
           ActivityCompat.requestPermissions(context,permission,code);

       }else{
           listener.onNoRequestPermission(Build.VERSION.SDK_INT+"");
       }




   }

  public interface  OnPxPermissionListener{
       void onNoRequestPermission(String versionCode);
       void onSucceed(String permission,int request);
       void onFailed(String permission,int request);
  }


}
