package com.example.androiddemo.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public abstract class BaseFragment extends Fragment {
    protected View contentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(getLayoutId(), container, false);
        initView();
        initEvent();
        initContent(savedInstanceState);
        return contentView;
    }
    public abstract void initEvent();
    protected abstract void initView();
    protected abstract int getLayoutId();
    protected abstract void initContent(Bundle savedInstanceState);
}
