package com.cxc.firstproject.ui.fragment.gankchild;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cxc.firstproject.R;
import com.cxc.firstproject.adapter.EverydayAdapter;
import com.cxc.firstproject.app.AppConstants;
import com.cxc.firstproject.base.BaseFragment;
import com.cxc.firstproject.bean.AndroidBean;
import com.cxc.firstproject.bean.FrontpageBean;
import com.cxc.firstproject.http.RequestImpl;
import com.cxc.firstproject.http.cache.ACache;
import com.cxc.firstproject.http.rx.RxBus;
import com.cxc.firstproject.http.rx.RxBusBaseMessage;
import com.cxc.firstproject.http.rx.RxCodeConstants;
import com.cxc.firstproject.model.EverydayModel;
import com.cxc.firstproject.utils.DebugUtil;
import com.cxc.firstproject.utils.GlideImageLoader;
import com.cxc.firstproject.utils.PerfectClickListener;
import com.cxc.firstproject.utils.SPUtils;
import com.cxc.firstproject.utils.TimeUtil;
import com.cxc.firstproject.view.refresh.PtrDefaultHandler;
import com.cxc.firstproject.view.refresh.PtrFrameLayout;
import com.cxc.firstproject.view.refresh.PtrHandler;
import com.cxc.firstproject.view.refresh.TouchEventPtrFrameLayout;
import com.cxc.firstproject.view.refresh.smileyloadingview.SmileyHeaderView;
import com.cxc.firstproject.view.webview.WebViewActivity;
import com.example.xrecyclerview.XRecyclerView;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Subscription;

/**
 * 每日推荐 更新逻辑：判断是否是第二天 是：判断是否是大于12：30 ***** |是：刷新当天数据 *****
 * |否：使用缓存：|无：请求前一天数据,直到请求到数据为止 ********** |有：使用缓存 否：使用缓存 ： |无：请求今天数据 **********
 * |有：使用缓存
 */
public class EverydayFragment extends BaseFragment {

    private ArrayList<String> mBannerImages;
    private boolean mIsPrepared = false;
    private boolean mIsFirst = true;
    private boolean mIsFirsts = true;
    private View mHeaderView;
    private View mFooterView;
    private ACache maCache;
    @Bind(R.id.material_style_ptr_frame)
    TouchEventPtrFrameLayout mPtrFrameLayout;
    ScrollView scrollView;
    private Banner banner;
    private EverydayModel mEverydayModel;
    @Bind(R.id.rv_view)
    XRecyclerView recyclerView;
    // 是否是上一天的请求
    // 记录请求的日期
    private ArrayList<List<AndroidBean>> mLists;
    String year = getTodayTime().get(0);
    String month = getTodayTime().get(1);
    String day = getTodayTime().get(2);

    private boolean isOldDayRequest;
    /**
     * 刷新头部
     */
    private SmileyHeaderView header;
    private TextView mTvdate;

    private ImageButton mIbXd, mIbMovie;
    private EverydayAdapter mEverydayAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_everyday;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        mEverydayModel = new EverydayModel();
        maCache = ACache.get(getContext());
        mBannerImages = (ArrayList<String>) maCache
                .getAsObject(AppConstants.BANNER_PIC);
        DebugUtil.error("----mBannerImages: " + (mBannerImages == null));

        mHeaderView = View.inflate(getActivity(), R.layout.header_item_everyday,
                null);
        banner = (Banner) mHeaderView.findViewById(R.id.banner);
        mTvdate = (TextView) mHeaderView.findViewById(R.id.tv_daily_text);
        mIbXd = (ImageButton) mHeaderView.findViewById(R.id.ib_xiandu);
        mIbMovie = (ImageButton) mHeaderView.findViewById(R.id.ib_movie_hot);
        initRecyclerView();
        initLocalSetting();
        mIsPrepared = true;
        scrollView = (ScrollView) getActivity().findViewById(R.id.scrollViews);
        header = new SmileyHeaderView(getActivity());
        mPtrFrameLayout.setResistance(3.0f);
        mPtrFrameLayout.setRatioOfHeaderHeightToRefresh(1f);
        mPtrFrameLayout.setDurationToCloseHeader(500);
        mPtrFrameLayout.setDurationToClose(100);
        mPtrFrameLayout.setKeepHeaderWhenRefresh(true);
        mPtrFrameLayout.setHeaderView(header);
        mPtrFrameLayout.addPtrUIHandler(header);

        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content,
                    View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,
                        scrollView, header);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadBannerPicture();
                        showContentData();
                    }
                }, 200);
            }
        });
        loadData();
    }

    private void initLocalSetting() {
        mEverydayModel.setData(getTodayTime().get(0), getTodayTime().get(1),
                getTodayTime().get(2));
        // DebugUtil.error("" + year + month + day);
        // 显示日期,去掉第一位的"0"
        mTvdate.setText(getTodayTime().get(2).indexOf("0") == 0
                ? getTodayTime().get(2).replace("0", "")
                : getTodayTime().get(2));
        mIbXd.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                WebViewActivity.loadUrl(v.getContext(),
                        "https://gank.io/xiandu", "加载中...");
            }
        });
        mIbMovie.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                RxBus.getDefault().post(RxCodeConstants.JUMP_TYPE_TO_ONE,
                        new RxBusBaseMessage());
            }
        });
    }

    /**
     * 获取当天日期
     */
    private ArrayList<String> getTodayTime() {
        String data = TimeUtil.getData();
        String[] split = data.split("-");
        String year = split[0];
        String month = split[1];
        String day = split[2];
        ArrayList<String> list = new ArrayList<>();
        list.add(year);
        list.add(month);
        list.add(day);
        return list;
    }

    @Override
    protected void loadData() {
        // 显示时轮播图滚动
        if (banner != null) {
            banner.startAutoPlay();
            banner.setDelayTime(4000);
        }

        if (!mIsVisible || !mIsPrepared) {
            return;
        }

        String oneData = SPUtils.getString("everyday_data", "2017-5-23");
        if (!oneData.equals(TimeUtil.getData())) {// 是第二天
            if (TimeUtil.isRightTime()) {// 大于12：30,请求
                isOldDayRequest = false;
                mEverydayModel.setData(getTodayTime().get(0),
                        getTodayTime().get(1), getTodayTime().get(2));
                loadBannerPicture();
                showContentData();
            } else {// 小于，取缓存没有请求前一天

                ArrayList<String> lastTime = TimeUtil.getLastTime(
                        getTodayTime().get(0), getTodayTime().get(1),
                        getTodayTime().get(2));
                mEverydayModel.setData(lastTime.get(0), lastTime.get(1),
                        lastTime.get(2));
                year = lastTime.get(0);
                month = lastTime.get(1);
                day = lastTime.get(2);

                isOldDayRequest = true;// 是昨天
                getACacheData();
            }
        } else {// 当天，取缓存没有请求当天
            isOldDayRequest = false;
            getACacheData();
        }
    }

    /**
     * 取缓存
     */
    private void getACacheData() {
        if (!mIsFirst) {
            return;
        }

        if (mBannerImages != null && mBannerImages.size() > 0) {
            showContentView();
            header.setText("刷新成功");
            mPtrFrameLayout.refreshComplete();
            banner.setImages(mBannerImages)
                    .setImageLoader(new GlideImageLoader()).start();
        } else {
            loadBannerPicture();
        }
        mLists = (ArrayList<List<AndroidBean>>) maCache
                .getAsObject(AppConstants.EVERYDAY_CONTENT);
        if (mLists != null && mLists.size() > 0) {
            showContentView();
            setAdapter(mLists);
        } else {
            showContentData();
        }
    }

    private void setAdapter(ArrayList<List<AndroidBean>> lists) {
        if (mEverydayAdapter == null) {
            mEverydayAdapter = new EverydayAdapter();
        } else {
            mEverydayAdapter.clear();
        }
        mEverydayAdapter.addAll(lists);
        // DebugUtil.error("----111111 ");
        // bindingView.xrvEveryday.setAdapter(mEverydayAdapter);
        // mEverydayAdapter.notifyDataSetChanged();
        // DebugUtil.error("----222222 ");
        maCache.remove(AppConstants.EVERYDAY_CONTENT);
        // 缓存三天，这样就可以取到缓存了！
        maCache.put(AppConstants.EVERYDAY_CONTENT, lists, 259200);

        if (isOldDayRequest) {
            ArrayList<String> lastTime = TimeUtil.getLastTime(
                    getTodayTime().get(0), getTodayTime().get(1),
                    getTodayTime().get(2));
            SPUtils.put("everyday_data", lastTime.get(0) + "-" + lastTime.get(1)
                    + "-" + lastTime.get(2));
        } else {
            // 保存请求的日期
            SPUtils.put("everyday_data", TimeUtil.getData());
        }
        mIsFirst = false;

        recyclerView.setAdapter(mEverydayAdapter);
        mEverydayAdapter.notifyDataSetChanged();
    }

    /**
     * 加载正文内容
     */
    private void showContentData() {
        mEverydayModel.showRecyclerViewData(new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                showContentView();
                if (mLists != null) {
                    mLists.clear();
                }
                mLists = (ArrayList<List<AndroidBean>>) object;
                if (mLists.size() > 0 && mLists.get(0).size() > 0) {
                    header.setText("刷新成功");
                    mPtrFrameLayout.refreshComplete();
                    setAdapter(mLists);
                } else {
                    requestBeforeData();
                }
            }

            @Override
            public void loadFailed() {
                if (mLists != null && mLists.size() > 0) {
                    return;
                }
                if (mIsFirsts) {
                    showError();
                    mIsFirst = false;
                }
                header.setText("刷新失败");
                mPtrFrameLayout.refreshComplete();
            }

            @Override
            public void addSubscription(Subscription subscription) {
                EverydayFragment.this.addSubscription(subscription);
            }
        });
    }

    private void loadBannerPicture() {
        mEverydayModel.showBanncerPage(new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                showContentView();
                header.setText("刷新成功");
                mPtrFrameLayout.refreshComplete();
                if (mBannerImages == null) {
                    mBannerImages = new ArrayList<String>();
                } else {
                    mBannerImages.clear();
                }
                FrontpageBean bean = (FrontpageBean) object;
                if (bean != null && bean.getResult() != null
                        && bean.getResult().getFocus() != null
                        && bean.getResult().getFocus().getResult() != null) {
                    final List<FrontpageBean.ResultBeanXXXXXXXXXXXXXX.FocusBean.ResultBeanX> result = bean
                            .getResult().getFocus().getResult();
                    if (result != null && result.size() > 0) {
                        for (int i = 0; i < result.size(); i++) {
                            // 获取所有图片
                            mBannerImages.add(result.get(i).getRandpic());
                        }
                        banner.setImages(mBannerImages)
                                .setImageLoader(new GlideImageLoader()).start();
                        banner.setOnBannerClickListener(
                                new OnBannerClickListener() {
                                    @Override
                                    public void OnBannerClick(int position) {
                                        position = position - 1;
                                        // 链接没有做缓存，如果轮播图使用的缓存则点击图片无效
                                        if (result.get(position) != null
                                                && result.get(position)
                                                        .getCode() != null
                                                && result.get(position)
                                                        .getCode()
                                                        .startsWith("http")) {
                                            WebViewActivity.loadUrl(
                                                    getContext(),
                                                    result.get(position)
                                                            .getCode(),
                                                    "加载中...");
                                        }
                                    }
                                });
                        maCache.remove(AppConstants.BANNER_PIC);
                        maCache.put(AppConstants.BANNER_PIC, mBannerImages,
                                30000);
                    }
                }
            }

            @Override
            public void loadFailed() {
                header.setText("刷新失败");
                mPtrFrameLayout.refreshComplete();
            }

            @Override
            public void addSubscription(Subscription subscription) {
                EverydayFragment.this.addSubscription(subscription);
            }
        });
    }

    /**
     * 没请求到数据就取缓存，没缓存一直请求前一天数据
     */
    private void requestBeforeData() {
        mLists = (ArrayList<List<AndroidBean>>) maCache
                .getAsObject(AppConstants.EVERYDAY_CONTENT);
        if (mLists != null && mLists.size() > 0) {
            setAdapter(mLists);
            header.setText("刷新成功");
            mPtrFrameLayout.refreshComplete();
        } else {
            // 一直请求，知道请求到数据为止
            ArrayList<String> lastTime = TimeUtil.getLastTime(year, month, day);
            mEverydayModel.setData(lastTime.get(0), lastTime.get(1),
                    lastTime.get(2));
            year = lastTime.get(0);
            month = lastTime.get(1);
            day = lastTime.get(2);
            showContentData();
        }
    }

    private void initRecyclerView() {
        recyclerView.setPullRefreshEnabled(false);
        recyclerView.setLoadingMoreEnabled(false);
        if (mHeaderView != null) {
            recyclerView.addHeaderView(mHeaderView);
        }
        if (mFooterView == null) {
            mFooterView = View.inflate(getActivity(),
                    R.layout.footer_item_everyday, null);
            recyclerView.addFootView(mFooterView, true);
            recyclerView.noMoreLoading();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // 需加，不然滑动不流畅
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                    int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder,
                    int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        });
    }

    @Override
    protected void onRefresh() {
        loadData();
    }

    @Override
    protected void onInvisible() {
        super.onInvisible();
        // 不可见时轮播图停止滚动
        if (mHeaderView != null && banner != null) {
            banner.stopAutoPlay();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // 失去焦点，否则RecyclerView第一个item会回到顶部
        recyclerView.setFocusable(false);
        DebugUtil.error("-----EverydayFragment----onResume()");
        // 开始图片请求
        Glide.with(getActivity()).resumeRequests();
    }

    @Override
    public void onPause() {
        super.onPause();
        DebugUtil.error("-----EverydayFragment----onPause()");
        // 停止全部图片请求 跟随着Activity
        Glide.with(getActivity()).pauseRequests();

    }
}
