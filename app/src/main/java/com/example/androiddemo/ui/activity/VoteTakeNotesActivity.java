package com.example.androiddemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseActivity;
import com.example.androiddemo.app.BaseApplication;
import com.example.androiddemo.bean.VoteBean;
import com.example.androiddemo.bean.VoteItemBean;
import com.example.androiddemo.db.DBHelper;
import com.example.androiddemo.ui.adapter.HomeVoteAdapter;

import java.util.ArrayList;
import java.util.List;


//个人中心的投票记录页面
public class VoteTakeNotesActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvTitle;
    private RecyclerView recyclerView;
    private List<VoteBean> voteBeans;
    private HomeVoteAdapter homeVoteAdapter;

    @Override
    public void initEvent() {
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        ivBack = findViewById(R.id.ivBack);
        tvTitle = findViewById(R.id.tvTitle);
        recyclerView = findViewById(R.id.recyclerView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vote_take_notes;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {
        tvTitle.setText(BaseApplication.userBean.getIs_user() == 1 ? "我参与的投票" : "我创建的投票");
    }

    @Override
    protected void onResume() {
        super.onResume();
        voteBeans = new ArrayList<>();
        DBHelper instance = DBHelper.getInstance(this);
        if (BaseApplication.userBean.getIs_user() == 1){
            //用户
            VoteBean data = null;
            for (VoteBean voteBean : instance.queryAllVote()) {
                for (VoteItemBean voteItemBean : instance.queryVoteItem(voteBean.get_id())) {
                    if (voteItemBean.getUser_ids().contains(Long.toString(BaseApplication.userBean.get_id()))){
                        data = voteBean;
                    }
                }
                if (data != null)
                    voteBeans.add(data);
            }
        }else{
            //管理员
            voteBeans = instance.queryMeCreateVote();
        }
        //加载列表数据
        homeVoteAdapter = new HomeVoteAdapter(voteBeans, this);
        homeVoteAdapter.setOnItemClick(voteBean -> {
            Intent intent = new Intent(this, VoteDetailsActivity.class);
            intent.putExtra("data", voteBean);
            startActivity(intent);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(homeVoteAdapter);
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