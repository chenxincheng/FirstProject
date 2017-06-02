package com.cxc.firstproject.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cxc.firstproject.R;
import com.cxc.firstproject.app.App;
import com.cxc.firstproject.base.baseadapter.BaseRecyclerViewAdapter;
import com.cxc.firstproject.base.baseadapter.BaseRecyclerViewHolder;
import com.cxc.firstproject.bean.GankIoDataBean;
import com.cxc.firstproject.utils.ImgLoadUtil;
import com.cxc.firstproject.utils.TimeUtil;
import com.cxc.firstproject.view.webview.WebViewActivity;

/**
 * Created by jingbin on 2016/12/2.
 */

public class AndroidAdapter
        extends BaseRecyclerViewAdapter<GankIoDataBean.ResultBean> {

    private boolean isAll = false;

    public void setAllType(boolean isAll) {
        this.isAll = isAll;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent,
            int viewType) {
        View view_info = View.inflate(App.getInstance(), R.layout.item_android,
                null);
        return new ViewHolder(view_info);
    }

    private class ViewHolder
            extends BaseRecyclerViewHolder<GankIoDataBean.ResultBean> {

        private ImageView ivAllWelfare, ivAndroidPic;
        private LinearLayout llWelfareOther, llAll;
        private TextView tvContentType,tvAndroidDes,tvAndroidWho,tvTime;

        ViewHolder(View view) {
            super(view);
            ivAllWelfare = (ImageView) view.findViewById(R.id.iv_all_welfare);
            ivAndroidPic = (ImageView) view.findViewById(R.id.iv_android_pic);
            llWelfareOther = (LinearLayout) view
                    .findViewById(R.id.ll_welfare_other);
            llAll = (LinearLayout) view.findViewById(R.id.ll_all);
            tvContentType  = (TextView) view.findViewById(R.id.tv_content_type);
            tvAndroidDes = (TextView) view.findViewById(R.id.tv_android_des);
            tvAndroidWho = (TextView) view.findViewById(R.id.tv_android_who);
            tvTime = (TextView) view.findViewById(R.id.tv_android_time);
        }

        @Override
        public void onBindViewHolder(final GankIoDataBean.ResultBean object,
                int position) {

            if (isAll && "福利".equals(object.getType())) {
                ivAllWelfare.setVisibility(View.VISIBLE);
                llWelfareOther.setVisibility(View.GONE);
                ImgLoadUtil.displayEspImage(object.getUrl(), ivAllWelfare, 1);
            } else {
                ivAllWelfare.setVisibility(View.GONE);
                llWelfareOther.setVisibility(View.VISIBLE);
            }

            if (isAll) {
                tvContentType.setVisibility(View.VISIBLE);
                tvContentType.setText(" · " + object.getType());
            } else {
                tvContentType.setVisibility(View.GONE);

            }
            tvAndroidDes.setText(object.getDesc()+"");
            tvAndroidWho.setText(object.getWho());
            tvTime.setText(TimeUtil.getTranslateTime(object.getPublishedAt()));

            // 显示gif图片会很耗内存
            if (object.getImages() != null && object.getImages().size() > 0
                    && !TextUtils.isEmpty(object.getImages().get(0))) {
                // ivAndroidPic.setVisibility(View.GONE);
                ivAndroidPic.setVisibility(View.VISIBLE);
                ImgLoadUtil.displayGif(object.getImages().get(0), ivAndroidPic);
            } else {
                ivAndroidPic.setVisibility(View.GONE);
            }

            llAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebViewActivity.loadUrl(v.getContext(), object.getUrl(),
                            "加载中...");
                }
            });
        }

    }
}
