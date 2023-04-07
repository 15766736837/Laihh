package com.example.androiddemo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androiddemo.R;
import com.example.androiddemo.bean.VoteBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewMsgAdapter extends RecyclerView.Adapter<NewMsgAdapter.ViewHolder> {
    private List<VoteBean> voteBeanList;
    private Context context;
    private OnItemClickListener listener;

    public NewMsgAdapter(List<VoteBean> voteBeanList, Context context) {
        this.voteBeanList = voteBeanList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg,null,false);
        return new NewMsgAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VoteBean voteBean = voteBeanList.get(position);
        holder.tvTitle.setText(voteBean.getTitle());
        holder.tvTime.setText(getTime(voteBean.getMsg_time()));
        if (voteBean.getMsg_type() == 1){
            holder.tvContent.setText(voteBean.getMsg_contain_me());
        }else if(voteBean.getMsg_type() == 2){
            holder.tvContent.setText(voteBean.getMsg_dying_period());
        }else if(voteBean.getMsg_type() == 3){
            holder.tvContent.setText(voteBean.getMsg_expire());
        }

        if (listener != null)
            holder.rlRoot.setOnClickListener(v -> listener.itemClick(voteBean));
    }

    public void setOnItemClick(OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return voteBeanList.size();
    }

    //ViewHolder类将子项布局中所有控件绑定为一个对象，该对象包含子项布局的所有控件
    static class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout rlRoot;
        TextView tvTitle, tvTime, tvContent;

        public ViewHolder(View view) {
            //父类构造函数
            super(view);
            //获取RecyclerView布局的子项布局中的所有控件id
            rlRoot = view.findViewById(R.id.rlRoot);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvTime = view.findViewById(R.id.tvTime);
            tvContent = view.findViewById(R.id.tvContent);
        }
    }

    public interface OnItemClickListener{
        void itemClick(VoteBean voteBean);
    }

    private String getTime(long milSecond) {
        //根据需要自行截取数据显示
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
}
