package com.itheima.mobile;

import android.app.Application;

import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * Created by ThinkPad on 2017-06-15.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Thread.currentThread().setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());

    }

    private class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {

            try {
                e.printStackTrace(new PrintStream(getFilesDir().getAbsoluteFile()));
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

}
