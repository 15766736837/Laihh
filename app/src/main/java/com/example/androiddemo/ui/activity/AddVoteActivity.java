package com.example.androiddemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseActivity;
import com.example.androiddemo.app.BaseApplication;
import com.example.androiddemo.bean.VoteBean;
import com.example.androiddemo.bean.VoteItemBean;
import com.example.androiddemo.db.DBHelper;
import com.example.androiddemo.ui.adapter.VoteAdapter;
import com.example.androiddemo.widget.AmountView;
import com.example.androiddemo.widget.SwitchButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.androiddemo.app.MainActivity.REQUEST_CODE_CHOOSE;

/**
 * 创建投票
 */
public class AddVoteActivity extends BaseActivity {
    //投票选项的数据
    private List<VoteItemBean> voteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView tvDate;
    private Button btnSubmit;
    private VoteAdapter voteAdapter;
    private ImageView ivBack, ivImg;
    private EditText etTitle, etDescribe;
    //控制选项类型是图片还是文本
    private SwitchButton sbImg, sbMultipleChoice;
    private RelativeLayout rlMin, rlMax;
    private AmountView avMin, avMax;
    private TimePickerView pvTime;
    private int imgPosition = -1;
    private String imgUrl = "";
    private long endTime;

    @Override
    public void initEvent() {
        ivBack.setOnClickListener(v -> finish());
        ivImg.setOnClickListener(v -> {
            //单选
            imgPosition = -1;
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
        btnSubmit.setOnClickListener(v -> {
            //创建投票
            etTitle.getText().toString();
            if ("".equals(etTitle.getText().toString())){
                Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
                return;
            }
            if (sbImg.isChecked()){
                if ("".equals(voteList.get(0).getUrl()) || "".equals(voteList.get(1).getUrl())){
                    Toast.makeText(this, "请输入选项内容", Toast.LENGTH_SHORT).show();
                    return;
                }
            }else{
                if ("".equals(voteList.get(0).getContent()) || "".equals(voteList.get(1).getContent())){
                    Toast.makeText(this, "请输入选项内容", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if("".equals(tvDate.getText().toString())){
                Toast.makeText(this, "请选择截止日期", Toast.LENGTH_SHORT).show();
                return;
            }

            VoteBean voteBean = new VoteBean();
            voteBean.setTitle(etTitle.getText().toString());
            voteBean.setDescribe(etDescribe.getText().toString());
            voteBean.setVote_url(imgUrl);
            voteBean.setType(sbImg.isChecked() ? 2 : 1);
            voteBean.setEnd_time(endTime);
            voteBean.setSingle(sbMultipleChoice.isChecked() ? 0 : 1);
            voteBean.setSingle(sbMultipleChoice.isChecked() ? 0 : 1);
            voteBean.setMin(Integer.decode(avMin.getAmount()));
            voteBean.setMax(Integer.decode(avMax.getAmount()));
            //把数据保存到数据库
            long id = DBHelper.getInstance(this).insertVote(voteBean);
            for (int i = 0; i < voteList.size(); i++) {
                if (i != voteList.size() - 1){
                    VoteItemBean voteItemBean = new VoteItemBean();
                    voteItemBean.set_id(id);
                    voteItemBean.setContent(voteList.get(i).getContent());
                    voteItemBean.setUrl(voteList.get(i).getUrl());
                    DBHelper.getInstance(this).insertVoteItem(voteItemBean);
                }
            }
            Toast.makeText(this, "创建成功", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    protected void initView() {
        ivBack = findViewById(R.id.ivBack);
        etTitle = findViewById(R.id.etTitle);
        etDescribe = findViewById(R.id.etDescribe);
        tvDate = findViewById(R.id.tvDate);
        ivImg = findViewById(R.id.ivImg);
        sbImg = findViewById(R.id.sbImg);
        sbMultipleChoice = findViewById(R.id.sbMultipleChoice);
        avMin = findViewById(R.id.avMin);
        avMax = findViewById(R.id.avMax);
        rlMin = findViewById(R.id.rlMin);
        rlMax = findViewById(R.id.rlMax);
        btnSubmit = findViewById(R.id.btnSubmit);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //系统当前时间
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.DAY_OF_WEEK, 3);
        //初始化时间选择器
        pvTime = new TimePickerBuilder(this, (date, v1) -> {
            //选择完截止日期后 会进入这个回调 在这里面把选择完的日期显示在页面上
            endTime = date.getTime();
            tvDate.setText(getTime(date.getTime()));
        }).setType(new boolean[]{true, true, true, true, true, false})
                .setRangDate(instance, null)//起始终止年月日设定
                .build();
    }

    private void initData(){
        //添加默认数据 默认显示的待选项
        voteList.add(new VoteItemBean(1));
        voteList.add(new VoteItemBean(2));
        voteList.add(new VoteItemBean());
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
                    imgUrl = url;
                }else {
                    voteList.get(imgPosition).setUrl(url);
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