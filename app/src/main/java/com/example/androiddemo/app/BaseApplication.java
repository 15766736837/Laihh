package com.example.androiddemo.app;

import android.app.Application;

import com.tencent.mmkv.MMKV;

public class BaseApplication extends Application {
   public static BaseApplication app;


   @Override
   public void onCreate() {
      super.onCreate();
      app = this;
      MMKV.initialize(this);
   }
}
