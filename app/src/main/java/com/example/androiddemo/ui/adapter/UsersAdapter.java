package com.example.androiddemo.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.androiddemo.R;
import com.example.androiddemo.bean.UserDataBean;

import androidx.annotation.NonNull;

public class UsersAdapter extends BaseQuickAdapter<UserDataBean.UserBean, BaseViewHolder> {
    public UsersAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, UserDataBean.UserBean userBean) {
        baseViewHolder.setText(R.id.tvContent, userBean.getNickname());
        baseViewHolder.getView(R.id.rlRoot).setOnClickListener(v -> {
            userBean.setSelect(!userBean.isSelect());
            baseViewHolder.setImageResource(R.id.ivSelect, userBean.isSelect() ? R.mipmap.icon_cb_select : R.mipmap.icon_cb_not_selected);
            notifyDataSetChanged();
        });
    }
}
