package com.itheima.enginee;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;


public class SmsEnginee {

    public interface ShowProgress {
        public void setProgress(int progress);
        public void setMax(int max);
    }

    public static void getAllSms(Context context, ShowProgress showProgress) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri.parse("content://sms");
        Cursor cursor = contentResolver.query(uri, new String[]{"address", "date", "type", "body"}, null, null, null);

        int count = cursor.getCount();
        showProgress.setMax(count);

        int progress = 0;

        XmlSerializer xmlSerializer = Xml.newSerializer();
        File file = new File("/mnt/sdcard/backupsms.xml");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            xmlSerializer.setOutput(fileOutputStream, "utf-8");
            xmlSerializer.startDocument("utf-8", true);
            xmlSerializer.startTag(null, "smss");

            while (cursor.moveToNext()) {
                SystemClock.sleep(10);
                xmlSerializer.startTag(null, "sms");

                xmlSerializer.startTag(null, "address");
                String address = cursor.getString(0);
                xmlSerializer.text(address);
                xmlSerializer.endTag(null, "address");

                xmlSerializer.startTag(null, "date");
                String date = cursor.getString(1);
                xmlSerializer.text(date);
                xmlSerializer.endTag(null, "date");

                xmlSerializer.startTag(null, "type");
                String type = cursor.getString(2);
                xmlSerializer.text(type);
                xmlSerializer.endTag(null, "type");

                xmlSerializer.startTag(null, "body");
                String body = cursor.getString(3);
                xmlSerializer.text(body);
                xmlSerializer.endTag(null, "body");

                xmlSerializer.endTag(null, "sms");

                progress++;
                showProgress.setProgress(progress);

            }

            xmlSerializer.endTag(null, "smss");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
