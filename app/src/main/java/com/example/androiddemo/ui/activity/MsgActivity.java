package com.example.androiddemo.ui.activity;

import android.os.Bundle;

import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseActivity;
import com.tencent.mmkv.MMKV;

/**
 * 消息提醒
 */
public class MsgActivity extends BaseActivity {

    @Override
    public void initEvent() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_msg;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {
        MMKV mmkv = MMKV.defaultMMKV();
        mmkv.putBoolean("isNewMsg", false);
    }
}