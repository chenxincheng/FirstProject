package com.cxc.firstproject.ui.fragment.gankchild;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.cxc.firstproject.MainActivity;
import com.cxc.firstproject.R;
import com.cxc.firstproject.adapter.OneAdapter;
import com.cxc.firstproject.app.AppConstants;
import com.cxc.firstproject.app.ConstantsImageUrl;
import com.cxc.firstproject.base.BaseFragment;
import com.cxc.firstproject.bean.HotMovieBean;
import com.cxc.firstproject.http.HttpClient;
import com.cxc.firstproject.http.cache.ACache;
import com.cxc.firstproject.utils.DebugUtil;
import com.cxc.firstproject.utils.ImgLoadUtil;
import com.cxc.firstproject.utils.PerfectClickListener;
import com.cxc.firstproject.utils.SPUtils;
import com.cxc.firstproject.utils.TimeUtil;
import com.example.xrecyclerview.XRecyclerView;

import butterknife.Bind;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：chenxincheng on 2017/6/2.
 * 邮件：chenxincheng@xwtec.cn
 */

public class MovieFragment extends BaseFragment {

    // 初始化完成后加载数据
    private boolean isPrepared = false;
    // 第一次显示时加载数据，第二次不显示
    private boolean isFirst = true;
    // 是否正在刷新（用于刷新数据时返回页面不再刷新）
    private boolean mIsLoading = false;
    private ACache aCache;
    private MainActivity activity;
    private HotMovieBean mHotMovieBean;
    private View mHeaderView = null;
    private OneAdapter oneAdapter;
    @Bind(R.id.list_one)
    XRecyclerView listOne;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_one;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        showContentView();
        aCache = ACache.get(getActivity());
        oneAdapter = new OneAdapter(activity);
        mHotMovieBean = (HotMovieBean) aCache.getAsObject(AppConstants.ONE_HOT_MOVIE);
        isPrepared = true;
        DebugUtil.error("---OneFragment   --onActivityCreated");
    }

    /**
     * 懒加载
     * 从此页面新开activity界面返回此页面 不会走这里
     */
    @Override
    protected void loadData() {
        DebugUtil.error("------OneFragment---loadData: ");

        if (!isPrepared || !mIsVisible) {
            return;
        }

        // 显示，准备完毕，不是当天，则请求数据（正在请求时避免再次请求）
        String oneData = SPUtils.getString("one_data", "2016-11-26");

        if (!oneData.equals(TimeUtil.getData()) && !mIsLoading) {
            showLoading();
            /**延迟执行防止卡顿*/
            postDelayLoad();
        } else {
            // 为了正在刷新时不执行这部分
            if (mIsLoading) {
                return;
            }
            if (!isFirst) {
                return;
            }

            showLoading();
            if (mHotMovieBean == null && !mIsLoading) {
                postDelayLoad();
            } else {
                listOne.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (this) {
                            setAdapter(mHotMovieBean);
                            showContentView();
                        }
                    }
                }, 150);
                DebugUtil.error("----缓存: " + oneData);
            }
        }

    }

    private void loadHotMovie() {
        Subscription subscription = HttpClient.Builder.getDouBanService().getHotMovie().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<HotMovieBean>() {
                    @Override
                    public void onCompleted() {
                        showContentView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showContentView();
                        if (oneAdapter != null && oneAdapter.getItemCount() == 0) {
                            showError();
                        }
                    }

                    @Override
                    public void onNext(HotMovieBean hotMovieBean) {
                        if (hotMovieBean != null) {
                            aCache.remove(AppConstants.ONE_HOT_MOVIE);
                            // 保存12个小时
                            aCache.put(AppConstants.ONE_HOT_MOVIE, hotMovieBean, 43200);
                            setAdapter(hotMovieBean);
                            // 保存请求的日期
                            SPUtils.put("one_data", TimeUtil.getData());
                            // 刷新结束
                            mIsLoading = false;
                        }

                        //构造器中，第一个参数表示列数或者行数，第二个参数表示滑动方向,瀑布流
//                        bindingContentView.listOne.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
                        // GridView
//                        bindingContentView.listOne.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    }
                });
        addSubscription(subscription);
    }

    private void setAdapter(HotMovieBean hotMovieBean) {

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listOne.setLayoutManager(mLayoutManager);

        // 加上这两行代码，下拉出提示才不会产生出现刷新头的bug，不加拉不下来
        listOne.setPullRefreshEnabled(false);
        listOne.clearHeader();

        listOne.setLoadingMoreEnabled(false);
        // 需加，不然滑动不流畅
        listOne.setNestedScrollingEnabled(false);
        listOne.setHasFixedSize(false);

        if (mHeaderView == null) {
            mHeaderView = View.inflate(getContext(), R.layout.header_item_one, null);
            View llMovieTop = mHeaderView.findViewById(R.id.ll_movie_top);
            ImageView ivImg = (ImageView) mHeaderView.findViewById(R.id.iv_img);
            ImgLoadUtil.displayRandom(3, ConstantsImageUrl.ONE_URL_01, ivImg);
            llMovieTop.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
//                    DoubanTopActivity.start(v.getContext());
                }
            });
        }
        listOne.addHeaderView(mHeaderView);
        oneAdapter.clear();
        oneAdapter.addAll(hotMovieBean.getSubjects());
        listOne.setAdapter(oneAdapter);
        oneAdapter.notifyDataSetChanged();

        isFirst = false;
    }

    /**
     * 延迟执行，避免卡顿
     * 加同步锁，避免重复加载
     */
    private void postDelayLoad() {
        synchronized (this) {
            if (!mIsLoading) {
                mIsLoading = true;
                listOne.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadHotMovie();
                    }
                }, 150);
            }
        }
    }


    @Override
    protected void onRefresh() {
        loadHotMovie();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DebugUtil.error("--OneFragment   ----onDestroy");
    }

    /**
     * 从此页面新开activity界面返回此页面 走这里
     */
    @Override
    public void onResume() {
        super.onResume();
        DebugUtil.error("--OneFragment   ----onResume");
    }
}
