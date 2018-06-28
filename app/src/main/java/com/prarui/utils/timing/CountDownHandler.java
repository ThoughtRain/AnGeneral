package com.prarui.utils.timing;

import android.os.Handler;
import android.os.Message;

/**
 * Created by prarui on 2017/4/10.
 * Class of this class is a countdown, often used to verify send or other countdown,
 * has carried on the packaging will be the result of the callback, easy to use.
 * 用于倒计时的工具类；
 */

public class CountDownHandler {
    private int time = 10;

    public void setTime(int time) {
        this.time = time;
    }

    private boolean isCanClickTimer = true;

    public boolean isCanClickTimer() {
        return isCanClickTimer;
    }

    public void setCanClickTimer(boolean canClickTimer) {
        isCanClickTimer = canClickTimer;
    }

    private OnCountDownHandlerOverListener listener = null;

    public void setOnCountDownHandlerOverListener(OnCountDownHandlerOverListener listener) {
        this.listener = listener;
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    time--;
                    if (null != listener) {
                        listener.onTick(time);
                    }
                    isCanClickTimer = false;
                    if (time > 0) {
                        setHandlerData();
                    } else {
                        isCanClickTimer = true;
                        handler.removeMessages(1);
                        if (null != listener) {
                            listener.onFinish();
                        }
                    }
                    break;
                case 2:
                    handler.removeMessages(1);
                    break;

            }
            return false;

        }
    });

    //获取一个对象；
    public void setHandlerData() {
        Message message = handler.obtainMessage(1);
        handler.sendMessageDelayed(message, 1000);
    }

    public void startTime(int time) {
        if (isCanClickTimer()) {
            setCanClickTimer(false);
            setTime(time);
            setHandlerData();
        }
    }

    public void stopTime() {
        Message message = handler.obtainMessage(2);
        handler.sendMessageDelayed(message, 0);
    }

    //默认开始
    public void startTime() {
        if (isCanClickTimer()) {
            setCanClickTimer(false);
            setTime(10);
            setHandlerData();
        }
    }

    public interface OnCountDownHandlerOverListener {

        void onTick(int time);

        /**
         * Callback fired when the time is up.
         */
        void onFinish();

    }
}
