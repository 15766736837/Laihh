package com.example.androiddemo.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseFragment;
import com.example.androiddemo.bean.RoomBean;
import com.example.androiddemo.db.DBHelper;
import com.example.androiddemo.ui.adapter.RoomAdapter;

import java.util.List;

public class HomeFragment extends BaseFragment {

    private RecyclerView recyclerView;

    @Override
    public void initEvent() {

    }

    @Override
    protected void initView() {
        recyclerView = contentView.findViewById(R.id.recyclerView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {
        List<RoomBean> roomBeans = DBHelper.getInstance(requireContext()).queryAllRoom();   //查询出所有的自习室
        //初始化列表并设置数据
        RoomAdapter roomAdapter = new RoomAdapter(roomBeans, requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(roomAdapter);
        roomAdapter.setOnItemClick(roomBean -> {
            //列表的点击事件
        });
    }
}
