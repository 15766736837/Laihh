package com.example.androiddemo.ui.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseActivity;
import com.example.androiddemo.app.BaseApplication;
import com.example.androiddemo.bean.UserBean;
import com.example.androiddemo.db.DBHelper;

/**
 * 修改个人信息页面
 */
public class EditInfoActivity extends BaseActivity {

    private EditText etClass;
    private EditText etStudentNumber;
    private EditText etStudentName;

    @Override
    public void initEvent() {
        findViewById(R.id.btnConfirm).setOnClickListener(v -> {
            if("".equals(etClass.getText().toString())){
                Toast.makeText(this, "请输入您的班级", Toast.LENGTH_SHORT).show();
                return;
            }else if("".equals(etStudentNumber.getText().toString())){
                Toast.makeText(this, "请输入您的学号", Toast.LENGTH_SHORT).show();
                return;
            }else if("".equals(etStudentName.getText().toString())){
                Toast.makeText(this, "请输入您的姓名", Toast.LENGTH_SHORT).show();
                return;
            }
            //修改个人信息
            DBHelper.getInstance(this).updateUser(etClass.getText().toString(), etStudentNumber.getText().toString(), etStudentName.getText().toString());
            Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
            BaseApplication.userBean = DBHelper.getInstance(this).queryUser(1);
            finish();
        });
    }

    @Override
    protected void initView() {
        etClass = findViewById(R.id.etClass);
        etStudentNumber = findViewById(R.id.etStudentNumber);
        etStudentName = findViewById(R.id.etStudentName);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_info;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {
        etClass.setText(BaseApplication.userBean.getUser_class());
        etStudentNumber.setText(BaseApplication.userBean.getStudent_number());
        etStudentName.setText(BaseApplication.userBean.getStudent_name());
    }
}