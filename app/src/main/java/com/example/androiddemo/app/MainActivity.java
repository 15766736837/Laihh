package com.example.androiddemo.app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.androiddemo.ui.adapter.MainFragmentPagerAdapter;
import com.example.androiddemo.R;
import com.example.androiddemo.ui.fragment.HomeFragment;
import com.example.androiddemo.ui.fragment.MeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private BottomNavigationView mNavigation;
    private ViewPager mViewpager;

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

        mNavigation.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
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
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new MeFragment());
        MainFragmentPagerAdapter pagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(),fragmentList);
        mViewpager.setAdapter(pagerAdapter);
        mViewpager.setOffscreenPageLimit(fragmentList.size());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {

    }
}