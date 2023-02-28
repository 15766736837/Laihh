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

import com.bumptech.glide.Glide;
import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseApplication;
import com.example.androiddemo.bean.UserBean;
import com.example.androiddemo.bean.VoteBean;
import com.example.androiddemo.bean.VoteItemBean;
import com.example.androiddemo.db.DBHelper;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

public class VoteItemAdapter extends RecyclerView.Adapter<VoteItemAdapter.ViewHolder> {
    private List<VoteItemBean> voteList;
    private Context context;
    private VoteItemAdapter.OnItemClickListener listener;
    private int total = 0;
    private boolean isSelect = false;   //是否已经投票过
    private VoteBean voteBean;

    public VoteItemAdapter(List<VoteItemBean> list, Context context, VoteBean voteBean) {
        voteList = list;
        this.context = context;
        this.voteBean = voteBean;
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
        holder.rlRoot.setVisibility(voteBean.getType() == 1 ? View.VISIBLE : View.GONE);
        holder.rlImgRoot.setVisibility(voteBean.getType() == 2 ? View.VISIBLE : View.GONE);
        if (voteBean.getType() == 2)
            //显示选项的图片
            Glide.with(context).load(voteItemBean.getUrl()).into(holder.ivImg);
        //投票过 或者 投票时间已截止 或者 是管理员就不再给进行投票 而是显示投票结果
        if(isSelect || voteBean.getEnd_time() < Calendar.getInstance().getTimeInMillis() || BaseApplication.userBean.getIs_user() != 1){
            setData(holder.ivSelect, holder.tvAmount, holder.tvScale, holder.progressBar, voteItemBean);
            setData(holder.ivImgSelect, holder.tvImgAmount, holder.tvImgScale, holder.imgProgressBar, voteItemBean);
            if (BaseApplication.userBean.getIs_user() != 1 && voteItemBean.getUser_ids() != null){
                //是管理员的话就显示投票的人都有谁
                String users = "";
                for (String s : voteItemBean.getUser_ids().replace("null", "").split(",")) {
                    if(!"".equals(s)){
                        UserBean userBean = DBHelper.getInstance(context).queryUser(Long.parseLong(s));
                        users = users + "，" + userBean.getStudent_name();
                    }
                }
                if(!"".equals(users)){
                    holder.tvPersonnel.setText(users.substring(1));
                    holder.tvPersonnel.setVisibility(View.VISIBLE);
                }
            }else{
                holder.tvPersonnel.setVisibility(View.GONE);
            }
        }else{
            holder.rlRoot.setOnClickListener(v -> {
                if (listener != null)
                    listener.itemClick(voteItemBean, position);
            });
            holder.rlImgRoot.setOnClickListener(v -> {
                if (listener != null)
                    listener.itemClick(voteItemBean, position);
            });
            holder.ivSelect.setImageResource(voteItemBean.isIs_select() ? R.mipmap.icon_cb_select : R.mipmap.icon_cb_not_selected);
            holder.ivImgSelect.setImageResource(voteItemBean.isIs_select() ? R.mipmap.icon_cb_select : R.mipmap.icon_cb_not_selected);
        }
    }

    private void setData(ImageView select, TextView amount, TextView scale, ProgressBar progressBar, VoteItemBean voteItemBean) {
        select.setVisibility(voteItemBean.getUser_ids() != null && voteItemBean.getUser_ids().contains(BaseApplication.userBean.get_id() + "") ? View.VISIBLE : View.GONE);
        select.setImageResource(R.mipmap.icon_cb_select);
        amount.setVisibility(View.VISIBLE);
        amount.setText(voteItemBean.getAmount() + "票");
        scale.setVisibility(View.VISIBLE);
        scale.setText(voteItemBean.getAmount() != 0 ? new BigDecimal(voteItemBean.getAmount() / (total + 0.0f) * 100).setScale(2, BigDecimal.ROUND_HALF_UP) + "%" : "0%");
        progressBar.setMax(total);
        progressBar.setProgress(voteItemBean.getAmount());
    }

    @Override
    public int getItemCount() {
        return voteList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlRoot, rlImgRoot;
        ImageView ivSelect, ivImgSelect, ivImg;
        ProgressBar progressBar, imgProgressBar;
        TextView tvContent, tvAmount, tvScale, tvPersonnel, tvImgAmount, tvImgScale;

        public ViewHolder(View view) {
            //父类构造函数
            super(view);
            //获取RecyclerView布局的子项布局中的所有控件id
            rlRoot = view.findViewById(R.id.rlRoot);
            rlImgRoot = view.findViewById(R.id.rlImgRoot);
            progressBar = view.findViewById(R.id.progressBar);
            imgProgressBar = view.findViewById(R.id.imgProgressBar);
            ivSelect = view.findViewById(R.id.ivSelect);
            ivImgSelect = view.findViewById(R.id.ivImgSelect);
            tvContent = view.findViewById(R.id.tvContent);
            tvAmount = view.findViewById(R.id.tvAmount);
            tvImgAmount = view.findViewById(R.id.tvImgAmount);
            tvScale = view.findViewById(R.id.tvScale);
            tvImgScale = view.findViewById(R.id.tvImgScale);
            tvPersonnel = view.findViewById(R.id.tvPersonnel);
            ivImg = view.findViewById(R.id.ivImg);
        }
    }
    public void setOnItemClick(VoteItemAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
    public interface OnItemClickListener{
        void itemClick(VoteItemBean voteItemBean, int position);
    }
}
