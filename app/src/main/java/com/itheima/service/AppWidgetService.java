package com.itheima.service;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.itheima.mobile.R;
import com.itheima.receiver.MyWidget;
import com.itheima.utils.TaskUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ThinkPad on 2017-06-09.
 */

public class AppWidgetService extends Service {

    private AppWidgetManager widgetManager;
    private Timer timer;
    private WidgetReceiver widgetReceiver;
    private IntentFilter intentFilter;
    private ScreenOffReceiver screenOffReceiver;
    private IntentFilter intentFilter_screen;
    private ScreenOnReceiver screenOnReceiver;


    public void onCreate() {
        super.onCreate();
        widgetManager = AppWidgetManager.getInstance(this);

        widgetReceiver = new WidgetReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("abc");
        registerReceiver(widgetReceiver, intentFilter);

        screenOffReceiver = new ScreenOffReceiver();
        intentFilter_screen = new IntentFilter();
        intentFilter_screen.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenOffReceiver, intentFilter_screen);

        screenOnReceiver = new ScreenOnReceiver();
        IntentFilter intentFilter_on = new IntentFilter();
        intentFilter_on.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(screenOffReceiver, intentFilter_on);

        updateAppWidget();

    }

    private class WidgetReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            killProcess();
        }
    }

    private void killProcess() {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : runningAppProcesses) {
            if (!info.processName.equals((getPackageName()))) {
                am.killBackgroundProcesses(info.processName);
            }
        }
//        updateAppWidget();
    }

    private void updateAppWidget() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override

            public void run() {

                ComponentName componentName = new ComponentName(AppWidgetService.this, MyWidget.class);
                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.process_widget);
                remoteViews.setTextViewText(R.id.process_count, "正在运行的进程:" + TaskUtil.getProcessCount(AppWidgetService.this));
                remoteViews.setTextViewText(R.id.process_memory, "可用内存: " + Formatter.formatFileSize(AppWidgetService.this, TaskUtil.getAvaiableRam(AppWidgetService.this)));

                Intent intent = new Intent();
                intent.setAction("abc");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(AppWidgetService.this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                remoteViews.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
                widgetManager.updateAppWidget(componentName, remoteViews);

            }
        }, 2000, 2000);
    }

    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (widgetReceiver != null) {
            unregisterReceiver(widgetReceiver);
            widgetReceiver = null;
        }
        if (screenOffReceiver != null) {
            unregisterReceiver(screenOffReceiver);
            screenOffReceiver = null;
        }
        if (screenOnReceiver!= null) {
            unregisterReceiver(screenOnReceiver);
            screenOnReceiver = null;
        }

    }

    public class ScreenOffReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            killProcess();
            if (timer != null) {
                timer.cancel();
                timer = null;
            }

        }
    }

    public class ScreenOnReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateAppWidget();
        }
    }

}
