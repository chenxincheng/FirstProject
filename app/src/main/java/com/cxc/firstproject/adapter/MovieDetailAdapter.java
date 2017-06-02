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
import com.cxc.firstproject.bean.moviechild.PersonBean;
import com.cxc.firstproject.utils.ImgLoadUtil;
import com.cxc.firstproject.utils.PerfectClickListener;
import com.cxc.firstproject.view.webview.WebViewActivity;

/**
 * Created by jingbin on 2016/12/10.
 */

public class MovieDetailAdapter extends BaseRecyclerViewAdapter<PersonBean> {
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view_info = View.inflate(App.getInstance(), R.layout.item_movie_detail_person,
                null);
        return new ViewHolder(view_info);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<PersonBean> {
        private TextView tvName,tvType;
        private LinearLayout llItem;
        private ImageView imageView;
        ViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvType = (TextView) view.findViewById(R.id.tv_type);
            llItem = (LinearLayout)view.findViewById(R.id.ll_item);
            imageView = (ImageView) view.findViewById(R.id.iv_avatar);
        }

        @Override
        public void onBindViewHolder(final PersonBean bean, int position) {
            tvName.setText(bean.getName());
            tvType.setText(bean.getType());
            ImgLoadUtil.showImg(imageView,bean.getAvatars().getLarge());
            llItem.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    if (bean != null && !TextUtils.isEmpty(bean.getAlt())) {
                        WebViewActivity.loadUrl(v.getContext(), bean.getAlt(), bean.getName());
                    }
                }
            });
        }
    }
}
