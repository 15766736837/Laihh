package com.example.androiddemo.ui.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.androiddemo.R;
import com.example.androiddemo.bean.CommentDataBean;
import com.example.androiddemo.bean.NoticeDataBean;

public class NoticeCommentAdapter extends BaseQuickAdapter<CommentDataBean.CommentBean, BaseViewHolder> {
    public NoticeCommentAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, CommentDataBean.CommentBean noticeBean) {
        baseViewHolder.setText(R.id.tvTitle, "用户: " + noticeBean.getUsername());
        baseViewHolder.setText(R.id.tvContent, noticeBean.getContent());
    }
}
