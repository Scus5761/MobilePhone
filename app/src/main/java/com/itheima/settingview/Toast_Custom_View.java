package com.itheima.settingview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.mobile.R;

public class Toast_Custom_View extends RelativeLayout {
    public TextView tv_home_update;
    public TextView tv_home_closeupdate;
    public CheckBox cb_setting_checkBox;


    public Toast_Custom_View(Context context) {
        super(context);
        init();
    }

    public Toast_Custom_View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public Toast_Custom_View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private  void init() {

        View view = View.inflate(getContext(), R.layout.toast_custom_view, this);

        tv_home_update = (TextView) view.findViewById(R.id.tv_home_update);
        tv_home_closeupdate = (TextView) view.findViewById(R.id.tv_home_closeupdate);
        cb_setting_checkBox = (CheckBox) findViewById(R.id.cb_setting_checkBox);
    }

    public void setTitle(String title) {
        tv_home_update.setText(title);

    }

    public void setDes(String des) {
        tv_home_closeupdate.setText(des);
    }

    public void setChecked(boolean isChecked) {
        cb_setting_checkBox.setChecked(isChecked);
    }

    public boolean isChecked() {
        return cb_setting_checkBox.isChecked();
    }

}
