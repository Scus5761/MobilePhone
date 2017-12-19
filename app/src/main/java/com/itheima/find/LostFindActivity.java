package com.itheima.find;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.mobile.R;

public class LostFindActivity extends Activity {

    private SharedPreferences sp;
    private  TextView tv_lost_number;
    private ImageView iv_lost_lock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        if (sp.getBoolean("first", true)) {
            Intent intent = new Intent(this,setStepActivity.class);
            startActivity(intent);
            this.finish();
        } else {
            setContentView(R.layout.activity_lostfind);
            tv_lost_number = (TextView) findViewById(R.id.tv_lost_number);
            tv_lost_number.setText(sp.getString("safenumber", ""));
            iv_lost_lock = (ImageView) findViewById(R.id.iv_lost_lock);
            if (sp.getBoolean("protected", false)) {
                iv_lost_lock.setImageResource(R.drawable.lock);
            }
        }
    }

    public void restartstep(View view) {
        Intent intent = new Intent(this, setStepActivity.class);
        startActivity(intent);
        finish();
    }
}
