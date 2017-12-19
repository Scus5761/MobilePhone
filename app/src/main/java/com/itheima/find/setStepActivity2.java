package com.itheima.find;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.itheima.mobile.R;

public class setStepActivity2 extends findParent {
    public   EditText et_find_step2;
    public   SharedPreferences sp;
    public   String safenumber;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_stepset2);

        et_find_step2 = (EditText) findViewById(R.id.et_find_step2);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        et_find_step2.setText(sp.getString("safenumber", ""));

    }

    @Override
    public void next_activity() {
        safenumber = et_find_step2.getText().toString().trim();
        if (TextUtils.isEmpty(safenumber)) {
            Toast.makeText(this, "請輸入安全號碼", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("safenumber", safenumber);
        edit.commit();
        Intent intent = new Intent(this, setStepActivity3.class);
        startActivity(intent);
        this.finish();
        overridePendingTransition(R.anim.setstep_next_anima, R.anim.setstep_exit_anima);
    }

    @Override
    public void pre_activity() {
        Intent intent = new Intent(this, setStepActivity1.class);
        startActivity(intent);
        this.finish();
        overridePendingTransition(R.anim.setstep_pre_anima, R.anim.setstep_exit_pre_anima);
    }

    public void select(View v) {
       /* Intent intent = new Intent(this, ContactsActivity.class);
        startActivityForResult(intent, 0);*/

        Intent intent = new Intent();
        intent.setAction("android.intent.action.PICK");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("vnd.android.cursor.dir/phone_v2");
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      /*  if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String num = data.getStringExtra("num");
                et_find_step2.setText(num);
            }
        }*/

        if(data !=null){
            //	String num = data.getStringExtra("num");
            Uri uri = data.getData();
            String num = null;
            // 创建内容解析者
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(uri,
                    null, null, null, null);
            while(cursor.moveToNext()){
                num = cursor.getString(cursor.getColumnIndex("data1"));

            }
            cursor.close();
            num = num.replaceAll("-", "");
            et_find_step2.setText(num);
        }

    }
}
