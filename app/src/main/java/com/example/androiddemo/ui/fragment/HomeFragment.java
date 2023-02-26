package com.example.androiddemo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseFragment;
import com.example.androiddemo.ui.activity.AddVoteActivity;
import com.example.androiddemo.ui.activity.SettingActivity;

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    @Override
    public void initEvent() {

    }

    @Override
    protected void initView() {
        contentView.findViewById(R.id.addVote).setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {

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
