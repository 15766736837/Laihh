package com.example.androiddemo.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseActivity;
import com.example.androiddemo.bean.VoteBean;
import com.example.androiddemo.ui.adapter.NewMsgAdapter;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;

/**
 * 消息提醒
 */
public class MsgActivity extends BaseActivity {

    private ArrayList<VoteBean> newMsgBean;
    private RecyclerView recyclerView;
    private NewMsgAdapter newMsgAdapter;

    @Override
    public void initEvent() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recyclerView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_msg;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {
        MMKV mmkv = MMKV.defaultMMKV();
        mmkv.putBoolean("isNewMsg", false);

        newMsgBean = (ArrayList<VoteBean>) getIntent().getSerializableExtra("data");
        if (newMsgBean != null){
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            newMsgAdapter = new NewMsgAdapter(newMsgBean, this);
            recyclerView.setAdapter(newMsgAdapter);
            newMsgAdapter.setOnItemClick(voteBean -> {
                // TODO: 2023/4/4 跳转详情
            });
        }
    }
}