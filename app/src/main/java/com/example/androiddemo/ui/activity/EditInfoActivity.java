package com.example.androiddemo.ui.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseActivity;
import com.example.androiddemo.bean.UserDataBean;
import com.example.androiddemo.utils.HttpUtils;
import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改个人信息页面
 */
public class EditInfoActivity extends BaseActivity {

    private EditText etStudentName;
    UserDataBean userDataBean;
    @Override
    public void initEvent() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnConfirm).setOnClickListener(v -> {
            if("".equals(etStudentName.getText().toString())){
                Toast.makeText(this, "请输入您的用户名", Toast.LENGTH_SHORT).show();
                return;
            }
            if (userDataBean == null)
                return;

            //修改个人信息
            Map<String, String> params = new HashMap<>();
            params.put("id", userDataBean.getUser().getId() + "");
            params.put("nickname", etStudentName.getText().toString());
            HttpUtils.put("user/", new Gson().toJson(params), new HttpUtils.HttpCallback() {
                @Override
                public void onSuccess(String response) {
                    MMKV.defaultMMKV().encode("user", response);
                    Toast.makeText(EditInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        });
    }

    @Override
    protected void initView() {
        etStudentName = findViewById(R.id.etStudentName);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_info;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {
        String user = MMKV.defaultMMKV().decodeString("user");
        userDataBean = new Gson().fromJson(user, UserDataBean.class);
        etStudentName.setText(userDataBean.getUser().getNickname());
    }
}