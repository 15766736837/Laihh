package com.example.androiddemo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androiddemo.R;
import com.example.androiddemo.bean.VoteItemBean;

import java.util.List;

public class VoteItemAdapter extends RecyclerView.Adapter<VoteItemAdapter.ViewHolder> {
    private List<VoteItemBean> voteList;
    private Context context;


    public VoteItemAdapter(List<VoteItemBean> list, Context context) {
        voteList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vote_details,null,false);
        return new VoteItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VoteItemBean voteItemBean = voteList.get(position);
        holder.tvContent.setText(voteItemBean.getContent());
    }

    @Override
    public int getItemCount() {
        return voteList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent;

        public ViewHolder(View view) {
            //父类构造函数
            super(view);
            //获取RecyclerView布局的子项布局中的所有控件id
            tvContent = view.findViewById(R.id.tvContent);
        }
    }
}
