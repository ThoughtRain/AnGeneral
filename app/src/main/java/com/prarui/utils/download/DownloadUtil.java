package com.prarui.utils.download;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.prarui.utils.common.TagLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by prarui on 2017/9/29.
 * app升级代码
 */

public class DownloadUtil {
    //直接切换回主线程；
    private DownloadService downloadService;
    private ServiceConnection mSc;
    private static DownloadUtil downloadUtil;
    public static DownloadUtil get() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtil();
        }
        return downloadUtil;
    }
    public void init(Context context){
        Intent service = new Intent(context,DownloadService.class);
        mSc = new ServiceConnection(){
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                TagLog.d( "service connected");
                downloadService = ((DownloadService.DownloadBinder)service).getDownloadService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                TagLog.d(  "service disconnected");
            }
        };
        context.bindService(service, mSc, Context.BIND_AUTO_CREATE);
    }
    public void starDownload(String url,final String saveDir,DownloadService.OnDownloadListener listener){
      if(url==null){
         return;
       }
        downloadService.startDownloadForService(url,saveDir,listener);


    }


    public  void removeService(Context context){
        context.unbindService(mSc);
    }

}
