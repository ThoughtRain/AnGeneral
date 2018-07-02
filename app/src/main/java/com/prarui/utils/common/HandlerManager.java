package com.prarui.utils.common;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Administrator on 2018/6/29.
 */

public class HandlerManager {
    private Callback callback;
   private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(callback!=null){
             callback.handleMessage(msg);
            }
            return false;
        }
    });
   public void sendData(Message message){
      // handler.getLooper();
       handler.sendMessageDelayed(message, 0);
    }
    public void setCallback(Callback callback){
       this.callback=callback;
    }
    public interface  Callback{
       void handleMessage(Message message);
    }
}
