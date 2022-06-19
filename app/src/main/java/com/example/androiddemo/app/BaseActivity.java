package com.example.androiddemo.app;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddemo.utils.ActivityStackManager;
import com.example.androiddemo.utils.statusBar.StatusBarUtil;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.setTranslucentStatus(this);
        super.onCreate(savedInstanceState);
        ActivityStackManager.getInstance().pushActivity(this);
        setContentView(getLayoutId());
        initView();
        initEvent();
        initContent(savedInstanceState);
    }

    public abstract void initEvent();

    protected abstract void initView();

    protected abstract int getLayoutId();
    protected abstract void initContent(Bundle savedInstanceState);
}
