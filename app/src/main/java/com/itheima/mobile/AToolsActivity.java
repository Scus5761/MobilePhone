package com.itheima.mobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.itheima.enginee.SmsEnginee;

public class AToolsActivity extends Activity {
    private ProgressDialog progressDialog;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }

    public void query(View v) {
        Intent intent = new Intent(this, AddressActivity.class);
        startActivity(intent);
    }

    public void backupsms(View view) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
//        progressDialog.setMax();
//        progressDialog.setProgress();
        progressDialog.setTitle("短信备份");
        progressDialog.setMessage("短信备份中，请稍等！");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        new Thread() {
            @Override
            public void run() {
                SmsEnginee.getAllSms(getApplicationContext(), new SmsEnginee.ShowProgress() {
                    @Override
                    public void setProgress(int progress) {
                        progressDialog.setProgress(progress);
                    }

                    @Override
                    public void setMax(int max) {
                        progressDialog.setMax(max);
                    }
                });
                progressDialog.dismiss();
            }
        }.start();
    }

    public void restoresms(View view) {
//        XmlPullParser xmlPullParser = Xml.newPullParser();

        ContentResolver contentResolver = getContentResolver();
        Uri uri = Uri.parse("content://sms");
        ContentValues values = new ContentValues();
        values.put("address","110");
        values.put("date", System.currentTimeMillis());
        values.put("type", 1);
        values.put("body", "songgeini $ 1000000000000000000");
//        contentResolver.insert(uri, values);
        AlertDialog.Builder builder= new AlertDialog.Builder(AToolsActivity.this);
        builder.setMessage("安卓系统一禁止开发者向短信数据库中插入数据，需将应用变成短信应用");
        builder.show();
    }

}
