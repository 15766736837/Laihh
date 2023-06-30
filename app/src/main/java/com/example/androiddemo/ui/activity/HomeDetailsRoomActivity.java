package com.example.androiddemo.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import okhttp3.OkHttpClient;

/**
 * 预约会议室
 */
public class HomeDetailsRoomActivity extends BaseActivity {

    private TextView title, tvContent, tvDate, tvEndDate, tvTitle, tvUsers;
    private TimePickerView pvTime, pvEndTime;
    private long time, endTime;
    private EditText etTitle, etDescribe;
    private Button btnReservation, btnModify;
    private HomeRoomDataBean.HomeRoomBean data;
    private int type;
    private UsersBean usersBean;

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
        btnReservation = findViewById(R.id.btnReservation);
        btnModify = findViewById(R.id.btnModify);
        tvDate.setOnClickListener(v -> pvTime.show());
        tvEndDate.setOnClickListener(v -> pvEndTime.show());
        tvUsers.setOnClickListener(v -> {
            UsersDialog dialogFragment = new UsersDialog();
            dialogFragment.show(getSupportFragmentManager(), "MyDialog");
            dialogFragment.setDialogCallback(response -> {
                Map<String, String> params = new HashMap<>();
                params.put("uid", response.getId() + "");
                params.put("mid", data.getMid() + "");
                HttpUtils.post("client/meeting/group/add/", new Gson().toJson(params), new HttpUtils.HttpCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Toast.makeText(HomeDetailsRoomActivity.this, "邀请加入会议成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            });
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

        btnReservation.setOnClickListener(v -> {
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
                    params.put("endTime", endTime + "");
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
            }else if (type == 2){
                //预定
                clientMeeting();
            }/*else if(type == 3){
                if (usersBean.getList().size() <= 0){
                    Toast.makeText(this, "请先添加参会人员", Toast.LENGTH_SHORT).show();
                }else{
                    //发起会议
                    Map<String, String> params = new HashMap<>();
                    params.put("id", data.getMid() + "");
                    HttpUtils.get("client/meeting/start/"+data.getMid(), new HttpUtils.HttpCallback() {
                        @Override
                        public void onSuccess(String response) {
                            Toast.makeText(HomeDetailsRoomActivity.this, "会议开始", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFailure(Exception e) {

                        }
                    });
                }
            }*/
        });

        btnModify.setOnClickListener(v -> {
            //修改会议信息
            if (etTitle.getText() != null && !etTitle.getText().toString().isEmpty() &&
                    etDescribe.getText() != null && !etDescribe.getText().toString().isEmpty()) {
                Map<String, String> params = new HashMap<>();
                params.put("id", data.getMid() + "");
                params.put("title", etTitle.getText().toString());
                params.put("topic", etDescribe.getText().toString());
                HttpUtils.put("client/meeting/", new Gson().toJson(params), new HttpUtils.HttpCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Toast.makeText(HomeDetailsRoomActivity.this, "信息修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            }else {
                Toast.makeText(this, "会议信息不能为空", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnDel).setOnClickListener(v -> {
            //删除会议
            HttpUtils.get("client/meeting/cancel/" + data.getMid(), new HttpUtils.HttpCallback() {
                @Override
                public void onSuccess(String response) {
                    Toast.makeText(HomeDetailsRoomActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
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
            HttpUtils.post("client/meeting/", new Gson().toJson(params), new HttpUtils.HttpCallback() {
                @Override
                public void onSuccess(String response) {
                    Toast.makeText(HomeDetailsRoomActivity.this, "预定成功", Toast.LENGTH_SHORT).show();
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

        switch (type){
            case 1:
                //预约详情
                findViewById(R.id.rlStart).setVisibility(View.VISIBLE);
                findViewById(R.id.rlEnd).setVisibility(View.VISIBLE);
                findViewById(R.id.view1).setVisibility(View.VISIBLE);
                tvTitle.setText("预约详情");
                btnReservation.setText("确定预约");
                ((TextView)findViewById(R.id.tvInfoTitle)).setText("会议室预约信息");
                break;
            case 2:
                //预定详情
                etTitle.setVisibility(View.VISIBLE);
                etDescribe.setVisibility(View.VISIBLE);
                findViewById(R.id.view).setVisibility(View.VISIBLE);
                tvTitle.setText("预定详情");
                btnReservation.setText("确定预定");
                ((TextView)findViewById(R.id.tvInfoTitle)).setText("会议室预定信息");
                break;
            case 3:
                findViewById(R.id.rlUsers).setVisibility(View.VISIBLE);
                tvTitle.setText("我的会议详情");
                ((TextView)findViewById(R.id.tvInfoTitle)).setText("会议详情信息");
                btnReservation.setVisibility(View.GONE);
                etTitle.setVisibility(View.VISIBLE);
                etDescribe.setVisibility(View.VISIBLE);
                etTitle.setText(data.getTitle());
                etDescribe.setText(data.getTopic());
                btnModify.setVisibility(View.VISIBLE);
                findViewById(R.id.rlStart).setVisibility(View.VISIBLE);
                findViewById(R.id.rlEnd).setVisibility(View.VISIBLE);
                findViewById(R.id.btnDel).setVisibility(View.VISIBLE);
                tvDate.setText(getTime(data.getAppointTime()));
                tvEndDate.setText(getTime(data.getEndTime()));
                tvDate.setEnabled(false);
                tvEndDate.setEnabled(false);
                getUser();
                break;
            case 4:
                findViewById(R.id.rlUsers).setVisibility(View.VISIBLE);
                tvUsers.setEnabled(false);
                btnReservation.setVisibility(View.GONE);
                tvTitle.setText("我的会议详情");
                ((TextView)findViewById(R.id.tvInfoTitle)).setText("会议详情信息");
                etTitle.setVisibility(View.VISIBLE);
                etDescribe.setVisibility(View.VISIBLE);
                etTitle.setText(data.getTitle());
                etDescribe.setText(data.getTopic());
                findViewById(R.id.rlStart).setVisibility(View.VISIBLE);
                findViewById(R.id.rlEnd).setVisibility(View.VISIBLE);
                tvDate.setText(getTime(data.getAppointTime()));
                tvEndDate.setText(getTime(data.getEndTime()));
                tvDate.setEnabled(false);
                tvEndDate.setEnabled(false);
                getUser();
                break;
        }
        title.setText(data.getName());
        tvContent.setText(data.getLocation());
    }

    private void getUser() {
        HttpUtils.get("client/group/" + data.getMid(), new HttpUtils.HttpCallback() {
            @Override
            public void onSuccess(String response) {
                /**
                 * 如果没有参会人员：
                 *  1，只显示添加参会人员按钮
                 *  如果有参会人员：
                 *  1，显示修改按钮
                 *  2，显示发起会议按钮
                 */
                usersBean = new Gson().fromJson(response, UsersBean.class);
                if (usersBean.getList().size() <= 0){
                    //没有参会人员
//                            btnReservation.setText("添加参会人员");
                }else{
                    String user = "";
                    for (int i = 0; i < usersBean.getList().size(); i++) {
                        user += usersBean.getList().get(i).getNickname() + " ";
                    }
                    tvUsers.setText(user);
//                    btnReservation.setText("发起会议");
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private String getTime(long milSecond) {
        //根据需要自行截取数据显示
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
}