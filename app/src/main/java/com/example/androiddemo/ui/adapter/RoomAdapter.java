package com.example.androiddemo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androiddemo.R;
import com.example.androiddemo.bean.RoomBean;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder>{
    private List<RoomBean> roomBeanList;
    private Context context;
    private RoomAdapter.OnItemClickListener listener;

    public RoomAdapter(List<RoomBean> roomBeanList, Context context) {
        this.roomBeanList = roomBeanList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room,null,false);
        return new RoomAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoomBean roomBean = roomBeanList.get(position);
        holder.rlRoot.setOnClickListener(v -> listener.itemClick(roomBean));
        holder.roomName.setText(roomBean.getRoom_name());
        holder.tvDescribe.setText(roomBean.getDescribe());
        holder.tvStatus.setText(roomBean.getStatus() != 0 ? "已约满" : "可预约");
        holder.tvStatus.setBackground(context.getResources().getDrawable(roomBean.getStatus() != 0 ? R.drawable.shape_grey : R.drawable.shape_green));
    }

    @Override
    public int getItemCount() {
        return roomBeanList.size();
    }

    public void setOnItemClick(OnItemClickListener listener){
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlRoot;
        TextView roomName, tvDescribe, tvStatus;
        public ViewHolder(View view) {
            //父类构造函数
            super(view);
            //获取RecyclerView布局的子项布局中的所有控件id
            rlRoot = view.findViewById(R.id.rlRoot);
            roomName = view.findViewById(R.id.roomName);
            tvDescribe = view.findViewById(R.id.tvDescribe);
            tvStatus = view.findViewById(R.id.tvStatus);
        }
    }

    public interface OnItemClickListener{
        void itemClick(RoomBean roomBean);
    }
}
