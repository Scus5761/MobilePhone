package com.itheima.db.Dao;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;


public class AnitivirusDB extends Activity {

    public static boolean queryAnitivirus(Context context,String md5) {

        File file = new File(context.getFilesDir(), "antivirus.db");
        SQLiteDatabase database = SQLiteDatabase.openDatabase(file.getAbsolutePath(),
                null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = database.query("datable", null, "md5=?", new String[]{md5}, null, null, null);
        boolean ishave = false;
        if (cursor.moveToNext()) {
            ishave=true;
        }
        cursor.close();
        database.close();
        return ishave;
    }

}
