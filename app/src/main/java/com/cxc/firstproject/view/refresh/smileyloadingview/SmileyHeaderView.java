package com.cxc.firstproject.view.refresh.smileyloadingview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.cxc.firstproject.R;
import com.cxc.firstproject.view.refresh.PtrFrameLayout;
import com.cxc.firstproject.view.refresh.PtrUIHandler;
import com.cxc.firstproject.view.refresh.indicator.PtrIndicator;

/**
 * Created by 2144 on 2016/8/26.
 */
public class SmileyHeaderView extends LinearLayout implements PtrUIHandler {
    public SmileyHeaderView(Context context) {
        this(context, null);
    }

    public SmileyHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmileyHeaderView(Context context, AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SmileyHeaderView(Context context, AttributeSet attrs,
            int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private SmileyLoadingView loadingView;
    private Scroller mScroller;
    private TextView sucess;

    private void init(Context context) {
        View contentView = LayoutInflater.from(context)
                .inflate(R.layout.smiley_headerview, this);
        loadingView = (SmileyLoadingView) contentView
                .findViewById(R.id.loading_view);
        sucess = (TextView) contentView.findViewById(R.id.tv_sucess);
        mScroller = new Scroller(getContext(), new LinearInterpolator());
    }

    public void setText(String text){
        sucess.setText(text);
    }

    private void start() {
        if (!refreshing) {
            return;
        }
        loadingView.mRunning = true;
        if (!hasHeaderMove) {
            mAngle = 90;
        }
        int duration = (int) ((720.0f + 2 * 90 - mAngle) * 2000
                / (720.0f + 90));
        mScroller.startScroll(mAngle, 0, (int) (720.0f + 2 * 90 - mAngle), 0,
                duration);
        post(mRunnable);
    }

    private boolean hasHeaderMove = false;

    private AnimalRunnable mRunnable = new AnimalRunnable();

    @Override
    public void onUIReset(PtrFrameLayout frame) {
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        sucess.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        finished = false;
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        refreshing = true;
        start();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        refreshing = false;
        finished = true;
        mScroller.forceFinished(true);
        removeCallbacks(mRunnable);
        loadingView.smile(360 + 180);
        hasHeaderMove = false;
        loadingView.setVisibility(View.GONE);
        sucess.setVisibility(View.VISIBLE);
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch,
            byte status, PtrIndicator ptrIndicator) {

        float headerMovePercent = ptrIndicator.getCurrentPercent();
        if (finished || refreshing)
            return;
        hasHeaderMove = true;
        if (headerMovePercent <= 1) {
            mAngle = (int) ((360 + 180 - 90) * headerMovePercent + 90);
            loadingView.smile(mAngle);
        }
    }

    public class AnimalRunnable implements Runnable {

        @Override
        public void run() {
            int curX = mScroller.getCurrX();
            if (curX != 0)
                loadingView.smile(curX);
            if (mScroller.computeScrollOffset()) {
                post(this);
            } else {
                mAngle = 90;
                loadingView.mRunning = false;
                start();
            }
        }
    }

    private boolean finished = false;
    private boolean refreshing = false;
    
    private int mAngle;
}
