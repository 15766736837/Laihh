package com.example.androiddemo.ui.activity;

import static com.example.androiddemo.app.MainActivity.REQUEST_CODE_CHOOSE;

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

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseActivity;
import com.example.androiddemo.app.BaseApplication;
import com.example.androiddemo.bean.UserBean;
import com.example.androiddemo.bean.VoteBean;
import com.example.androiddemo.bean.VoteItemBean;
import com.example.androiddemo.db.DBHelper;
import com.example.androiddemo.ui.adapter.AllowUserAdapter;
import com.example.androiddemo.ui.adapter.VoteAdapter;
import com.example.androiddemo.utils.MyLayoutManager;
import com.example.androiddemo.widget.AmountView;
import com.example.androiddemo.widget.SwitchButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 创建投票
 */
public class AddVoteActivity extends BaseActivity {
    //投票选项的数据
    private List<VoteItemBean> voteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView tvDate, tvAll;
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
    private VoteBean data;
    private RecyclerView rvUser;
    private AllowUserAdapter allowUserAdapter;
    private List<UserBean> userBeans;

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
        //全选
        tvAll.setOnClickListener(v -> allowUserAdapter.allSelect());
        btnSubmit.setOnClickListener(v -> {
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
            boolean isSelect = false;
            StringBuilder users = new StringBuilder();
            for (int i = 0; i < userBeans.size(); i++) {
                if(userBeans.get(i).isSelect()){
                    users.append(userBeans.get(i).get_id()).append(",");
                    isSelect = true;
                }
            }
            if (!isSelect){
                Toast.makeText(this, "请至少选择一个可投票用户", Toast.LENGTH_SHORT).show();
                return;
            }
            //修改内容
            if(data != null){
                for (int i = 0; i < voteList.size(); i++) {
                    if (i != voteList.size() - 1){
                        if (data.getType() == 1){
                            DBHelper.getInstance(this).updateVoteItemContent(voteList.get(i));
                        }else{
                            DBHelper.getInstance(this).updateVoteItemUrl(voteList.get(i));
                        }
                    }
                }
                Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            //创建投票
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
            voteBean.setUsers(users.toString());
            voteBean.setStart_time(Calendar.getInstance().getTimeInMillis());
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
        rvUser = findViewById(R.id.rvUser);
        recyclerView = findViewById(R.id.recyclerView);
        tvAll = findViewById(R.id.tvAll);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //系统当前时间
        long oneDay = 24 * 60 * 60 * 1000;
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date(new Date().getTime() + oneDay));  //设置当前时间的后一天
        //初始化时间选择器
        pvTime = new TimePickerBuilder(this, (date, v1) -> {
            //选择完截止日期后 会进入这个回调 在这里面把选择完的日期显示在页面上
            endTime = date.getTime();
            tvDate.setText(getTime(date.getTime()));
        }).setType(new boolean[]{true, true, true, true, true, false})
                .setRangDate(instance, null)//起始终止年月日设定
                .build();

        //选择用户的RecyclerView
        userBeans = DBHelper.getInstance(this).queryAllUser();
        //去除掉自己
        Iterator<UserBean> iterator = userBeans.iterator();
        while (iterator.hasNext()) {
            UserBean platform = iterator.next();
            if (BaseApplication.userBean.get_id() == platform.get_id())
                iterator.remove();
        }
        MyLayoutManager layout = new MyLayoutManager();
        allowUserAdapter = new AllowUserAdapter(userBeans, this);
        layout.setAutoMeasureEnabled(true);//防止recyclerview高度为wrap时测量item高度0(一定要加这个属性，否则显示不出来）
        rvUser.setLayoutManager(layout);
        rvUser.setAdapter(allowUserAdapter);
        allowUserAdapter.setOnItemClick(userBean -> {
            //Item点击事件
            userBean.setSelect(!userBean.isSelect());
            allowUserAdapter.notifyDataSetChanged();
        });
    }

    private void initData(){
        //添加默认数据 默认显示的待选项
        voteList.add(new VoteItemBean(1));
        voteList.add(new VoteItemBean(2));
        voteList.add(new VoteItemBean());
        voteAdapter = new VoteAdapter(voteList, this, data);
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
        data = (VoteBean) getIntent().getSerializableExtra("data");
        initData();
        //如果有接收到数据 就代表这个页面是编辑页面
        if (data != null){
            //设置以前编辑过的数据
            etTitle.setText(data.getTitle());
            etDescribe.setText(data.getDescribe() == null ? "" : data.getDescribe());
            if (data.getVote_url() != null || !"".equals(data.getVote_url())){
                imgUrl = data.getVote_url();
                Glide.with(this).load(data.getVote_url()).into(ivImg);
            }
            sbImg.setChecked(data.getType() == 2);
            if (data.getType() == 2)
                voteAdapter.isImg = true;

            tvDate.setText(getTime(data.getEnd_time()));
            endTime = data.getEnd_time();
            sbMultipleChoice.setChecked(data.getSingle() != 1);
            avMin.setAmount(data.getMin());
            avMax.setAmount(data.getMax());
            List<VoteItemBean> voteItemBeans = DBHelper.getInstance(this).queryVoteItem(data.get_id());
            for (int i = 0; i < voteItemBeans.size(); i++) {
                if (!(i < voteList.size() - 1))
                    voteList.add(voteList.size() - 1, new VoteItemBean());
                voteList.get(i).setId(voteItemBeans.get(i).getId());
                if (data.getType() == 1){
                    //文字
                    voteList.get(i).setContent(voteItemBeans.get(i).getContent());
                }else{
                    //图片
                    voteList.get(i).setUrl(voteItemBeans.get(i).getUrl());
                }
            }
            String[] split = data.getUsers().split(",");
            for (int i = 0; i < split.length; i++) {
                Log.e("split", split[i] + "");
                if ((userBeans.get(i).get_id() + "").equals(split[i])){
                    userBeans.get(i).setSelect(true);
                }
            }
            for (int i = 0; i < userBeans.size(); i++) {
                Log.e("userBeans", userBeans.get(i).get_id() + " ~~~~ " + userBeans.get(i).isSelect());
            }
            voteAdapter.notifyDataSetChanged();
            allowUserAdapter.notifyDataSetChanged();
            //禁止部分功能修改
            etTitle.setFocusable(false);
            etDescribe.setFocusable(false);
            ivImg.setEnabled(false);
            sbImg.setEnabled(false);
            sbMultipleChoice.setEnabled(false);
            tvDate.setEnabled(false);
        }
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