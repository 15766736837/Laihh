package com.example.androiddemo.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.example.androiddemo.ui.adapter.MainFragmentPagerAdapter;
import com.example.androiddemo.R;
import com.example.androiddemo.ui.fragment.HomeFragment;
import com.example.androiddemo.ui.fragment.MeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    public static final int REQUEST_CODE_CHOOSE = 10086;

    private BottomNavigationView mNavigation;
    private ViewPager mViewpager;
    private ArrayList<Fragment> mFragmentList;

    @Override
    public void initEvent() {
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {//滑动到第几个
                mNavigation.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    mViewpager.setCurrentItem(0);
                    break;
                case R.id.me:
                    mViewpager.setCurrentItem(1);
            }
            return false;
        });
    }

    @Override
    protected void initView() {
        mNavigation = findViewById(R.id.navigation);
        mViewpager = findViewById(R.id.viewpager);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new HomeFragment());
        mFragmentList.add(new MeFragment());
        MainFragmentPagerAdapter pagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mViewpager.setAdapter(pagerAdapter);
        mViewpager.setOffscreenPageLimit(mFragmentList.size());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            //获取选择器返回的数据
            ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
            /**
             * 是否是来自于相机拍照的图片，
             * 只有本次调用相机拍出来的照片，返回时才为true。
             * 当为true时，图片返回的结果有且只有一张图片。
             */
            boolean isCameraImage = data.getBooleanExtra(ImageSelector.IS_CAMERA_IMAGE, false);
            for (String url : images) {
                ((MeFragment)mFragmentList.get(1)).setAvatar(url);
            }
        }
    }
}