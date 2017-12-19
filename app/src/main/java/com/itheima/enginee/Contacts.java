package com.itheima.enginee;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ThinkPad on 2017-05-25.
 */

public class Contacts {


    public static List<HashMap<String, String>> getAllContacts(Context context) {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        ContentResolver resolver = context.getContentResolver();

        Uri raw_uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri data_uri = Uri.parse("content://com.android.contacts/data");
        Cursor cursor = resolver.query(raw_uri, new String[]{"contact_id"}, null, null, null);
        while (cursor.moveToNext()) {
            String contact_id = cursor.getString(0);
            if (!TextUtils.isEmpty(contact_id)) {
                Cursor cursor1 = resolver.query(data_uri, new String[]{"data1", "mimetype"}, "raw_contact_id=?", new String[]{"contact_id"}, null);
                HashMap<String, String> hash = new HashMap<String, String>();

                while (cursor1.moveToNext()) {
                    String data1 = cursor.getString(0);
                    String mimetype = cursor.getString(1);
                    if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
                        hash.put("phone", data1);
                    } else if (mimetype.equals("vnd.android.cursor.item/name")) {
                        hash.put("name", data1);
                    }
                    list.add(hash);
                    cursor1.close();
                }
            }
        }
        cursor.close();
        return list;
    }
}
