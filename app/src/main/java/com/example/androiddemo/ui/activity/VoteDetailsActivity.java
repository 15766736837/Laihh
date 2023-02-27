package com.example.androiddemo.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseActivity;
import com.example.androiddemo.app.BaseApplication;
import com.example.androiddemo.bean.VoteBean;
import com.example.androiddemo.bean.VoteItemBean;
import com.example.androiddemo.db.DBHelper;
import com.example.androiddemo.ui.adapter.VoteItemAdapter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 投票详情
 */
public class VoteDetailsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack, ivImg;
    private TextView tvTitle, tvStatus, tvDescribe, tvEndTime;
    private RecyclerView recyclerView;
    private Button btnSubmit, btnDelete;
    private VoteItemAdapter voteItemAdapter;
    private List<VoteItemBean> voteItemBeans;
    private VoteBean voteBean;

    @Override
    public void initEvent() {
        ivBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        ivBack = findViewById(R.id.ivBack);
        ivImg = findViewById(R.id.ivImg);
        tvTitle = findViewById(R.id.tvTitle);
        tvEndTime = findViewById(R.id.tvEndTime);
        tvStatus = findViewById(R.id.tvStatus);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnDelete = findViewById(R.id.btnDelete);
        tvDescribe = findViewById(R.id.tvDescribe);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vote_details;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {
        voteBean = (VoteBean) getIntent().getSerializableExtra("data");
        btnDelete.setVisibility(BaseApplication.userBean.getIs_user() != 1 ? View.VISIBLE : View.GONE);
        initData();
        tvTitle.setText(voteBean.getTitle());
        tvStatus.setText(voteBean.getSingle() == 1 ? "单选" : "多选");
        tvEndTime.setText("截止日期：" + getTime(voteBean.getEnd_time()));
        tvDescribe.setVisibility(voteBean.getDescribe() != null && !"".equals(voteBean.getDescribe()) ? View.VISIBLE : View.GONE);
        ivImg.setVisibility(voteBean.getVote_url() != null && !"".equals(voteBean.getVote_url()) ? View.VISIBLE : View.GONE);
        tvDescribe.setText(voteBean.getDescribe());
        Glide.with(this).load(voteBean.getVote_url()).into(ivImg);
        //选项的操作
        voteItemAdapter.setOnItemClick((voteItemBean, position) -> {
            //recyclerView item的点击事件回调
            if (voteBean.getSingle() == 1){
                //单选
                for (int i = 0; i < voteItemBeans.size(); i++) {
                    voteItemBeans.get(i).setIs_select(false);
                }
                voteItemBean.setIs_select(true);
            }else {
                //多选
                if(voteItemBeans.get(position).isIs_select()){
                    voteItemBeans.get(position).setIs_select(false);
                }else{
                    int number = getVoteNumber();
                    if (number < voteBean.getMax()){
                        voteItemBean.setIs_select(true);
                    }else {
                        Toast.makeText(this, "管理员设置了最多选择" + voteBean.getMax() + "个选项", Toast.LENGTH_LONG).show();
                    }
                }
            }
            voteItemAdapter.notifyDataSetChanged();
        });
    }

    private void initData() {
        voteItemBeans = DBHelper.getInstance(this).queryVoteItem(voteBean.get_id());
        voteItemAdapter = new VoteItemAdapter(voteItemBeans, this, voteBean.getEnd_time());
        recyclerView.setAdapter(voteItemAdapter);
        btnSubmit.setVisibility(View.GONE);
        //投票过 或者 投票时间已截止 或者 是管理员就不再给进行投票 而是显示投票结果
        btnSubmit.setVisibility(voteBean.getEnd_time() < Calendar.getInstance().getTimeInMillis() || BaseApplication.userBean.getIs_user() != 1 ? View.GONE : View.VISIBLE);
        for (VoteItemBean voteItemBean : voteItemBeans) {
            //如果已经投票过了 就不显示投票按钮
            if (voteItemBean.getUser_ids() != null && voteItemBean.getUser_ids().contains(BaseApplication.userBean.get_id() + "")){
                btnSubmit.setVisibility(View.GONE);
                return;
            }
        }
    }

    /**
     * 获取投了几个选项数量
     * @return
     */
    private int getVoteNumber() {
        int number = 0;
        for (int i = 0; i < voteItemBeans.size(); i++) {
            number = voteItemBeans.get(i).isIs_select() ? number + 1 : number;
        }
        return number;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnDelete:
                //删除投票
                DBHelper.getInstance(this).deleteVoteItem(voteBean);
                Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.btnSubmit:
                //确认投票按钮
                if (voteBean.getSingle() == 1){
                    //单选
                    if (getVoteNumber() < 1){
                        Toast.makeText(this, "请选择您要投的选项", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else{
                    //多选
                    if(getVoteNumber() < voteBean.getMin()){
                        Toast.makeText(this, "最少选择" + voteBean.getMin() + "个选项", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                //提交投票
                for (int i = 0; i < voteItemBeans.size(); i++) {
                    if (voteItemBeans.get(i).isIs_select())
                        DBHelper.getInstance(this).updateVoteItem(voteItemBeans.get(i));
                }
                Toast.makeText(this, "投票成功", Toast.LENGTH_SHORT).show();
                //刷新页面数据
                initData();
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