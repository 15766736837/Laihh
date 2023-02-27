package com.example.androiddemo.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseActivity;
import com.example.androiddemo.bean.VoteBean;
import com.example.androiddemo.bean.VoteItemBean;
import com.example.androiddemo.db.DBHelper;
import com.example.androiddemo.ui.adapter.VoteItemAdapter;

import java.io.Serializable;
import java.util.List;

/**
 * 投票详情
 */
public class VoteDetailsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack, ivImg;
    private TextView tvTitle, tvStatus, tvDescribe;
    private RecyclerView recyclerView;
    private VoteItemAdapter voteItemAdapter;

    @Override
    public void initEvent() {
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        ivBack = findViewById(R.id.ivBack);
        ivImg = findViewById(R.id.ivImg);
        tvTitle = findViewById(R.id.tvTitle);
        tvStatus = findViewById(R.id.tvStatus);
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
        VoteBean data = (VoteBean) getIntent().getSerializableExtra("data");
        List<VoteItemBean> voteItemBeans = DBHelper.getInstance(this).queryVoteItem(data.get_id());
        voteItemAdapter = new VoteItemAdapter(voteItemBeans, this);
        recyclerView.setAdapter(voteItemAdapter);
        tvTitle.setText(data.getTitle());
        tvStatus.setText(data.getSingle() == 1 ? "单选" : "多选");
        tvDescribe.setVisibility(data.getDescribe() != null && !"".equals(data.getDescribe()) ? View.VISIBLE : View.GONE);
        ivImg.setVisibility(data.getVote_url() != null && !"".equals(data.getVote_url()) ? View.VISIBLE : View.GONE);
        tvDescribe.setText(data.getDescribe());
        Glide.with(this).load(data.getVote_url()).into(ivImg);
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