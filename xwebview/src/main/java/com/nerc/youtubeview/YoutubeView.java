package com.nerc.youtubeview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by nerc on 2017/12/1.
 */

public class YoutubeView extends LinearLayout {


    private View mVideoView;
    // video类的包装类，用于属性动画
    private VideoViewWrapper mVideoWrapper;

    public YoutubeView(Context context) {
        this(context, null);
    }

    public YoutubeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YoutubeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initView();
    }

    private void initView() {
        mVideoView = findViewById(R.id.videoView);
        //初始化包装类
        mVideoWrapper = new VideoViewWrapper();

        mVideoView.setOnTouchListener(new VideoTouchListener());
    }

    private class VideoTouchListener implements View.OnTouchListener {

        private VelocityTracker tracker;

        //保存上一个滑动事件手指的坐标
        private int mLastY;
        private int mLastX;
        //刚触摸时手指的坐标
        private int mDownY;
        private int mDownX;


        private int dy;//和上一次滑动的差值 设置为全局变量是因为 UP里也要使用

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            int rawX = ((int) event.getRawX());
            int rawY = ((int) event.getRawY());
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    tracker = VelocityTracker.obtain();
                    mDownY = (int) event.getRawY();
                    mDownX = (int) event.getRawX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    tracker.addMovement(event);
                    dy = rawY - mLastY; //和上一次滑动的差值

                    int dx = rawX - mLastX;

                    int newMarY = mVideoWrapper.getMarginTop() + dy; //新的marginTop值
                    int newMarX = mVideoWrapper.getMarginRight() - dx;//新的marginRight值
                    int dDownY = rawY - mDownY;
                    int dDownX = rawX - mDownX; // 从点击点开始产生的的差值


                    break;
                case MotionEvent.ACTION_UP:

                    break;
                case MotionEvent.ACTION_CANCEL:

                    break;
            }

            mLastY = rawY;
            mLastX = rawX;
            return true;
        }

    }


    private class VideoViewWrapper {


        private final LinearLayout.LayoutParams mLayoutParams;

        public VideoViewWrapper() {
            mLayoutParams = ((LayoutParams) mVideoView.getLayoutParams());

        }

        void setMarginTop(int m) {
            mLayoutParams.topMargin = m;
            mVideoView.setLayoutParams(mLayoutParams);
        }

        int getMarginTop() {
            return mLayoutParams.topMargin;
        }

        void setMarginRight(int mr) {
            mLayoutParams.rightMargin = mr;
            mVideoView.setLayoutParams(mLayoutParams);
        }

        int getMarginRight() {
            return mLayoutParams.rightMargin;
        }

    }
}
