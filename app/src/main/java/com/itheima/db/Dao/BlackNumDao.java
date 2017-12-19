package com.itheima.db.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThinkPad on 2017-06-02.
 */

public class BlackNumDao {

    private BlackNumHelper helper;
    public static final int CALL = 0;
    public static final int SMS = 1;
    public static final int ALL = 2;

    public BlackNumDao(Context context) {
        helper = new BlackNumHelper(context);
    }

    public BlackNumDao() {

    }

    public void addBlackNum(String blacknum, int mode) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("blacknum", blacknum);
        values.put("mode", mode);
        db.insert(BlackNumHelper.DB_NAME, null, values);
        db.close();
    }

    public void updateBlackNum(String blacknum, String mode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode", mode);
        db.update(BlackNumHelper.DB_NAME, values, "blacknum=?", new String[]{blacknum});
        db.close();
    }

    public void deleteBlackNmu(String blacknum) {
        int mode = -1;
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(BlackNumHelper.DB_NAME, "blacknum=?", new String[]{blacknum});
        db.close();
    }

    public int queryBlackNumMode(String blacknum) {
        int mode = -1;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(BlackNumHelper.DB_NAME, new String[]{"mode"}, "balacknum=?",
                new String[]{blacknum}, null, null, "_id desc");
        if (cursor.moveToNext()) {
            mode = cursor.getInt(0);
        }
        return mode;
    }

    public List<BlackNumInfo> queryAllBlack() {
        List<BlackNumInfo> list = new ArrayList<BlackNumInfo>();
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor query = database.query(BlackNumHelper.DB_NAME, new String[]{"blacknum", "mode"}, null, null, null, null, null);
        while (query.moveToNext()) {
            String blacknum = query.getString(0);
            int mode = query.getInt(1);
            BlackNumInfo info = new BlackNumInfo(blacknum, mode);
            list.add(info);
        }
        query.close();
        database.close();
        return list;
    }

    public List<BlackNumInfo> getPartBlackNum(int MaxMum, int startindex) {

        List<BlackNumInfo> list = new ArrayList<BlackNumInfo>();
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor= database.rawQuery("select blacknum,mode from info order by _id desc limit ? offset ?", new String[]{MaxMum + "", startindex + ""});
        while (cursor.moveToNext()) {
            String blacknum = cursor.getString(0);
            int mode = cursor.getInt(1);
            BlackNumInfo info = new BlackNumInfo(blacknum, mode);
            list.add(info);
        }
        cursor.close();
        database.close();
        return list;
    }
}
