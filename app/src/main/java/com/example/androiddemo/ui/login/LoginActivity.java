package com.example.androiddemo.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiddemo.app.BaseActivity;
import com.example.androiddemo.app.MainActivity;
import com.example.androiddemo.R;
import com.example.androiddemo.bean.UserBean;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText mEtPwd, mEtName;
    private TextView mBtnReg;
    private Button mBtnLogin;

    @Override
    public void initEvent() {
        setEditTextTextChangedListener(mEtName);
        setEditTextTextChangedListener(mEtPwd);
        mBtnReg.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        mBtnReg = findViewById(R.id.btnReg);
        mBtnLogin = findViewById(R.id.btnLogin);
        mEtName = findViewById(R.id.etName);
        mEtPwd = findViewById(R.id.etPwd);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {
/*        UserBean userBean = DBHelper.getInstance(this).queryUser(1);
        if(userBean != null){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }*/
    }

    private void setEditTextTextChangedListener(EditText et) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mBtnLogin.setEnabled(!"".equals(mEtPwd.getText().toString()) && !"".equals(mEtName.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnReg:
                startActivity(new Intent(this, RegActivity.class));
                break;
            case R.id.btnLogin:
/*                UserBean userBean = DBHelper.getInstance(this).queryUser(mEtName.getText().toString());
                if (userBean == null) {
                    Toast.makeText(this, "账号不存在", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!mEtPwd.getText().toString().equals(userBean.getPassword())) {
                    Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
                    return;
                }

                userBean.setIs_login(1);
                DBHelper.getInstance(this).updateUser(userBean.get_id(), 1);
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();*/
                break;
        }
    }
}