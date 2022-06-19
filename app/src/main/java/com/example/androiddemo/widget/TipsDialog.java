package com.example.androiddemo.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.androiddemo.R;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;


public class TipsDialog extends Dialog implements View.OnClickListener {



    private TextView mTitleTips;
    private TextView mContentTips;
    private TextView mBtnLeft;
    private TextView mBtnRight;
    private String mTitle = "";
    private TipsDialogListener mListener;
    private TipsDialogExtListener mExtListener;
    private Activity activity;
    private boolean autoDismiss = false;

    public TipsDialog(@NonNull Activity activity) {
        super(activity);
        this.activity = activity;
        initView();
    }

    public TipsDialog(Activity activity, String title) {
        super(activity);
        this.activity = activity;
        mTitle = title;
        initView();
    }

    public TipsDialog(@NonNull Activity activity, int themeResId) {
        super(activity, themeResId);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_tips, null);

        mTitleTips = view.findViewById(R.id.title_tips);
        mContentTips = view.findViewById(R.id.content_tips);
        mBtnLeft = view.findViewById(R.id.btn_left);
        mBtnRight = view.findViewById(R.id.btn_right);

        mBtnLeft.setOnClickListener(this);
        mBtnRight.setOnClickListener(this);

        setContentView(view);
        setCancelable(true);

        mTitleTips.setText(mTitle);
    }

    /*****************************标题内容******************************/
    @Override
    public void setTitle(@StringRes int strId) {
        setTitle(getContext().getString(strId));
    }

    public TipsDialog setTitle(String title) {
        mTitleTips.setText(title);
        return this;
    }

    public TipsDialog setTitle(String title, int color) {
        mTitleTips.setText(title);
        mTitleTips.setTextColor(color);
        return this;
    }

    public TipsDialog setTitleVisible(boolean visible) {
        mTitleTips.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public TipsDialog setContentTips(String title) {
        setVisible(true);
        mContentTips.setText(title);
        return this;
    }

    public TipsDialog setContentTips(String title, int color) {
        setVisible(true);
        mContentTips.setText(title);
        mContentTips.setTextColor(color);
        return this;
    }

    public TipsDialog setVisible(boolean visible) {
        mContentTips.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public TextView getContentView() {
        return mContentTips;
    }

    public TextView getmTitleTips() {
        return mTitleTips;
    }

    public TextView getmContentTips() {
        return mContentTips;
    }

    public TextView getmBtnLeft() {
        return mBtnLeft;
    }

    public TextView getmBtnRight() {
        return mBtnRight;
    }

    public String getmTitle() {
        return mTitle;
    }

    /*****************************按钮设置******************************/
    public TipsDialog setBtnLeft(String title) {
        mBtnLeft.setText(title);
        return this;
    }

    public TipsDialog setBtnLeft(String title, int color) {
        mBtnLeft.setText(title);
        mBtnLeft.setTextColor(color);
        return this;
    }

    public TipsDialog setBtnLeftVisible(boolean visible) {
        mBtnLeft.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public TipsDialog setBtnRightVisible(boolean visible) {
        mBtnRight.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public TipsDialog setBtnRight(String title) {
        mBtnRight.setText(title);
        return this;
    }

    public TipsDialog setBtnRight(String title, int color) {
        mBtnRight.setText(title);
        mBtnRight.setTextColor(color);
        return this;
    }

    public TipsDialog banCancel() {
        setCancelable(false);
        return this;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_left) {
            if (mListener != null) {
                mListener.onCancel();
            }
            if (mExtListener != null) {
                mExtListener.onCancel(this);
            }
        } else if (id == R.id.btn_right) {
            if (mListener != null) {
                mListener.onConfirm();
            }
            if (mExtListener != null) {
                mExtListener.onConfirm(this);
            }
        }
        if (autoDismiss) {
            dismiss();
        }
    }

    public Activity getActivity() {
        return activity;
    }

    /*****************************事件监听******************************/
    public TipsDialog setListener(TipsDialogListener listener) {
        mListener = listener;
        return this;
    }

    public TipsDialog setExtListener(TipsDialogExtListener listener) {
        mExtListener = listener;
        return this;
    }

    public TipsDialog setAutoDismiss(boolean autoDismiss) {
        this.autoDismiss = autoDismiss;
        return this;
    }

    public interface TipsDialogListener {
        void onCancel();

        void onConfirm();
    }

    public interface TipsDialogExtListener {
        void onCancel(TipsDialog dialog);

        void onConfirm(TipsDialog dialog);
    }
}
