package com.example.androiddemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.androiddemo.app.BaseApplication;
import com.example.androiddemo.bean.UserBean;
import com.example.androiddemo.bean.VoteBean;
import com.example.androiddemo.bean.VoteItemBean;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TB_NAME = "user";
    private static final String TB_VOTE = "vote";
    private static final String TB_VOTE_ITEM = "vote_item";
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
                "is_login INTEGER DEFAULT 0," +
                "is_user INTEGER DEFAULT 1," +
                "user_class varchar," +
                "student_number varchar," +
                "student_name varchar" +
                ") ");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                TB_VOTE + " ( _id integer primary key autoincrement," +
                "create_id INTEGER DEFAULT 0," +
                "title varchar," +
                "describe varchar," +
                "vote_url varchar," +
                "type INTEGER DEFAULT 0," +
                "end_time INTEGER DEFAULT 0," +
                "single INTEGER DEFAULT 0," +
                "min INTEGER DEFAULT 1," +
                "max INTEGER DEFAULT 1," +
                "users varchar," +
                "start_time INTEGER DEFAULT 0," +
                "msg_contain_me varchar DEFAULT \"\" ," +
                "msg_dying_period varchar DEFAULT \"\" ," +
                "msg_expire varchar DEFAULT \"\" " +
                ") ");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                TB_VOTE_ITEM + " (  id integer primary key autoincrement," +
                "_id INTEGER DEFAULT 0," +
                "content varchar," +
                "url varchar," +
                "amount INTEGER DEFAULT 0," +
                "user_ids varchar DEFAULT \"\" " +
                ") ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        dbHelper.getWritableDatabase().execSQL("DROP TABLE IF EXISTS user");
        onCreate(dbHelper.getWritableDatabase());
    }

    public long insert(String user_name, String password, boolean isUser, String userClass, String studentNumber, String studentName) {
        // 通过DBHelper类获取一个读写的SQLiteDatabase对象
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // 创建ContentValue设置参数
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_name", user_name);
        contentValues.put("password", password);
        contentValues.put("is_user", isUser ? 1 : 0);
        contentValues.put("user_class", userClass);
        contentValues.put("student_number", studentNumber);
        contentValues.put("student_name", studentName);
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

    public void updateUserPwd(String pwd){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update user set password=? where _id=?",new Object[]{pwd, BaseApplication.userBean.get_id()});
        db.close();
    }

    public void updateUser(String studentClass, String studentNumber, String studentName){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("user_class", studentClass);
        cv.put("student_number", studentNumber);
        cv.put("student_name", studentName);
        db.update("user", cv, "_id=?", new String[]{String.valueOf(BaseApplication.userBean.get_id())});
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
            userBean.setIs_user(cursor.getInt(5));
            userBean.setUser_class(cursor.getString(6));
            userBean.setStudent_number(cursor.getString(7));
            userBean.setStudent_name(cursor.getString(8));
            // 将游标移到下一行
            cursor.moveToNext();
        }
        db.close();
        return userBean;
    }

    public UserBean queryUser(long id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from user where _id=?", new String[]{Long.toString(id)});
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
            userBean.setIs_user(cursor.getInt(5));
            userBean.setUser_class(cursor.getString(6));
            userBean.setStudent_number(cursor.getString(7));
            userBean.setStudent_name(cursor.getString(8));
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
            userBean.setIs_user(cursor.getInt(5));
            userBean.setUser_class(cursor.getString(6));
            userBean.setStudent_number(cursor.getString(7));
            userBean.setStudent_name(cursor.getString(8));
            // 将游标移到下一行
            cursor.moveToNext();
        }
        db.close();
        return userBean;
    }

    public List<UserBean> queryAllUser(){
        List<UserBean> userBeans = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from user", new String[]{});
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
            userBean.setIs_user(cursor.getInt(5));
            userBean.setUser_class(cursor.getString(6));
            userBean.setStudent_number(cursor.getString(7));
            userBean.setStudent_name(cursor.getString(8));
            userBeans.add(userBean);
            // 将游标移到下一行
            cursor.moveToNext();
        }
        db.close();
        return userBeans;
    }

    /**************************************************/
    /*****************投票信息的数据库操作*****************/
    /**************************************************/
    //插入一条投票数据
    public long insertVote(VoteBean bean) {
        // 通过DBHelper类获取一个读写的SQLiteDatabase对象
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // 创建ContentValue设置参数
        ContentValues contentValues = new ContentValues();
        contentValues.put("create_id", BaseApplication.userBean.get_id());
        contentValues.put("title", bean.getTitle());
        contentValues.put("describe", bean.getDescribe());
        contentValues.put("vote_url", bean.getVote_url());
        contentValues.put("type", bean.getType());
        contentValues.put("end_time", bean.getEnd_time());
        contentValues.put("single", bean.getSingle());
        contentValues.put("min", bean.getMin());
        contentValues.put("max", bean.getMax());
        contentValues.put("users", bean.getUsers());
        contentValues.put("start_time", bean.getStart_time());
        contentValues.put("msg_contain_me", bean.getMsg_contain_me());
        contentValues.put("msg_dying_period", bean.getMsg_dying_period());
        contentValues.put("msg_expire", bean.getMsg_expire());
        // 插入数据
        // insert方法参数1：要插入的表名
        // insert方法参数2：如果发现将要插入的行为空时，会将这个列名的值设为null
        // insert方法参数3：contentValue
        long i = db.insert(TB_VOTE, "create_id", contentValues);
        // 释放连接
        db.close();
        return i;
    }

    public void updateVote(VoteBean bean){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("create_id", bean.getCreate_id());
        contentValues.put("title", bean.getTitle());
        contentValues.put("describe", bean.getDescribe());
        contentValues.put("vote_url", bean.getVote_url());
        contentValues.put("type", bean.getType());
        contentValues.put("end_time", bean.getEnd_time());
        contentValues.put("single", bean.getSingle());
        contentValues.put("min", bean.getMin());
        contentValues.put("max", bean.getMax());
        contentValues.put("users", bean.getUsers());
        contentValues.put("start_time", bean.getStart_time());
        contentValues.put("msg_contain_me", bean.getMsg_contain_me());
        contentValues.put("msg_dying_period", bean.getMsg_dying_period());
        contentValues.put("msg_expire", bean.getMsg_expire());
        String[] args = {String.valueOf(bean.get_id())};
        db.update("vote", contentValues, "_id=?",args);
//        db.close();
    }

    public long insertVoteItem(VoteItemBean bean) {
        // 通过DBHelper类获取一个读写的SQLiteDatabase对象
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // 创建ContentValue设置参数
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", bean.get_id());
        contentValues.put("content", bean.getContent());
        contentValues.put("url", bean.getUrl());
        // 插入数据
        // insert方法参数1：要插入的表名
        // insert方法参数2：如果发现将要插入的行为空时，会将这个列名的值设为null
        // insert方法参数3：contentValue
        long i = db.insert(TB_VOTE_ITEM, "content", contentValues);
        // 释放连接
        db.close();
        return i;
    }

    //如果是管理员的话 就只查询出自己创建的投票记录
    public List<VoteBean> queryMeCreateVote(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from vote where create_id=?", new String[]{Long.toString(BaseApplication.userBean.get_id())});
        // 游标只要不是在最后一行之后，就一直循环
        cursor.moveToFirst();
        List<VoteBean> voteBeanList = new ArrayList<>();
        while (!cursor.isAfterLast()){
            VoteBean voteBean = new VoteBean();
            voteBean.set_id(cursor.getLong(0));
            voteBean.setCreate_id(cursor.getLong(1));
            voteBean.setTitle(cursor.getString(2));
            voteBean.setDescribe(cursor.getString(3));
            voteBean.setVote_url(cursor.getString(4));
            voteBean.setType(cursor.getInt(5));
            voteBean.setEnd_time(cursor.getLong(6));
            voteBean.setSingle(cursor.getInt(7));
            voteBean.setMin(cursor.getInt(8));
            voteBean.setMax(cursor.getInt(9));
            voteBean.setUsers(cursor.getString(10));
            voteBean.setStart_time(cursor.getLong(11));
            voteBean.setMsg_contain_me(cursor.getString(12));
            voteBean.setMsg_dying_period(cursor.getString(13));
            voteBean.setMsg_expire(cursor.getString(14));
            voteBeanList.add(voteBean);
            // 将游标移到下一行
            cursor.moveToNext();
        }
        db.close();
        return voteBeanList;
    }

    //如果是用户就查出所有的投票记录
    public List<VoteBean> queryAllVote(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from vote", new String[]{});
        // 游标只要不是在最后一行之后，就一直循环
        cursor.moveToFirst();
        List<VoteBean> voteBeanList = new ArrayList<>();
        while (!cursor.isAfterLast()){
            VoteBean voteBean = new VoteBean();
            voteBean.set_id(cursor.getLong(0));
            voteBean.setCreate_id(cursor.getLong(1));
            voteBean.setTitle(cursor.getString(2));
            voteBean.setDescribe(cursor.getString(3));
            voteBean.setVote_url(cursor.getString(4));
            voteBean.setType(cursor.getInt(5));
            voteBean.setEnd_time(cursor.getLong(6));
            voteBean.setSingle(cursor.getInt(7));
            voteBean.setMin(cursor.getInt(8));
            voteBean.setMax(cursor.getInt(9));
            voteBean.setUsers(cursor.getString(10));
            voteBean.setStart_time(cursor.getLong(11));
            voteBean.setMsg_contain_me(cursor.getString(12));
            voteBean.setMsg_dying_period(cursor.getString(13));
            voteBean.setMsg_expire(cursor.getString(14));
            voteBeanList.add(voteBean);
            // 将游标移到下一行
            cursor.moveToNext();
        }
        db.close();
        return voteBeanList;
    }

    public VoteBean queryAllVote(long id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from vote where _id=?", new String[]{Long.toString(id)});
        // 游标只要不是在最后一行之后，就一直循环
        cursor.moveToFirst();
        VoteBean voteBean = new VoteBean();
        while (!cursor.isAfterLast()){
            voteBean.set_id(cursor.getLong(0));
            voteBean.setCreate_id(cursor.getLong(1));
            voteBean.setTitle(cursor.getString(2));
            voteBean.setDescribe(cursor.getString(3));
            voteBean.setVote_url(cursor.getString(4));
            voteBean.setType(cursor.getInt(5));
            voteBean.setEnd_time(cursor.getLong(6));
            voteBean.setSingle(cursor.getInt(7));
            voteBean.setMin(cursor.getInt(8));
            voteBean.setMax(cursor.getInt(9));
            voteBean.setUsers(cursor.getString(10));
            voteBean.setStart_time(cursor.getLong(11));
            voteBean.setMsg_contain_me(cursor.getString(12));
            voteBean.setMsg_dying_period(cursor.getString(13));
            voteBean.setMsg_expire(cursor.getString(14));
            // 将游标移到下一行
            cursor.moveToNext();
        }
        db.close();
        return voteBean;
    }

    public List<VoteItemBean> queryVoteItem(long id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from vote_item where _id=? ORDER BY amount desc", new String[]{Long.toString(id)});
        // 游标只要不是在最后一行之后，就一直循环
        cursor.moveToFirst();
        List<VoteItemBean> voteBeanList = new ArrayList<>();
        while (!cursor.isAfterLast()){
            VoteItemBean voteBean = new VoteItemBean();
            voteBean.setId(cursor.getLong(0));
            voteBean.set_id(cursor.getLong(1));
            voteBean.setContent(cursor.getString(2));
            voteBean.setUrl(cursor.getString(3));
            voteBean.setAmount(cursor.getInt(4));
            voteBean.setUser_ids(cursor.getString(5));
            voteBeanList.add(voteBean);
            // 将游标移到下一行
            cursor.moveToNext();
        }
//        db.close();
        return voteBeanList;
    }

    public void updateVoteItem(VoteItemBean bean){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("amount", bean.getAmount() + 1);
        cv.put("user_ids", bean.getUser_ids() + BaseApplication.userBean.get_id() + ",");
        String[] args = {String.valueOf(bean.getId())};
        db.update("vote_item", cv, "id=?",args);
//        db.close();
    }

    public void updateVoteItemContent(VoteItemBean bean){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("content", bean.getContent());
        String[] args = {String.valueOf(bean.getId())};
        db.update("vote_item", cv, "id=?",args);
//        db.close();
    }

    public void updateVoteItemUrl(VoteItemBean bean){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("url", bean.getUrl());
        String[] args = {String.valueOf(bean.getId())};
        db.update("vote_item", cv, "id=?",args);
//        db.close();
    }

    public void deleteVoteItem(VoteBean voteBean) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //删除选中的投票主题
        db.delete("vote", "_id=?", new String[]{Long.toString(voteBean.get_id())});
        //删除选中的投票主题里面的所有选项
        db.delete("vote_item", "_id=?", new String[]{Long.toString(voteBean.get_id())});
//        db.close();
    }
}

