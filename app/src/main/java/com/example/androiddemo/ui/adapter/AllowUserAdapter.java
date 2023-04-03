package com.example.androiddemo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androiddemo.R;
import com.example.androiddemo.bean.UserBean;

import java.util.List;

public class AllowUserAdapter extends RecyclerView.Adapter<AllowUserAdapter.ViewHolder>{
    private List<UserBean> userBeanList;
    private Context context;
    private AllowUserAdapter.OnItemClickListener listener;

    public AllowUserAdapter(List<UserBean> userBeanList, Context context) {
        this.userBeanList = userBeanList;
        this.context = context;
    }

    @NonNull
    @Override
    public AllowUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_allow_user,null,false);
        return new AllowUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllowUserAdapter.ViewHolder holder, int position) {
        if (listener != null)
            holder.rlRoot.setOnClickListener(v -> listener.itemClick(userBeanList.get(position)));
    }

    public void setOnItemClick(AllowUserAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return userBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayoutCompat rlRoot;
        ImageView ivSelect;
        TextView tvContent;

        public ViewHolder(View view) {
            //父类构造函数
            super(view);
            //获取RecyclerView布局的子项布局中的所有控件id
            rlRoot = view.findViewById(R.id.rlRoot);
            ivSelect = view.findViewById(R.id.ivSelect);
            tvContent = view.findViewById(R.id.tvContent);
        }
    }

    public interface OnItemClickListener{
        void itemClick(UserBean userBean);
    }
}
