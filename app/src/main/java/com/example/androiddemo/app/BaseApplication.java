package com.example.androiddemo.app;

import android.app.Application;

import com.example.androiddemo.bean.RoomBean;
import com.example.androiddemo.bean.UserBean;
import com.example.androiddemo.db.DBHelper;
import com.tencent.mmkv.MMKV;

import java.util.List;

public class BaseApplication extends Application {
   public static BaseApplication app;
   public UserBean userBean;


   @Override
   public void onCreate() {
      super.onCreate();
      app = this;
      MMKV.initialize(this);

      //初始化一些科室的信息
      DBHelper db = DBHelper.getInstance(this);
      userBean = db.queryUser(1);
      List<RoomBean> roomBeans = DBHelper.getInstance(this).queryAllRoom();
      if (roomBeans.isEmpty()) {
         RoomBean roomBean1 = new RoomBean();
         roomBean1.setRoom_name("自习室1");
         roomBean1.setDescribe("自习室1的描述!!!!");
         roomBean1.setRegion("A区,B区,C区,D区");
         roomBean1.setSeat("1,2,3,4,5,6,7,8,9,10");
         db.insertRoom(roomBean1);

         roomBean1.setRoom_name("自习室2");
         roomBean1.setDescribe("自习室2的描述!!!!");
         db.insertRoom(roomBean1);

         roomBean1.setRoom_name("自习室3");
         roomBean1.setDescribe("自习室3的描述!!!!");
         db.insertRoom(roomBean1);
      } else {
         // 这里统一处理自习室的状态
      }
   }
}
