package com.fangmingdong.androiddemo.customViewAniamte;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.fangmingdong.androiddemo.R;

/**
 * Created by double on 2018/1/21.
 */

public class AnimateView extends View {


    private static final String TAG = AnimateView.class.getSimpleName();

    private String mContent;
    private Paint mPaint;
    private float mDensity;

    private int textSize = 36;  //sp

    private float mProgress;
    private ObjectAnimator mAnimator;


    public AnimateView(Context context) {
        this(context, null);
    }

    public AnimateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mDensity = context.getResources().getDisplayMetrics().density;
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnimateView);
            mContent = typedArray.getString(R.styleable.AnimateView_av_text);
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize * mDensity);
        mPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.d(TAG, "onMeasure: ");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.d(TAG, "onDraw: ");

        // draw background
        canvas.drawRGB(0xff, 0x00, 0x00);

        // draw text
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float y = getHeight() / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2;

        canvas.drawText(mContent + mProgress, 0, y, mPaint);

    }

    public void startAnimate() {
        if (mAnimator == null) {
            mAnimator = ObjectAnimator.ofFloat(this, "progress", 0.5f, 1);
            mAnimator.setDuration(2000);
        }

        if (mAnimator.isStarted()) {
            return;
        }

        mAnimator.start();
    }

    public void stopAnimate() {
        if (mAnimator == null) return;

        mAnimator.cancel();
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        invalidate();

        requestLayout();
    }
}
