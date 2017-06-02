package com.cxc.firstproject.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.cxc.firstproject.R;
import com.cxc.firstproject.adapter.MyFragmentPagerAdapter;
import com.cxc.firstproject.base.BaseFragment;
import com.cxc.firstproject.http.rx.RxBus;
import com.cxc.firstproject.http.rx.RxCodeConstants;
import com.cxc.firstproject.ui.fragment.gankchild.AndroidFragment;
import com.cxc.firstproject.ui.fragment.gankchild.CustomFragment;
import com.cxc.firstproject.ui.fragment.gankchild.EverydayFragment;
import com.cxc.firstproject.ui.fragment.gankchild.MovieFragment;
import com.cxc.firstproject.ui.fragment.gankchild.WelfareFragment;
import com.cxc.firstproject.view.PagerSlidingTabStrip;

import java.util.ArrayList;

import butterknife.Bind;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 作者：chenxincheng on 2017/4/24.
 * 邮件：chenxincheng@xwtec.cn
 */

public class GankFragment extends BaseFragment {

    @Bind(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @Bind(R.id.vp_gank)
    ViewPager pager;

    private ArrayList<String> mTitleList = new ArrayList<>(7);
    private ArrayList<Fragment> mFragments = new ArrayList<>(7);
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_gank;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        showContentView();
        initFragmentList();
        MyFragmentPagerAdapter myAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), mFragments, mTitleList);
        pager.setAdapter(myAdapter);
        pager.setOffscreenPageLimit(4);
        myAdapter.notifyDataSetChanged();
        tabs.setViewPager(pager);
        tabs.setIndicatorColorResource(R.color.maincolor);
        tabs.setOnTabReselectedListener(new PagerSlidingTabStrip.OnTabReselectedListener() {
            @Override
            public void onTabReselected(int position) {
                Toast.makeText(getActivity(), "Tab reselected: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        initRxBus();

    }
    private void initFragmentList() {
        mTitleList.add("每日推荐");
        mTitleList.add("福利");
        mTitleList.add("干货订制");
        mTitleList.add("大安卓");
        mTitleList.add("热映电影");
//        mTitleList.add("吃喝玩乐");
//        mTitleList.add("嘿嘿");
        mFragments.add(new EverydayFragment());
        mFragments.add(new WelfareFragment());
        mFragments.add(new CustomFragment());
        mFragments.add(AndroidFragment.newInstance("Android"));
        mFragments.add(new MovieFragment());
//        mFragments.add(new BookFragment());
//        mFragments.add(new BookFragment());
    }
    /**
     * 每日推荐点击"更多"跳转
     */
    private void initRxBus() {
        Subscription subscription = RxBus.getDefault().toObservable(RxCodeConstants.JUMP_TYPE, Integer.class)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        if (integer == 0) {
                            pager.setCurrentItem(3);
                        } else if (integer == 1) {
                            pager.setCurrentItem(1);
                        } else if (integer == 2) {
                            pager.setCurrentItem(2);
                        }
                    }
                });
        addSubscription(subscription);
    }
}
