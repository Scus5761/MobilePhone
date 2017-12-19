package com.itheima.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.itheima.db.Dao.WatchDogDao;
import com.itheima.mobile.WatchDogActivity;

import java.util.List;

public class WatchDogService extends Service {

    private ActivityManager am;
    private ComponentName baseActivity;
    private WatchDogDao watchDogDao;
    private boolean istrue = false;
    private boolean lockApp;
    private UnlockCurrentReceiver unlockCurrentReceiver;
    private String UnlockCurrentpackageName;
    private ScreenOffrReceiver screenOffrReceiver;
    private List<String> list;


    private class UnlockCurrentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            UnlockCurrentpackageName = intent.getStringExtra("packageName");
        }
    }

    private class ScreenOffrReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            UnlockCurrentpackageName = null;
        }
    }


    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        watchDogDao = new WatchDogDao(getApplicationContext());
        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        unlockCurrentReceiver = new UnlockCurrentReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.itheima.mobile.WatchDogActivity");
        registerReceiver(unlockCurrentReceiver, intentFilter);

        screenOffrReceiver = new ScreenOffrReceiver();
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenOffrReceiver, intentFilter1);

        new Thread() {
            public void run() {
                istrue = true;
                Uri uri = Uri.parse("content://com.itheima.mobile.lock.changed");
                getContentResolver().registerContentObserver(uri, true, new ContentObserver(null) {
                    @Override
                    public void onChange(boolean selfChange) {
                        super.onChange(selfChange);
                        list = watchDogDao.queryAllLockApp();
                    }
                });
                list = watchDogDao.queryAllLockApp();
                while (istrue) {
                    List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1);
                    for (ActivityManager.RunningTaskInfo taskInfo : runningTasks) {
                        baseActivity = taskInfo.baseActivity;
                        String packageName = baseActivity.getPackageName();
//                        lockApp = watchDogDao.queryLockApp(packageName);
                        boolean contains = list.contains(packageName);
                        Log.d("WatchDOgService", "-----------" + packageName + "--------" + lockApp);
//                        System.out.print("---------------" +packageName+"-------"+ lockApp);
                        if (contains) {
                            if (!packageName.equals(UnlockCurrentpackageName)) {
                                Intent intent = new Intent(WatchDogService.this, WatchDogActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("packageName", packageName);
                                startActivity(intent);
                            }

                        }
                    }
                    SystemClock.sleep(300);
                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        istrue = false;

        if (unlockCurrentReceiver != null) {
            unregisterReceiver(unlockCurrentReceiver);
            unlockCurrentReceiver = null;
        }
        if (screenOffrReceiver != null) {
            unregisterReceiver(screenOffrReceiver);
            screenOffrReceiver = null;
        }
    }
}
