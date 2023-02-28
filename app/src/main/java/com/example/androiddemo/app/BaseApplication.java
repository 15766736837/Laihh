package com.example.androiddemo.app;

import android.app.Application;

import com.example.androiddemo.bean.UserBean;
import com.example.androiddemo.db.DBHelper;
import com.tencent.mmkv.MMKV;

import java.util.Date;

public class BaseApplication extends Application {
   public static BaseApplication app;
   public static UserBean userBean;

   @Override
   public void onCreate() {
      super.onCreate();
      app = this;
      MMKV.initialize(this);
      userBean = DBHelper.getInstance(this).queryUser(1);
   }
}
