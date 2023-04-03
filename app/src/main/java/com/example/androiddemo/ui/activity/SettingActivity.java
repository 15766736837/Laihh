package com.example.androiddemo.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseActivity;
import com.example.androiddemo.app.MainActivity;
import com.example.androiddemo.bean.UserBean;
import com.example.androiddemo.db.DBHelper;
import com.example.androiddemo.ui.login.LoginActivity;
import com.example.androiddemo.utils.ActivityStackManager;
import com.example.androiddemo.widget.SwitchButton;
import com.example.androiddemo.widget.TipsDialog;
import com.tencent.mmkv.MMKV;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private TipsDialog mTipsDialog;
    private SwitchButton sbImg;

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
                UserBean userBean = DBHelper.getInstance(SettingActivity.this).queryUser(1);
                DBHelper.getInstance(SettingActivity.this).updateUser(userBean.get_id(), 0);
                ActivityStackManager.getInstance().getActivity(MainActivity.class).finish();
                mTipsDialog.dismiss();
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void initView() {
        MMKV mmkv = MMKV.defaultMMKV();
        int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        sbImg = findViewById(R.id.sbImg);
        sbImg.setOnCheckedChangeListener((view, isChecked) -> {
            if (mode == Configuration.UI_MODE_NIGHT_YES){
                //当前是夜间模式需要切换成日间模式
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //切换为日间间模式
                mmkv.putBoolean("isNight", false);
            }else{
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); //切换为夜间模式
                mmkv.putBoolean("isNight", true);
            }
            recreate();//需要调用该方法才能生效
        });
        mTipsDialog = new TipsDialog(this, "是否退出登录?");
        if (mode == Configuration.UI_MODE_NIGHT_YES){
            sbImg.setChecked(true);
        }else{
            sbImg.setChecked(false);
        }
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