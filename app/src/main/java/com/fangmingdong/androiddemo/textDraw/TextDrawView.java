package com.fangmingdong.androiddemo.textDraw;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.fangmingdong.androiddemo.R;

/**
 * Created by nerc on 2018/1/19.
 */

public class TextDrawView extends View {


    private static final String TAG = TextDrawView.class.getSimpleName();
    private String mText = "default";
    private int textSize = 36;  //sp
    private int mTextColor = Color.BLACK;
    private Paint mPaint;
    private float mDensity;
    private boolean isLiked;

    private int oldNumber;
    private int number;

    public TextDrawView(Context context) {
        this(context, null);
    }

    public TextDrawView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextDrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mDensity = context.getResources().getDisplayMetrics().density;

        initView(context, attrs);
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextDrawView);
            textSize = typedArray.getInt(R.styleable.TextDrawView_tdv_text_size, 36);
            mTextColor = typedArray.getColor(R.styleable.TextDrawView_tdv_text_color, Color.BLACK);
            typedArray.recycle();
        }


        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize * mDensity);
        mPaint.setColor(mTextColor);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked) {
                    minusOne();
                } else {
                    addOne();
                }
                isLiked = !isLiked;
//                listener.OnLikeChanged(isLiked);
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float ascent = mPaint.ascent();
        float descent = mPaint.descent();

        //
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float y = getHeight() / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2;
        //

        canvas.drawRGB(0x56, 0x56, 0x56);

        mPaint.setAlpha(((int) (255 * progress)));
        canvas.drawText(String.valueOf(number), 0, y - (ascent*(1-progress)), mPaint);


        mPaint.setAlpha(((int) (255 * (1 - progress))));
        canvas.drawText(String.valueOf(oldNumber), 0, y + (ascent*(progress)), mPaint);


    }

    //---- measure
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    private int measureHeight(int heightMeasureSpec) {
        int ret = 0;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (specMode) {
            case MeasureSpec.EXACTLY:   // 子控件设置了指定的大小
                ret = MeasureSpec.getSize(heightMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:   //
                float height = -mPaint.ascent() + mPaint.descent();
                ret = (int) height * 3 + getPaddingTop() + getPaddingBottom();
                ret = Math.min(ret, specSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                ret = (int) (mPaint.measureText(String.valueOf(mText)) * 3 + getPaddingTop() + getPaddingBottom());
                break;
        }

        return ret;
    }

    private int measureWidth(int widthMeasureSpec) {
        int ret = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        switch (specMode) {
            case MeasureSpec.EXACTLY:   // 子控件设置了指定的大小
                ret = MeasureSpec.getSize(widthMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:   //
                ret = (int) (mPaint.measureText(String.valueOf(oldNumber*10)) + getPaddingLeft() + getPaddingRight());
                ret = Math.min(ret, specSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                ret = (int) (mPaint.measureText(String.valueOf(oldNumber*10)) + getPaddingLeft() + getPaddingRight());
                break;
        }

        return ret;
    }
    //---- -----------

    private void addOne() {

    }

    private void minusOne() {

    }


    private float progress;

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    public void setText(String text) {
        mText = text;
        requestLayout();
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }


    public void addNumber() {
        oldNumber = number;
        ++number;


        if (number / 10 != oldNumber /10) {
            requestLayout();
        }

        startAnimate();
    }

    private void startAnimate() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "progress", 0, 1);
        animator.setDuration(800);
        animator.start();
    }

}
