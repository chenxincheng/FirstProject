package com.cxc.firstproject.base;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cxc.firstproject.R;
import com.cxc.firstproject.view.mysnackbar.Prompt;
import com.cxc.firstproject.view.mysnackbar.TSnackbar;

import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

/**
 * 作者：chenxincheng on 2017/4/25.
 * 邮件：chenxincheng@xwtec.cn
 */

public abstract class BaseNoTitleFragment<T extends BasePresenter, E extends BaseModel> extends Fragment {
    protected View rootView;
    private TSnackbar snackBar;
    private int APP_DOWn = TSnackbar.APPEAR_FROM_TOP_TO_DOWN;
    private ViewGroup viewGroup;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null)
            rootView = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.bind(this, rootView);
        viewGroup = (ViewGroup) getActivity()
                .findViewById(android.R.id.content).getRootView();
        initView();
        return rootView;
    }
    public void showSnackar(String string){
        Snackbar.make(viewGroup, string, Snackbar.LENGTH_LONG)
                .show();
    }

    public void showScuess(String msg){
        snackBar = TSnackbar.make(viewGroup, msg, TSnackbar.LENGTH_SHORT, APP_DOWn);
        snackBar.setPromptThemBackground(Prompt.SUCCESS);
        snackBar.show();
    }

    public void showfail(String msg){
        snackBar = TSnackbar.make(viewGroup, msg, TSnackbar.LENGTH_LONG, APP_DOWn);
        snackBar.setPromptThemBackground(Prompt.ERROR);
        snackBar.show();
    }
    public void showWarn(String msg){
        snackBar = TSnackbar.make(viewGroup, msg, TSnackbar.LENGTH_LONG, APP_DOWn);
        snackBar.setPromptThemBackground(Prompt.WARNING);
        snackBar.show();
    }

    public TSnackbar showLoading(String msg){
        TSnackbar TSnackbars;
        TSnackbars = TSnackbar.make(viewGroup,msg, TSnackbar.LENGTH_INDEFINITE, APP_DOWn);
        TSnackbars.setAction("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        TSnackbars.setPromptThemBackground(Prompt.SUCCESS);
        TSnackbars.addIconProgressLoading(0,true,false);
        TSnackbars.show();
        return  TSnackbars;
    }
    //获取布局文件
    protected abstract int getLayoutResource();
    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
    public abstract void initPresenter();
    //初始化view
    protected abstract void initView();
    private CompositeSubscription mCompositeSubscription;
}
