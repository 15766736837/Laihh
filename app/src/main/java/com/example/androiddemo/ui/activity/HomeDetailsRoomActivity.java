package com.example.androiddemo.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseActivity;
import com.example.androiddemo.bean.HomeRoomDataBean;
import com.example.androiddemo.bean.UserDataBean;
import com.example.androiddemo.utils.HttpUtils;
import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 预约会议室
 */
public class HomeDetailsRoomActivity extends BaseActivity {

    private TextView title, tvContent, tvDate;
    private TimePickerView pvTime;
    private long time;
    private EditText etTitle, etDescribe;
    private HomeRoomDataBean.HomeRoomBean data;

    @Override
    public void initEvent() {

    }

    @Override
    protected void initView() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        title = findViewById(R.id.title);
        tvContent = findViewById(R.id.tvContent);
        tvDate = findViewById(R.id.tvDate);
        etTitle = findViewById(R.id.etTitle);
        etDescribe = findViewById(R.id.etDescribe);
        tvDate.setOnClickListener(v -> pvTime.show());

        //系统当前时间
        long oneDay = 24 * 60 * 60 * 1000;
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date(new Date().getTime() + oneDay));  //设置当前时间的后一天
        //初始化时间选择器
        pvTime = new TimePickerBuilder(this, (date, v1) -> {
            //选择完截止日期后 会进入这个回调 在这里面把选择完的日期显示在页面上
            time = date.getTime();
            tvDate.setText(getTime(date.getTime()));
        }).setType(new boolean[]{true, true, true, true, true, false})
                .setRangDate(instance, null)//起始终止年月日设定
                .build();

        findViewById(R.id.btnReservation).setOnClickListener(v -> {
            if (etTitle.getText() != null && !etTitle.getText().toString().isEmpty() &&
                    etDescribe.getText() != null && !etDescribe.getText().toString().isEmpty() &&
                    tvDate.getText() != null && !tvDate.getText().toString().isEmpty()) {
                //调用预约接口
                String user = MMKV.defaultMMKV().decodeString("user");
                UserDataBean userDataBean = new Gson().fromJson(user, UserDataBean.class);
                Map<String, String> params = new HashMap<>();
                params.put("title", etTitle.getText().toString());
                params.put("topic", etDescribe.getText().toString());
                params.put("uid", userDataBean.getUser().getId() + "");
                params.put("rid", data.getId() + "");
                params.put("appointTime", time + "");
                HttpUtils.post("client/meeting/", new Gson().toJson(params), new HttpUtils.HttpCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Toast.makeText(HomeDetailsRoomActivity.this, "预约成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            }else {
                Toast.makeText(this, "请填写预约信息", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home_details_room;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {
        data = (HomeRoomDataBean.HomeRoomBean) getIntent().getSerializableExtra("data");
        title.setText(data.getName());
        tvContent.setText(data.getLocation());
    }

    private String getTime(long milSecond) {
        //根据需要自行截取数据显示
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
}