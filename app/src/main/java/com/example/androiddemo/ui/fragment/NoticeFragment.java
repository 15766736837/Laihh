package com.example.androiddemo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseFragment;
import com.example.androiddemo.bean.NoticeDataBean;
import com.example.androiddemo.ui.activity.NoticeDetailsActivity;
import com.example.androiddemo.ui.adapter.NoticeAdapter;
import com.example.androiddemo.utils.HttpUtils;
import com.google.gson.Gson;

/**
 * 公告
 */
public class NoticeFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private NoticeAdapter noticeAdapter;

    @Override
    public void initEvent() {

    }

    @Override
    protected void initView() {
        recyclerView = contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        noticeAdapter = new NoticeAdapter(R.layout.item_notice);
        recyclerView.setAdapter(noticeAdapter);
        noticeAdapter.setOnItemClickListener((adapter, view, position) -> {
            NoticeDataBean.NoticeBean item = (NoticeDataBean.NoticeBean) adapter.getItem(position);
            Intent intent = new Intent(requireContext(), NoticeDetailsActivity.class);
            intent.putExtra("data", item);
            startActivity(intent);
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_notice;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        HttpUtils.get("notice", new HttpUtils.HttpCallback() {
            @Override
            public void onSuccess(String response) {
                NoticeDataBean noticeDataBean = new Gson().fromJson(response, NoticeDataBean.class);
                noticeAdapter.setNewInstance(noticeDataBean.getList());
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }
}
