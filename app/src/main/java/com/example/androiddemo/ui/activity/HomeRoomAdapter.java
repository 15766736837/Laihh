package com.example.androiddemo.ui.activity;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.androiddemo.R;
import com.example.androiddemo.bean.CommentDataBean;
import com.example.androiddemo.bean.HomeRoomDataBean;

public class HomeRoomAdapter extends BaseQuickAdapter<HomeRoomDataBean.HomeRoomBean, BaseViewHolder> {
    public HomeRoomAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, HomeRoomDataBean.HomeRoomBean noticeBean) {
        baseViewHolder.setText(R.id.tvTitle, noticeBean.getName());
        baseViewHolder.setText(R.id.tvContent, noticeBean.getLocation());
    }
}
