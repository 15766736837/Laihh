package com.example.androiddemo.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseActivity;
import com.example.androiddemo.bean.RoomBean;
import com.example.androiddemo.widget.BottomDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 预约详情
 */
public class RoomDetailsActivity extends BaseActivity implements View.OnClickListener {

    private TextView roomName, tvDescribe, tvStartTime, tvEndTime, tvRegion, tvSeat;
    private TimePickerView pvStartTime, pvEndTime;
    private BottomDialog regionDialog, seatDialog;
    private long startTime, endTime;
    private RoomBean roomBean;

    @Override
    public void initEvent() {
        tvStartTime = findViewById(R.id.tvStartTime);
        tvEndTime = findViewById(R.id.tvEndTime);
        tvRegion = findViewById(R.id.tvRegion);
        tvSeat = findViewById(R.id.tvSeat);
        findViewById(R.id.ivBack).setOnClickListener(this);
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        tvRegion.setOnClickListener(this);
        tvSeat.setOnClickListener(this);
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
        roomBean = (RoomBean) getIntent().getSerializableExtra("data");
        if(roomBean != null){
            roomName.setText(roomBean.getRoom_name());
            tvDescribe.setText(roomBean.getDescribe());
            regionDialog = new BottomDialog("自习室区域", roomBean.getRegion());
            seatDialog = new BottomDialog("自习室位置", roomBean.getSeat());
            regionDialog.setOnSelectListener(content -> tvRegion.setText(content));
            seatDialog.setOnSelectListener(content -> tvSeat.setText(content));
        }

        //初始化开始时间选择器
        pvStartTime = new TimePickerBuilder(this, (date, v1) -> {
            //选择完截止日期后 会进入这个回调 在这里面把选择完的日期显示在页面上
            startTime = date.getTime();
            tvStartTime.setText(getTime(date.getTime()));
        }).setType(new boolean[]{true, true, true, true, true, false})
                .setRangDate(Calendar.getInstance(), null)//起始终止年月日设定
                .build();

        //初始化时间选择器
        pvEndTime = new TimePickerBuilder(this, (date, v1) -> {
            //选择完截止日期后 会进入这个回调 在这里面把选择完的日期显示在页面上
            endTime = date.getTime();
            tvEndTime.setText(getTime(date.getTime()));
        }).setType(new boolean[]{true, true, true, true, true, false})
                .setRangDate(Calendar.getInstance(), null)//起始终止年月日设定
                .build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvStartTime:
                pvStartTime.show();
                break;
            case R.id.tvEndTime:
                pvEndTime.show();
                break;
            case R.id.tvRegion:
                if(regionDialog != null)
                    regionDialog.show(getSupportFragmentManager(), null);
                break;
            case R.id.tvSeat:
                if(seatDialog != null)
                    seatDialog.show(getSupportFragmentManager(), null);
                break;
        }
    }

    private String getTime(long milSecond) {
        //根据需要自行截取数据显示
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
}