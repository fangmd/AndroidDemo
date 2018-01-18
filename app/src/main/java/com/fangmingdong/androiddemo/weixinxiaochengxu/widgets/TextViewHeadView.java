package com.fangmingdong.androiddemo.weixinxiaochengxu.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by nerc on 2018/1/11.
 */

public class TextViewHeadView extends android.support.v7.widget.AppCompatTextView implements IHeadView {

    private static final String TAG = TextViewHeadView.class.getSimpleName();
    private State mState;
    private float mValue;

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
        mValue = value;
        changeUI();
    }

    @Override
    public void setState(State state) {
        mState = state;
        changeUI();
        Log.d(TAG, "setState: " + state);
    }

    private void changeUI() {
        setText("State" + mState + "Head:" + mValue);
    }

    @Override
    public int getContentHeight() {
        return (int) (getContext().getResources().getDisplayMetrics().density * 80);
    }
}
