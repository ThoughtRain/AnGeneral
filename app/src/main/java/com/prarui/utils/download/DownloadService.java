package com.prarui.utils.download;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.prarui.utils.R;
import com.prarui.utils.TwoActivity;
import com.prarui.utils.common.TagLog;
import com.prarui.utils.timing.TimerHandler;

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
 * Created by prarui on 2018/6/28.
 * 下载Service
 *
 */

public class DownloadService extends Service {
    private NotificationManager notificationManager;
    private NotificationCompat.Builder mBuilder;
    private RemoteViews remoteViews;
    private  OkHttpClient okHttpClient;
    private boolean isFinish = false;
    private TimerHandler hdd=new TimerHandler();
    //定义个更新速率，避免更新通知栏过于频繁导致卡顿
    private float rate = .0f;
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
            okHttpClient = new OkHttpClient().newBuilder().build();
            return DownloadService.this;
        }
}
    /**
     * 下载
     * @param url 下载apk的路径
     */
    public void startDownloadForService(String url,String saveDir,OnDownloadListener listener){
        TagLog.d("开始进行下载");
        setOnDownloadListener(listener);
        setNotificationDemoSecond(0);
        download(url,saveDir);

    }
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    if (null != listener) {
                        listener.onDownloadFailed((String) message.obj);
                    }
                    break;
                case 1:

                    int progress = message.arg1;
                    TagLog.d("下载的progress"+progress);
                    if (null != listener) {
                        listener.onDownloading(progress);
                    }
                    TagLog.d("progressBar:"+progress);
                    refreshProgressBar(progress);
                    break;
                case 2:
                    if (null != listener) {
                        listener.onDownloadSuccess();
                    }
                case 3:
                    String status = (String) message.obj;
                     //状态
                    break;

            }
            return false;
        }
    });
    private void setOnDownloadListener(OnDownloadListener listener) {
        this.listener = listener;

    }
    /**
     * @param url     下载连接
     * @param saveDir 储存下载文件的SDCard目录
     */

    private void  download(final String url, final String saveDir) {
       new Thread(new Runnable() {
           @Override
           public void run() {
               TagLog.d("启动APp");
             //  okhttpdown(url,saveDir);
           }
       }).start();

    }
    private void okhttpdown(final String url, final String saveDir){
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.what = 0;
                message.obj=e;
                handler.sendMessage(message);
                // 下载失败
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() == null) {
                    Message message = Message.obtain();
                    message.what = 0;
                    message.obj = "下载错误";
                    handler.sendMessage(message);
                    return;
                }

                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir(saveDir);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, getNameFromUrl(url));
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        if (rate != progress) {
                            Message message = Message.obtain();
                            message.what = 1;
                            message.arg1 = progress;
                            handler.sendMessage(message);
                            rate = progress;
                        }
                        TagLog.d("开始" +progress);
                        // 下载中

                    }
                    isFinish = true;
                    TagLog.d("完成");
                    // 下载完成
                    fos.flush();
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = 0;
                    message.obj=e;
                    handler.sendMessage(message);
                    isFinish = false;
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
                if (isFinish) {
                    Message message = Message.obtain();
                    message.what = 2;
                    handler.sendMessage(message);
                    refreshStatus("下载完成");
                }

            }

        });
    }
    /**
     * 自定义通知栏
     */
    private void setNotificationDemoSecond(int progress) {
        Bitmap bitmap=Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        mBuilder = new NotificationCompat.Builder(this,"1");
        remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
        notificationManager= (NotificationManager) getSystemService
                (NOTIFICATION_SERVICE);
         mBuilder.setSmallIcon(R.mipmap.ic_launcher);
//        mBuilder.setLargeIcon(bitmap);
        Intent intent = new Intent(this, TwoActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        mBuilder.setContentIntent(pendingIntent);
        if (progress == 1) {
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        }
        remoteViews.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
        remoteViews.setTextViewText(R.id.title, "下载中");
        remoteViews.setTextViewText(R.id.app_name, "百度app");

      //  mBuilder.setCustomBigContentView(remoteViews);
        mBuilder.setCustomHeadsUpContentView(remoteViews);
        //这个是重要方法setCustomBigContentView
        mBuilder.setContent(remoteViews);
        mBuilder.setTicker("消息来了");
        mBuilder.setAutoCancel(true);
        refreshProgressBar(10);
       }
       private void refreshProgressBar(int time){
           remoteViews.setProgressBar(R.id.pBar, 100, time, false);
           remoteViews.setTextViewText(R.id.app_progress,time+"%");
           notificationManager.notify(10, mBuilder.build());
    }
    private  void refreshStatus(String status){
         remoteViews.setTextViewText(R.id.title, status);
         notificationManager.notify(10, mBuilder.build());
    }
    private OnDownloadListener listener;
    /**
     * @param saveDir
     * @return
     * @throws IOException 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();

        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }


    /**
     * @param url
     * @return 从下载连接中解析出文件名
     */
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed(String e);
    }
}
