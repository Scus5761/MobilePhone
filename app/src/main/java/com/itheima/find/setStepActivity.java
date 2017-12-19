package com.itheima.find;

import android.content.Intent;
import android.os.Bundle;

import com.itheima.mobile.R;

public class setStepActivity extends findParent {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepset);
    }

    @Override
    public void next_activity() {
        Intent intent = new Intent(this, setStepActivity1.class);
        startActivity(intent);
        this.finish();
        overridePendingTransition(R.anim.setstep_next_anima, R.anim.setstep_exit_anima);
    }

    @Override
    public void pre_activity() {


    }

}
