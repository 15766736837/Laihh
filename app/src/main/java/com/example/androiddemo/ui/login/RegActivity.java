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
import com.example.androiddemo.db.DBHelper;

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

                UserBean userBean = DBHelper.getInstance(this).queryUser(mEtName.getText().toString());
                if (userBean != null) {
                    Toast.makeText(this, "账号已存在", Toast.LENGTH_SHORT).show();
                    return;
                }

                //注册信息储存到数据库
                long insert = DBHelper.getInstance(this).insert(mEtName.getText().toString(), mEtPwd.getText().toString());
                if (insert != -1) {
                    Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}