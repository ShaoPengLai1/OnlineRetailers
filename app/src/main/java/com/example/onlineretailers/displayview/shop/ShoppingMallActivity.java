package com.example.onlineretailers.displayview.shop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.onlineretailers.R;
import com.example.onlineretailers.displayview.fragment.HomeFragment;
import com.example.onlineretailers.displayview.fragment.MineFragment;
import com.example.onlineretailers.displayview.fragment.MomentsFragment;
import com.example.onlineretailers.displayview.fragment.OrderFragment;
import com.example.onlineretailers.displayview.fragment.ShoppingFragment;
import com.example.onlineretailers.displayview.fragment.cousom.NoScrollViewPager;
import com.example.onlineretailers.utils.concrete.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingMallActivity extends BaseActivity {


    @BindView(R.id.contents)
    NoScrollViewPager contents;
    @BindView(R.id.homeRadio)
    RadioButton homeRadio;
    @BindView(R.id.momentsRadio)
    RadioButton momentsRadio;
    @BindView(R.id.shoppingTrolleyRadio)
    RadioButton shoppingTrolleyRadio;
    @BindView(R.id.purchaseOrderRadio)
    RadioButton purchaseOrderRadio;
    @BindView(R.id.mineRadio)
    RadioButton mineRadio;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    private HomeFragment homeFragment;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_shopping_mall;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setPageChange();
    }

    private void setPageChange() {
        final List<Fragment> list = new ArrayList<>();
        homeFragment = new HomeFragment();
        list.add(homeFragment);
        list.add(new MomentsFragment());
        list.add(new ShoppingFragment());
        list.add(new OrderFragment());
        list.add(new MineFragment());
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.homeRadio:
                    contents.setCurrentItem(0);
                    break;
                case R.id.momentsRadio:
                    contents.setCurrentItem(1);
                    break;
                case R.id.shoppingTrolleyRadio:
                    contents.setCurrentItem(2);
                    break;
                case R.id.purchaseOrderRadio:
                    contents.setCurrentItem(3);
                    break;
                case R.id.mineRadio:
                    contents.setCurrentItem(4);
                    break;
            }
        });

        contents.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return list.get(i);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        });
    }


}
