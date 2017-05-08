package com.cxc.firstproject.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.cxc.firstproject.R;

import butterknife.ButterKnife;

/**
 * 作者：chenxincheng on 2017/4/20. 邮件：chenxincheng@xwtec.cn
 */

public abstract class BaseTitleActivity extends BaseActivity {
    protected Toolbar toolbarBaseActivity;
    private FrameLayout flToolbarBase;
    private View contentView;
    @Override
    public int getLayoutId() {
        return R.layout.activity_toolbar_base;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarBaseActivity = (Toolbar) findViewById(
                R.id.toolbar_base_activity);
        flToolbarBase = (FrameLayout) findViewById(R.id.fl_toolbar_base);
        contentView = LayoutInflater.from(this).inflate(getContentLayoutId(),
                null);
        flToolbarBase.addView(contentView);
        setToolBar(toolbarBaseActivity, "");
        initData();
    }
    public abstract void initData();
    public abstract int getContentLayoutId();
}
