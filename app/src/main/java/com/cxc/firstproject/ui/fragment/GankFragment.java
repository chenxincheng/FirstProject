package com.cxc.firstproject.ui.fragment;

import com.cxc.firstproject.R;
import com.cxc.firstproject.base.BaseFragment;

/**
 * 作者：chenxincheng on 2017/4/24.
 * 邮件：chenxincheng@xwtec.cn
 */

public class GankFragment extends BaseFragment {
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
    }
}
