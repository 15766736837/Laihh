package com.example.androiddemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseActivity;
import com.example.androiddemo.app.MainActivity;
import com.example.androiddemo.bean.VoteBean;
import com.example.androiddemo.ui.adapter.VoteAdapter;
import com.example.androiddemo.ui.fragment.MeFragment;
import com.example.androiddemo.widget.SwitchButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.androiddemo.app.MainActivity.REQUEST_CODE_CHOOSE;

/**
 * 创建投票
 */
public class AddVoteActivity extends BaseActivity {
    //投票选项的数据
    private List<VoteBean> voteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView tvDate;
    private VoteAdapter voteAdapter;
    private ImageView ivBack, ivImg;
    //控制选项类型是图片还是文本
    private SwitchButton sbImg, sbMultipleChoice;
    private RelativeLayout rlMin, rlMax;
    private int imgPosition = -1;
    private TimePickerView pvTime;

    @Override
    public void initEvent() {
        ivBack.setOnClickListener(v -> finish());
        ivImg.setOnClickListener(v -> {
            //单选
            ImageSelector.builder()
                    .useCamera(true) // 设置是否使用拍照
                    .setSingle(true)  //设置是否单选
                    .canPreview(true) //是否可以预览图片，默认为true
                    .start(this, REQUEST_CODE_CHOOSE); // 打开相册
        });
        sbImg.setOnCheckedChangeListener((view, isChecked) -> {
            //控制选项的类型为图片或者文字
            voteAdapter.isImg = isChecked;
            voteAdapter.notifyDataSetChanged();
        });
        sbMultipleChoice.setOnCheckedChangeListener((view, isChecked) -> {
            rlMin.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            rlMax.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });
        tvDate.setOnClickListener(v -> {
            //打开日期选择器
            pvTime.show();
        });
    }

    @Override
    protected void initView() {
        ivBack = findViewById(R.id.ivBack);
        tvDate = findViewById(R.id.tvDate);
        ivImg = findViewById(R.id.ivImg);
        sbImg = findViewById(R.id.sbImg);
        sbMultipleChoice = findViewById(R.id.sbMultipleChoice);
        rlMin = findViewById(R.id.rlMin);
        rlMax = findViewById(R.id.rlMax);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //系统当前时间
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.DAY_OF_WEEK, 3);
        //初始化时间选择器
        pvTime = new TimePickerBuilder(this, (date, v1) -> {
            //选择完截止日期后 会进入这个回调 在这里面把选择完的日期显示在页面上
            tvDate.setText(getTime(date.getTime()));
        }).setType(new boolean[]{true, true, true, true, true, false})
                .setRangDate(instance, null)//起始终止年月日设定
                .build();
    }

    private void initData(){
        //添加默认数据 默认显示的待选项
        voteList.add(new VoteBean(1));
        voteList.add(new VoteBean(2));
        voteList.add(new VoteBean());
        voteAdapter = new VoteAdapter(voteList, this);
        voteAdapter.setOnImgClick(position -> {
            imgPosition = position;
            ImageSelector.builder()
                    .useCamera(true) // 设置是否使用拍照
                    .setSingle(true)  //设置是否单选
                    .canPreview(true) //是否可以预览图片，默认为true
                    .start(this, REQUEST_CODE_CHOOSE); // 打开相册
        });
        recyclerView.setAdapter(voteAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_vote;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            //获取选择器返回的数据
            ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
            /**
             * 是否是来自于相机拍照的图片，
             * 只有本次调用相机拍出来的照片，返回时才为true。
             * 当为true时，图片返回的结果有且只有一张图片。
             */
            for (String url : images) {
                if (imgPosition == -1){
                    Glide.with(this).load(url).into(ivImg);
                    imgPosition = -1;
                }else {
                    voteList.get(imgPosition).setImg(url);
                    voteAdapter.notifyItemChanged(imgPosition);
                }
            }
        }
    }

    private String getTime(long milSecond) {
        //根据需要自行截取数据显示
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
}