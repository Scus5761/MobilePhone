package com.itheima.db.Dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThinkPad on 2017-06-02.
 */

public class WatchDogDao {

    private WatchDogOpenHelper helper;
    private Context context;
    public WatchDogDao(Context context) {
        helper = new WatchDogOpenHelper(context);
        this.context = context;
    }

    public void addLockApp(String packagename) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("packagename", packagename);
        db.insert(WatchDogOpenHelper.DB_NAME, null, values);

        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.itheima.mobile.lock.changed");
        contentResolver.notifyChange(uri, null);

        db.close();
    }

    public void deleteLockApp(String  packagename) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(WatchDogOpenHelper.DB_NAME, "packagename=?", new String[]{packagename});

        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.itheima.mobile.lock.changed");
        contentResolver.notifyChange(uri, null);

        db.close();
    }

    public boolean queryLockApp(String  packagename) {
        boolean isLock = false;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(WatchDogOpenHelper.DB_NAME, null, "packagename=?", new String[]{packagename}, null, null, null);
        if (cursor.moveToNext()) {
            isLock = true;
        }
        cursor.close();
        db.close();
        return isLock;
    }

    public List<String> queryAllLockApp() {
        List<String> list = new ArrayList<String>();
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor query = database.query(WatchDogOpenHelper.DB_NAME, new String[]{"packagename"}, null, null, null, null, null);
        while (query.moveToNext()) {
            String packagename = query.getString(0);
            list.add(packagename);
        }
        query.close();
        database.close();
        return list;
    }
}
