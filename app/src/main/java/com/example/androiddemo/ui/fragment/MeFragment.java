package com.example.androiddemo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseFragment;
import com.example.androiddemo.app.MainActivity;
import com.example.androiddemo.bean.UserDataBean;
import com.example.androiddemo.ui.activity.EditInfoActivity;
import com.example.androiddemo.ui.activity.MyReservationActivity;
import com.example.androiddemo.ui.activity.SettingActivity;
import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

public class MeFragment extends BaseFragment implements View.OnClickListener {
    private TextView mTvName;
    private TextView mTvId;
    private ImageView ivAvatar;
    private UserDataBean mUserBean;

    @Override
    public void initEvent() {
        contentView.findViewById(R.id.ivSetting).setOnClickListener(this);
        contentView.findViewById(R.id.rlInfo).setOnClickListener(this);
        ivAvatar.findViewById(R.id.ivAvatar).setOnClickListener(this);
        contentView.findViewById(R.id.rlReservation).setOnClickListener(this);
    }

    @Override
    protected void initView() {
        mTvName = contentView.findViewById(R.id.tvName);
        mTvId = contentView.findViewById(R.id.tvId);
        ivAvatar = contentView.findViewById(R.id.ivAvatar);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        String user = MMKV.defaultMMKV().decodeString("user");
        UserDataBean userDataBean = new Gson().fromJson(user, UserDataBean.class);
        mTvName.setText(userDataBean.getUser().getNickname());
        mTvId.setText("ID: " + userDataBean.getUser().getId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivSetting:
                startActivity(new Intent(requireContext(), SettingActivity.class));
                break;
            case R.id.rlInfo:
                startActivity(new Intent(getContext(), EditInfoActivity.class));
                break;
            case R.id.rlReservation:
                startActivity(new Intent(getContext(), MyReservationActivity.class));
                break;
        }
    }
}
