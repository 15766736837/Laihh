package com.example.androiddemo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseApplication;
import com.example.androiddemo.app.BaseFragment;

public class HomeFragment extends BaseFragment {
    @Override
    public void initEvent() {

    }

    @Override
    protected void initView() {
        contentView.findViewById(R.id.addRoom).setOnClickListener(v -> {
            // TODO: 2023/6/28 创建会议室
            Toast.makeText(requireContext(), "创建会议室", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {

    }
}
