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
import com.example.androiddemo.bean.UsersBean;
import com.example.androiddemo.utils.HttpUtils;
import com.example.androiddemo.widget.UsersDialog;
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

    private TextView title, tvContent, tvDate, tvEndDate, tvTitle, tvUsers;
    private TimePickerView pvTime, pvEndTime;
    private long time, endTime;
    private EditText etTitle, etDescribe;
    private HomeRoomDataBean.HomeRoomBean data;
    private int type;

    @Override
    public void initEvent() {

    }

    @Override
    protected void initView() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        title = findViewById(R.id.title);
        tvTitle = findViewById(R.id.tvTitle);
        tvContent = findViewById(R.id.tvContent);
        tvDate = findViewById(R.id.tvDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        etTitle = findViewById(R.id.etTitle);
        etDescribe = findViewById(R.id.etDescribe);
        tvUsers = findViewById(R.id.tvUsers);
        tvDate.setOnClickListener(v -> pvTime.show());
        tvEndDate.setOnClickListener(v -> pvEndTime.show());
        tvUsers.setOnClickListener(v -> {
            UsersDialog dialogFragment = new UsersDialog();
            dialogFragment.show(getSupportFragmentManager(), "MyDialog");
            dialogFragment.setDialogCallback(response -> Toast.makeText(HomeDetailsRoomActivity.this, response, Toast.LENGTH_SHORT).show());
        });

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

        pvEndTime = new TimePickerBuilder(this, (date, v1) -> {
            //选择完截止日期后 会进入这个回调 在这里面把选择完的日期显示在页面上
            endTime = date.getTime();
            tvEndDate.setText(getTime(date.getTime()));
        }).setType(new boolean[]{true, true, true, true, true, false})
                .setRangDate(instance, null)//起始终止年月日设定
                .build();

        findViewById(R.id.btnReservation).setOnClickListener(v -> {
            if (type == 1){
                //预约
                if (tvDate.getText() != null && !tvDate.getText().toString().isEmpty() &&
                        tvEndDate.getText() != null && !tvEndDate.getText().toString().isEmpty()) {
                    String user = MMKV.defaultMMKV().decodeString("user");
                    UserDataBean userDataBean = new Gson().fromJson(user, UserDataBean.class);
                    Map<String, String> params = new HashMap<>();
                    params.put("uid", userDataBean.getUser().getId() + "");
                    params.put("rid", data.getId() + "");
                    params.put("appointTime", time + "");
                    HttpUtils.post("meeting/reservation/", new Gson().toJson(params), new HttpUtils.HttpCallback() {
                        @Override
                        public void onSuccess(String response) {
                            Toast.makeText(HomeDetailsRoomActivity.this, "预约成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFailure(Exception e) {

                        }
                    });
                }else{
                    Toast.makeText(this, "请选择时间", Toast.LENGTH_SHORT).show();
                }
            }else{
                //预定
                clientMeeting();
            }
        });
    }

    /**
     * 预定会议
     */
    private void clientMeeting(){
        if (etTitle.getText() != null && !etTitle.getText().toString().isEmpty() &&
                etDescribe.getText() != null && !etDescribe.getText().toString().isEmpty()) {
            //调用预约接口
            String user = MMKV.defaultMMKV().decodeString("user");
            UserDataBean userDataBean = new Gson().fromJson(user, UserDataBean.class);
            Map<String, String> params = new HashMap<>();
            params.put("title", etTitle.getText().toString());
            params.put("topic", etDescribe.getText().toString());
            params.put("uid", userDataBean.getUser().getId() + "");
            params.put("rid", data.getId() + "");
//            params.put("appointTime", time + "");
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
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home_details_room;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {
        data = (HomeRoomDataBean.HomeRoomBean) getIntent().getSerializableExtra("data");
        type = getIntent().getIntExtra("type", 1);
        type = 2;
        etTitle.setVisibility(type == 1 ? View.GONE : View.VISIBLE);
        etDescribe.setVisibility(type == 1 ? View.GONE : View.VISIBLE);
        findViewById(R.id.rlUsers).setVisibility(type == 1 ? View.GONE : View.VISIBLE);
        findViewById(R.id.view).setVisibility(type == 1 ? View.GONE : View.VISIBLE);
        findViewById(R.id.rlStart).setVisibility(type == 1 ? View.VISIBLE : View.GONE);
        findViewById(R.id.rlEnd).setVisibility(type == 1 ? View.VISIBLE : View.GONE);
        findViewById(R.id.view1).setVisibility(type == 1 ? View.VISIBLE : View.GONE);
        tvTitle.setText(type == 1 ? "预约详情" : "预定详情");
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