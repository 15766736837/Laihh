package com.example.androiddemo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.androiddemo.ui.fragment.HomeFragment;
import com.example.androiddemo.ui.fragment.MeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private BottomNavigationView mNavigation;
    private ViewPager mViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initEvent() {
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


    private void initView() {
        mNavigation = findViewById(R.id.navigation);
        mViewpager = findViewById(R.id.viewpager);
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new MeFragment());
        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList);
        mViewpager.setAdapter(pagerAdapter);
        mViewpager.setOffscreenPageLimit(fragmentList.size());
    }
}