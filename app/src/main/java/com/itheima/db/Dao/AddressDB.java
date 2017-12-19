package com.itheima.db.Dao;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;


public class AddressDB extends Activity {

    private static SQLiteDatabase database;
    private static String location="";

/*    public static String queryAddress(String num, Context context) {
        File file = new File(context.getFilesDir(), "address0.db");
        database = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);

        Cursor cursor = database.rawQuery("select location from data2 where id=(select outkey from data1 where id=?)",
                new String[]{num.substring(0, 7)});
        if (cursor.moveToNext()) {
            location = cursor.getString(0);
        }

        cursor.close();
        database.endTransaction();
        database.close();
        return location;
    }*/

    public static String queryAddress(String num,Context context){
        String location = "";
        //1.获取数据库的路径
        File file = new File(context.getFilesDir(), "address0.db");
        //2.打开数据库
        //getAbsolutePath : 获取文件的绝对路径
        //factory : 游标工厂
        SQLiteDatabase database = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
        //3.查询号码归属地
        //sql : sql语句
        //selectionArgs :查询条件的参数
        //substring : 包含头不包含尾
        //使用正则表达式进行判断
        //^1[34578]\d{9}$
        //身份证   ^[0-9]{17}[0-9x]$
        //前六位:出生地   往后8位:出生年月日  剩下4位:前两位:出生编号   剩下2位:前一位:性别  奇数男  偶数女    最后一位:前17位的校验   x
        if (num.matches("^1[34578]\\d{9}$")) {
            Cursor cursor = database.rawQuery("select location from data2 where id=(select outkey from data1 where id=?)", new String[]{num.substring(0, 7)});
            //4.解析cursor
            //因为每个号码对应一个号码归属地,所以查询出来的是一个号码归属地,没有必要用whlie,用if就可以了
            if (cursor.moveToNext()) {
                location = cursor.getString(0);
            }
            //5.关闭数据库
            cursor.close();
        }else{
            //对特殊电话做出来
            switch (num.length()) {
                case 3://110  120  119  911
                    location = "特殊电话";
                    break;
                case 4://5554   5556
                    location = "虚拟电话";
                    break;
                case 5://10086  10010  10000
                    location ="客服电话";
                    break;
                case 7://座机,本地电话
                case 8:
                    location="本地电话";
                    break;

                default:// 010 1234567   10位  	010 12345678  11位     0372  12345678  12位
                    //长途电话
                    if (num.length() >= 10  &&  num.startsWith("0")) {
                        //根据区号查询号码归属
                        //1.获取号码的区号
                        //3位,4位
                        //3位
                        String result = num.substring(1, 3);//010   10
                        //2.根据区号查询号码归属地
                        Cursor cursor = database.rawQuery("select location from data2 where area=?", new String[]{result});
                        //3.解析cursor
                        if (cursor.moveToNext()) {
                            location = cursor.getString(0);
                            //截取数据
                            location = location.substring(0, location.length()-2);
                            cursor.close();
                        }else{
                            //3位没有查询到,直接查询4位
                            //获取4位的区号
                            result = num.substring(1, 4);//0372    372
                            cursor = database.rawQuery("select location from data2 where area=?", new String[]{result});
                            if (cursor.moveToNext()) {
                                location = cursor.getString(0);
                                location = location.substring(0, location.length()-2);
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
        database.close();
        return location;
    }

}
