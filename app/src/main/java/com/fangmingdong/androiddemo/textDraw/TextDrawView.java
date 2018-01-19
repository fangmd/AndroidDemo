package com.fangmingdong.androiddemo.textDraw;

import android.content.Context;
import android.content.res.TypedArray;
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


    private String mText="default";
    private int textSize = 13;  //sp
    private int mTextColor = Color.BLACK;
    private Paint mPaint;
    private float mDensity;
    private boolean isLiked;

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
            textSize = typedArray.getInt(R.styleable.TextDrawView_tdv_text_size, 13);
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
                ret = (int) (mPaint.measureText(String.valueOf(mText)) + getPaddingTop() + getPaddingBottom());
                ret = Math.min(ret, specSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                ret = (int) (mPaint.measureText(String.valueOf(mText)) + getPaddingTop() + getPaddingBottom());
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
                ret = (int) (mPaint.measureText(String.valueOf(mText)) + getPaddingLeft() + getPaddingRight());
                ret = Math.min(ret, specSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                ret = (int) (mPaint.measureText(String.valueOf(mText)) + getPaddingLeft() + getPaddingRight());
                break;
        }

        return ret;
    }

    private void addOne() {

    }

    private void minusOne() {

    }


    public void setText(String text) {
        mText = text;
        invalidate();
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }


}
