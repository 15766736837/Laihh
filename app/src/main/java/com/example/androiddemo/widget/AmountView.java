package com.example.androiddemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.androiddemo.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public final class AmountView extends LinearLayout implements View.OnClickListener, TextWatcher {
    private int amount = 1;
    private int minAmount = 1;
    private int maxAmount = 10;
    private OnAmountChangeListener mListener;
    private EditText etAmount;
    private ImageView btnDecrease;
    private ImageView btnIncrease;

    public void setOnAmountChangeListener(@Nullable OnAmountChangeListener onAmountChangeListener) {
        this.mListener = onAmountChangeListener;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public void setMinAmount(int minAmount) {
        this.minAmount = minAmount;
    }

    public void onClick(@NotNull View v) {
        int i = v.getId();
        if (i == R.id.btnDecrease) {
            if (this.amount > this.minAmount) {
                this.amount--;
                this.etAmount.setText(this.amount + "");
            }
        } else if (i == R.id.btnIncrease && this.amount < this.maxAmount) {
            this.amount++;
            this.etAmount.setText(this.amount + "");
        }

        if (this.amount == this.minAmount) {
            this.btnDecrease.setImageResource(R.mipmap.icon_create_reduce_nor);
        } else if (this.amount == this.maxAmount) {
            this.btnIncrease.setImageResource(R.mipmap.icon_create_add_nor);
        } else {
            this.btnDecrease.setImageResource(R.mipmap.icon_create_reduce_sel);
            this.btnIncrease.setImageResource(R.mipmap.icon_create_add_sel);
        }

        this.etAmount.clearFocus();
        if (this.mListener != null) {
            this.mListener.onAmountChange(this, this.amount);
        }

    }

    public void beforeTextChanged(@NotNull CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(@NotNull CharSequence s, int start, int before, int count) {
    }

    public void afterTextChanged(@NotNull Editable s) {
        if (!s.toString().isEmpty()) {
            this.amount = Integer.decode(s.toString());
            if (this.amount > this.maxAmount) {
                this.etAmount.setText(this.maxAmount + "");
            } else {
                if (this.mListener != null) {
                    mListener.onAmountChange(this, this.amount);
                }

            }
        }
    }

    public AmountView(@Nullable Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_amount, this);
        etAmount = findViewById(R.id.etAmount);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnDecrease.setOnClickListener(this);
        btnIncrease.setOnClickListener(this);
        etAmount.addTextChangedListener(this);
        etAmount.setText(amount + "");
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.AmountView);
        int tvWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_tvWidth, 80);
        int tvTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_tvTextSize, 0);
        obtainStyledAttributes.recycle();
        LayoutParams textParams = new LayoutParams(tvWidth, LayoutParams.MATCH_PARENT);
        etAmount.setLayoutParams(textParams);
        if (tvTextSize != 0) {
            etAmount.setTextSize(tvTextSize);
        }
    }

    public interface OnAmountChangeListener {
        void onAmountChange(@Nullable View var1, int amount);
    }
}

