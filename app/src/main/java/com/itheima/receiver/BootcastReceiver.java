package com.itheima.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * Created by ThinkPad on 2017-05-25.
 */

public class BootcastReceiver extends BroadcastReceiver {
   private SharedPreferences sp;
    @Override
    public void onReceive(Context context, Intent intent) {


        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        String number = sp.getString("number", "13929704684");

        if (sp.getBoolean("protected", false)) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String number1 = tm.getSimSerialNumber();
            String safenumber = sp.getString("safenumber", "");
            if (!TextUtils.isEmpty(safenumber)) {

                if (TextUtils.isEmpty(number) || TextUtils.isEmpty(number1)) {
                    if (!number.equals(number1)) {
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(safenumber,null,"手机被盗",null,null);
                    }
                }
            }
        }

    }
}
