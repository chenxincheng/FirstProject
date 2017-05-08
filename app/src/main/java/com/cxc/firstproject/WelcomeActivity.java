package com.cxc.firstproject;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cxc.firstproject.app.AppConstants;
import com.cxc.firstproject.base.BaseActivity;
import com.cxc.firstproject.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 作者：chenxincheng on 2017/4/21.
 * 邮件：chenxincheng@xwtec.cn
 */

public class WelcomeActivity extends BaseActivity{

    @Bind(R.id.view_pager)
    ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    @Bind(R.id.layoutDots)
    LinearLayout dotsLayout;
    TextView[] dots;
    @Bind(R.id.btn_next)
    Button btnNext;
    @Bind(R.id.btn_skip)
    Button btnSkip;
    private List<View> list;
    // 图片
    private int[] imageView = { R.drawable.yindaoye1, R.drawable.yindaoye2,
            R.drawable.yindaoye3, R.drawable.yindaoye4 };
    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        list = new ArrayList<View>();
        // 将imageview添加到view
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < imageView.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(params);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageResource(imageView[i]);
            list.add(iv);
        }
        //添加点
        addBottomDots(0);

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if(current < list.size()){
                    viewPager.setCurrentItem(current);
                }else{
                    launchHomeScreen();
                }
            }
        });
    }
    private int getItem(int i){
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen(){
        startActivity(new Intent(this,MainActivity.class));
        SPUtils.put(this, AppConstants.FIRST_OPEN,true);
        finish();
    }

    private void addBottomDots(int currentPage){
        dots = new TextView[list.size()];

        dotsLayout.removeAllViews();
        for(int i = 0; i < dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));//圆点
            dots[i].setTextSize(20);
            dots[i].setTextColor(this.getResources().getColor(R.color.bg_textmy));
            dotsLayout.addView(dots[i]);
        }
        if(dots.length > 0){
            dots[currentPage].setTextColor(this.getResources().getColor(R.color.main_color));
        }
    }
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            //改变下一步按钮text  “NEXT”或“GOT IT”
            if(position == list.size() - 1){
                btnNext.setText("立即体验");
                btnSkip.setVisibility(View.GONE);
            }else{
                btnNext.setText("下一页");
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public class MyViewPagerAdapter extends PagerAdapter {


        public MyViewPagerAdapter(){

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }
    }
}
