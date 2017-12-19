package com.itheima.ui;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public abstract class MyAsycnTask {
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            postTak();
        }
    };

    public abstract void preTak() ;
    public abstract void postTak();
    public abstract void doinback();



    public void execute() {
        preTak();
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(500);
                doinback();
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

}
