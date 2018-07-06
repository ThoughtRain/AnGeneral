package com.prarui.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import com.prarui.base.BaseActivity;
import com.prarui.utils.common.TagLog;
import com.prarui.utils.common.ToastUtils;
import com.prarui.utils.download.DownloadService;
import com.prarui.utils.download.DownloadUtil;
import com.prarui.utils.json.GsonUtils;
import com.prarui.utils.network.NetWorkUtils;
import com.prarui.utils.network.OkHttpManager;
import com.prarui.utils.notification.NotificationUtils;
import com.prarui.utils.timing.TimerHandler;
import com.prarui.utils.transmit.EventMS;

import java.util.HashMap;

public class MainActivity extends BaseActivity {
   private Button send,sendTwo;
   TimerHandler handler=new TimerHandler();
   private String url="http://192.168.2.23:8080/test";

   @Override
    protected void onStart() {
        super.onStart();
       DownloadUtil.get().init(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send=findViewById(R.id.send);
        sendTwo=findViewById(R.id.sendTwo);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                OkHttpManager.with().setPostRequest("tag", url, "keee", new HashMap<String, String>(), new OkHttpManager.OnOkHttpResultCallbackListener() {
//                    @Override
//                    public void onLoading() {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//
//                    @Override
//                    public void onError(String e) {
//
//                    }
//
//                    @Override
//                    public void onResponseSucceed(String json) {
//                        String xx=json;
//                      TagLog.d(json);
//                    }
//                });
               startActivity(new Intent(MainActivity.this,TwoActivity.class));
            }
        });
        sendTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 handler.endTimer();
            }
        });
        EventMS.getInstance().receiveData(MainActivity.class, new EventMS.OnPostEventListener() {
            @Override
            public void onPostInformation(Bundle bundle) {

                String code = bundle.getString("code");
                String codeTwo = bundle.getString("codeTwo");
                String codeThere = bundle.getString("codeThere");
                if(code!=null){
                    Log.d("code",code);
                }
                if(codeTwo!=null){
                    Log.d("codeTwo",codeTwo);
                }
                if(codeThere!=null){
                    Log.d("codeThere",codeThere);
                }

            }
        });
    }
    //发送自定义视图通知
    public void sendCustomViewNotification() {
        //普通notification用到的视图
        RemoteViews normalView = new RemoteViews(getPackageName(), R.layout.normal_notification);
        //显示bigView的notification用到的视图
        RemoteViews bigView = new RemoteViews(getPackageName(), R.layout.big_notification);
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("开始播放啦~~")
                .setOngoing(true)
                .setContent(normalView)//设置普通notification视图
                .setCustomBigContentView(bigView)//设置显示bigView的notification视图
                .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                .build();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(22, notification);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //管理生命周期
        DownloadUtil.get().removeService(MainActivity.this);
    }
}
