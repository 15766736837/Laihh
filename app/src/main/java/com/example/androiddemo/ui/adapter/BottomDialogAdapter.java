package com.example.androiddemo.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androiddemo.R;
import com.example.androiddemo.bean.RoomBean;

import java.sql.Struct;
import java.util.List;

public class BottomDialogAdapter extends RecyclerView.Adapter<BottomDialogAdapter.ViewHolder>{
    private List<String> contents;
    private BottomDialogAdapter.OnItemClickListener listener;

    public BottomDialogAdapter(List<String> contents) {
        this.contents = contents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bottom_dialog,null,false);
        return new BottomDialogAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.content.setText(contents.get(position));
        holder.content.setOnClickListener(v -> {
            if (listener != null)
                listener.itemClick(contents.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public void setOnItemClick(BottomDialogAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        public ViewHolder(View view) {
            //父类构造函数
            super(view);
            //获取RecyclerView布局的子项布局中的所有控件id
            content = view.findViewById(R.id.tvContent);
        }
    }

    public interface OnItemClickListener{
        void itemClick(String content);
    }
}
