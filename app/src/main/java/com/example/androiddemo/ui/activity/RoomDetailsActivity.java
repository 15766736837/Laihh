package com.example.androiddemo.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseActivity;
import com.example.androiddemo.bean.RoomBean;

/**
 * 预约详情
 */
public class RoomDetailsActivity extends BaseActivity implements View.OnClickListener {

    private TextView roomName, tvDescribe;

    @Override
    public void initEvent() {
        findViewById(R.id.ivBack).setOnClickListener(this);
    }

    @Override
    protected void initView() {
        roomName = findViewById(R.id.roomName);
        tvDescribe = findViewById(R.id.tvDescribe);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_details;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {
        RoomBean roomBean = (RoomBean) getIntent().getSerializableExtra("data");
        if(roomBean != null){
            roomName.setText(roomBean.getRoom_name());
            tvDescribe.setText(roomBean.getDescribe());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;
        }
    }
}