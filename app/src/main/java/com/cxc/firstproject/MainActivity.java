package com.cxc.firstproject;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cxc.firstproject.adapter.MyFragmentPagerAdapter;
import com.cxc.firstproject.app.AppConstants;
import com.cxc.firstproject.base.BaseActivity;
import com.cxc.firstproject.ui.activity.login.LoginActivity;
import com.cxc.firstproject.ui.fragment.BookFragment;
import com.cxc.firstproject.ui.fragment.GankFragment;
import com.cxc.firstproject.ui.fragment.OneFragment;
import com.cxc.firstproject.utils.AppManager;
import com.cxc.firstproject.utils.CommonUtils;
import com.cxc.firstproject.utils.PerfectClickListener;
import com.cxc.firstproject.utils.SPUtils;
import com.cxc.firstproject.view.statusbar.StatusBarUtil;

import java.util.ArrayList;

import butterknife.Bind;

public class MainActivity extends BaseActivity
        implements OnClickListener, ViewPager.OnPageChangeListener {

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.vp_content)
    ViewPager vpContent;
    @Bind(R.id.nav_view)
    NavigationView navView;
    private TextView tvUserName;
    private String userName;
    @Bind(R.id.ll_title_menu)
    FrameLayout fLmenu;
    @Bind(R.id.iv_title_gank)
    ImageView llTitleGank;
    @Bind(R.id.iv_title_one)
    ImageView llTitleOne;
    @Bind(R.id.iv_title_dou)
    ImageView llTitleDou;
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        AppManager.getAppManager().addActivity(this);
        StatusBarUtil.setColorNoTranslucentForDrawerLayout(MainActivity.this,
                drawerLayout, CommonUtils.getColor(R.color.colorTheme));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
        fLmenu.setOnClickListener(this);
        llTitleGank.setOnClickListener(this);
        llTitleDou.setOnClickListener(this);
        llTitleOne.setOnClickListener(this);
        initContentFragment();
        initDrawerLayout();
    }

    private void initDrawerLayout() {
        navView.inflateHeaderView(R.layout.nav_header_main);
        View headerView = navView.getHeaderView(0);
        LinearLayout llNavExit = (LinearLayout) headerView
                .findViewById(R.id.ll_nav_exit);
        llNavExit.setOnClickListener(this);
        LinearLayout llLogin = (LinearLayout) headerView
                .findViewById(R.id.ll_nav_login);
        tvUserName = (TextView) headerView.findViewById(R.id.tv_username);
        TextView tv_Login = (TextView) headerView.findViewById(R.id.tv_login);
        LinearLayout ivHead = (LinearLayout) headerView
                .findViewById(R.id.iv_avatar);
        TextView tvHead = (TextView) headerView.findViewById(R.id.tv_avatar);
        llLogin.setOnClickListener(listener);
        userName = SPUtils.getString(MainActivity.this, "username");
        if (!TextUtils.isEmpty(userName)) {
            tvUserName.setText(userName);
            tv_Login.setText("切换账号");
            tvHead.setText(userName.substring(0, 1));
        } else {
            tv_Login.setText("登录");
        }
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(final View v) {
            drawerLayout.closeDrawer(GravityCompat.START);
            drawerLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    switch (v.getId()) {
                    case R.id.ll_nav_homepage:// 主页
                        // NavHomePageActivity.startHome(MainActivity.this);
                        break;
                    case R.id.ll_nav_scan_download:// 扫码下载
                        // NavDownloadActivity.start(MainActivity.this);
                        break;
                    case R.id.ll_nav_deedback:// 问题反馈
                        // NavDeedBackActivity.start(MainActivity.this);
                        break;
                    case R.id.ll_nav_about:// 关于云阅
                        // NavAboutActivity.start(MainActivity.this);
                        break;
                    case R.id.ll_nav_login:// 登录GitHub账号
                        // WebViewActivity.loadUrl(v.getContext(),
                        // "https://github.com/login", "登录GitHub账号");
                        startActivity(LoginActivity.class);
                        break;

                    }
                }
            }, 260);
        }
    };

    private void initContentFragment() {
        ArrayList<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(new GankFragment());
        mFragmentList.add(new OneFragment());
        mFragmentList.add(new BookFragment());
        // 注意使用的是：getSupportFragmentManager
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(
                getSupportFragmentManager(), mFragmentList);
        vpContent.setAdapter(adapter);
        // 设置ViewPager最大缓存的页面个数(cpu消耗少)
        vpContent.setOffscreenPageLimit(2);
        vpContent.addOnPageChangeListener(this);
        llTitleGank.setSelected(true);
        vpContent.setCurrentItem(0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
            int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                llTitleGank.setSelected(true);
                llTitleOne.setSelected(false);
                llTitleDou.setSelected(false);
                break;
            case 1:
                llTitleOne.setSelected(true);
                llTitleGank.setSelected(false);
                llTitleDou.setSelected(false);
                break;
            case 2:
                llTitleDou.setSelected(true);
                llTitleOne.setSelected(false);
                llTitleGank.setSelected(false);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.ll_nav_exit:// 退出应用
            finish();
            break;
        case R.id.ll_title_menu:// 开启菜单
            drawerLayout.openDrawer(GravityCompat.START);
            break;
            case R.id.iv_title_gank:// 干货栏
                if (vpContent.getCurrentItem() != 0) {//不然cpu会有损耗
                    llTitleGank.setSelected(true);
                    llTitleOne.setSelected(false);
                    llTitleDou.setSelected(false);
                    vpContent.setCurrentItem(0);
                }
                break;
            case R.id.iv_title_one:// 电影栏
                if (vpContent.getCurrentItem() != 1) {
                    llTitleOne.setSelected(true);
                    llTitleGank.setSelected(false);
                    llTitleDou.setSelected(false);
                    vpContent.setCurrentItem(1);
                }
                break;
            case R.id.iv_title_dou:// 书籍栏
                if (vpContent.getCurrentItem() != 2) {
                    llTitleDou.setSelected(true);
                    llTitleOne.setSelected(false);
                    llTitleGank.setSelected(false);
                    vpContent.setCurrentItem(2);
                }
                break;
            case R.id.iv_avatar: // 头像进入GitHub
//                WebViewActivity.loadUrl(v.getContext(),CommonUtils.getString(R.string.string_url_cloudreader),"CloudReader");
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_search:
            // Toast.makeText(this, "搜索", Toast.LENGTH_SHORT).show();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
