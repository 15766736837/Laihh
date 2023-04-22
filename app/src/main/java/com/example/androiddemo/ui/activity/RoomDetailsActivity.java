package com.example.androiddemo.ui.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseActivity;
import com.example.androiddemo.app.BaseApplication;
import com.example.androiddemo.bean.RoomBean;
import com.example.androiddemo.bean.RoomOrder;
import com.example.androiddemo.db.DBHelper;
import com.example.androiddemo.widget.BottomDialog;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 预约详情
 */
public class RoomDetailsActivity extends BaseActivity implements View.OnClickListener {

    private TextView roomName, tvDescribe, tvStartTime, tvEndTime, tvRegion, tvSeat;
    private TimePickerView pvStartTime, pvEndTime;
    private BottomDialog regionDialog, seatDialog;
    private Button btnSubmit;
    private ImageView ivCode;
    private long startTime, endTime;
    private RoomBean roomBean;
    private String region, seat;

    @Override
    public void initEvent() {
        tvStartTime = findViewById(R.id.tvStartTime);
        tvEndTime = findViewById(R.id.tvEndTime);
        tvRegion = findViewById(R.id.tvRegion);
        tvSeat = findViewById(R.id.tvSeat);
        ivCode = findViewById(R.id.ivCode);
        findViewById(R.id.ivBack).setOnClickListener(this);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
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
            List<RoomOrder> roomOrders = DBHelper.getInstance(this).queryRoomOrder(roomBean.getId(), BaseApplication.app.userBean.get_id());
            if (roomOrders.isEmpty()){
                regionDialog = new BottomDialog("自习室区域", roomBean.getRegion());
                seatDialog = new BottomDialog("自习室位置", roomBean.getSeat());
                regionDialog.setOnSelectListener(content -> {
                    tvRegion.setText(content);
                    region = content;
                });
                seatDialog.setOnSelectListener(content -> {
                    tvSeat.setText(content);
                    seat = content;
                });
            }else {
                //已经预约过
                tvStartTime.setText(getTime(roomOrders.get(0).getStart_time()));
                tvEndTime.setText(getTime(roomOrders.get(0).getEnd_time()));
                tvRegion.setText(roomOrders.get(0).getRegion());
                tvSeat.setText(roomOrders.get(0).getSeat());
                btnSubmit.setVisibility(View.GONE);
                tvStartTime.setEnabled(false);
                tvEndTime.setEnabled(false);
                ivCode.setVisibility(View.VISIBLE);

                String s = roomOrders.get(0).getRoom_id() + "/" + roomOrders.get(0).getRegion() + "/" + roomOrders.get(0).getSeat() + "/" + BaseApplication.app.userBean.get_id();
                MultiFormatWriter writer = new MultiFormatWriter();
                try {
                    BitMatrix matrix = writer.encode(s, BarcodeFormat.QR_CODE,350,350);
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    Bitmap bitmap = encoder.createBitmap(matrix);
                    ivCode.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
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
            case R.id.btnSubmit:
                Date date = new Date();
                if (startTime < date.getTime()){
                    Toast.makeText(this, "开始时间不能小于当前时间", Toast.LENGTH_SHORT).show();
                    return;
                }else if(endTime < startTime){
                    Toast.makeText(this, "结束时间不能小于开始时间", Toast.LENGTH_SHORT).show();
                    return;
                }else if(region == null){
                    Toast.makeText(this, "请选择自习室区域", Toast.LENGTH_SHORT).show();
                    return;
                }else if(seat == null){
                    Toast.makeText(this, "请选择自习室位置", Toast.LENGTH_SHORT).show();
                    return;
                }
                DBHelper db = DBHelper.getInstance(this);
                List<RoomOrder> roomOrders = db.queryRoomOrder(roomBean.getId(), region, seat);
                List<RoomOrder> myOrder = db.queryRoomOrder(roomBean.getId(), BaseApplication.app.userBean.get_id());
                if (!myOrder.isEmpty()){
                    Toast.makeText(this, "您已经预约过了", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!roomOrders.isEmpty()){
                    //判断选中的时间段跟区域和位置是否有人预约了
                    for (int i = 0; i < roomOrders.size(); i++) {
                        RoomOrder roomOrder = roomOrders.get(i);
                        Date aStartTime = new Date(startTime);
                        Date aEndTime = new Date(endTime);
                        Date bStartTime = new Date(roomOrder.getStart_time());
                        Date bEndTime = new Date(roomOrder.getEnd_time());
                        if (aEndTime.getTime() <= bStartTime.getTime() || bEndTime.getTime() <= aStartTime.getTime()) {
                            // 两个时间段没有重叠,提交预约
                            submitOrder(db);
                        } else {
                            // 两个时间段有重叠
                            Toast.makeText(this, "该自习室的这个座位在这个时间段应该被人预约了", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    //提交预约
                    submitOrder(db);
                }
                break;
        }
    }

    private void submitOrder(DBHelper db) {
        RoomOrder roomOrder = new RoomOrder();
        roomOrder.setRoom_id(roomBean.getId());
        roomOrder.setStart_time(startTime);
        roomOrder.setEnd_time(endTime);
        roomOrder.setRegion(region);
        roomOrder.setSeat(seat);
        db.insertRoomOrder(roomOrder);
        Toast.makeText(this, "预约成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    private String getTime(long milSecond) {
        //根据需要自行截取数据显示
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
}