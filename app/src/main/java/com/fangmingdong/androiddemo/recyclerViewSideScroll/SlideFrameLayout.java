package com.fangmingdong.androiddemo.recyclerViewSideScroll;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.fangmingdong.androiddemo.R;

/**
 * Created by double on 2018/1/24.
 */

public class SlideFrameLayout extends FrameLayout implements View.OnClickListener {

    private static final String TAG = SlideFrameLayout.class.getSimpleName();

    private static final int MOVE_TYPE_LEFT = 0;
    private static final int MOVE_TYPE_RIGHT = 1;
    private static final int MOVE_TYPE_TOP = 2;
    private static final int MOVE_TYPE_BOTTOM = 3;
    private static final int MOVE_TYPE_UNKNOW = -1;


    private static final int STATE_EXPAND = 0;
    private static final int STATE_CLOSE = 1;


    private View mViewSlideBg;
    private float mLastX;
    private float mLastY;
    private int mState;

    public SlideFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public SlideFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        addBgView(context);
    }

    private void addBgView(Context context) {
        mViewSlideBg = LayoutInflater.from(context).inflate(R.layout.slide_bg, this, false);
        View confirm = mViewSlideBg.findViewById(R.id.tv_confirm);
        View cancel = mViewSlideBg.findViewById(R.id.tv_cancel);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);

        addView(mViewSlideBg);
    }

    private boolean isHandleTouchEvent;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = ev.getX();
                mLastY = ev.getY();
                isHandleTouchEvent = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float yDistance = ev.getY() - mLastY;
                float xDistance = ev.getX() - mLastX;
                mLastY = ev.getY();
                mLastX = ev.getX();

                int type = moveType(xDistance, yDistance);
                switch (type) {
                    case MOVE_TYPE_LEFT:
                        isHandleTouchEvent = true;
                        break;
                    case MOVE_TYPE_RIGHT:
                        isHandleTouchEvent = true;
                        break;
                    case MOVE_TYPE_TOP:
                        isHandleTouchEvent = false;
                        break;
                    case MOVE_TYPE_BOTTOM:
                        isHandleTouchEvent = false;
                        break;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                isHandleTouchEvent = false;
                break;
            case MotionEvent.ACTION_UP:
                isHandleTouchEvent = false;
                break;
        }

        return isHandleTouchEvent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                mLastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                float yDistance = event.getY() - mLastY;
                float xDistance = event.getX() - mLastX;
                mLastY = event.getY();
                mLastX = event.getX();

                int type = moveType(xDistance, yDistance);
                switch (type) {
                    case MOVE_TYPE_LEFT:
                        scrollBy(-(int) xDistance, 0);

                        Log.d(TAG, "onTouchEvent: MOVE_TYPE_LEFT");
                        return true;
                    case MOVE_TYPE_RIGHT:
                        scrollBy(-(int) xDistance, 0);

                        Log.d(TAG, "onTouchEvent: MOVE_TYPE_RIGHT");
                        return true;
                    case MOVE_TYPE_TOP:
                    case MOVE_TYPE_BOTTOM:
                        return false;
                }

                break;
            case MotionEvent.ACTION_CANCEL:

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }

    private int moveType(float x, float y) {
        int ret = MOVE_TYPE_UNKNOW;
        Log.d(TAG, "moveType: x=" + x + ", y=" + y);
        if (Math.abs(x) > Math.abs(y)) {
            if (x > 0) {
                ret = MOVE_TYPE_RIGHT;
            } else {
                ret = MOVE_TYPE_LEFT;
            }
        } else {
            if (x > 0) {
                ret = MOVE_TYPE_BOTTOM;
            } else {
                ret = MOVE_TYPE_TOP;
            }
        }
        return ret;
    }


    public void resetState() {
        setState(STATE_CLOSE);
    }

    //    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//
//        View view = getChildAt(0);
//
//
//        super.onLayout(changed, left, top, right, bottom);
//
//
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm:
                Log.d(TAG, "onClick: Confirm");
                break;
            case R.id.tv_cancel:
                Log.d(TAG, "onClick: Cancel");
                break;
        }
    }


    public void setState(int state) {
        mState = state;
        autoScrollLayout();
    }

    private void autoScrollLayout() {
        switch (mState) {
            case STATE_EXPAND:

                break;
            case STATE_CLOSE:

                break;
        }
    }


}
