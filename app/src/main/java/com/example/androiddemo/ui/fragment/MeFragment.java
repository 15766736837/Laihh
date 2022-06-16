package com.example.androiddemo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseFragment;
import com.example.androiddemo.bean.UserBean;
import com.example.androiddemo.db.DBHelper;

import org.w3c.dom.Text;

public class MeFragment extends BaseFragment {

    private TextView mTvName;
    private TextView mTvId;

    @Override
    public void initEvent() {

    }

    @Override
    protected void initView() {
        mTvName = contentView.findViewById(R.id.tvName);
        mTvId = contentView.findViewById(R.id.tvId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {
        UserBean userBean = DBHelper.getInstance(requireContext()).queryUser(1);
        mTvName.setText(userBean.getUser_name());
        mTvId.setText("ID: " + userBean.get_id());
    }
}
