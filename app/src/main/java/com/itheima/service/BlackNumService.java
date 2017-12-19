package com.itheima.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.itheima.db.Dao.BlackNumDao;

import java.lang.reflect.Method;

public class BlackNumService extends Service {

    private MyBlackNumReceiver receiver;
    private TelephonyManager manager;
    private Myphonestatelistener myphonestatelistener;
    private BlackNumDao dao;

    @Override
    public void onCreate() {
        super.onCreate();
        dao = new BlackNumDao(getApplicationContext());

        receiver = new MyBlackNumReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(101);
        registerReceiver(receiver, filter);
        manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        myphonestatelistener = new Myphonestatelistener();
        manager.listen(myphonestatelistener, PhoneStateListener.LISTEN_CALL_STATE);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        manager.listen(myphonestatelistener, PhoneStateListener.LISTEN_NONE);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyBlackNumReceiver extends BroadcastReceiver {
        String sender;

        public void onReceive(Context context, Intent intent) {
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : objs) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String body = smsMessage.getMessageBody();
                sender = smsMessage.getOriginatingAddress();

                int mode = dao.queryBlackNumMode(sender);

                switch (mode) {
                    case BlackNumDao.ALL:
                        abortBroadcast();
                        break;
                    case BlackNumDao.CALL:
                        break;
                    case BlackNumDao.SMS:
                        abortBroadcast();
                        break;
                }
            }
        }
    }

    class Myphonestatelistener extends PhoneStateListener {
        Uri uri;
        @Override
        public void onCallStateChanged(int state, final String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                int mode = dao.queryBlackNumMode(incomingNumber);
                if (mode == BlackNumDao.ALL || mode == BlackNumDao.CALL) {
                    endCall();

                    final ContentResolver resolver = getContentResolver();
                    uri = Uri.parse("content://call_log/calls");
                    //内容观察者，观察内容分提供者数据变化
                    resolver.registerContentObserver(uri, true, new ContentObserver(new Handler()) {
                        @Override
                        public void onChange(boolean selfChange) {
                            super.onChange(selfChange);
                            resolver.delete(uri, "number=?", new String[]{incomingNumber});
                            resolver.unregisterContentObserver(this);
                        }
                    });
                }
            }
        }

        private void endCall() {

            try {
                Class<?> loadClass= BlackNumService.class.getClassLoader().loadClass("android.os.ServiceManager");
                Method method = loadClass.getDeclaredMethod("getService", String.class);
                IBinder invoke = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
                ITelephony iTelephony=ITelephony.Stub.asInterface(invoke);
                iTelephony.endCall();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
