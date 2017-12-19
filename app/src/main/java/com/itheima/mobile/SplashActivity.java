package com.itheima.mobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.utils.StreamUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {



    private TextView tv_version;
    private String versionName;
    private TextView tv_splash_plan;

    private String code;
    private String apkurl;
    private String des;
    private SharedPreferences sp;


    public static final int HANDLER_UPDATE_MESSAGE=1;
    public static final int HANDLER_UPDATE_ERROR=2;
    private static final int HANDLER_UPDATE_UNUSUAL = 3;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_UPDATE_MESSAGE:
                    showDialog();
                    break;
                case HANDLER_UPDATE_ERROR:
                    Toast.makeText(SplashActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case HANDLER_UPDATE_UNUSUAL:
                    Toast.makeText(SplashActivity.this, "亲，服务器或者网络出现异常...", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
            }
        }
    };

    //弹出对话框
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("新版本：" + code);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(des);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                download();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                enterHome();
            }
        });

        builder.show();
    }

    private void download() {

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.download("https://raw.githubusercontent.com/guolindev/eclipse/master/eclipse-inst-win64.exe",
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getParent(),
                new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                Toast.makeText(SplashActivity.this, "正在下载和安装", Toast.LENGTH_SHORT).show();
                enterHome();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                tv_splash_plan.setVisibility(View.VISIBLE);
                tv_splash_plan.setText(current + "/" + total);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(SplashActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                enterHome();

            }
        });

    }

    public void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_splash_plan= (TextView) findViewById(R.id.tv_splash_plan);
        getVersionName();
        tv_version.setText("版本号是：" + versionName);
        if (sp.getBoolean("update", true)) {
            update();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(2000);
                    enterHome();
                }
            }).start();

        }
         copyDb("address0.db");
        copyDb("antivirus.db");
//创建快捷方式，向桌面发送一广播。
        createShortCut();

    }

    private void createShortCut() {
        Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "手机卫士");
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON,icon);

        Intent intent1 = new Intent();
        intent1.setAction("com.itheima.mobile.HomeActivity");
        intent1.addCategory("android.intent.category.DEFAULT");
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent1);
        sendBroadcast(intent);
    }

    private void copyDb(String dbName) {
        File file = new File(getFilesDir(), dbName);
        //判断文件是否存在
        if (!file.exists()) {
            //从assets目录中将数据库读取出来
            //1.获取assets的管理者
            AssetManager am = getAssets();
            InputStream in = null;
            FileOutputStream out = null;
            try {
                //2.读取数据库
                in = am.open(dbName);
                //写入流
                //getCacheDir : 获取缓存的路径
                //getFilesDir : 获取文件的路径
                out = new FileOutputStream(file);
                //3.读写操作
                //设置缓冲区
                byte[] b = new byte[1024];
                int len = -1;
                while((len=in.read(b)) != -1){
                    out.write(b, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
//				in.close();
//				out.close();
                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(out);
            }
        }

   /* private void copyDb() {
        InputStream in = null;
        FileOutputStream out = null;
        File file = new File(getFilesDir(), "address0.db");
        if (!file.exists()) {
            try {
                AssetManager assetManager = getAssets();
                in = assetManager.open("address0.db");
                out = new FileOutputStream(file);
                byte[] bytes = new byte[1024];
                int length = -1;
                while ((length = in.read(bytes)) != -1) {
                    out.write(bytes, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    if (out!=null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
*/
    }


    private void update() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long st_time = System.currentTimeMillis();
                Message message = Message.obtain();
                try {
                    URL url = new URL("http://baidu.com");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    int responseCode = conn.getResponseCode();

                    if (responseCode == 200) {
                        InputStream inputStream = conn.getInputStream();
                        String json = StreamUtil.parserStreamUtil(inputStream); //获取json数据

                       // JSONObject object = new JSONObject(json);
                        Log.v("SplashActivity", json.toString());
                       /* code = object.getString("code");
                        apkurl = object.getString("apkurl");
                        des = object.getString("des");*/

                        code="2.0";
                        apkurl = "http://baidu.com";
                        des = "发现新版本，请及时更新：\n" +
                                "新版本增加了内存管理功能，" +
                                "优化了省电性能";

                        message.what=HANDLER_UPDATE_MESSAGE;

                    }else{
                        message.what=HANDLER_UPDATE_ERROR;
                        //连接不成功
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    message.what=HANDLER_UPDATE_UNUSUAL;
                }finally {
                    //无论成功与否都会执行
                    long en_time = System.currentTimeMillis();
                    if ((st_time - en_time) < 2000) {
                        SystemClock.sleep(2000-(st_time - en_time));
                    }
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    private String getVersionName() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo("com.itheima.mobile", 0);
            versionName = info.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }
}
