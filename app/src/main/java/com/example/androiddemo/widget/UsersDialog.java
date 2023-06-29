package com.example.androiddemo.widget;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.androiddemo.R;
import com.example.androiddemo.bean.UserDataBean;
import com.example.androiddemo.bean.UsersBean;
import com.example.androiddemo.ui.adapter.UsersAdapter;
import com.example.androiddemo.utils.HttpUtils;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UsersDialog extends DialogFragment {
    private RecyclerView mRecyclerView;
    private UsersAdapter usersAdapter;
    private DialogCallback listener;
    String users = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_user, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        view.findViewById(R.id.btn).setOnClickListener(v -> {
            users = "";
            for (int i = 0; i < usersAdapter.getData().size(); i++) {
                if (usersAdapter.getData().get(i).isSelect())
                    users += usersAdapter.getData().get(i).getId() + ",";
            }

            if (!users.isEmpty())
                users = users.substring(0, users.length() - 1);
//            if (this.listener != null)
//                this.listener.onDefine(users);
            dismiss();
        });
        // 在这里可以对RecyclerView进行进一步设置，例如设置Adapter等
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        usersAdapter = new UsersAdapter(R.layout.item_user);
        mRecyclerView.setAdapter(usersAdapter);
        usersAdapter.setOnItemClickListener((adapter, view1, position) -> {
            UserDataBean.UserBean userBean = (UserDataBean.UserBean) adapter.getItem(position);
            userBean.setSelect(!userBean.isSelect());
            usersAdapter.notifyDataSetChanged();
            if (this.listener != null)
                this.listener.onDefine(userBean);
            dismiss();
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUsers();
        // 将DialogFragment设置为从底部弹出
        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dp2pxInt(250));
        window.setGravity(Gravity.BOTTOM);
    }

    private void getUsers(){
        HttpUtils.get("user/", new HttpUtils.HttpCallback() {
            @Override
            public void onSuccess(String response) {
                UsersBean usersBean = new Gson().fromJson(response, UsersBean.class);
                usersAdapter.setNewInstance(usersBean.getList());
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    public void setDialogCallback(DialogCallback listener){
        this.listener = listener;
    }

    private static float dp2px(float dp) {
        Resources r = Resources.getSystem();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    private static int dp2pxInt(float dp) {
        return (int) dp2px(dp);
    }

    public interface DialogCallback {
        void onDefine(UserDataBean.UserBean userBean);
    }
}
