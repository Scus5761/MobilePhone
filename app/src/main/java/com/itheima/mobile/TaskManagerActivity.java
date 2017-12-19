package com.itheima.mobile;

import android.app.Activity;
import android.app.ActivityManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.enginee.TaskEnginee;
import com.itheima.enginee.TaskInfo;
import com.itheima.ui.MyAsycnTask;
import com.itheima.utils.TaskUtil;

import java.util.ArrayList;
import java.util.List;

public class TaskManagerActivity extends Activity {
    private ListView lv_taskmanager_progress;
    private ProgressBar pb_softmanager;
    private MyAdapter adapter;
    private List<TaskInfo> list;
    private List<TaskInfo> userInfo;
    private List<TaskInfo> systemInfo;
    private TaskInfo taskInfo;
    private TextView tv_taskmanager_process;
    private TextView tv_taskmanager_freetotalstorage;
    private boolean isShowSystem = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskmanager);
        lv_taskmanager_progress = (ListView) findViewById(R.id.lv_taskmanager_progress);
        pb_softmanager = (ProgressBar) findViewById(R.id.pb_softmanager);
        tv_taskmanager_process = (TextView) findViewById(R.id.tv_taskmanager_process);
        tv_taskmanager_freetotalstorage = (TextView) findViewById(R.id.tv_taskmanager_freetotalstorage);
        fillData();
        listViewItemClick();
        tv_taskmanager_process.setText("正在运行进程\n" + TaskUtil.getProcessCount(getApplicationContext()));
        long avaiableRam = TaskUtil.getAvaiableRam(getApplicationContext());
        int sdkInt = Build.VERSION.SDK_INT;
        long totalRam;
        if (sdkInt > 16) {
            totalRam = TaskUtil.getTotalRam(getApplicationContext());
        } else {
            totalRam = TaskUtil.getTotalRam();
        }
        String avaiable = Formatter.formatFileSize(getApplicationContext(), avaiableRam);
        String total = Formatter.formatFileSize(getApplicationContext(), totalRam);
        tv_taskmanager_freetotalstorage.setText("剩余/总内存\n " + avaiable + "/" + total);
    }

    private void listViewItemClick() {
        lv_taskmanager_progress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 || position == userInfo.size() + 1) {
                    return;
                }
                if (position <= (userInfo.size())) {
                    taskInfo = userInfo.get(position - 1);
                } else {
                    taskInfo = systemInfo.get(position - userInfo.size() - 2);
                }

                if (taskInfo.isChecked()) {
                    taskInfo.setChecked(false);
                } else {
                    if (!taskInfo.getPackageName().equals(getPackageName())) {
                        taskInfo.setChecked(true);
                    }
                }
//                adapter.notifyDataSetChanged();
                ViewHolder holder = (ViewHolder) view.getTag();
                holder.cb_taskmanager_ischecked.setChecked(taskInfo.isChecked());
            }
        });
    }

    private void fillData() {

        new MyAsycnTask() {
            @Override
            public void preTak() {
                pb_softmanager.setVisibility(View.VISIBLE);
            }

            @Override
            public void postTak() {
                if (adapter == null) {
                    adapter = new MyAdapter();
                    lv_taskmanager_progress.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
                pb_softmanager.setVisibility(View.INVISIBLE);
            }

            @Override
            public void doinback() {
                list = TaskEnginee.getTaskInfo(getApplicationContext());
                userInfo = new ArrayList<TaskInfo>();
                systemInfo = new ArrayList<TaskInfo>();
                for (TaskInfo taskInfo : list) {
                    if (taskInfo.isUser()) {
                        userInfo.add(taskInfo);
                    } else {
                        systemInfo.add(taskInfo);
                    }
                }
            }
        }.execute();
    }


    class MyAdapter extends BaseAdapter {

        TaskManagerActivity.ViewHolder holder;
        View view;

        @Override
        public int getCount() {
            return isShowSystem == true ? userInfo.size() + systemInfo.size() + 2 : userInfo.size() + 1;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == 0) {
                TextView textView = new TextView(getApplicationContext());
                textView.setBackgroundColor(Color.RED);
                textView.setText("当前用户进程个数为：(" + userInfo.size() + ")");
                textView.setTextSize(18);
                textView.setPadding(10, 5, 0, 5);
                return textView;
            } else if (position == (userInfo.size() + 1)) {
                TextView textView = new TextView(getApplicationContext());
                textView.setBackgroundColor(Color.YELLOW);
                textView.setText("当前系统进程个数为：(" + systemInfo.size() + ")");
                textView.setTextSize(18);
                textView.setPadding(10, 5, 0, 5);
                return textView;
            }

            holder = new ViewHolder();
            if (convertView != null & convertView instanceof RelativeLayout) {
                view = convertView;
                holder = (TaskManagerActivity.ViewHolder) view.getTag();
            } else {
                view = View.inflate(getApplicationContext(), R.layout.item_taskmanager, null);
                holder.tv_taskmanager_name = (TextView) view.findViewById(R.id.tv_taskmanager_name);
                holder.cb_taskmanager_ischecked = (CheckBox) view.findViewById(R.id.cb_taskmanager_ischecked);
                holder.iv_taskmanager = (ImageView) view.findViewById(R.id.iv_taskmanager);
                holder.tv_taskmanager_ram = (TextView) view.findViewById(R.id.tv_taskmanager_ram);
                view.setTag(holder);
            }
//            AppInfo appInfo = list.get(position);

            if (position <= (userInfo.size())) {
                taskInfo = userInfo.get(position - 1);
            } else {
                taskInfo = systemInfo.get(position - userInfo.size() - 2);
            }
            if (taskInfo.getDrawable() == null) {
                holder.iv_taskmanager.setImageResource(R.mipmap.ic_launcher);
            } else {
                holder.iv_taskmanager.setImageDrawable(taskInfo.getDrawable());
            }
            if (taskInfo.getName() == null) {
                holder.tv_taskmanager_name.setText(taskInfo.getPackageName());
            } else {
                holder.tv_taskmanager_name.setText(taskInfo.getName());
            }

            long ramSize = taskInfo.getRamSize();
            String formatFileSize = Formatter.formatFileSize(getApplicationContext(), ramSize);
            holder.tv_taskmanager_ram.setText("进程占用内存为：(" + formatFileSize + ")MB");

            if (taskInfo.isChecked()) {
                holder.cb_taskmanager_ischecked.setChecked(true);
            } else {
                holder.cb_taskmanager_ischecked.setChecked(false);
            }
            if (taskInfo.getPackageName().equals(getPackageName())) {
                holder.cb_taskmanager_ischecked.setVisibility(View.INVISIBLE);
            } else {
                holder.cb_taskmanager_ischecked.setVisibility(View.VISIBLE);
            }
            return view;
        }
    }

    static class ViewHolder {

        ImageView iv_taskmanager;
        TextView tv_taskmanager_name, tv_taskmanager_ram;
        CheckBox cb_taskmanager_ischecked;
    }

    public void select(View v) {

        for (TaskInfo info : userInfo) {
            if (!info.getPackageName().equals(getPackageName())) {
                info.setChecked(true);
            }
        }
        for (int i = 0; i < systemInfo.size(); i++) {
            systemInfo.get(i).setChecked(true);
        }
        adapter.notifyDataSetChanged();
    }

    public void cancel(View v) {

        for (TaskInfo info : userInfo) {
            info.setChecked(false);
        }
        for (int i = 0; i < systemInfo.size(); i++) {
            systemInfo.get(i).setChecked(false);
        }
        adapter.notifyDataSetChanged();
    }

    public void setting(View v) {
        isShowSystem = !isShowSystem;
        adapter.notifyDataSetChanged();
    }

    public void clear(View v) {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<TaskInfo> deleteInfo = new ArrayList<TaskInfo>();
        for (TaskInfo taskInfo : userInfo) {
            if (taskInfo.isChecked()) {
                am.killBackgroundProcesses(taskInfo.getPackageName());
                deleteInfo.add(taskInfo);
            }
        }
        for (TaskInfo taskInfo : systemInfo) {
            if (taskInfo.isChecked()) {
                am.killBackgroundProcesses(taskInfo.getPackageName());
                deleteInfo.add(taskInfo);
            }
        }
        long memory = 0;
        for (TaskInfo taskInfo : deleteInfo) {
            if (taskInfo.isUser()) {
                userInfo.remove(taskInfo);
            } else {
                systemInfo.remove(taskInfo);
            }
            memory += taskInfo.getRamSize();
        }
        String deleteSize = Formatter.formatFileSize(getApplicationContext(), memory);
        Toast.makeText(getApplicationContext(), "一共删除" + deleteInfo.size() + "个软件，清除内存" + deleteSize, Toast.LENGTH_SHORT).show();
        deleteInfo.clear();
        deleteInfo = null;
        adapter.notifyDataSetChanged();
    }

}
