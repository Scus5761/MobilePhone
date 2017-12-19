package com.itheima.db.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BlackNumHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "info";

    public BlackNumHelper(Context context) {

        super(context, "blacknum.db", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+DB_NAME+"(_id integer primary key autoincrement,blacknum varchar(20),mode varchar(2))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
