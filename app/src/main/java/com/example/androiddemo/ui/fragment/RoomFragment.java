package com.example.androiddemo.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseFragment;
import com.example.androiddemo.bean.HomeRoomDataBean;
import com.example.androiddemo.bean.UserDataBean;
import com.example.androiddemo.ui.activity.HomeDetailsRoomActivity;
import com.example.androiddemo.ui.activity.HomeRoomAdapter;
import com.example.androiddemo.utils.HttpUtils;
import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

/**
 * 会议
 */
public class RoomFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvMyAll, tvAttend;
    private boolean isMyAll = true;
    private HomeRoomAdapter homeRoomAdapter;

    @Override
    public void initEvent() {
        tvMyAll.setOnClickListener(this);
        tvAttend.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        tvMyAll = contentView.findViewById(R.id.tvMyAll);
        tvAttend = contentView.findViewById(R.id.tvAttend);
        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        homeRoomAdapter = new HomeRoomAdapter(R.layout.item_notice);
        recyclerView.setAdapter(homeRoomAdapter);
        homeRoomAdapter.setOnItemClickListener((adapter, view, position) -> {
            HomeRoomDataBean.HomeRoomBean item = (HomeRoomDataBean.HomeRoomBean) adapter.getItem(position);
            Intent intent = new Intent(requireContext(), HomeDetailsRoomActivity.class);
            intent.putExtra("data", item);
            intent.putExtra("type", isMyAll ? 3 : 4);
            startActivity(intent);
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_room;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        getData(isMyAll);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvMyAll:
                setTabStyle(true);
                break;
            case R.id.tvAttend:
                setTabStyle(false);
                break;
        }
    }

    private void setTabStyle(boolean isMyAll) {
        this.isMyAll = isMyAll;
        getData(isMyAll);
        tvMyAll.setBackground(isMyAll ? requireContext().getDrawable(R.drawable.shape_account_type_selected) : null);
        tvAttend.setBackground(!isMyAll ? requireContext().getDrawable(R.drawable.shape_account_type_selected) : null);
        tvMyAll.setTextColor(Color.parseColor(isMyAll ? "#FFFFFF" : "#262936"));
        tvAttend.setTextColor(Color.parseColor(!isMyAll ? "#FFFFFF" : "#262936"));
    }

    private void getData(boolean isMyAll) {
        String user = MMKV.defaultMMKV().decodeString("user");
        UserDataBean userDataBean = new Gson().fromJson(user, UserDataBean.class);
        if (isMyAll){
            HttpUtils.get("client/meeting/" + userDataBean.getUser().getId(), new HttpUtils.HttpCallback() {
                @Override
                public void onSuccess(String response) {
                    HomeRoomDataBean homeRoomDataBean = new Gson().fromJson(response, HomeRoomDataBean.class);
                    homeRoomAdapter.setNewInstance(homeRoomDataBean.getList());
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        }else{
            HttpUtils.get("client/meeting/group/" + userDataBean.getUser().getId(), new HttpUtils.HttpCallback() {
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
    }
}
