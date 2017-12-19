package com.itheima.find;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import com.itheima.mobile.R;


public class setStepActivity1 extends findParent {
    private SharedPreferences sp;
    private SIM sim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepset1);
        sp = getSharedPreferences("config",MODE_PRIVATE);
        sim = (SIM) findViewById(R.id.sim_selfview);

        sim.setTitle("点击绑定SIM卡");

        if (sp.getBoolean("sim", true)) {
            sim.setDes("SIM卡绑定");
            sim.setChecked(true);
        } else {
            sim.setDes("SIM卡没有绑定");
            sim.setChecked(false);
        }

        sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                if (sim.isChecked()) {
                    sim.setDes("SIM卡没有绑定");
                    sim.setChecked(false);
                    editor.putBoolean("sim", false);
                    editor.putString("number", "");
                } else {
                    TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String number = tm.getSimSerialNumber();
                    sim.setDes("绑定SIM卡");
                    sim.setChecked(true);
                    editor.putBoolean("sim", true);
                    editor.putString("number", number);
                }
                editor.commit();
            }
        });

    }

    @Override
    public void next_activity() {
        if (sim.isChecked()) {
            Intent intent = new Intent(this, setStepActivity2.class);
            startActivity(intent);
            this.finish();
            overridePendingTransition(R.anim.setstep_next_anima, R.anim.setstep_exit_anima);
            return;
        }
        Toast.makeText(this, "請先綁定SIM卡", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void pre_activity() {
        Intent intent = new Intent(this, setStepActivity.class);
        startActivity(intent);
        this.finish();
        overridePendingTransition(R.anim.setstep_pre_anima, R.anim.setstep_exit_pre_anima);
    }

}
