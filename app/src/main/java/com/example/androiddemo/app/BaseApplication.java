package com.example.androiddemo.app;

import android.app.Application;

import com.tencent.mmkv.MMKV;

public class BaseApplication extends Application {
   public static BaseApplication app;
   public static String BASE_URL = "http://42.192.154.61:58088/";

   @Override
   public void onCreate() {
      super.onCreate();
      app = this;
      MMKV.initialize(this);
   }
}
