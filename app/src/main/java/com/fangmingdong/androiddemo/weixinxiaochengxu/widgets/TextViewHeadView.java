package com.fangmingdong.androiddemo.weixinxiaochengxu.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by nerc on 2018/1/11.
 */

public class TextViewHeadView extends android.support.v7.widget.AppCompatTextView implements IHeadView {

    private State mState;

    public TextViewHeadView(Context context) {
        super(context);
    }

    public TextViewHeadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewHeadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onPull(float value) {
        setText("Head:" + value);
    }

    @Override
    public void setState(State state) {
        mState = state;
    }

    @Override
    public int getContentHeight() {
        return getContext().getResources().getDisplayMetrics().densityDpi * 80;
    }
}
