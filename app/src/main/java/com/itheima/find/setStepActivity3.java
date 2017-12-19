package com.itheima.find;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.itheima.mobile.R;

/**
 * Created by ThinkPad on 2017-05-23.
 */

public class setStepActivity3 extends findParent {
    private SharedPreferences sp;
    private CheckBox checkBox2;
    private  SharedPreferences.Editor edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_stepset3);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        edit = sp.edit();
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        if (sp.getBoolean("protected", false)) {
            checkBox2.setChecked(true);
            checkBox2.setText("你已经开启防盗功能");
        } else {
            checkBox2.setChecked(false);
            checkBox2.setText("你没有开启防盗功能");
        }
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBox2.setText("你已经开启防盗保护");
                    checkBox2.setChecked(true);
                    edit.putBoolean("protected", true);

                } else {
                  checkBox2.setText("你还没开启防盗保护");
                    checkBox2.setChecked(false);
                    edit.putBoolean("protected", false);
                }
                edit.commit();
            }
        });
    }


    @Override
    public void next_activity() {

        edit.putBoolean("first", false);
        edit.commit();
        Intent intent = new Intent(this, LostFindActivity.class);
        startActivity(intent);
        this.finish();
        overridePendingTransition(R.anim.setstep_next_anima, R.anim.setstep_exit_anima);
    }

    @Override
    public void pre_activity() {
        Intent intent = new Intent(this, setStepActivity2.class);
        startActivity(intent);
        this.finish();
        overridePendingTransition(R.anim.setstep_pre_anima, R.anim.setstep_exit_pre_anima);
    }
}