package com.fangmingdong.androiddemo.scrollView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.OverScroller;

/**
 * Created by double on 2018/1/22.
 */

public class CustomScrollView extends LinearLayout {

    private static final String TAG = CustomScrollView.class.getSimpleName();
    private OverScroller mOverScroller;

    public CustomScrollView(Context context) {
        this(context, null);
    }

    public CustomScrollView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {

        mOverScroller = new OverScroller(context);
    }

    public void smoothScrollTo(int dstX, int dstY) {
        int scrollX = getScrollX();
        int startY = getScrollY();
        int delta = dstX - scrollX;
        int offsetY = dstY - startY;
        mOverScroller.startScroll(scrollX, startY, delta, offsetY, 1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mOverScroller.computeScrollOffset()) {
            scrollTo(mOverScroller.getCurrX(), mOverScroller.getCurrY());
            postInvalidate();
        }
    }

    public void startAutoScroll() {
        smoothScrollTo(1000, 0);
    }
}
