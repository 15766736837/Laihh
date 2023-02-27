package com.example.androiddemo.ui.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.androiddemo.R;
import com.example.androiddemo.bean.VoteItemBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.ViewHolder> {
    private List<VoteItemBean> voteList;
    public boolean isImg = false;   //控制是图片还是文本
    private OnImgClick listener;
    private Context context;

    //TextAdapter构造函数
    public VoteAdapter(List<VoteItemBean> list, Context context) {
        voteList = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vote,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        VoteItemBean voteBean = voteList.get(position);
        //如果不是最后一项就显示投票选项，如果是最后一项就显示添加选项的样式
        viewHolder.rlDelete.setVisibility(position < voteList.size() - 1 ? View.VISIBLE : View.GONE);
        viewHolder.rlAdd.setVisibility(position < voteList.size() - 1 ? View.GONE : View.VISIBLE);
        //图片投票
        if(isImg){
            viewHolder.ivImg.setVisibility(View.VISIBLE);
            viewHolder.etContent.setVisibility(View.GONE);
            viewHolder.ivImg.setOnClickListener(v -> {
                if (listener != null)
                    listener.click(position);
            });
            //设置选择的图片
            if(voteBean.getUrl() != null && !voteBean.getUrl().isEmpty()){
                Glide.with(context).load(voteBean.getUrl()).into(viewHolder.ivImg);
            }else{
                viewHolder.ivImg.setImageResource(R.mipmap.add_img);
            }
        }else{
            //文本投票
            viewHolder.ivImg.setVisibility(View.GONE);
            viewHolder.etContent.setVisibility(View.VISIBLE);
            viewHolder.etContent.setHint("请输入第" + (position + 1) + "项（30个字以内）");
            viewHolder.etContent.setText(voteBean.getContent());
            //添加EditText的文本发生改变的监听事件
            final TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(viewHolder.etContent.hasFocus()){
                        voteBean.setContent(s.toString());
                    }
                }
            };
            //解决RecycleView多个EditText数据混乱问题
            viewHolder.etContent.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus){
                    viewHolder.etContent.addTextChangedListener(textWatcher);
                }else {
                    viewHolder.etContent.removeTextChangedListener(textWatcher);
                }
            });
        }
        //删除按钮的点击事件
        viewHolder.ivDeleteVote.setOnClickListener(v -> {
            //删除选项，最少两个选项
            if (voteList.size() <= 3){
                Toast.makeText(v.getContext(), "最少两个选项", Toast.LENGTH_SHORT).show();
                return;
            }
            //删除选项
            voteList.remove(position);
            //刷新列表
            notifyDataSetChanged();
        });
        //添加按钮的点击事件
        viewHolder.rlAdd.setOnClickListener(v -> {
            if(voteList.size() < 11){
                //添加新的选项
                voteList.add(voteList.size() - 1, new VoteItemBean());
                //刷新列表
                notifyDataSetChanged();
            }else {
                Toast.makeText(v.getContext(), "最多十个选项", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return voteList.size();
    }

    public void setOnImgClick(OnImgClick click){
        listener = click;
    }

    //ViewHolder类将子项布局中所有控件绑定为一个对象，该对象包含子项布局的所有控件
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivDeleteVote, ivImg;
        EditText etContent;
        RelativeLayout rlAdd;
        RelativeLayout rlDelete;

        public ViewHolder(View view) {
            //父类构造函数
            super(view);
            //获取RecyclerView布局的子项布局中的所有控件id
            ivDeleteVote = view.findViewById(R.id.ivDeleteVote);
            etContent = view.findViewById(R.id.etTitle);
            rlAdd = view.findViewById(R.id.rlAdd);
            rlDelete = view.findViewById(R.id.rlDelete);
            ivImg = view.findViewById(R.id.ivImg);
        }
    }

    public interface OnImgClick{
        void click(int position);
    }
}
