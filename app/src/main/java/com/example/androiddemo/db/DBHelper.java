package com.example.androiddemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.androiddemo.bean.UserBean;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TB_NAME = "user";
    private volatile static DBHelper dbHelper;
    public static final String DB_NAME = "AppDatabase.db";
    public static final int DB_VERSION = 1;

    public synchronized static DBHelper getInstance(Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
        return dbHelper;
    }

    /**
     * 作为SQLiteOpenHelper子类必须有的构造方法
     *
     * @param context 上下文参数
     * @param name    数据库名字
     * @param factory 游标工厂 ，通常是null
     * @param version 数据库的版本
     */
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 数据库第一次被创建时调用该方法
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 初始化数据库的表结构，执行一条建表的SQL语句
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                TB_NAME + " ( _id integer primary key autoincrement," +
                "user_name varchar," +
                "password varchar," +
                "avatar_url varchar," +
                "is_login INTEGER DEFAULT 0" +
                ") ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        dbHelper.getWritableDatabase().execSQL("DROP TABLE IF EXISTS user");
        onCreate(dbHelper.getWritableDatabase());
    }

    public long insert(String user_name, String password) {
        // 通过DBHelper类获取一个读写的SQLiteDatabase对象
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // 创建ContentValue设置参数
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_name", user_name);
        contentValues.put("password", password);
        // 插入数据
        // insert方法参数1：要插入的表名
        // insert方法参数2：如果发现将要插入的行为空时，会将这个列名的值设为null
        // insert方法参数3：contentValue
        long i = db.insert(TB_NAME, "user_name", contentValues);
        // 释放连接
        db.close();
        return i;
    }

    public void updateUser(long _id, int is_login){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update user set is_login=? where _id=?",new Object[]{is_login,_id});
        db.close();
    }

    public void updateUser(long id, String avatar_url){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update user set avatar_url=? where _id=?",new Object[]{avatar_url, id});
        db.close();
    }

    public UserBean queryUser(String user_name){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from user where user_name=?", new String[]{user_name});
        // 游标只要不是在最后一行之后，就一直循环
        cursor.moveToFirst();
        UserBean userBean = null;
        while (!cursor.isAfterLast()){
            userBean = new UserBean();
            userBean.set_id(cursor.getLong(0));
            userBean.setUser_name(cursor.getString(1));
            userBean.setPassword(cursor.getString(2));
            userBean.setAvatarUrl(cursor.getString(3));
            userBean.setIs_login(cursor.getInt(4));
            // 将游标移到下一行
            cursor.moveToNext();
        }
        db.close();
        return userBean;
    }

    public UserBean queryUser(int isLogin){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from user where is_login=?", new String[]{Integer.toString(isLogin)});
        // 游标只要不是在最后一行之后，就一直循环
        cursor.moveToFirst();
        UserBean userBean = null;
        while (!cursor.isAfterLast()){
            userBean = new UserBean();
            userBean.set_id(cursor.getLong(0));
            userBean.setUser_name(cursor.getString(1));
            userBean.setPassword(cursor.getString(2));
            userBean.setAvatarUrl(cursor.getString(3));
            userBean.setIs_login(cursor.getInt(4));
            // 将游标移到下一行
            cursor.moveToNext();
        }
        db.close();
        return userBean;
    }
}

