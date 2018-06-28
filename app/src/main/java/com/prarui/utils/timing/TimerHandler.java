package com.prarui.utils.timing;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Prarui on 2017/11/29.
 * 这个函数的封装是实现在handler中进行自动计时；
 *
 */

public class TimerHandler {
    private OnTimerHandlerListener listener = null;
    private int startTime = 0;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    startTime++;
                    if (null != listener) {
                     listener.onTimer(startTime+"");
                    }
                    startTime();
                    break;
                    case 1:
                        if (null != listener) {
                            listener.endTimer(startTime+"");
                        }
                        startTime=0;
                        handler.removeMessages(0);
                        break;
            }

            return false;
        }
    });

    public void startTimer(OnTimerHandlerListener listener) {
        this.listener = listener;
        startTime();
    }
    private void startTime(){
        Message message = handler.obtainMessage();
        message.what = 0;
        handler.sendMessageDelayed(message, 1000);
    }

    public void endTimer() {
        Message message = handler.obtainMessage();
        message.what = 1;
        handler.sendMessageDelayed(message, 0);
    }

    public interface OnTimerHandlerListener {
        void onTimer(String time);

        void endTimer(String endTime);
    }
}
