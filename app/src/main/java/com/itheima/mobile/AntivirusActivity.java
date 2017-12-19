package com.itheima.mobile;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itheima.utils.MD5;

import java.util.List;

/**
 * Created by ThinkPad on 2017-06-13.
 */

public class AntivirusActivity extends Activity {
    private ImageView iv_antivrus_scanner;
    private ProgressBar pb_antivirus_progressbar;
    private TextView tv_antivirus_text;
    private PackageManager pm;
    private LinearLayout ll_antivirus_safeapk;


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PackageInfo packageInfo = (PackageInfo) msg.obj;
            String name = packageInfo.applicationInfo.loadLabel(pm).toString();

            TextView textView = new TextView(getApplicationContext());
            textView.setText(name);

            Signature[] signatures = packageInfo.signatures;
            String string = signatures[0].toCharsString();
            String md5signature= MD5.Md5Password(string);
           /* boolean ishave = AnitivirusDB.queryAnitivirus(AntivirusActivity.this, md5signature);
            if (ishave) {
                textView.setTextColor(Color.RED);
                textView.setTextSize(18);
            } else {
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(16);
            }*/
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(18);
            ll_antivirus_safeapk.addView(textView,0);
            tv_antivirus_text.setText("正在扫描" + name);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antivirus);
        iv_antivrus_scanner = (ImageView) findViewById(R.id.iv_antivrus_scanner);
        pb_antivirus_progressbar = (ProgressBar) findViewById(R.id.pb_antivirus_progressbar);
        tv_antivirus_text = (TextView) findViewById(R.id.tv_antivirus_text);
        ll_antivirus_safeapk = (LinearLayout) findViewById(R.id.ll_antivirus_safeapk);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(2000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        rotateAnimation.setInterpolator(linearInterpolator);
        iv_antivrus_scanner.setAnimation(rotateAnimation);
        scanner();
    }

    private void scanner() {

        pm = getPackageManager();
        tv_antivirus_text.setText("正在初始化64核大引擎...");
        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(100);
                List<PackageInfo> installedPackages = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
                pb_antivirus_progressbar.setMax(installedPackages.size());
                int count = 0;
                for (PackageInfo packageInfo : installedPackages) {
                    SystemClock.sleep(100);
                    count++;
                    Message message = new Message();
                    message.obj = packageInfo;
                    handler.sendMessage(message);
                    pb_antivirus_progressbar.setProgress(count);

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_antivirus_text.setText("扫描完成，未发现病毒！");
                        iv_antivrus_scanner.clearAnimation();
                    }
                });
            }
        }.start();


    }
}
