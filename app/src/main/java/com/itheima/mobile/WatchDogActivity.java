package com.itheima.mobile;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ThinkPad on 2017-06-11.
 */

public class WatchDogActivity extends Activity {

    private TextView tv_watchdog_name;
    private ImageView iv_watchdog_icon;
    private Button btn_watchdog;
    private String packageName;
    private EditText ed_watchdog_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchdog);
        tv_watchdog_name = (TextView) findViewById(R.id.tv_watchdog_name);
        iv_watchdog_icon = (ImageView) findViewById(R.id.iv_watchdog_icon);
        btn_watchdog = (Button) findViewById(R.id.btn_watchdog);
        ed_watchdog_password = (EditText) findViewById(R.id.ed_watchdog_password);
        PackageManager pm = getPackageManager();
        Intent intent = getIntent();
        packageName = intent.getStringExtra("packageName");
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
            Drawable drawable = applicationInfo.loadIcon(pm);
            String name = applicationInfo.loadLabel(pm).toString();
            iv_watchdog_icon.setImageDrawable(drawable);
            tv_watchdog_name.setText(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.Home");
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    public void click(View view) {
        String password = ed_watchdog_password.getText().toString().trim();
        if ("123".equals(password)) {
            Intent intent = new Intent();
            intent.setAction("com.itheima.mobile.WatchDogActivity");
            intent.putExtra("packageName", packageName);
            sendBroadcast(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "密码错误，请重输入", Toast.LENGTH_SHORT).show();
        }

    }
}
