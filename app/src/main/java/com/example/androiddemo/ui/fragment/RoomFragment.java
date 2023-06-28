package com.example.androiddemo.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.viewmodel.CreationExtras;

import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseFragment;

/**
 * 会议
 */
public class RoomFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvMyAll, tvAttend;
    private boolean isMyAll = true;
    @Override
    public void initEvent() {
        tvMyAll.setOnClickListener(this);
        tvAttend.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        tvMyAll = contentView.findViewById(R.id.tvMyAll);
        tvAttend = contentView.findViewById(R.id.tvAttend);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_room;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {

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
        if (isMyAll){
//            getAllRoom();
        }else{

        }
        tvMyAll.setBackground(isMyAll ? requireContext().getDrawable(R.drawable.shape_account_type_selected) : null);
        tvAttend.setBackground(!isMyAll ? requireContext().getDrawable(R.drawable.shape_account_type_selected) : null);
        tvMyAll.setTextColor(Color.parseColor(isMyAll ? "#FFFFFF" : "#262936"));
        tvAttend.setTextColor(Color.parseColor(!isMyAll ? "#FFFFFF" : "#262936"));
    }
}
