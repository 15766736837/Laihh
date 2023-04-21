package com.example.androiddemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseActivity;
import com.example.androiddemo.bean.RoomBean;
import com.example.androiddemo.db.DBHelper;
import com.example.androiddemo.ui.adapter.RoomAdapter;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//我预约的自习室
public class MyOrderActivity extends BaseActivity {

    private RecyclerView recyclerView;

    @Override
    public void initEvent() {

    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recyclerView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_order;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {
        List<RoomBean> roomBeans = DBHelper.getInstance(this).queryMyRoom();   //查询出所有的自习室
        //初始化列表并设置数据
        RoomAdapter roomAdapter = new RoomAdapter(roomBeans, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(roomAdapter);
        roomAdapter.setOnItemClick(roomBean -> {
            //列表的点击事件
            Intent intent = new Intent(this, RoomDetailsActivity.class);
            intent.putExtra("data", roomBean);
            startActivity(intent);
        });
    }
}