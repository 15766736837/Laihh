package com.example.androiddemo.ui.login;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiddemo.app.BaseActivity;
import com.example.androiddemo.R;
import com.example.androiddemo.bean.UserBean;
import com.example.androiddemo.db.DBHelper;

/**
 * 注册
 */
public class RegActivity extends BaseActivity implements View.OnClickListener {
    private EditText mEtPwd, mEtConfirmPwd, mEtName, mEtClass, mEtStudentNumber, mEtStudentName;
    private Button mBtnReg;
    private ImageView mIvBack;
    private TextView tvUser, tvAdmin;
    private boolean isUser = true; //true = 用户 false = 管理员

    @Override
    public void initEvent() {
        setEditTextTextChangedListener(mEtClass);
        setEditTextTextChangedListener(mEtStudentNumber);
        setEditTextTextChangedListener(mEtStudentName);
        setEditTextTextChangedListener(mEtName);
        setEditTextTextChangedListener(mEtPwd);
        setEditTextTextChangedListener(mEtConfirmPwd);
        mBtnReg.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        tvUser.setOnClickListener(this);
        tvAdmin.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        mIvBack = findViewById(R.id.ivBack);
        mEtClass = findViewById(R.id.etClass);
        mEtStudentNumber = findViewById(R.id.etStudentNumber);
        mEtStudentName = findViewById(R.id.etStudentName);
        mEtName = findViewById(R.id.etName);
        mEtPwd = findViewById(R.id.etPwd);
        mEtConfirmPwd = findViewById(R.id.etConfirmPwd);
        mBtnReg = findViewById(R.id.btnReg);
        tvUser = findViewById(R.id.tvUser);
        tvAdmin = findViewById(R.id.tvAdmin);
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
                mBtnReg.setEnabled(
                        !"".equals(mEtClass.getText().toString()) &&
                                !"".equals(mEtStudentNumber.getText().toString()) &&
                                !"".equals(mEtStudentName.getText().toString()) &&
                                !"".equals(mEtPwd.getText().toString()) &&
                                !"".equals(mEtConfirmPwd.getText().toString()) &&
                                !"".equals(mEtName.getText().toString())
                );
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvUser:
                setTabStyle(true);
                break;
            case R.id.tvAdmin:
                setTabStyle(false);
                break;
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
                long insert = DBHelper.getInstance(this).insert(
                        mEtName.getText().toString(), mEtPwd.getText().toString(),
                        isUser, mEtClass.getText().toString(), mEtStudentNumber.getText().toString(),
                        mEtStudentName.getText().toString()
                );
                if (insert != -1) {
                    Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 设置用户/管理员切换按钮的样式
     */
    private void setTabStyle(boolean isUser) {
        this.isUser = isUser;
        tvUser.setBackground(isUser ? getDrawable(R.drawable.shape_account_type_selected) : null);
        tvAdmin.setBackground(!isUser ? getDrawable(R.drawable.shape_account_type_selected) : null);
        tvUser.setTextColor(Color.parseColor(isUser ? "#FFFFFF" : "#262936"));
        tvAdmin.setTextColor(Color.parseColor(!isUser ? "#FFFFFF" : "#262936"));
    }
}