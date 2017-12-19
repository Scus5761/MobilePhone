package com.itheima.enginee;

import android.graphics.drawable.Drawable;

/**
 * Created by ThinkPad on 2017-06-07.
 */
public class TaskInfo {

    private String name;
    private Drawable drawable;
    private long ramSize;
    private String packageName;
    private boolean isUser;
    private boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public TaskInfo() {
        super();
    }

    public TaskInfo(String name, Drawable drawable, long ramSize, String packageName, boolean isUser) {
        this.name = name;
        this.drawable = drawable;
        this.ramSize = ramSize;
        this.packageName = packageName;
        this.isUser = isUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public long getRamSize() {
        return ramSize;
    }

    public void setRamSize(long ramSize) {
        this.ramSize = ramSize;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
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
