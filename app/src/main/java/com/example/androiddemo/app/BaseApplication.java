package com.example.androiddemo.app;

import android.app.Application;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;

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
      MMKV mmkv = MMKV.defaultMMKV();
      if (mmkv.getBoolean("isNight", false)){
         AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); //切换为夜间模式
      }else{
         AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //切换为日间间模式
      }
   }
}
