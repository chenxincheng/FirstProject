package com.cxc.firstproject.ui.activity.login;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.cxc.firstproject.R;
import com.cxc.firstproject.adapter.TabViewPagerAdapter;
import com.cxc.firstproject.ui.fragment.FragmentLogin;
import com.cxc.firstproject.ui.fragment.FragmentRegist;
import com.cxc.firstproject.utils.AppManager;

/**
 * 作者：chenxincheng on 2017/4/20.
 * 邮件：chenxincheng@xwtec.cn
 */

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppManager.getAppManager().addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setupViewPager();
    }
    //设置tab下的viewpager
    private void setupViewPager() {

        final ViewPager login_viewpager = (ViewPager) findViewById(R.id.login_viewpager);
        setupViewPager(login_viewpager);
        TabLayout login_tabs = (TabLayout) findViewById(R.id.login_tabs);
        login_tabs.setupWithViewPager(login_viewpager);
        login_tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                final 	int f=tab.getPosition();
                login_viewpager.setCurrentItem(f);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });

    }


    private void setupViewPager(ViewPager viewPager) {
        TabViewPagerAdapter adapter = new TabViewPagerAdapter(this.getSupportFragmentManager());
        adapter.addFrag(new FragmentLogin(), "登录");
        adapter.addFrag(new FragmentRegist(), "注册");
        viewPager.setAdapter(adapter);
    }
}
