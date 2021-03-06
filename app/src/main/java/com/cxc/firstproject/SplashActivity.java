package com.cxc.firstproject;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cxc.firstproject.app.AppConstants;
import com.cxc.firstproject.base.BaseActivity;
import com.cxc.firstproject.utils.PerfectClickListener;
import com.cxc.firstproject.utils.SPUtils;
import com.cxc.firstproject.view.shimmer.Shimmer;
import com.cxc.firstproject.view.shimmer.ShimmerTextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：chenxincheng on 2017/4/20. 邮件：chenxincheng@xwtec.cn
 */

public class SplashActivity extends AppCompatActivity {
    private ShimmerTextView mTvShimmer;
    private ShimmerTextView mTvInside;
    private Shimmer shimmer;
    private ImageView ivGg;
    private TextView tvJump;
    Timer timer = new Timer();
    int recLen = 5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() { // UI thread
                @Override
                public void run() {
                    recLen--;
                    tvJump.setVisibility(View.VISIBLE);
                    tvJump.setText("跳过" + recLen);
                    if (recLen <= 1) {
                        enterHomeActivity();

                    }
                }
            });
        }
    };

    public void initView() {

        // 判断是否是第一次开启应用
        boolean isFirstOpen = SPUtils.getBoolean(AppConstants.FIRST_OPEN);
        // 如果是第一次启动，则先进入功能引导页
        if (!isFirstOpen) {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        // 如果不是第一次启动app，则正常显示启动屏
        setContentView(R.layout.splash_activity);
        mTvShimmer = (ShimmerTextView) findViewById(R.id.tv_shimmer);
        mTvInside = (ShimmerTextView) findViewById(R.id.tv_inside);
        ivGg = (ImageView) findViewById(R.id.iv_pic);
        tvJump = (TextView) findViewById(R.id.tv_jump);
        tvJump.setOnClickListener(listener);

        if (shimmer != null && shimmer.isAnimating()) {
            shimmer.cancel();
        } else {
            shimmer = new Shimmer();
            shimmer.start(mTvShimmer);
            shimmer.start(mTvInside);
        }
        final int i = new Random().nextInt(AppConstants.TRANSITION_URLS.length);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ivGg.setVisibility(View.VISIBLE);
                Glide.with(SplashActivity.this)
                        .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494912576482&di=26b063a2d6a7632f593d4491780d142b&imgtype=0&src=http%3A%2F%2Fimglf1.ph.126.net%2FOgFYUZbmKQt_l5sCvubNWQ%3D%3D%2F2032812281904492532.jpg").into(ivGg);
                    timer.schedule(task, 1000, 1000);
            }
        }, 2000);
    }

    PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            switch (v.getId()) {
            case R.id.tv_jump:
                enterHomeActivity();
                timer.cancel();
                break;
            }
        }
    };

    private void enterHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        timer.cancel();
        finish();
    }
}
