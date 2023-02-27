package com.example.androiddemo.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseApplication;
import com.example.androiddemo.bean.UserBean;
import com.example.androiddemo.bean.VoteBean;
import com.example.androiddemo.bean.VoteItemBean;
import com.example.androiddemo.db.DBHelper;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

public class VoteItemAdapter extends RecyclerView.Adapter<VoteItemAdapter.ViewHolder> {
    private List<VoteItemBean> voteList;
    private Context context;
    private VoteItemAdapter.OnItemClickListener listener;
    private int total = 0;
    private boolean isSelect = false;   //是否已经投票过
    private long endTime = 0;   //投票截止日期

    public VoteItemAdapter(List<VoteItemBean> list, Context context, long endTime) {
        voteList = list;
        this.context = context;
        this.endTime = endTime;
        for (VoteItemBean voteItemBean : voteList) {
            total += voteItemBean.getAmount();
            if (voteItemBean.getUser_ids() != null && voteItemBean.getUser_ids().contains(BaseApplication.userBean.get_id() + "")){
                isSelect = true;
            }
        }
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
        //投票过 或者 投票时间已截止 或者 是管理员就不再给进行投票 而是显示投票结果
        if(isSelect || endTime < Calendar.getInstance().getTimeInMillis() || BaseApplication.userBean.getIs_user() != 1){
            holder.ivSelect.setVisibility(voteItemBean.getUser_ids() != null && voteItemBean.getUser_ids().contains(BaseApplication.userBean.get_id() + "") ? View.VISIBLE : View.GONE);
            holder.ivSelect.setImageResource(R.mipmap.icon_cb_select);
            holder.tvAmount.setVisibility(View.VISIBLE);
            holder.tvScale.setVisibility(View.VISIBLE);
            holder.tvAmount.setText(voteItemBean.getAmount() + "票");
            holder.tvScale.setText(voteItemBean.getAmount() != 0 ? new BigDecimal(voteItemBean.getAmount() / (total + 0.0f) * 100).setScale(2, BigDecimal.ROUND_HALF_UP) + "%" : "0%");
            holder.progressBar.setMax(total);
            holder.progressBar.setProgress(voteItemBean.getAmount());
            if (BaseApplication.userBean.getIs_user() != 1 && voteItemBean.getUser_ids() != null){
                //是管理员的话就显示投票的人都有谁
                String users = "";
                for (String s : voteItemBean.getUser_ids().replace("null", "").split(",")) {
                    UserBean userBean = DBHelper.getInstance(context).queryUser(Long.parseLong(s));
                    users = users + "，" + userBean.getStudent_name();
                }
                holder.tvPersonnel.setText(users.substring(1));
                holder.tvPersonnel.setVisibility(View.VISIBLE);
            }else{
                holder.tvPersonnel.setVisibility(View.GONE);
            }
        }else{
            holder.rlRoot.setOnClickListener(v -> {
                if (listener != null)
                    listener.itemClick(voteItemBean, position);
            });
            holder.ivSelect.setImageResource(voteItemBean.isIs_select() ? R.mipmap.icon_cb_select : R.mipmap.icon_cb_not_selected);
        }
    }

    @Override
    public int getItemCount() {
        return voteList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent, tvAmount, tvScale, tvPersonnel;
        RelativeLayout rlRoot;
        ImageView ivSelect;
        ProgressBar progressBar;

        public ViewHolder(View view) {
            //父类构造函数
            super(view);
            //获取RecyclerView布局的子项布局中的所有控件id
            tvContent = view.findViewById(R.id.tvContent);
            tvAmount = view.findViewById(R.id.tvAmount);
            tvScale = view.findViewById(R.id.tvScale);
            rlRoot = view.findViewById(R.id.rlRoot);
            ivSelect = view.findViewById(R.id.ivSelect);
            progressBar = view.findViewById(R.id.progressBar);
            tvPersonnel = view.findViewById(R.id.tvPersonnel);
        }
    }
    public void setOnItemClick(VoteItemAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
    public interface OnItemClickListener{
        void itemClick(VoteItemBean voteItemBean, int position);
    }
}
