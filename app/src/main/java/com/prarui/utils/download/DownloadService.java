package com.prarui.utils.download;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.prarui.utils.common.TagLog;

/**
 * Created by prarui on 2018/6/28.
 * 下载Service
 *
 */

public class DownloadService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new DownloadBinder();
    }

    /**
     * 这个作用是用于控制
     */
    public class DownloadBinder extends Binder {
     public  DownloadService getDownloadService(){
            return DownloadService.this;
        }
}
    /**
     * 下载
     * @param url 下载apk的路径
     */
    public void startDownload(String url){
        TagLog.d("开始进行下载");
    }
}
