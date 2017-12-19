package com.itheima.enginee;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThinkPad on 2017-06-06.
 */

public class AppEnginee {

    public static List<AppInfo> getAppInfos(Context context) {

        List<AppInfo> list = new ArrayList<AppInfo>();
        PackageManager pm= context.getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            String packageName = packageInfo.packageName;
            String versionName = packageInfo.versionName;
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            Drawable drawable = applicationInfo.loadIcon(pm);
            String name = applicationInfo.loadLabel(pm).toString();
            int flags = applicationInfo.flags;
            boolean isUser ;
            if ((applicationInfo.FLAG_SYSTEM & flags) == applicationInfo.FLAG_SYSTEM) {
                isUser = false;
            } else {
                isUser = true;
            }
            boolean isSD;
            if ((applicationInfo.FLAG_EXTERNAL_STORAGE & flags) == applicationInfo.FLAG_EXTERNAL_STORAGE) {
                isSD = true;
            } else {
                isSD = false;
            }
            AppInfo appInfo = new AppInfo(name, drawable, packageName, versionName, isSD, isUser);
            list.add(appInfo);
        }

        return list;
    }
}
