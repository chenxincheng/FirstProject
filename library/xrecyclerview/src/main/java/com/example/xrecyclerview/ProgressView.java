package com.example.xrecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 作者：chenxincheng on 2017/6/1. 邮件：chenxincheng@xwtec.cn
 */

public class ProgressView extends View {

    private Paint paint;

    /**
     * 圆环的厚度
     */
    private int roundWith;

    /**
     * 圆环颜色
     * 
     * @param context
     */
    private int color;

    private int roundProgressColor;
    /**
     * 最大进度
     */
    private int max = 90;
    /**
     * 当前进度
     */
    private int progress;
    public ProgressView(Context context) {
        super(context);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.ProgressRoundView);
        roundWith = array.getInteger(R.styleable.ProgressRoundView_round_width,
                10);
        color = array.getColor(R.styleable.ProgressRoundView_round_color,
                Color.BLUE);
        roundProgressColor = array.getColor(R.styleable.ProgressRoundView_round_progress_color,Color.GREEN);
        array.recycle();

    }

    public ProgressView(Context context, @Nullable AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int center = getWidth() / 2;
        int innerCircle = center - roundWith / 2;
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(roundWith);
        paint.setColor(color);
        canvas.drawCircle(center, center, innerCircle, this.paint);

        paint.setStrokeWidth(roundWith); //设置圆环的宽度
        paint.setColor(roundProgressColor);
        RectF oval = new RectF(center - innerCircle, center - innerCircle, center
                + innerCircle, center + innerCircle);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(oval, 0, 360 * progress / max, false, paint);

        super.onDraw(canvas);

    }
    /**
     * 获取进度.需要同步
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if(progress < 0){
            throw new IllegalArgumentException("progress not less than 0");
        }
        if(progress > max){
            progress = max;
        }
        if(progress <= max){
            this.progress = progress;
            postInvalidate();
        }

    }

}
