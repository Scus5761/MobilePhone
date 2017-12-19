package com.itheima.enginee;

import android.graphics.drawable.Drawable;

/**
 * Created by ThinkPad on 2017-06-06.
 */
public class AppInfo {

    private String name;
    private Drawable icon;
    private String packageName;
    private String versionName;
    private boolean isSD;
    private boolean isUser;

    public AppInfo() {
        super();
    }

    public AppInfo(String name, Drawable icon, String packageName,
                   String versionName, boolean isSD, boolean isUser) {
        this.name = name;
        this.icon = icon;
        this.packageName = packageName;
        this.versionName = versionName;
        this.isSD = isSD;
        this.isUser = isUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public boolean isSD() {
        return isSD;
    }

    public void setSD(boolean SD) {
        isSD = SD;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
