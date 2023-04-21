package com.example.androiddemo.widget;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androiddemo.R;
import com.example.androiddemo.ui.adapter.BottomDialogAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class BottomDialog extends DialogFragment{
    String title, content;
    OnSelectListener listener;
    public BottomDialog(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_bottom, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ArrayList resultList= new ArrayList<>(Arrays.asList(content.split(",")));
        BottomDialogAdapter bottomDialogAdapter = new BottomDialogAdapter(resultList);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(bottomDialogAdapter);
        bottomDialogAdapter.setOnItemClick(content -> {
            if (listener != null){
                listener.onSelect(content);
                dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = ViewGroup.LayoutParams.MATCH_PARENT;
        attributes.height = (int) (0.5f + 250 * Resources.getSystem().getDisplayMetrics().density);
        attributes.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void setOnSelectListener(OnSelectListener listener){
        this.listener = listener;
    }

    public interface OnSelectListener{
        void onSelect(String content);
    }
}
