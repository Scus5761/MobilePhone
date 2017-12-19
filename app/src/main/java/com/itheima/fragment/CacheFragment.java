package com.itheima.fragment;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itheima.mobile.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThinkPad on 2017-06-14.
 */

public class CacheFragment extends Fragment {
    private TextView tv_antivirus_text;
    private ProgressBar pb_antivirus_progressbar;
    private ListView lv_fragment_cache;
    private List<CacheInfo> list;
    private PackageManager pm;
    private Button btn_cachefragment_clear;
    private MyAdapter adapter;
    private View view;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        list = new ArrayList<CacheFragment.CacheInfo>();
        list.clear();
        view = inflater.inflate(R.layout.fragment_cache, container, false);
        tv_antivirus_text = (TextView) view.findViewById(R.id.tv_antivirus_text);
        lv_fragment_cache = (ListView) view.findViewById(R.id.lv_fragment_cache);
        btn_cachefragment_clear = (Button) view.findViewById(R.id.btn_cachefragment_clear);
        pb_antivirus_progressbar = (ProgressBar) view.findViewById(R.id.pb_antivirus_progressbar);

        lv_fragment_cache.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.parse("package:" + list.get(position).getPackageName()));
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        scanner();
    }

    private void scanner() {
        pm = getActivity().getPackageManager();
        tv_antivirus_text.setText("正在初始化杀毒引擎！");
        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(300);
                List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
                pb_antivirus_progressbar.setMax(installedPackages.size());
                int count = 0;
                for (PackageInfo packageInfo : installedPackages) {
                    SystemClock.sleep(100);
                    count++;
                    pb_antivirus_progressbar.setProgress(count);
                    try {
                        Class<?> loadClass = getActivity().getClass().getClassLoader().loadClass("android.content.pm.PackageManager");
                        Method method = loadClass.getDeclaredMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
                        method.invoke(pm, packageInfo.packageName, mStatsObserver);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final String packagename = packageInfo.applicationInfo.loadLabel(pm).toString();
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_antivirus_text.setText("正在扫描 " + packagename);
                            }
                        });
                    }
                }
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_antivirus_text.setVisibility(View.GONE);
                            pb_antivirus_progressbar.setVisibility(View.GONE);
                            adapter = new MyAdapter();
                            lv_fragment_cache.setAdapter(adapter);

                            if (list.size() > 0) {
                                btn_cachefragment_clear.setVisibility(View.VISIBLE);
                                btn_cachefragment_clear.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {

                                        //真正的实现清理缓存
                                        try {
                                            Class<?> loadClass = getActivity().getClass().getClassLoader().loadClass("android.content.pm.PackageManager");
                                            //Long.class  Long     TYPE  long
                                            Method method = loadClass.getDeclaredMethod("freeStorageAndNotify", Long.TYPE,IPackageDataObserver.class);
                                            method.invoke(pm, Long.MAX_VALUE,new MyIPackageDataObserver());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        //清理缓存
                                        list.clear();
                                        //更新界面
                                        adapter.notifyDataSetChanged();
                                        //隐藏button按钮
                                        btn_cachefragment_clear.setVisibility(View.GONE);
                                    }

                                  /*  public void onClick(View v) {
                                        try {
                                            Class<?> loadClass = getActivity().getClass().getClassLoader().loadClass("android.content.pm.PackageManager");
                                            Method method = loadClass.getDeclaredMethod("freeStorageAndNotify", Long.TYPE, IPackageDataObserver.class);
                                            method.invoke(pm, Long.MAX_VALUE, new MyIPackageDataObserver());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        list.clear();
                                        adapter.notifyDataSetChanged();
                                        btn_cachefragment_clear.setVisibility(View.GONE);

                                    }*/
                                });
                            }
                        }
                    });
                }
            }
        }.start();

    }

    IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub() {
        public void onGetStatsCompleted(PackageStats stats, boolean succeeded) {
            long cachesize = stats.cacheSize;//缓存大小
//            long codesize = stats.codeSize;//应用程序的大小
//            long datasize = stats.dataSize;//数据大小

            if (cachesize > 0) {
                String cache = Formatter.formatFileSize(getActivity(), cachesize);
                list.add(new CacheInfo(stats.packageName, cache));
            }
//            String code = Formatter.formatFileSize(getActivity(), codesize);
//            String data = Formatter.formatFileSize(getActivity(), datasize);
        }
    };

    class CacheInfo {
        private String packageName;
        private String cacheSize;

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getCacheSize() {
            return cacheSize;
        }

        public void setCacheSize(String cacheSize) {
            this.cacheSize = cacheSize;
        }

        public CacheInfo() {
            super();
        }

        public CacheInfo(String packageName, String cacheSize) {
            this.packageName = packageName;
            this.cacheSize = cacheSize;
        }

        @Override
        public String toString() {
            return "CacheInfo{" +
                    "packageName='" + packageName + '\'' +
                    ", cacheSize='" + cacheSize + '\'' +
                    '}';
        }
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder;
            if (convertView == null) {
                view = View.inflate(getActivity(), R.layout.activity_item_cache, null);
                holder = new ViewHolder();
                holder.tv_itemcache_cache = (TextView) view.findViewById(R.id.tv_itemcache_cache);
                holder.tv_itemcache_name = (TextView) view.findViewById(R.id.tv_itemcache_name);
                holder.iv_itemcache_icon = (ImageView) view.findViewById(R.id.iv_itemcache_icon);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }

            CacheInfo cacheInfo = list.get(position);
            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(cacheInfo.getPackageName(), 0);
                Drawable drawable = applicationInfo.loadIcon(pm);
                holder.iv_itemcache_icon.setImageDrawable(drawable);
                holder.tv_itemcache_name.setText(applicationInfo.loadLabel(pm).toString());
                holder.tv_itemcache_cache.setText(cacheInfo.getCacheSize());

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            return view;
        }

    }

        static class ViewHolder {
            ImageView iv_itemcache_icon;
            TextView tv_itemcache_name, tv_itemcache_cache;
        }

    }

class MyIPackageDataObserver extends IPackageDataObserver.Stub {
    @Override
    public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {

    }
}
