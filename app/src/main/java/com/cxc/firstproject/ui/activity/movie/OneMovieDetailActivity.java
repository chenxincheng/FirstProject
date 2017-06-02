package com.cxc.firstproject.ui.activity.movie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cxc.firstproject.R;
import com.cxc.firstproject.adapter.MovieDetailAdapter;
import com.cxc.firstproject.base.BaseHeaderActivity;
import com.cxc.firstproject.bean.MovieDetailBean;
import com.cxc.firstproject.bean.moviechild.SubjectsBean;
import com.cxc.firstproject.http.HttpClient;
import com.cxc.firstproject.utils.CommonUtils;
import com.cxc.firstproject.utils.ImgLoadUtil;
import com.cxc.firstproject.utils.StringFormatUtil;
import com.cxc.firstproject.view.webview.WebViewActivity;
import com.example.xrecyclerview.XRecyclerView;

import butterknife.Bind;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 继承基类而写的电影详情页 2016-12-13
 */
public class OneMovieDetailActivity extends BaseHeaderActivity {

    private SubjectsBean subjectsBean;
    private String mMoreUrl;
    private String mMovieName;

    @Bind(R.id.img_item_bg)
     ImageView imgBg;
    @Bind(R.id.iv_one_photo)
     ImageView onePhoto;
    @Bind(R.id.tv_one_rating_rate)
    TextView rate;
    @Bind(R.id.tv_one_rating_number)
    TextView number;
    @Bind(R.id.tv_one_directors)
    TextView directors;
    @Bind(R.id.tv_one_casts)
    TextView casts;
    @Bind(R.id.tv_one_genres)
    TextView genres;
    @Bind(R.id.tv_one_title)
    TextView title;
    @Bind(R.id.tv_one_day)
    TextView day;

    @Bind(R.id.tv_one_city)
    TextView city;
    @Bind(R.id.tv_details)
    TextView details;
    @Bind(R.id.xrv_cast)
    XRecyclerView xrvCast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_movie_detail);
        if (getIntent() != null) {
            subjectsBean = (SubjectsBean) getIntent().getSerializableExtra("bean");
        }
        initSlideShapeTheme(setHeaderImgUrl(), setHeaderImageView());
        setTitle(subjectsBean.getTitle());
        setSubTitle(String.format("主演：%s", StringFormatUtil.formatName(subjectsBean.getCasts())));
        initData();
        loadMovieDetail();
    }

    private void initData() {
        ImgLoadUtil.showImg(imgBg,subjectsBean.getImages().getMedium());
        ImgLoadUtil.showImg(onePhoto,subjectsBean.getImages().getLarge());
        rate.setText("评分："+subjectsBean.getRating().getAverage());
        number.setText(subjectsBean.getCollect_count()+"人评分");
        directors.setText(""+StringFormatUtil.formatName(subjectsBean.getDirectors()));
        casts.setText(""+StringFormatUtil.formatName(subjectsBean.getCasts()));
        genres.setText(""+StringFormatUtil.formatGenres(subjectsBean.getGenres()));
    }

    @Override
    protected void setTitleClickMore() {
        WebViewActivity.loadUrl(OneMovieDetailActivity.this, mMoreUrl, mMovieName);
    }

    @Override
    protected int setHeaderLayout() {
        return R.layout.header_slide_shape;
    }

    @Override
    protected String setHeaderImgUrl() {
        if (subjectsBean == null) {
            return "";
        }
        return subjectsBean.getImages().getMedium();
    }

    @Override
    protected ImageView setHeaderImageView() {
        return imgBg;
    }

    private void loadMovieDetail() {
        Subscription get = HttpClient.Builder.getDouBanService().getMovieDetail(subjectsBean.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieDetailBean>() {
                    @Override
                    public void onCompleted() {
                        showContentView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showError();
                    }

                    @Override
                    public void onNext(final MovieDetailBean movieDetailBean) {
                        // 上映日期
                        day.setText(String.format("上映日期：%s", movieDetailBean.getYear()));
                        // 制片国家
                        city.setText(String.format("制片国家/地区：%s", StringFormatUtil.formatGenres(movieDetailBean.getCountries())));

//                        bindingContentView.setBean(movieDetailBean);
                        title.setText(StringFormatUtil.formatGenres(movieDetailBean.getAka()));
                        details.setText(movieDetailBean.getSummary());

                        mMoreUrl = movieDetailBean.getAlt();
                        mMovieName = movieDetailBean.getTitle();

                        transformData(movieDetailBean);
                    }
                });
        addSubscription(get);

    }

    /**
     * 异步线程转换数据
     */
    private void transformData(final MovieDetailBean movieDetailBean) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < movieDetailBean.getDirectors().size(); i++) {
                    movieDetailBean.getDirectors().get(i).setType("导演");
                }
                for (int i = 0; i < movieDetailBean.getCasts().size(); i++) {
                    movieDetailBean.getCasts().get(i).setType("演员");
                }

                OneMovieDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setAdapter(movieDetailBean);
                    }
                });
            }
        }).start();
    }

    /**
     * 设置导演&演员adapter
     */
    private void setAdapter(MovieDetailBean movieDetailBean) {
        xrvCast.setVisibility(View.VISIBLE);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(OneMovieDetailActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xrvCast.setLayoutManager(mLayoutManager);
        xrvCast.setPullRefreshEnabled(false);
        xrvCast.setLoadingMoreEnabled(false);
        // 需加，不然滑动不流畅
        xrvCast.setNestedScrollingEnabled(false);
        xrvCast.setHasFixedSize(false);

        MovieDetailAdapter mAdapter = new MovieDetailAdapter();
        mAdapter.addAll(movieDetailBean.getDirectors());
        mAdapter.addAll(movieDetailBean.getCasts());
        xrvCast.setAdapter(mAdapter);
    }

    @Override
    protected void onRefresh() {
        loadMovieDetail();
    }

    /**
     * @param context      activity
     * @param positionData bean
     * @param imageView    imageView
     */
    public static void start(Activity context, SubjectsBean positionData, ImageView imageView) {
        Intent intent = new Intent(context, OneMovieDetailActivity.class);
        intent.putExtra("bean", positionData);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                        imageView, CommonUtils.getString(R.string.transition_movie_img));//与xml文件对应
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }

}
