package com.itheima.enginee;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThinkPad on 2017-06-07.
 */

public class TaskEnginee {

    public static List<TaskInfo> getTaskInfo(Context context){

        List<TaskInfo> list = new ArrayList<TaskInfo>();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = context.getPackageManager();
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo runningAppProcessinfo : runningAppProcesses) {
            TaskInfo taskInfo = new TaskInfo();
            String packageName = runningAppProcessinfo.processName;
            Debug.MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(new int[]{runningAppProcessinfo.pid});
            int totalPss = processMemoryInfo[0].getTotalPss();
            long ramSize = totalPss * 1024;

            taskInfo.setPackageName(packageName);
            taskInfo.setRamSize(ramSize);

            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
                Drawable drawable = applicationInfo.loadIcon(pm);
                String name = applicationInfo.loadLabel(pm).toString();
                boolean isUser;
                int flags = applicationInfo.flags;
                if ((applicationInfo.FLAG_SYSTEM & flags) == applicationInfo.FLAG_SYSTEM) {
                    isUser = false;
                } else {
                    isUser = true;
                }

                taskInfo.setDrawable(drawable);
                taskInfo.setName(name);
                taskInfo.setUser(isUser);

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            list.add(taskInfo);
        }
        return list;
    }

}
