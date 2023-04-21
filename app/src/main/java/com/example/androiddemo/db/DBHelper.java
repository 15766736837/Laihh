package com.example.androiddemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Region;

import androidx.annotation.Nullable;

import com.example.androiddemo.app.BaseApplication;
import com.example.androiddemo.bean.RoomBean;
import com.example.androiddemo.bean.RoomOrder;
import com.example.androiddemo.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TB_NAME = "user";
    private static final String TB_ROOM = "room";
    private static final String TB_ROOM_ORDER = "room_order";
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

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                TB_ROOM + " ( id integer primary key autoincrement," +
                "room_name varchar," +
                "region varchar," +
                "seat varchar," +
                "describe varchar," +
                "status INTEGER DEFAULT 0" +
                ") ");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                TB_ROOM_ORDER + " ( id integer primary key autoincrement," +
                "room_id INTEGER DEFAULT 0," +
                "user_id INTEGER DEFAULT 0," +
                "start_time INTEGER DEFAULT 0," +
                "end_time INTEGER DEFAULT 0," +
                "region varchar," +
                "seat varchar" +
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

    public void updateUserPwd(String pwd){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update user set password=? where _id=?",new Object[]{pwd, BaseApplication.app.userBean.get_id()});
        db.close();
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

    //查询出所有的自习室
    public List<RoomBean> queryAllRoom(){
        List<RoomBean> roomBeans = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from room", new String[]{});
        // 游标只要不是在最后一行之后，就一直循环
        cursor.moveToFirst();
        RoomBean roomBean = null;
        while (!cursor.isAfterLast()){
            roomBean = new RoomBean();
            roomBean.setId(cursor.getLong(0));
            roomBean.setRoom_name(cursor.getString(1));
            roomBean.setRegion(cursor.getString(2));
            roomBean.setSeat(cursor.getString(3));
            roomBean.setDescribe(cursor.getString(4));
            roomBean.setStatus(cursor.getInt(5));
            roomBeans.add(roomBean);
            // 将游标移到下一行
            cursor.moveToNext();
        }
//        db.close();
        return roomBeans;
    }

    public List<RoomBean> queryMyRoom(){
        List<RoomBean> roomBeans = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from room", new String[]{});
        // 游标只要不是在最后一行之后，就一直循环
        cursor.moveToFirst();
        RoomBean roomBean = null;
        while (!cursor.isAfterLast()){
            roomBean = new RoomBean();
            roomBean.setId(cursor.getLong(0));
            roomBean.setRoom_name(cursor.getString(1));
            roomBean.setRegion(cursor.getString(2));
            roomBean.setSeat(cursor.getString(3));
            roomBean.setDescribe(cursor.getString(4));
            roomBean.setStatus(cursor.getInt(5));
            List<RoomOrder> roomOrders = queryRoomOrder(roomBean.getId(), BaseApplication.app.userBean.get_id());
            if(!roomOrders.isEmpty())
                roomBeans.add(roomBean);
            // 将游标移到下一行
            cursor.moveToNext();
        }
//        db.close();
        return roomBeans;
    }

    //新增自习室
    public long insertRoom(RoomBean bean) {
        // 通过DBHelper类获取一个读写的SQLiteDatabase对象
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // 创建ContentValue设置参数
        ContentValues contentValues = new ContentValues();
        contentValues.put("room_name", bean.getRoom_name());
        contentValues.put("region", bean.getRegion());
        contentValues.put("seat", bean.getSeat());
        contentValues.put("describe", bean.getDescribe());
        contentValues.put("status", bean.getStatus());
        // 插入数据
        // insert方法参数1：要插入的表名
        // insert方法参数2：如果发现将要插入的行为空时，会将这个列名的值设为null
        // insert方法参数3：contentValue
        long i = db.insert(TB_ROOM, "id", contentValues);
        // 释放连接
        db.close();
        return i;
    }

    //新增自习室预约单
    public long insertRoomOrder(RoomOrder bean) {
        // 通过DBHelper类获取一个读写的SQLiteDatabase对象
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // 创建ContentValue设置参数
        ContentValues contentValues = new ContentValues();
        contentValues.put("room_id", bean.getRoom_id());
        contentValues.put("user_id", BaseApplication.app.userBean.get_id());
        contentValues.put("start_time", bean.getStart_time());
        contentValues.put("end_time", bean.getEnd_time());
        contentValues.put("region", bean.getRegion());
        contentValues.put("seat", bean.getSeat());
        // 插入数据
        // insert方法参数1：要插入的表名
        // insert方法参数2：如果发现将要插入的行为空时，会将这个列名的值设为null
        // insert方法参数3：contentValue
        long i = db.insert(TB_ROOM_ORDER, "id", contentValues);
        // 释放连接
        db.close();
        return i;
    }

    //查询出指定自习室指定区域和座位的数据
    public List<RoomOrder> queryRoomOrder(long room_id, String region, String seat){
        List<RoomOrder> roomBeans = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from room_order where room_id=? and region=? and seat=?", new String[]{Long.toString(room_id), region, seat});
        // 游标只要不是在最后一行之后，就一直循环
        return getRoomOrders(roomBeans, db, cursor);
    }

    //查询出指定自习室我预约的数据
    public List<RoomOrder> queryRoomOrder(long room_id, long user_id){
        List<RoomOrder> roomBeans = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from room_order where room_id=? and user_id=?", new String[]{Long.toString(room_id), Long.toString(user_id)});
        // 游标只要不是在最后一行之后，就一直循环
        return getRoomOrders(roomBeans, db, cursor);
    }

    private List<RoomOrder> getRoomOrders(List<RoomOrder> roomBeans, SQLiteDatabase db, Cursor cursor) {
        cursor.moveToFirst();
        RoomOrder roomOrder = null;
        while (!cursor.isAfterLast()){
            roomOrder = new RoomOrder();
            roomOrder.setId(cursor.getLong(0));
            roomOrder.setRoom_id(cursor.getLong(1));
            roomOrder.setUser_id(cursor.getLong(2));
            roomOrder.setStart_time(cursor.getLong(3));
            roomOrder.setEnd_time(cursor.getLong(4));
            roomOrder.setRegion(cursor.getString(5));
            roomOrder.setSeat(cursor.getString(6));
            roomBeans.add(roomOrder);
            // 将游标移到下一行
            cursor.moveToNext();
        }
        db.close();
        return roomBeans;
    }
}

