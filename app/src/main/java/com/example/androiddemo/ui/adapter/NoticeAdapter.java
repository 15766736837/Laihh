package com.example.androiddemo.ui.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.androiddemo.R;
import com.example.androiddemo.bean.NoticeDataBean;

public class NoticeAdapter extends BaseQuickAdapter<NoticeDataBean.NoticeBean, BaseViewHolder> {
    public NoticeAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, NoticeDataBean.NoticeBean noticeBean) {
        baseViewHolder.setText(R.id.tvTitle, noticeBean.getTitle());
        baseViewHolder.setText(R.id.tvContent, noticeBean.getContent());
    }
}
