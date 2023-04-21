package com.example.androiddemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseActivity;
import com.example.androiddemo.app.BaseApplication;
import com.example.androiddemo.app.MainActivity;
import com.example.androiddemo.db.DBHelper;
import com.example.androiddemo.ui.login.LoginActivity;
import com.example.androiddemo.utils.ActivityStackManager;

/**
 * 修改密码
 */
public class ModifyPwdActivity extends BaseActivity {

    private EditText etNewPwd;
    private EditText etConfirmPwd;

    @Override
    public void initEvent() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnConfirm).setOnClickListener(v -> {
            //修改密码
            if ("".equals(etConfirmPwd.getText().toString()) || "".equals(etNewPwd.getText().toString())){
                Toast.makeText(this, "请输入您的新密码", Toast.LENGTH_SHORT).show();
                return;
            }else if(!etConfirmPwd.getText().toString().equals(etNewPwd.getText().toString())){
                Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                return;
            }
            DBHelper.getInstance(this).updateUserPwd(etConfirmPwd.getText().toString());
            DBHelper.getInstance(this).updateUser(BaseApplication.app.userBean.get_id(), 0);
            Toast.makeText(this, "修改成功,请重新登录", Toast.LENGTH_SHORT).show();
            ActivityStackManager.getInstance().getActivity(MainActivity.class).finish();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    @Override
    protected void initView() {
        etNewPwd = findViewById(R.id.etNewPwd);
        etConfirmPwd = findViewById(R.id.etConfirmPwd);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_pwd;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {

    }
}