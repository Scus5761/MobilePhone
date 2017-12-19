package com.itheima.mobile;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ThinkPad on 2017-05-29.
 */

public class AddressActivity extends Activity {
    private EditText et_tool_address;
    private Button bt_tool_adress;
    private TextView tv_tool_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        et_tool_address = (EditText) findViewById(R.id.et_tool_address);
        bt_tool_adress = (Button) findViewById(R.id.bt_tool_adress);
        tv_tool_address = (TextView) findViewById(R.id.tv_tool_address);
        et_tool_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String phone = s.toString();
                if (TextUtils.isEmpty(phone)) {
                    Animation shake = AnimationUtils.loadAnimation(AddressActivity.this, R.anim.shake);
                    et_tool_address.startAnimation(shake);
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vibrator.vibrate(new long[]{20l, 10l, 20l, 20}, -1);

                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone = s.toString();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(AddressActivity.this, "请输入号码", Toast.LENGTH_SHORT).show();
                    Animation shake = AnimationUtils.loadAnimation(AddressActivity.this, R.anim.shake);
                    et_tool_address.setAnimation(shake);
                } else {
                    //      String address = AddressDB.queryAddress(phone, getApplicationContext());
                        String address="广东茂名";
                        tv_tool_address.setText(address);
                }

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void queryToolAddress(View view) {
        String phone = et_tool_address.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入号码", Toast.LENGTH_SHORT).show();
            Animation shake = AnimationUtils.loadAnimation(AddressActivity.this, R.anim.shake);
            et_tool_address.setAnimation(shake);

        } else {
            // String address = AddressDB.queryAddress(phone, getApplicationContext());
            String address="广东茂名";
            if (!TextUtils.isEmpty(address)) {
                tv_tool_address.setText(address);
            }

        }

    }
}
