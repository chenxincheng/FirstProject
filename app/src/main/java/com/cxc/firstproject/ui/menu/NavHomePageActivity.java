package com.cxc.firstproject.ui.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cxc.firstproject.R;
import com.cxc.firstproject.base.BaseActivity;
import com.cxc.firstproject.view.statusbar.StatusBarUtil;

import butterknife.Bind;


public class NavHomePageActivity extends BaseActivity {

    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout titleLayout;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab_share)
    FloatingActionButton shareButton;
    @Override
    public int getLayoutId() {
        return R.layout.activity_nav_home_page;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        titleLayout.setTitle(getString(R.string.app_name));
        StatusBarUtil.setTranslucentForImageView(this, 0, toolbar);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ShareUtils.share(v.getContext(), R.string.string_share_text);
            }
        });
    }

    public static void startHome(Context mContext) {
        Intent intent = new Intent(mContext, NavHomePageActivity.class);
        mContext.startActivity(intent);
    }
}
