package com.example.androiddemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseActivity;
import com.example.androiddemo.app.MainActivity;
import com.example.androiddemo.ui.login.LoginActivity;
import com.example.androiddemo.utils.ActivityStackManager;
import com.example.androiddemo.widget.TipsDialog;
import com.tencent.mmkv.MMKV;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private TipsDialog mTipsDialog;

    @Override
    public void initEvent() {
        findViewById(R.id.tvSignOut).setOnClickListener(this);
        findViewById(R.id.ivBack).setOnClickListener(this);
        mTipsDialog.setListener(new TipsDialog.TipsDialogListener() {
            @Override
            public void onCancel() {
                mTipsDialog.dismiss();
            }

            @Override
            public void onConfirm() {
                MMKV.defaultMMKV().encode("user", "");
                mTipsDialog.dismiss();
                ActivityStackManager.getInstance().getActivity(MainActivity.class).finish();
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void initView() {
        mTipsDialog = new TipsDialog(this, "是否退出登录?");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvSignOut:
                mTipsDialog.show();
                break;
            case R.id.ivBack:
                finish();
                break;
        }
    }
}