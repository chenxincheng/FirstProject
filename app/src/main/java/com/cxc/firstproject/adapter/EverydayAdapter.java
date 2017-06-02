package com.cxc.firstproject.adapter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cxc.firstproject.R;
import com.cxc.firstproject.app.App;
import com.cxc.firstproject.base.baseadapter.BaseRecyclerViewAdapter;
import com.cxc.firstproject.base.baseadapter.BaseRecyclerViewHolder;
import com.cxc.firstproject.bean.AndroidBean;
import com.cxc.firstproject.http.rx.RxBus;
import com.cxc.firstproject.http.rx.RxCodeConstants;
import com.cxc.firstproject.utils.CommonUtils;
import com.cxc.firstproject.utils.ImgLoadUtil;
import com.cxc.firstproject.utils.PerfectClickListener;
import com.cxc.firstproject.view.webview.WebViewActivity;

import java.util.List;

import butterknife.Bind;

/**
 * Created by jingbin on 2016/12/27.
 */

public class EverydayAdapter
        extends BaseRecyclerViewAdapter<List<AndroidBean>> {

    private static final int TYPE_TITLE = 1; // title
    private static final int TYPE_ONE = 2;// 一张图
    private static final int TYPE_TWO = 3;// 二张图
    private static final int TYPE_THREE = 4;// 三张图

    @Override
    public int getItemViewType(int position) {
        if (!TextUtils
                .isEmpty(getData().get(position).get(0).getType_title())) {
            return TYPE_TITLE;
        } else if (getData().get(position).size() == 1) {
            return TYPE_ONE;
        } else if (getData().get(position).size() == 2) {
            return TYPE_TWO;
        } else if (getData().get(position).size() == 3) {
            return TYPE_THREE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent,
            int viewType) {
        switch (viewType) {
        case TYPE_TITLE:
            View view_info = View.inflate(App.getInstance(),
                    R.layout.item_everyday_title, null);
            return new TitleHolder(view_info);
        case TYPE_ONE:
            // return new OneHolder(parent, R.layout.item_everyday_one,view);
            View oneHolder = View.inflate(App.getInstance(),
                    R.layout.item_everyday_one, null);
            return new OneHolder(oneHolder);
        case TYPE_TWO:
            // return new TwoHolder(parent, R.layout.item_everyday_two,view);
            View twoHolder = View.inflate(App.getInstance(),
                    R.layout.item_everyday_two, null);
            return new TwoHolder(twoHolder);
        default:
            View threeHolder = View.inflate(App.getInstance(),
                    R.layout.item_everyday_three, null);
            return new ThreeHolder(threeHolder);
        }
    }

    private class TitleHolder
            extends BaseRecyclerViewHolder<List<AndroidBean>> {
        private TextView tvTitleType;
        private View viewLine;
        private LinearLayout llTitleMore;
        private ImageView ivTitleType;

        TitleHolder(View itme) {
            super(itme);
            tvTitleType = (TextView) itme.findViewById(R.id.tv_title_type);
            viewLine = (View) itme.findViewById(R.id.view_line);
            llTitleMore = (LinearLayout) itme.findViewById(R.id.ll_title_more);
            ivTitleType = (ImageView) itme.findViewById(R.id.iv_title_type);
        }

        @Override
        public void onBindViewHolder(List<AndroidBean> object,
                final int position) {

            int index = 0;
            String title = object.get(0).getType_title();
            tvTitleType.setText(title + "");
            if ("Android".equals(title)) {
                ivTitleType.setImageDrawable(
                        CommonUtils.getDrawable(R.drawable.home_title_android));
                index = 0;
            } else if ("福利".equals(title)) {
                ivTitleType.setImageDrawable(
                        CommonUtils.getDrawable(R.drawable.home_title_meizi));
                index = 1;
            } else if ("IOS".equals(title)) {
                ivTitleType.setImageDrawable(
                        CommonUtils.getDrawable(R.drawable.home_title_ios));
                index = 2;
            } else if ("休息视频".equals(title)) {
                ivTitleType.setImageDrawable(
                        CommonUtils.getDrawable(R.drawable.home_title_movie));
                index = 2;
            } else if ("拓展资源".equals(title)) {
                ivTitleType.setImageDrawable(
                        CommonUtils.getDrawable(R.drawable.home_title_source));
                index = 2;
            } else if ("瞎推荐".equals(title)) {
                ivTitleType.setImageDrawable(
                        CommonUtils.getDrawable(R.drawable.home_title_xia));
                index = 2;
            } else if ("前端".equals(title)) {
                ivTitleType.setImageDrawable(
                        CommonUtils.getDrawable(R.drawable.home_title_qian));
                index = 2;
            } else if ("App".equals(title)) {
                ivTitleType.setImageDrawable(
                        CommonUtils.getDrawable(R.drawable.home_title_app));
                index = 2;
            }

            if (position != 0) {
                viewLine.setVisibility(View.VISIBLE);
            } else {
                viewLine.setVisibility(View.GONE);
            }

            final int finalIndex = index;
            llTitleMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RxBus.getDefault().post(RxCodeConstants.JUMP_TYPE,
                            finalIndex);
                }
            });
        }
    }

    private class OneHolder extends BaseRecyclerViewHolder<List<AndroidBean>> {
        private TextView tvOnePhotoTitle;
        private ImageView ivOnePhoto;
        private LinearLayout llOnePhoto;

        OneHolder(View view) {
            super(view);
            llOnePhoto = (LinearLayout) view.findViewById(R.id.ll_one_photo);
            tvOnePhotoTitle = (TextView) view
                    .findViewById(R.id.tv_one_photo_title);
            ivOnePhoto = (ImageView) view.findViewById(R.id.iv_one_photo);
        }

        @Override
        public void onBindViewHolder(final List<AndroidBean> object,
                int position) {
            if ("福利".equals(object.get(0).getType())) {
                tvOnePhotoTitle.setVisibility(View.GONE);
                ivOnePhoto.setScaleType(ImageView.ScaleType.FIT_XY);
                // ImgLoadUtil.displayEspImage(object.get(0).getUrl(),
                // ivOnePhoto, 1);
                Glide.with(ivOnePhoto.getContext()).load(object.get(0).getUrl())
                        .crossFade(1500).placeholder(R.drawable.img_two_bi_one)
                        .error(R.drawable.img_two_bi_one).into(ivOnePhoto);

            } else {
                tvOnePhotoTitle.setVisibility(View.VISIBLE);
                setDes(object, 0, tvOnePhotoTitle);
                displayRandomImg(1, 0, ivOnePhoto, object);
            }
            setOnClick(llOnePhoto, object.get(0));
        }
    }

    public class TwoHolder extends BaseRecyclerViewHolder<List<AndroidBean>> {
        ImageView ivTwoOneOne;
        ImageView ivTwoOneTwo;
        TextView tvTwoOneOneTitle;
        TextView tvTwoOneTwoTitle;
        LinearLayout llTwoOneOne;
        LinearLayout llTwoOneTwo;

        TwoHolder(View view) {
            super(view);
            ivTwoOneOne = (ImageView) view.findViewById(R.id.iv_two_one_one);
            ivTwoOneTwo = (ImageView) view.findViewById(R.id.iv_two_one_one);
            tvTwoOneOneTitle = (TextView) view
                    .findViewById(R.id.tv_two_one_one_title);
            tvTwoOneTwoTitle = (TextView) view
                    .findViewById(R.id.tv_two_one_two_title);
            llTwoOneOne = (LinearLayout) view.findViewById(R.id.ll_two_one_one);
            llTwoOneTwo = (LinearLayout) view.findViewById(R.id.ll_two_one_two);

        }

        @Override
        public void onBindViewHolder(List<AndroidBean> object, int position) {
            displayRandomImg(2, 0, ivTwoOneOne, object);
            displayRandomImg(2, 1, ivTwoOneTwo, object);
            setDes(object, 0, tvTwoOneOneTitle);
            setDes(object, 1, tvTwoOneTwoTitle);
            setOnClick(llTwoOneOne, object.get(0));
            setOnClick(llTwoOneTwo, object.get(1));
        }
    }

    public class ThreeHolder extends BaseRecyclerViewHolder<List<AndroidBean>> {
        ImageView ivThreeOneOne;
        ImageView ivThreeOneTwo;
        ImageView ivThreeOneThree;
        TextView tvThreeOneOneTitle;
        TextView tvThreeOneTwoTitle;
        TextView tvThreeOneThreeTitle;

        LinearLayout llThreeOneOne;
        LinearLayout llThreeOneTwo;
        LinearLayout llThreeOneThree;

        ThreeHolder(View view) {
            super(view);
            ivThreeOneOne = (ImageView) view
                    .findViewById(R.id.iv_three_one_one);
            ivThreeOneTwo = (ImageView) view
                    .findViewById(R.id.iv_three_one_two);
            ivThreeOneThree = (ImageView) view
                    .findViewById(R.id.iv_three_one_three);

            tvThreeOneOneTitle = (TextView) view
                    .findViewById(R.id.tv_three_one_one_title);
            tvThreeOneTwoTitle = (TextView) view
                    .findViewById(R.id.tv_three_one_two_title);
            tvThreeOneThreeTitle = (TextView) view
                    .findViewById(R.id.tv_three_one_three_title);
            llThreeOneOne = (LinearLayout) view
                    .findViewById(R.id.ll_three_one_one);
            llThreeOneTwo = (LinearLayout) view
                    .findViewById(R.id.ll_three_one_two);
            llThreeOneThree = (LinearLayout) view
                    .findViewById(R.id.ll_three_one_three);
        }

        @Override
        public void onBindViewHolder(List<AndroidBean> object, int position) {

            displayRandomImg(3, 0, ivThreeOneOne, object);
            ivThreeOneOne.setScaleType(ImageView.ScaleType.FIT_XY);
            displayRandomImg(3, 1, ivThreeOneTwo, object);
            displayRandomImg(3, 2, ivThreeOneThree, object);
            setOnClick(llThreeOneOne, object.get(0));
            setOnClick(llThreeOneTwo, object.get(1));
            setOnClick(llThreeOneThree, object.get(2));
            setDes(object, 0, tvThreeOneOneTitle);
            setDes(object, 1, tvThreeOneTwoTitle);
            setDes(object, 2, tvThreeOneThreeTitle);
        }
    }

    private void setDes(List<AndroidBean> object, int position,
            TextView textView) {
        if (object.get(position).getDesc().startsWith("一个使用")) {
            textView.setText("测试文字");

        } else {
            textView.setText(object.get(position).getDesc());
        }
    }

    private void displayRandomImg(int imgNumber, int position,
            ImageView imageView, List<AndroidBean> object) {
        // DebugUtil.error("-----Image_url:
        // "+object.get(position).getImage_url());
        ImgLoadUtil.displayRandom(imgNumber,
                object.get(position).getImage_url(), imageView);
    }

    private void setOnClick(final LinearLayout linearLayout,
            final AndroidBean bean) {
        linearLayout.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                WebViewActivity.loadUrl(App.getInstance(), bean.getUrl(),
                        "加载中...");
            }
        });

        linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(
//                        App.getInstance());
//                View view = View.inflate(v.getContext(),
//                        R.layout.title_douban_top, null);
//                TextView titleTop = (TextView) view
//                        .findViewById(R.id.title_top);
//                titleTop.setTextSize(14);
//                String title = TextUtils.isEmpty(bean.getType())
//                        ? bean.getDesc()
//                        : bean.getType() + "：  " + bean.getDesc();
//                titleTop.setText(title);
//                builder.setCustomTitle(view);
//                builder.setPositiveButton("查看详情",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                    int which) {
//                                WebViewActivity.loadUrl(
//                                        App.getInstance(),
//                                        bean.getUrl(), "加载中...");
//                            }
//                        });
//                builder.show();
                return false;
            }
        });

    }
}
