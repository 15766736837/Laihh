package com.example.androiddemo.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseFragment;
import com.example.androiddemo.bean.HomeRoomDataBean;
import com.example.androiddemo.bean.UserDataBean;
import com.example.androiddemo.ui.activity.HomeDetailsRoomActivity;
import com.example.androiddemo.ui.activity.HomeRoomAdapter;
import com.example.androiddemo.utils.HttpUtils;
import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private HomeRoomAdapter homeRoomAdapter;
    private TextView tvAll, tvMy;
    private boolean isAll = true; //true = 所有 false = 我预约的
    @Override
    public void initEvent() {
        tvAll.setOnClickListener(this);
        tvMy.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        tvAll = contentView.findViewById(R.id.tvAll);
        tvMy = contentView.findViewById(R.id.tvMy);
        recyclerView = contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        homeRoomAdapter = new HomeRoomAdapter(R.layout.item_notice);
        recyclerView.setAdapter(homeRoomAdapter);
        homeRoomAdapter.setOnItemClickListener((adapter, view, position) -> {
            HomeRoomDataBean.HomeRoomBean item = (HomeRoomDataBean.HomeRoomBean) adapter.getItem(position);
            Intent intent = new Intent(requireContext(), HomeDetailsRoomActivity.class);
            intent.putExtra("data", item);
            intent.putExtra("type", isAll ? 1 : 2);
            startActivity(intent);
        });
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
        if (isAll){
            getAllRoom();
        }else{
            getMyRoom();
        }
    }

    private void getAllRoom() {
        HttpUtils.get("room/", new HttpUtils.HttpCallback() {
            @Override
            public void onSuccess(String response) {
                HomeRoomDataBean homeRoomDataBean = new Gson().fromJson(response, HomeRoomDataBean.class);
                homeRoomAdapter.setNewInstance(homeRoomDataBean.getList());
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private void getMyRoom() {
        String user = MMKV.defaultMMKV().decodeString("user");
        UserDataBean userDataBean = new Gson().fromJson(user, UserDataBean.class);
        HttpUtils.get("client/room/" + userDataBean.getUser().getId(), new HttpUtils.HttpCallback() {
            @Override
            public void onSuccess(String response) {
                HomeRoomDataBean homeRoomDataBean = new Gson().fromJson(response, HomeRoomDataBean.class);
                homeRoomAdapter.setNewInstance(homeRoomDataBean.getList());
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvAll:
                setTabStyle(true);
                break;
            case R.id.tvMy:
                setTabStyle(false);
                break;
        }
    }

    private void setTabStyle(boolean isAll) {
        this.isAll = isAll;
        if (isAll){
            getAllRoom();
        }else{
            getMyRoom();
        }
        tvAll.setBackground(isAll ? requireContext().getDrawable(R.drawable.shape_account_type_selected) : null);
        tvMy.setBackground(!isAll ? requireContext().getDrawable(R.drawable.shape_account_type_selected) : null);
        tvAll.setTextColor(Color.parseColor(isAll ? "#FFFFFF" : "#262936"));
        tvMy.setTextColor(Color.parseColor(!isAll ? "#FFFFFF" : "#262936"));
    }
}
