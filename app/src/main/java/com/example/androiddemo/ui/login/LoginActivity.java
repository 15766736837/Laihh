package com.example.androiddemo.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiddemo.app.BaseActivity;
import com.example.androiddemo.R;
import com.example.androiddemo.app.MainActivity;
import com.example.androiddemo.utils.HttpUtils;
import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText mEtPwd, mEtName;
    private TextView mBtnReg;
    private Button mBtnLogin;
    String key = "user";

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
        String value = MMKV.defaultMMKV().decodeString(key);
        if(value != null && !value.isEmpty()){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
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
                Map<String, String> params = new HashMap<>();
                params.put("username", mEtName.getText().toString());
                params.put("password", mEtPwd.getText().toString());
                HttpUtils.post("login", new Gson().toJson(params), new HttpUtils.HttpCallback() {
                    @Override
                    public void onSuccess(String response) {
                        MMKV.defaultMMKV().encode(key, response);
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
                break;
        }
    }
}