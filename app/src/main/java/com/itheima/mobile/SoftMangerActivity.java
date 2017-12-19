package com.itheima.mobile;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.db.Dao.WatchDogDao;
import com.itheima.enginee.AppEnginee;
import com.itheima.enginee.AppInfo;
import com.itheima.ui.DensityUtil;
import com.itheima.ui.MyAsycnTask;
import com.itheima.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThinkPad on 2017-06-06.
 */

public class SoftMangerActivity extends Activity implements View.OnClickListener {

    private ProgressBar pb_softmanager;
    private ListView lv_softmanager_application;
    private List<AppInfo> list;
    private List<AppInfo> userInfo;
    private List<AppInfo> systemInfo;
    private TextView tv_softmanager_float;
    private AppInfo appInfo;
    private PopupWindow popupWindow;
    private LinearLayout ll_pupuwindow_uninstalling;
    private LinearLayout ll_pupuwindow_sharing;
    private LinearLayout ll_pupuwindow_starting;
    private LinearLayout ll_pupuwindow_detailing;
    private MyAdapter adapter;
    private TextView tv_softmanager_rom;
    private TextView tv_softmanager_sd;
    private WatchDogDao watchDogDao;
    private ViewHolder holder;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_softmanager);
        watchDogDao = new WatchDogDao(getApplicationContext());
        tv_softmanager_float = (TextView) findViewById(R.id.tv_softmanager_float);
        lv_softmanager_application = (ListView) findViewById(R.id.lv_softmanager_application);
        pb_softmanager = (ProgressBar) findViewById(R.id.pb_softmanager);
        tv_softmanager_rom = (TextView) findViewById(R.id.tv_softmanager_rom);
        tv_softmanager_sd = (TextView) findViewById(R.id.tv_softmanager_sd);
        long avaiableROM = AppUtils.getAvaiableROM();
        long avaiableSD = AppUtils.getAvaiableSD();
        String RomSize = Formatter.formatFileSize(getApplicationContext(), avaiableROM);
        String SDSize = Formatter.formatFileSize(getApplicationContext(), avaiableSD);
        tv_softmanager_rom.setText("手机内存可用 (" + RomSize + ")");
        tv_softmanager_sd.setText("SD内存可用 (" + SDSize + ")");

        fillData();
        listviewOnScroll();
        listviewItemClick();
        listviewItemLongClicked();
    }

    private void listviewItemLongClicked() {

        lv_softmanager_application.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                holder = (ViewHolder) view.getTag();
                if (position == 0 || position == userInfo.size() + 1) {
                    return true;
                }
                if (position <= (userInfo.size())) {
                    appInfo = userInfo.get(position - 1);
                } else {
                    appInfo = systemInfo.get(position - userInfo.size() - 2);
                }
                if (watchDogDao.queryLockApp(appInfo.getPackageName())) {
                    watchDogDao.deleteLockApp(appInfo.getPackageName());
                    holder.iv_softmanager_lock.setImageResource(R.drawable.unlock);
                } else {
                    if (!appInfo.getPackageName().equals(getPackageName())) {
                        watchDogDao.addLockApp(appInfo.getPackageName());
                        holder.iv_softmanager_lock.setImageResource(R.drawable.lock);
                    }

                }
                 adapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void listviewItemClick() {
        lv_softmanager_application.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 || position == userInfo.size() + 1) {
                    return;
                }
                if (position <= (userInfo.size())) {
                    appInfo = userInfo.get(position - 1);
                } else {
                    appInfo = systemInfo.get(position - userInfo.size() - 2);
                }

                if (popupWindow != null) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                View inflate = View.inflate(getApplicationContext(), R.layout.popuwindow, null);

                ll_pupuwindow_uninstalling = (LinearLayout) inflate.findViewById(R.id.ll_pupuwindow_uninstalling);
                ll_pupuwindow_sharing = (LinearLayout) inflate.findViewById(R.id.ll_pupuwindow_sharing);
                ll_pupuwindow_starting = (LinearLayout) inflate.findViewById(R.id.ll_pupuwindow_starting);
                ll_pupuwindow_detailing = (LinearLayout) inflate.findViewById(R.id.ll_pupuwindow_detailing);

                ll_pupuwindow_uninstalling.setOnClickListener(SoftMangerActivity.this);
                ll_pupuwindow_sharing.setOnClickListener(SoftMangerActivity.this);
                ll_pupuwindow_starting.setOnClickListener(SoftMangerActivity.this);
                ll_pupuwindow_detailing.setOnClickListener(SoftMangerActivity.this);

                popupWindow = new PopupWindow(inflate, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                int[] location = new int[2];
                view.getLocationInWindow(location);
                int x = location[0];
                int y = location[1];
                popupWindow.showAtLocation(parent, Gravity.LEFT | Gravity.TOP, x + DensityUtil.dip2px(getApplication(), 140), y);
                ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(500);

                AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
                alphaAnimation.setDuration(500);

                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(scaleAnimation);
                animationSet.addAnimation(alphaAnimation);
                inflate.setAnimation(animationSet);

            }
        });

    }

    private void listviewOnScroll() {
        lv_softmanager_application.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                if (userInfo != null & systemInfo != null) {
                    if (firstVisibleItem >= userInfo.size() + 1) {
                        tv_softmanager_float.setText("系统程序 (" + systemInfo.size() + ")");
                    } else {
                        tv_softmanager_float.setText("用户程序 (" + userInfo.size() + ")");

                    }
                }

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
                    lv_softmanager_application.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
                pb_softmanager.setVisibility(View.INVISIBLE);
            }

            @Override
            public void doinback() {
                list = AppEnginee.getAppInfos(getApplicationContext());
                userInfo = new ArrayList<AppInfo>();
                systemInfo = new ArrayList<AppInfo>();
                for (AppInfo appInfo : list) {
                    if (appInfo.isUser()) {
                        userInfo.add(appInfo);
                    } else {
                        systemInfo.add(appInfo);
                    }
                }
            }
        }.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_pupuwindow_uninstalling:
                uninstall();
                break;
            case R.id.ll_pupuwindow_starting:
                starting();
                break;
            case R.id.ll_pupuwindow_sharing:
                sharing();
                break;

            case R.id.ll_pupuwindow_detailing:
                detailing();
                break;
        }
        hidePopuWindow();
    }

    private void sharing() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "发现一款很牛X的软件" + appInfo.getPackageName() + ",你可以去百度搜搜");
        startActivity(intent);
    }

    private void detailing() {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + appInfo.getPackageName()));
        startActivity(intent);
    }

    private void starting() {
        PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(appInfo.getPackageName());
        if (intent != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "系统应用不能启动", Toast.LENGTH_SHORT).show();
        }

    }

    private void uninstall() {
        if (appInfo.isUser()) {
            if (!appInfo.getPackageName().equals(getPackageName())) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.DELETE");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse("package:" + appInfo.getPackageName()));
                startActivityForResult(intent, 0);
            } else {
                Toast.makeText(getApplicationContext(), "不能删除自己应用", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "请先Root下，再删除", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fillData();
    }

    class MyAdapter extends BaseAdapter {

        View view;

        @Override
        public int getCount() {
            return userInfo.size() + systemInfo.size() + 2;
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
                textView.setText("当前用户程序个数为：(" + userInfo.size() + ")");
                textView.setTextSize(18);
                textView.setPadding(10, 5, 0, 5);
                return textView;
            } else if (position == (userInfo.size() + 1)) {
                TextView textView = new TextView(getApplicationContext());
                textView.setBackgroundColor(Color.YELLOW);
                textView.setText("当前系统程序个数为：(" + systemInfo.size() + ")");
                textView.setTextSize(18);
                textView.setPadding(10, 5, 0, 5);
                return textView;
            }
            holder = new ViewHolder();
            if (convertView != null & convertView instanceof RelativeLayout) {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(getApplicationContext(), R.layout.item_softmanager, null);
                holder.tv_softmanager_name = (TextView) view.findViewById(R.id.tv_softmanager_name);
                holder.tv_softmanager_version = (TextView) view.findViewById(R.id.tv_softmanager_version);
                holder.iv_softmanager = (ImageView) view.findViewById(R.id.iv_softmanager);
                holder.iv_softmanager_lock = (ImageView) view.findViewById(R.id.iv_softmanager_lock);
               /* holder.iv_softmanager_lock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String packageName = appInfo.getPackageName();
                        watchDogDao.addLockApp(packageName);
                        holder.iv_softmanager_lock.setImageResource(R.drawable.lock);
                        Toast.makeText(getApplicationContext(), "已为该程序添加程序锁", Toast.LENGTH_SHORT).show();

                    }
                });*/

                holder.tv_softmanager_issd = (TextView) view.findViewById(R.id.tv_softmanager_issd);
                view.setTag(holder);
            }
//            AppInfo appInfo = list.get(position);
            if (position <= (userInfo.size())) {
                appInfo = userInfo.get(position - 1);
            } else {
                appInfo = systemInfo.get(position - userInfo.size() - 2);
            }

            boolean issd = appInfo.isSD();
            holder.iv_softmanager.setImageDrawable(appInfo.getIcon());
            holder.tv_softmanager_version.setText(appInfo.getVersionName());
            holder.tv_softmanager_name.setText(appInfo.getName());

            if (issd) {
                holder.tv_softmanager_issd.setText("SD卡内存");
            } else {
                holder.tv_softmanager_issd.setText("手机内存");
            }

            if (watchDogDao.queryLockApp(appInfo.getPackageName())) {
                holder.iv_softmanager_lock.setImageResource(R.drawable.lock);
            } else {
                holder.iv_softmanager_lock.setImageResource(R.drawable.unlock);
            }

            return view;
        }
    }

    static class ViewHolder {
        ImageView iv_softmanager, iv_softmanager_lock;
        TextView tv_softmanager_name, tv_softmanager_issd, tv_softmanager_version;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hidePopuWindow();
    }

    public void hidePopuWindow() {
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }
}
