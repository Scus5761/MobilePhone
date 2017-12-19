package com.itheima.mobile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.find.LostFindActivity;
import com.itheima.settingview.SettingActivity;
import com.itheima.utils.MD5;

public class HomeActivity extends Activity {
    public GridView gv_home_gridview;
    public ImageView iv_home_imageview;
    public  TextView tv_home__item_text_view;
    private AlertDialog dialog;
    private SharedPreferences sp;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sp = getSharedPreferences("config", MODE_PRIVATE);

        gv_home_gridview = (GridView) findViewById(R.id.gv_home_gridview);
        gv_home_gridview.setAdapter(new MyAdapter());
        gv_home_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (getSharedPreferences("config", MODE_PRIVATE).getString("passward", null) == null) {
                            showSetPassWardDialog();
                        } else {
                            showInputPassWordDialog();
                        }
                        break;

                    case 1:
                        Intent intent = new Intent(HomeActivity.this, CallSmsSafeActivity.class);
                        startActivity(intent);
                        break;

                    case 2:
                        Intent intent_app = new Intent(HomeActivity.this, SoftMangerActivity.class);
                        startActivity(intent_app);
                        break;

                    case 3:
                        Intent intent_progress = new Intent(HomeActivity.this, TaskManagerActivity.class);
                        startActivity(intent_progress);
                        break;

                    case 4:
                        Intent intent_stream = new Intent(HomeActivity.this, TrafficActivity.class);
                        startActivity(intent_stream);
                        break;

                    case 5:
                        Intent intent_5 = new Intent(HomeActivity.this, AntivirusActivity.class);
                        startActivity(intent_5);
                        break;

                    case 6:
                        Intent intent_6 = new Intent(HomeActivity.this, ClearCacheActivity.class);
                        startActivity(intent_6);
                        break;

                    case 7:
                        Intent  intent_atool= new Intent(HomeActivity.this, AToolsActivity.class);
                        startActivity(intent_atool);
                        break;

                    case 8:
                        Intent intent_safe = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(intent_safe);
                        break;

                }
            }
        });

    }

    private void showInputPassWordDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View view = View.inflate(getApplicationContext(), R.layout.dialog_inputpassward, null);
        final EditText et_inputpassword_passward = (EditText) view.findViewById(R.id.et_inputpassword_passward);
        Button btn_ok_input = (Button) view.findViewById(R.id.btn_ok_input);
        Button btn_cancel_input= (Button) view.findViewById(R.id.btn_cancel_input);
        ImageView imageView = (ImageView)view.findViewById(R.id.iv_password_show);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count % 2 == 0) {
                    et_inputpassword_passward.setInputType(0);
                } else {
                    et_inputpassword_passward.setInputType(129);
                }
                count++;
            }
        });
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        btn_ok_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passward = et_inputpassword_passward.getText().toString().trim();
                String md5Password = MD5.Md5Password(passward);
                if (TextUtils.isEmpty(passward)) {
                    Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
                } else if (md5Password.equals(getSharedPreferences("config", MODE_PRIVATE).getString("passward", null))) {
                    Toast.makeText(getApplicationContext(), "密码正确", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(getApplicationContext(), "密码不正确，请重输入", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        btn_cancel_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }



    private void showSetPassWardDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View view = View.inflate(getApplicationContext(), R.layout.dialog_setpassward, null);
        final EditText et_setpassword_passward = (EditText) view.findViewById(R.id.et_setpassword_passward);
        final EditText et_setpassword_confirm = (EditText) view.findViewById(R.id.et_setpassword_confirm);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passward = et_setpassword_passward.getText().toString().trim();
                if (TextUtils.isEmpty(passward)) {
                    Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                String confrim = et_setpassword_confirm.getText().toString().trim();
                if (passward.equals(confrim)) {
                    String md5Password = MD5.Md5Password(passward);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("passward", md5Password);
                    editor.commit();
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "密码设置成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "密码不一致", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private class MyAdapter extends BaseAdapter {

        int[] imageId = { R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app,
                R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan,
                R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings };
        String[] names = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理",
                "高级工具", "设置中心" };

        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public Object getItem(int position) {

            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.home_gv_item, null);
            iv_home_imageview = (ImageView) view.findViewById(R.id.iv_home_imageview);
            tv_home__item_text_view = (TextView)view.findViewById(R.id.tv_home__item_textview);
            iv_home_imageview.setImageResource(imageId[position]);
            tv_home__item_text_view.setText(names[position]);
            return view;

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}

