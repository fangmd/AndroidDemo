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
import android.widget.RelativeLayout;

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


    private int mExpandScrollX;
    private int mCloseScrollX = 0;


    private View mViewSlideBg;
    private float mLastX;
    private float mLastY;
    private int mState;
    private View mConfirm;
    private View mCancel;
    private RelativeLayout mRlContent;

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
        mConfirm = mViewSlideBg.findViewById(R.id.tv_confirm);
        mCancel = mViewSlideBg.findViewById(R.id.tv_cancel);
        mConfirm.setOnClickListener(this);
        mCancel.setOnClickListener(this);

        addView(mViewSlideBg);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mRlContent = ((RelativeLayout)findViewById(R.id.rl_content));
        mExpandScrollX = mConfirm.getMeasuredWidth() + mCancel.getMeasuredWidth();
    }

    private boolean isHandleTouchEvent;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = ev.getX();
                mLastY = ev.getY();
                return false;
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

        if (isHandleTouchEvent) {
            requestDisallowInterceptTouchEvent(true);
        } else {
            requestDisallowInterceptTouchEvent(false);
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

                        mRlContent.scrollBy(-(int) xDistance, 0);
                        setState(STATE_EXPAND);
                        Log.d(TAG, "onTouchEvent: MOVE_TYPE_LEFT");
                        return true;
                    case MOVE_TYPE_RIGHT:
                        int scrollX = mRlContent.getScrollX();
                        if (scrollX == 0) {
                            // close 状态，不能向右滑动
                            return true;
                        }

                        mRlContent.scrollBy(-(int) xDistance, 0);
                        setState(STATE_CLOSE);

                        Log.d(TAG, "onTouchEvent: MOVE_TYPE_RIGHT");
                        return true;
                    case MOVE_TYPE_TOP:
                    case MOVE_TYPE_BOTTOM:
                        return false;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                autoScrollLayout();
                break;
            case MotionEvent.ACTION_UP:
                autoScrollLayout();
                break;
        }
        return true;
    }


    private int moveType(float x, float y) {
        int ret = MOVE_TYPE_UNKNOW;
//        Log.d(TAG, "moveType: x=" + x + ", y=" + y);
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
        setStateAndScroll(STATE_CLOSE);
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
    }

    public void setStateAndScroll(int state) {
        mState = state;
        autoScrollLayout();
    }

    private void autoScrollLayout() {
        switch (mState) {
            case STATE_EXPAND:
                if (mRlContent != null) {
                    mRlContent.scrollTo(mExpandScrollX, 0);
                }
                break;
            case STATE_CLOSE:
                if (mRlContent != null) {
                    mRlContent.scrollTo(mCloseScrollX, 0);
                }
                break;
        }
    }



    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        Log.d(TAG, "onAttachedToWindow: ");
    }

    /**
     * 在 RecyclerView 中使用这个布局的时候
     * 当 Item 滑出屏幕 重置 Item 的状态，防止 Item 复用导致视图错误
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        resetState();
        Log.d(TAG, "onDetachedFromWindow: ");
    }
}
