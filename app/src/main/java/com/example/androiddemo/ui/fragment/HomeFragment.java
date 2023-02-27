package com.example.androiddemo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseApplication;
import com.example.androiddemo.app.BaseFragment;
import com.example.androiddemo.bean.VoteBean;
import com.example.androiddemo.db.DBHelper;
import com.example.androiddemo.ui.activity.AddVoteActivity;
import com.example.androiddemo.ui.activity.SettingActivity;
import com.example.androiddemo.ui.activity.VoteDetailsActivity;
import com.example.androiddemo.ui.adapter.HomeVoteAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private FloatingActionButton addVote;
    private RecyclerView recyclerView;
    private HomeVoteAdapter homeVoteAdapter;

    @Override
    public void initEvent() {
        addVote.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        addVote = contentView.findViewById(R.id.addVote);
        recyclerView = contentView.findViewById(R.id.recyclerView);
        addVote.setVisibility(BaseApplication.userBean.getIs_user() == 1 ? View.GONE : View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        List<VoteBean> voteBeans;
        if (BaseApplication.userBean.getIs_user() != 1){
            //如果是管理员就获取该管理员所创建的投票数据
            voteBeans = DBHelper.getInstance(getContext()).queryMeCreateVote();
        }else{
            //如果是用户就把所有投票数据都展示出来
            voteBeans = DBHelper.getInstance(getContext()).queryAllVote();
        }
        //加载列表数据
        homeVoteAdapter = new HomeVoteAdapter(voteBeans, getContext());
        homeVoteAdapter.setOnItemClick(voteBean -> {
            Intent intent = new Intent(getContext(), VoteDetailsActivity.class);
            intent.putExtra("data", voteBean);
            startActivity(intent);
        });
        recyclerView.setAdapter(homeVoteAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addVote:
                startActivity(new Intent(requireContext(), AddVoteActivity.class));
                break;
        }
    }
}
