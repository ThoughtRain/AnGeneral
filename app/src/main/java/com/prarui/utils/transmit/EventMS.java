package com.prarui.utils.transmit;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by prarui on 2018/6/27.
 * 一个简单的数据传输class
 */

public class EventMS {
    private static final String TAG=EventMS.class.getSimpleName();
    private static EventMS machine = null;
    private String sendKey;
    public static EventMS getInstance() {
        if (machine == null) {
            machine = new EventMS();
        }
        return machine;
    }

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
        switch (msg.what){
    case 1:
        Iterator iterator = listenerMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if(sendKey!=null&&sendKey.equals(entry.getKey().toString())){
                EventMS.OnPostEventListener listener = (EventMS.OnPostEventListener) entry.getValue();
                if (null != listener) {
                    listener.onPostInformation(msg.getData());
                }
                break;
            }

        }

         break;
     case 2:
         break;
}

            return false;
        }
    });
    /**
     *
     * @param c 传送到目标 Activity
     * @param bundle 传输数据的封装体
     */
    public void sendData(Class c, Bundle bundle){
        if(listenerMap.containsKey(c.getSimpleName())){
            sendMessage(bundle);
            Log.d(TAG,"在"+c.getSimpleName()+"注册了接收的入口");
        }else{
            Log.d(TAG,"没有注册接收的入口");
        }
    }
    private static HashMap<String, EventMS.OnPostEventListener> listenerMap= new HashMap<>();
    /**
     *  接收的注册
     * @param c
     * @param listener
     */

    public void receiveData(Class c,OnPostEventListener listener){
    //        设置数据->将目标类放入列表中
        sendKey=c.getSimpleName();
        listenerMap.put(c.getSimpleName(),listener);

    }
  private void sendMessage(Bundle bundle){
      //设置传递参数。
      Message message = handler.obtainMessage(1);
      message.setData(bundle);
      handler.sendMessageDelayed(message, 0);
    }
    public interface OnPostEventListener {
        void onPostInformation(Bundle bundle);
    }
}
