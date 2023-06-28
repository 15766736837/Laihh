package com.example.androiddemo.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseActivity;
import com.example.androiddemo.bean.CommentDataBean;
import com.example.androiddemo.bean.NoticeDataBean;
import com.example.androiddemo.bean.UserDataBean;
import com.example.androiddemo.ui.adapter.NoticeCommentAdapter;
import com.example.androiddemo.utils.HttpUtils;
import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 公告详情
 */
public class NoticeDetailsActivity extends BaseActivity {

    private TextView title, tvContent;
    private EditText etComment;
    private NoticeDataBean.NoticeBean data;
    private RecyclerView recyclerView;
    private NoticeCommentAdapter noticeCommentAdapter;

    @Override
    public void initEvent() {

    }

    @Override
    protected void initView() {
        title = findViewById(R.id.title);
        tvContent = findViewById(R.id.tvContent);
        etComment = findViewById(R.id.etComment);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        noticeCommentAdapter = new NoticeCommentAdapter(R.layout.item_notice);
        recyclerView.setAdapter(noticeCommentAdapter);

        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        findViewById(R.id.tvBtn).setOnClickListener(v -> {
            //调用评论接口
            if (etComment.getText() != null && !etComment.getText().toString().isEmpty()){
                String user = MMKV.defaultMMKV().decodeString("user");
                UserDataBean userDataBean = new Gson().fromJson(user, UserDataBean.class);

                Map<String, String> params = new HashMap<>();
                params.put("uid", userDataBean.getUser().getId() + "");
                params.put("nid", data.getId() + "");
                params.put("content", etComment.getText().toString());
                params.put("username", userDataBean.getUser().getUsername());
                HttpUtils.post("notice/comment", new Gson().toJson(params), new HttpUtils.HttpCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Toast.makeText(NoticeDetailsActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                        getComment();
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            }else {
                Toast.makeText(this, "请输入要评论的内容", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getComment(){
        HttpUtils.get("notice/comment/" + data.getId(), new HttpUtils.HttpCallback() {
            @Override
            public void onSuccess(String response) {
                CommentDataBean commentDataBean = new Gson().fromJson(response, CommentDataBean.class);
                noticeCommentAdapter.setNewInstance(commentDataBean.getList());
                Log.e("XXX评论", response);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_notice_details;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {
        data = (NoticeDataBean.NoticeBean) getIntent().getSerializableExtra("data");
        title.setText(data.getTitle());
        tvContent.setText(data.getContent());
        getComment();
    }
}