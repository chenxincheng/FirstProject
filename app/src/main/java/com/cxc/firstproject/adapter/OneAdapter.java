package com.cxc.firstproject.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cxc.firstproject.R;
import com.cxc.firstproject.app.App;
import com.cxc.firstproject.base.baseadapter.BaseRecyclerViewAdapter;
import com.cxc.firstproject.base.baseadapter.BaseRecyclerViewHolder;
import com.cxc.firstproject.bean.moviechild.SubjectsBean;
import com.cxc.firstproject.ui.activity.movie.OneMovieDetailActivity;
import com.cxc.firstproject.utils.CommonUtils;
import com.cxc.firstproject.utils.ImgLoadUtil;
import com.cxc.firstproject.utils.PerfectClickListener;
import com.cxc.firstproject.utils.StringFormatUtil;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * Created by jingbin on 2016/11/25.
 */

public class OneAdapter extends BaseRecyclerViewAdapter<SubjectsBean> {

    private Activity activity;

    public OneAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view_info = View.inflate(App.getInstance(), R.layout.item_one,
                null);
        return new ViewHolder(view_info);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<SubjectsBean> {
        private ImageView ivMovie;
        private TextView movieName,movieDirectors,movieCast,movieGeners,movieRate;
        private View viewColor;
        private LinearLayout llOneItem;
        ViewHolder(View view) {
            super(view);
            ivMovie =  (ImageView) view.findViewById(R.id.iv_one_photo);
            movieName = (TextView) view.findViewById(R.id.tv_one_title);
            movieDirectors = (TextView) view.findViewById(R.id.tv_one_directors);
            movieCast = (TextView) view.findViewById(R.id.tv_one_casts);
            movieGeners = (TextView) view.findViewById(R.id.tv_one_genres);
            movieRate = (TextView) view.findViewById(R.id.tv_one_rating_rate);
            viewColor = (View) view.findViewById(R.id.tv_one_rating_rate);
            llOneItem = (LinearLayout) view.findViewById(R.id.ll_one_item);
        }

        @Override
        public void onBindViewHolder(final SubjectsBean positionData, final int position) {
            if (positionData != null) {
                // 图片
                ImgLoadUtil.displayEspImage(positionData.getImages().getLarge(), ivMovie,0);
                // 导演
                movieDirectors.setText(StringFormatUtil.formatName(positionData.getDirectors())+"");
                // 主演
                movieCast.setText(StringFormatUtil.formatName(positionData.getCasts())+"");
                // 类型
                movieGeners.setText("类型：" + StringFormatUtil.formatGenres(positionData.getGenres()));
                // 评分
                movieRate.setText("评分：" + String.valueOf(positionData.getRating().getAverage()));
                movieName.setText(""+positionData.getTitle());
                // 分割线颜色
                viewColor.setBackgroundColor(CommonUtils.randomColor());

                ViewHelper.setScaleX(itemView,0.8f);
                ViewHelper.setScaleY(itemView,0.8f);
                ViewPropertyAnimator.animate(itemView).scaleX(1).setDuration(350).setInterpolator(new OvershootInterpolator()).start();
                ViewPropertyAnimator.animate(itemView).scaleY(1).setDuration(350).setInterpolator(new OvershootInterpolator()).start();

                llOneItem.setOnClickListener(new PerfectClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {

                        OneMovieDetailActivity.start(activity, positionData, ivMovie);

//                        if (position % 2 == 0) {

//                            SlideScrollViewActivity.start(activity, positionData, binding.ivOnePhoto);

//                            MovieDetailActivity.start(activity, positionData, binding.ivOnePhoto);
//                            OneMovieDetailActivity.start(activity, positionData, binding.ivOnePhoto);

//                            TestActivity.start(activity, positionData, binding.ivOnePhoto);
//                            activity.overridePendingTransition(R.anim.push_fade_out, R.anim.push_fade_in);
//                        } else {
//                            SlideScrollViewActivity.start(activity, positionData, binding.ivOnePhoto);
//                            SlideShadeViewActivity.start(activity, positionData, binding.ivOnePhoto);
//                            OneMovieDetailActivity.start(activity, positionData, binding.ivOnePhoto);
//                        }

                        // 这个可以
//                        SlideScrollViewActivity.start(activity, positionData, binding.ivOnePhoto);
//                        TestActivity.start(activity,positionData,binding.ivOnePhoto);
//                        v.getContext().startActivity(new Intent(v.getContext(), SlideScrollViewActivity.class));

//                        SlideShadeViewActivity.start(activity, positionData, binding.ivOnePhoto);

                    }
                });
            }
        }
    }
}
