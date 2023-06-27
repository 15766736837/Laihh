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
        ivAvatar.findViewById(R.id.ivAvatar).setOnClickListener(this);
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
        String user = MMKV.defaultMMKV().decodeString("user");
        UserDataBean userDataBean = new Gson().fromJson(user, UserDataBean.class);
        mTvName.setText(userDataBean.getUser().getUsername());
        mTvId.setText("ID: " + userDataBean.getUser().getId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivSetting:
                startActivity(new Intent(requireContext(), SettingActivity.class));
                break;
            case R.id.ivAvatar:
                //单选
                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
                        .setSingle(true)  //设置是否单选
                        .canPreview(true) //是否可以预览图片，默认为true
                        .start(requireActivity(), ((MainActivity)requireActivity()).REQUEST_CODE_CHOOSE); // 打开相册
                break;
        }
    }

    /**
     * 设置用户头像
     * @param url 图片地址
     */
    public void setAvatar(String url){
        Glide.with(this).load(url).into(ivAvatar);
        //入库
//        DBHelper.getInstance(requireContext()).updateUser(mUserBean.get_id(), url);
    }
}
