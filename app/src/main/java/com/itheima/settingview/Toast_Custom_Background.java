package com.itheima.settingview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.mobile.R;

public class Toast_Custom_Background extends RelativeLayout {
    public TextView tv_home_update;
    public TextView tv_home_closeupdate;


    public Toast_Custom_Background(Context context) {
        super(context);
        init();
    }

    public Toast_Custom_Background(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public Toast_Custom_Background(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {

        View view = View.inflate(getContext(), R.layout.toast_custom_background, this);

        tv_home_update = (TextView) view.findViewById(R.id.tv_home_update);
        tv_home_closeupdate = (TextView) view.findViewById(R.id.tv_home_closeupdate);
    }

    public void setTitle(String title) {
        tv_home_update.setText(title);

    }

    public void setDes(String des) {
        tv_home_closeupdate.setText(des);
    }
}
