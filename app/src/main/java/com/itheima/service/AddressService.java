package com.itheima.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.itheima.mobile.R;


/**
 * Created by ThinkPad on 2017-05-30.
 */

public class AddressService extends Service {

    private TelephonyManager telephonyManager;
    private MyPhoneStateListener listener;
    private WindowManager manager;
    //  private TextView tv;
    private TextView tv_toast_custom;
    private View view;
    private MyBroadCastReceiver receiver;
    private IntentFilter filter;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private WindowManager.LayoutParams params;
    private int widthPixels;
    private int heightPixels;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);


        receiver = new MyBroadCastReceiver();

        filter = new IntentFilter();
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        registerReceiver(receiver, filter);
        manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        heightPixels = metrics.heightPixels;
        widthPixels = metrics.widthPixels;

    }

    public class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    hideToast();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:

                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    //String address = AddressDB.queryAddress(incomingNumber, getApplicationContext());
                    String Address = "广东茂名";
                    showToast(Address);
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }

        private void hideToast() {
            if (manager != null | view != null) {
                manager.removeView(view);
            }
        }
    }

    public void showToast(String Addresss) {
        sp = getSharedPreferences("config", MODE_PRIVATE);
        int[] bgcolors = {R.drawable.btn, R.drawable.btn_green_normal, R.drawable.btn, R.drawable.btn_green_normal, R.drawable.btn};
      /*  tv = new TextViewgetApplictionContext());
        tv.setText(Addresss);*/

        view = View.inflate(getApplicationContext(), R.layout.toast_custom, null);
        tv_toast_custom = (TextView) view.findViewById(R.id.tv_toast_custom);
        tv_toast_custom.setText(Addresss);
        view.setBackgroundResource(bgcolors[sp.getInt("which", 0)]);
        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = sp.getInt("X", 60);
        params.y = sp.getInt("Y", 100);
//        manager.addView(tv, params);
        manager.addView(view, params);
        setTouch();
    }

    private void setTouch() {
        view.setOnTouchListener(new View.OnTouchListener() {
            private int startY;
            private int startX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        int newX = (int) event.getRawX();
                        int newY = (int) event.getRawY();
                        int dX = newX - startX;
                        int dY = newY - startY;
                        params.x += dX;
                        params.y += dY;
                        if (params.x < 0) {
                            params.x = 0;
                        }
                        if (params.y < 0) {
                            params.y = 0;
                        }
                        if (params.x > (widthPixels - view.getHeight())) {
                            params.x = widthPixels - view.getHeight();
                        }
                        if (params.y > (heightPixels - 25 - view.getHeight())) {
                            params.y = heightPixels - 25 - view.getHeight();
                        }
                        manager.updateViewLayout(view, params);

                        startX = newX;
                        startY = newY;
                        break;

                    case MotionEvent.ACTION_UP:
                        int X = params.x;
                        int Y = params.y;
                        editor = sp.edit();
                        editor.putInt("X", X);
                        editor.putInt("Y", Y);
                        editor.commit();
                        break;
                }
                return false;
            }

        });

    }

    @Override
    public void onDestroy() {
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public class MyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String data = getResultData();
//            String address = AddressDB.queryAddress(data, getApplicationContext());
            String Address = "北京（数据库信息暂时还没实现）";
            showToast(Address);
        }
    }

}
