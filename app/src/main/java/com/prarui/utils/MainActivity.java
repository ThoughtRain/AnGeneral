package com.prarui.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.prarui.utils.common.TagLog;
import com.prarui.utils.download.DownloadService;
import com.prarui.utils.timing.TimerHandler;
import com.prarui.utils.transmit.EventMS;

public class MainActivity extends AppCompatActivity {
   private Button send,sendTwo;
   TimerHandler handler=new TimerHandler();
   ServiceConnection mSc;
   private DownloadService downloadService;
   @Override
    protected void onStart() {
        super.onStart();
        Intent service = new Intent(this.getApplicationContext(),DownloadService.class);
        this.bindService(service, mSc, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send=findViewById(R.id.send);
        sendTwo=findViewById(R.id.sendTwo);
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

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                handler.startTimer(new TimerHandler.OnTimerHandlerListener() {
//                    @Override
//                    public void onTimer(String time) {
//                    send.setText(time);
//                    }
//
//                    @Override
//                    public void endTimer(String endTime) {
//                        sendTwo.setText(endTime);
//                    }
//                });
                downloadService.startDownload("xxxx");
            }
        });
        sendTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle bundle=new Bundle();
//                bundle.putString("codeTwo","300");
//                EventMS.getInstance().sendData(MainActivity.class,bundle);
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
}
