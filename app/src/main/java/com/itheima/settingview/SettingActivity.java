package com.itheima.settingview;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.itheima.mobile.DrawViewActivity;
import com.itheima.mobile.R;
import com.itheima.service.AddressService;
import com.itheima.service.BlackNumService;
import com.itheima.service.WatchDogService;
import com.itheima.utils.AdressUtils;

public class SettingActivity extends Activity {

    private Toast_Custom_View toast_custom;
    public SettingView settingView;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    private Intent intent;
    private Toast_Custom_Background toast_custom_background;
    private String[] items;
    private Toast_Custom_Background toast_custom_location;
    private BlackNum bn_black_num;
    private WatchDog watch_dog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_setting);

        shared = getSharedPreferences("config", MODE_PRIVATE);
        editor = shared.edit();

        settingView = (SettingView) findViewById(R.id.tv_home_selfview);
        toast_custom = (Toast_Custom_View) findViewById(R.id.toast_custom);
        toast_custom_background = (Toast_Custom_Background) findViewById(R.id.toast_custom_background);
        toast_custom_location = (Toast_Custom_Background) findViewById(R.id.toast_custom_location);
        bn_black_num = (BlackNum) findViewById(R.id.bn_black_num);
        watch_dog = (WatchDog) findViewById(R.id.watch_dog);
        update();
        changedbg();
        location();


        }

    private void location() {
        toast_custom_location.setTitle("归属地提示框位置");
        toast_custom_location.setDes("设置归属地提示框显示位置");
        toast_custom_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, DrawViewActivity.class);
                startActivity(intent);
            }
        });
    }

    private void changedbg() {
        items = new String[]{"半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿"};
        toast_custom_background.setTitle("归属地提示风格");
        toast_custom_background.setDes(items[shared.getInt("which", 0)]);
        toast_custom_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("归属地提示框风格");
                builder.setSingleChoiceItems(items, shared.getInt("which", 0), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toast_custom_background.setDes(items[which]);

                        editor.putInt("which", which);
                        editor.commit();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });
    }


    private void update() {
        settingView.setTitle("更新提示");
        if (shared.getBoolean("update", true)) {
            settingView.setDes("打开提示更新");
            settingView.setChecked(true);
        } else {
            settingView.setDes("关闭提示更新");
            settingView.setChecked(false);
        }
        settingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (settingView.isChecked()) {
                    settingView.setDes("关闭提示更新");
                    settingView.setChecked(false);
                    editor.putBoolean("update", false);
                } else {
                    settingView.setDes("开启提示更新");
                    settingView.setChecked(true);
                    editor.putBoolean("update", true);
                }
                editor.commit();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    // activity可见的时候调用
    @Override
    protected void onStart() {
        super.onStart();
        address();
        blackNum();
        lock();
    }


    private void lock() {
        watch_dog.setTitle("锁屏操作");
        watch_dog.setDes("关闭锁屏操作");
        if (AdressUtils.isRunningService("com.itheima.service.WatchDogService", getApplicationContext())) {
                    watch_dog.setChecked(true);
                } else {
                    watch_dog.setChecked(false);
                }

            watch_dog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SettingActivity.this,
                            WatchDogService.class);
                    if (watch_dog.isChecked()) {
                        watch_dog.setDes("关闭锁屏操作");
                        stopService(intent);
                        watch_dog.setChecked(false);
                    } else {
                        watch_dog.setDes("开启锁屏操作");
                        startService(intent);
                        watch_dog.setChecked(true);
                    }
                }
            });
        }



    private void blackNum() {
        bn_black_num.setTitle("黑名单操作");
        bn_black_num.setDes("关闭黑名单操作");

        if (AdressUtils.isRunningService("com.itheima.service.BlackNumService", getApplicationContext())) {
            // 开启服务
            bn_black_num.setChecked(true);
        } else {
            // 关闭服务
            bn_black_num.setChecked(false);
        }
        bn_black_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(SettingActivity.this, BlackNumService.class);
                if (bn_black_num.isChecked()) {
                    bn_black_num.setDes("关闭黑名单查询");
                    stopService(intent);
                    bn_black_num.setChecked(false);
                } else {
                    bn_black_num.setDes("开启黑名单");
                    startService(intent);
                    bn_black_num.setChecked(true);
                }
            }
        });
    }



    private void address() {
        toast_custom.setTitle("归属地查询");
        if (AdressUtils.isRunningService(
                "com.itheima.service.AddressService",
                getApplicationContext())) {
            // 开启服务
            toast_custom.setChecked(true);
        } else {
            // 关闭服务
            toast_custom.setChecked(false);
        }

        toast_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(SettingActivity.this, AddressService.class);
                if (toast_custom.isChecked()) {
                    toast_custom.setDes("关闭归属地查询");
                    stopService(intent);
                    toast_custom.setChecked(false);
                } else {
                    toast_custom.setDes("开启归属地查询");
                    startService(intent);
                    toast_custom.setChecked(true);
                }
            }
        });
    }





}
