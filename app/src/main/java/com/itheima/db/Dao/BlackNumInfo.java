package com.itheima.db.Dao;

/**
 * Created by ThinkPad on 2017-06-03.
 */
public class BlackNumInfo {
    private String blacknum;
    private int mode;


    public BlackNumInfo() {
        super();
    }
    public BlackNumInfo(String blacknum, int mode) {
        this.blacknum = blacknum;
        this.mode = mode;
    }

    public String getBlacknum() {
        return blacknum;
    }

    public void setBlacknum(String blacknum) {
        this.blacknum = blacknum;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        if (mode >= 0 && mode <= 2) {
            this.mode = mode;
        }
             mode = 0;
    }

    @Override
    public String toString() {
        return super.toString();
    }


}
