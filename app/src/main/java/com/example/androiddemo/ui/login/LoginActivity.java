package com.example.androiddemo.ui.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiddemo.app.BaseActivity;
import com.example.androiddemo.app.BaseApplication;
import com.example.androiddemo.app.MainActivity;
import com.example.androiddemo.R;
import com.example.androiddemo.bean.UserBean;
import com.example.androiddemo.db.DBHelper;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText mEtPwd, mEtName;
    private TextView mBtnReg, tvUser, tvAdmin;
    private Button mBtnLogin;
    private boolean isUser = true; //true = 用户 false = 管理员

    @Override
    public void initEvent() {
        setEditTextTextChangedListener(mEtName);
        setEditTextTextChangedListener(mEtPwd);
        //申明按钮点击事件回掉
        mBtnReg.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        tvUser.setOnClickListener(this);
        tvAdmin.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        mBtnReg = findViewById(R.id.btnReg);
        mBtnLogin = findViewById(R.id.btnLogin);
        mEtName = findViewById(R.id.etName);
        mEtPwd = findViewById(R.id.etPwd);
        tvUser = findViewById(R.id.tvUser);
        tvAdmin = findViewById(R.id.tvAdmin);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {
        UserBean userBean = DBHelper.getInstance(this).queryUser(1);
        if(userBean != null){
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

    /**
     * 按钮点击事件
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvUser:
                setTabStyle(true);
                break;
            case R.id.tvAdmin:
                setTabStyle(false);
                break;
            //注册按钮的事件回掉
            case R.id.btnReg:
                startActivity(new Intent(this, RegActivity.class));
                break;
            //登陆按钮的事件回掉
            case R.id.btnLogin:
                UserBean userBean = DBHelper.getInstance(this).queryUser(mEtName.getText().toString());
                if (userBean == null) {
                    Toast.makeText(this, "账号不存在", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!mEtPwd.getText().toString().equals(userBean.getPassword())) {
                    Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
                    return;
                }

                userBean.setIs_login(1);
                DBHelper.getInstance(this).updateUser(userBean.get_id(), 1);
                BaseApplication.userBean = userBean;
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
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