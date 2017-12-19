package com.itheima.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import com.itheima.mobile.R;
import com.itheima.service.LocationService;

/**
 * Created by ThinkPad on 2017-05-26.
 */

public class SmsReceiver extends BroadcastReceiver{

    private static  MediaPlayer mediaPlayer;
    private DevicePolicyManager policyManager;


    @Override
    public void onReceive(Context context, Intent intent) {

        policyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName name = new ComponentName(context, Admin.class);
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for (Object obj : objs) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
            String body = smsMessage.getMessageBody();
            String sender = smsMessage.getOriginatingAddress();

            if ("#*location*#".equals(body)) {
                Log.e("SmsReceiver", "开启位置定位");
                Intent intent_location = new Intent(context, LocationService.class);
                context.startService(intent_location);

            } else if ("#*alarm*#".equals(body)) {
                Log.e("SmsReceiver", "音乐报警");
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
              /*  mediaPlayer.setVolume(1.0f,1.0f);
                mediaPlayer.setLooping(true);*/
                mediaPlayer.start();

            } else if ("#*wipedata*#".equals(body)) {
                Log.e("SmsReceiver", "远程删除数据");
                if (policyManager.isAdminActive(name)) {
                    policyManager.wipeData(0);
                }
            } else if ("*#lockscreen*#".equals(body)) {
                Log.e("SmsReceiver", "远程锁屏");
                if (policyManager.isAdminActive(name)) {
                    policyManager.lockNow();
                }

            }
        }
    }
}
