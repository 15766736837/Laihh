package com.example.androiddemo.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.androiddemo.app.BaseActivity;
import com.example.androiddemo.R;
import com.example.androiddemo.bean.UserBean;

/**
 * 注册
 */
public class RegActivity extends BaseActivity implements View.OnClickListener {
    private EditText mEtPwd, mEtConfirmPwd, mEtName;
    private Button mBtnReg;
    private ImageView mIvBack;

    @Override
    public void initEvent() {
        setEditTextTextChangedListener(mEtName);
        setEditTextTextChangedListener(mEtPwd);
        setEditTextTextChangedListener(mEtConfirmPwd);
        mBtnReg.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        mIvBack = findViewById(R.id.ivBack);
        mEtName = findViewById(R.id.etName);
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
                        && !"".equals(mEtName.getText().toString()));
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

/*              //网络请求
                Map<String, String> params = new HashMap<>();
                params.put("key1", "value1");
                params.put("key2", "value2");
                HttpUtils.request("http://example.com/api", "POST", params, new HttpUtils.Callback() {
                    @Override
                    public void onSuccess(String response) {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });*/
                break;
        }
    }
}