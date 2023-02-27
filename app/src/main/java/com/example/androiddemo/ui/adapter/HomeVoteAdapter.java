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
import com.example.androiddemo.bean.VoteBean;

import java.util.Calendar;
import java.util.List;

public class HomeVoteAdapter extends RecyclerView.Adapter<HomeVoteAdapter.ViewHolder> {
    private List<VoteBean> voteBeanList;
    private Context context;

    public HomeVoteAdapter(List<VoteBean> voteBeanList, Context context) {
        this.voteBeanList = voteBeanList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_vote,null,false);
        return new HomeVoteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VoteBean voteBean = voteBeanList.get(position);
        holder.tvTitle.setText(voteBean.getTitle());
        holder.tvDescribe.setVisibility(voteBean.getDescribe() == null || "".equals(voteBean.getDescribe()) ? View.GONE : View.VISIBLE);
        if (voteBean.getDescribe() != null || !"".equals(voteBean.getDescribe()))
            holder.tvDescribe.setText(voteBean.getDescribe());
        holder.tvType.setText(voteBean.getType() == 1 ? "文" : "图");
        Calendar current = Calendar.getInstance();
        if (voteBean.getEnd_time() > current.getTimeInMillis()){
            //结束时间大于当前时间 表示进行中
            holder.tvStatus.setText("进行中");
            holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.shape_blue));
        }else{
            holder.tvStatus.setText("已结束");
            holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.shape_grey));
        }
    }

    @Override
    public int getItemCount() {
        return voteBeanList.size();
    }

    //ViewHolder类将子项布局中所有控件绑定为一个对象，该对象包含子项布局的所有控件
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvTitle, tvDescribe, tvStatus, tvType;

        public ViewHolder(View view) {
            //父类构造函数
            super(view);
            //获取RecyclerView布局的子项布局中的所有控件id
            ivAvatar = view.findViewById(R.id.ivAvatar);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvDescribe = view.findViewById(R.id.tvDescribe);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvType = view.findViewById(R.id.tvType);

        }
    }
}
