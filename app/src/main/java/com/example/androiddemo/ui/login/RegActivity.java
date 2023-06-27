package com.example.androiddemo.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseActivity;
import com.example.androiddemo.bean.UserDataBean;
import com.example.androiddemo.utils.HttpUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册
 */
public class RegActivity extends BaseActivity implements View.OnClickListener {
    private EditText mEtPwd, mEtConfirmPwd, mEtName, mEtPhone, mEtNickName, mEtJob, mEtSex;
    private Button mBtnReg;
    private ImageView mIvBack;

    @Override
    public void initEvent() {
        setEditTextTextChangedListener(mEtName);
        setEditTextTextChangedListener(mEtPwd);
        setEditTextTextChangedListener(mEtConfirmPwd);
        setEditTextTextChangedListener(mEtName);
        setEditTextTextChangedListener(mEtPhone);
        setEditTextTextChangedListener(mEtNickName);
        setEditTextTextChangedListener(mEtJob);
        setEditTextTextChangedListener(mEtSex);
        mBtnReg.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        mIvBack = findViewById(R.id.ivBack);
        mEtName = findViewById(R.id.etName);
        mEtPhone = findViewById(R.id.etPhone);
        mEtNickName = findViewById(R.id.etNickName);
        mEtJob = findViewById(R.id.etJob);
        mEtSex = findViewById(R.id.etSex);
        mEtPwd = findViewById(R.id.etPwd);
        mEtConfirmPwd = findViewById(R.id.etConfirmPwd);
        mBtnReg = findViewById(R.id.btnReg);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reg;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {

    }

    private void setEditTextTextChangedListener(EditText et) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mBtnReg.setEnabled(!"".equals(mEtPwd.getText().toString()) && !"".equals(mEtConfirmPwd.getText().toString())
                        && !"".equals(mEtName.getText().toString()) && !"".equals(mEtPhone.getText().toString())
                        && !"".equals(mEtNickName.getText().toString()) && !"".equals(mEtJob.getText().toString())
                        && !"".equals(mEtSex.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnReg:
                if (!mEtPwd.getText().toString().equals(mEtConfirmPwd.getText().toString())) {
                    Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

              //网络请求
                Map<String, String> params = new HashMap<>();
                params.put("username", mEtName.getText().toString());
                params.put("password", mEtConfirmPwd.getText().toString());
                params.put("phone", mEtPhone.getText().toString());
                params.put("sex", mEtSex.getText().toString());
                params.put("nickname", mEtNickName.getText().toString());
                params.put("job", mEtJob.getText().toString());
                // 发送POST请求
                HttpUtils.post("register", new Gson().toJson(params), new HttpUtils.HttpCallback() {
                    @Override
                    public void onSuccess(String response) {
                        // 请求成功，处理响应结果
                        Toast.makeText(RegActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        finish();
                        Log.e("XXX", "onSuccess: " + new Gson().fromJson(response, UserDataBean.class).toString());
                    }

                    @Override
                    public void onFailure(Exception e) {
                        // 请求失败，处理异常信息
                        Log.e("XXXX", "onFailure: " + e.getMessage(), e);
                    }
                });
                break;
        }
    }
}