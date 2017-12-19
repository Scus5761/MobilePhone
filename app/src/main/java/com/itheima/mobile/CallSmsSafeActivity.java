package com.itheima.mobile;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.db.Dao.BlackNumDao;
import com.itheima.db.Dao.BlackNumInfo;
import com.itheima.ui.MyAsycnTask;

import java.util.List;


public class CallSmsSafeActivity extends Activity {

    private ListView lv_black_contacts;
    private ProgressBar pb_black_contacts;
    private BlackNumDao dao;
    private List<BlackNumInfo> list;
    private MyAdapter adapter;
    private Button bt_addblacknum;
    private AlertDialog dialog;
    private final int MaxMum = 20;
    private int startIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callsmssafe);
        lv_black_contacts = (ListView) findViewById(R.id.lv_black_contacts);
        pb_black_contacts = (ProgressBar) findViewById(R.id.pb_black_contacts);
        bt_addblacknum = (Button) findViewById(R.id.bt_addblacknum);
        dao = new BlackNumDao(getApplicationContext());
        fillData();
        lv_black_contacts.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    int position = lv_black_contacts.getLastVisiblePosition();
                    if (position == list.size() - 1) {
                        startIndex += MaxMum;
                        fillData();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
/*
        lv_black_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
*/

    }

    private void fillData() {
        new MyAsycnTask() {
            @Override
            public void preTak() {
                pb_black_contacts.setVisibility(View.VISIBLE);
            }

            @Override
            public void postTak() {
                if (adapter == null) {
                    adapter = new MyAdapter();
                    lv_black_contacts.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
                pb_black_contacts.setVisibility(View.INVISIBLE);

            }

            @Override
            public void doinback() {
                if (list == null) {
                    list = dao.getPartBlackNum(MaxMum, startIndex);
                } else {
                    list.addAll(dao.getPartBlackNum(MaxMum, startIndex));
                }
            }
        }.execute();
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder;
            final BlackNumInfo info = list.get(position);
            if (convertView == null) {
                view = View.inflate(getApplicationContext(), R.layout.balcknum_item, null);
                holder = new ViewHolder();
                holder.tv_blacknum = (TextView) view.findViewById(R.id.tv_blacknum);
                holder.tv_mode = (TextView) view.findViewById(R.id.tv_mode);
                holder.iv_blacknum_delete = (ImageView) view.findViewById(R.id.iv_blacknum_delete);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }

            holder.tv_blacknum.setText(info.getBlacknum());
            switch (info.getMode()) {
                case BlackNumDao.SMS:
                    holder.tv_mode.setText("短信攔截");
                    break;
                case BlackNumDao.CALL:
                    holder.tv_mode.setText("電話攔截");
                    break;
                case BlackNumDao.ALL:
                    holder.tv_mode.setText("全部攔截");
                    break;
            }

            holder.iv_blacknum_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CallSmsSafeActivity.this);
                    builder.setTitle("是否刪除聯繫人數據");
                    builder.setMessage("點擊確認刪除聯繫人");
                    builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dao.deleteBlackNmu(info.getBlacknum());
                            list.remove(position);
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.show();
                }
            });
            return view;
        }
    }

   static class ViewHolder {
         TextView tv_blacknum, tv_mode;
         ImageView iv_blacknum_delete;
    }

    public void add_blacknum(View v) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(getApplicationContext(), R.layout.addblacknum, null);
        final EditText et_add_blacknum = (EditText) view.findViewById(R.id.et_add_blacknum);
        final RadioGroup rg_add_blacknum = (RadioGroup) view.findViewById(R.id.rg_add_blacknum);
        Button btn_ok_input = (Button) view.findViewById(R.id.btn_ok_input);
        Button btn_cancel_input = (Button) view.findViewById(R.id.btn_cancel_input);

        btn_ok_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = et_add_blacknum.getText().toString().trim();
                if (TextUtils.isEmpty(number)) {
                    Toast.makeText(getApplicationContext(), "号码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                int mode=-1;
                switch (rg_add_blacknum.getCheckedRadioButtonId()) {
                    case R.id.rb_add_blacknum_mobile:
                        mode = BlackNumDao.CALL;
                        break;
                    case R.id.rb_add_black_sms:
                        mode = BlackNumDao.SMS;
                        break;
                    case R.id.rb_add_black_all:
                        mode = BlackNumDao.ALL;
                        break;
                }

                dao.addBlackNum(number, mode);
                list.add(0,new BlackNumInfo(number, mode));
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        btn_cancel_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.show();

    }
}
