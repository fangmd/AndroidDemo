package com.nerc.youtubeview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by nerc on 2017/12/1.
 */

public class YoutubeView extends LinearLayout {


    private static final String TAG = YoutubeView.class.getSimpleName();

    // 可拖动的videoView 和下方的详情View
    private View mVideoView;
    private View mDetailView;

    // video类的包装类，用于属性动画
    private VideoViewWrapper mVideoWrapper;

    //1f为初始状态，0.5f或0.25f(横屏时)为最小状态
    private float mNowStateScale;
    //最小的缩放比例
    private float MIN_RATIO = 0.5f;

    //是否可以横滑删除
    private boolean mCanHide;

    //滑动区间,取值为是videoView最小化时距离屏幕顶端的高度
    private float mMaxScrollY;

    //是否是第一次Measure，用于获取播放器初始宽高
    private boolean mIsFirstMeasure = true;

    //VideoView初始宽高
    private int originalWidth;
    private int originalHeight;

    //最小时距离屏幕右边以及下边的 DP值 初始化时会转化为PX
    private static final int MARGIN_DP = 12;

    //播放器比例 需要通过这个值 和 屏幕宽度（播放器宽度） 获取 videoView 的高度
    private static final float VIDEO_RATIO = 16f / 9f;

    private int mMarginPx;


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

        mDetailView = getChildAt(1);

        initView();
    }

    private void initView() {
        mVideoView = findViewById(R.id.videoView);
        //初始化包装类
        mVideoWrapper = new VideoViewWrapper();

        //DP To PX
        mMarginPx = MARGIN_DP * (getContext().getResources().getDisplayMetrics().densityDpi / 160);

        //当前缩放比例
        mNowStateScale = 1f;

        //如果是横屏则最小化比例为0.25f
        if (mVideoView.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            MIN_RATIO = 0.25f;
        }

        originalWidth = mVideoView.getContext().getResources().getDisplayMetrics().widthPixels;
        originalHeight = (int) (originalWidth / VIDEO_RATIO);

        ViewGroup.LayoutParams lp = mVideoView.getLayoutParams();
        lp.width = originalWidth;
        lp.height = originalHeight;
        mVideoView.setLayoutParams(lp);

        mVideoView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        mVideoView.setOnTouchListener(new VideoTouchListener());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mIsFirstMeasure) {
            //滑动区间,取值为是videoView最小化时距离屏幕顶端的高度 也就是最小化时的marginTop
            int heightPixels = getContext().getResources().getDisplayMetrics().heightPixels;
            int measuredHeight = this.getMeasuredHeight(); //TODO: getMeasuredHeight 高度错误
            mMaxScrollY = heightPixels - MIN_RATIO * originalHeight - mMarginPx;
            mIsFirstMeasure = false;
        }
    }

    private class VideoTouchListener implements View.OnTouchListener {

        private VelocityTracker tracker;

        //保存上一个滑动事件手指的坐标
        private int mLastY;
        private int mLastX;
        //刚触摸时手指的坐标
        private int mDownY;
        private int mDownX;

        private boolean mIsClick;

        private int mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();


        private int dy;//和上一次滑动的差值 设置为全局变量是因为 UP里也要使用

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            int rawX = ((int) event.getRawX());
            int rawY = ((int) event.getRawY());
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mIsClick = true;

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

                    if (Math.abs(dDownX) > mTouchSlop || Math.abs(dDownY) > mTouchSlop) {
                        mIsClick = false;

                        if (Math.abs(dDownX) > Math.abs(dDownY) && mCanHide) {
                            //如果X>Y 且能滑动关闭, 则动态设置水平偏移量。
                            mVideoWrapper.setMarginRight(newMarX);
//                            mVideoWrapper.setMarginLeft(newMarX);
                        } else {
                            updateVideoView(newMarY); //否则通过新的marginTop的值更新大小
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (mIsClick) {
                        if (mNowStateScale == 1f && mCallback != null) {
                            //单击事件回调
                            mCallback.onVideoClick();
                        } else {
                            goMax();
                        }
                        break;
                    }

                    //
                    tracker.computeCurrentVelocity(100);
                    float yVelocity = Math.abs(tracker.getYVelocity());
                    tracker.clear();
                    tracker.recycle();
                    //----

                    if (mCanHide) {
                        //速度大于一定值或者滑动的距离超过了最小化时的宽度，则进行隐藏，否则保持最小状态。
                        if (yVelocity > mTouchSlop || Math.abs(mVideoWrapper.getMarginRight()) > MIN_RATIO * originalWidth) {
                            dismissView();
                        } else {
                            goMin();
                        }
                    } else {
                        confirmState(yVelocity, dy);//确定状态。
                    }

                    break;
                case MotionEvent.ACTION_CANCEL:

                    break;
            }

            mLastY = rawY;
            mLastX = rawX;
            return true;
        }

    }

    private void updateVideoView(int newMarY) {
        Log.d(TAG, "updateVideoView: " + newMarY);

        //如果当前状态是最小化，先把我们的的布局宽高设置为MATCH_PARENT
        if (mNowStateScale == MIN_RATIO) {
            ViewGroup.LayoutParams params = getLayoutParams();
            params.width = -1;  // ViewGroup.LayoutParams.MATCH_PARENT = -1
            params.height = -1;
            setLayoutParams(params);
        }

        mCanHide = false;

        if (newMarY > mMaxScrollY) {
            newMarY = ((int) mMaxScrollY);
        }

        if (newMarY < 0) {
            newMarY = 0;
        }

        // 视频View高度的百分比100% - 0%
        float marginPercent = (mMaxScrollY - newMarY) / mMaxScrollY;
        //视频View对应的大小的百分比 100% - 50%或25%
        float videoSizePercent = MIN_RATIO + (1f - MIN_RATIO) * marginPercent;

        //设置宽高
        mVideoWrapper.setWidth(originalWidth * videoSizePercent);
        mVideoWrapper.setHeight(originalHeight * videoSizePercent);

        //设置下方详情View的透明度
        mDetailView.setAlpha(marginPercent);
//        this.getBackground().setAlpha((int) (marginPercent * 255));


        int mr = (int) ((1f - marginPercent) * mMarginPx); //VideoView右边和详情View 上方的margin
        mVideoWrapper.setZ(mr / 2);//这个是Z轴的值，悬浮效果

        mVideoWrapper.setMarginTop(newMarY);
        mVideoWrapper.setMarginRight(mr);
        mVideoWrapper.setDetailMargin(mr);
    }

    private void confirmState(float v, int dy) { //dy用于判断是否反方向滑动了
        //如果手指抬起时宽度达到一定值 或者 速度达到一定值 则改变状态
        if (mNowStateScale == 1f) {
            if (mVideoView.getWidth() <= originalWidth * 0.75f || (v > 15 && dy > 0)) {
                goMin();
            } else
                goMax();
        } else {
            if (mVideoView.getWidth() >= originalWidth * 0.75f || (v > 15 && dy < 0)) {
                goMax();
            } else
                goMin();
        }
    }


    private void dismissView() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(mVideoView, "alpha", 1f, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(INVISIBLE);
                mVideoView.setAlpha(1f);
            }
        });
        anim.setDuration(300).start();

        if (mCallback != null) {
            mCallback.onVideoViewHide();
        }
    }

    //----- to state
    public void goMax() {
        Log.d(TAG, "goMax: ");
        if (mNowStateScale == MIN_RATIO) {
            ViewGroup.LayoutParams params = getLayoutParams();
            params.width = -1;
            params.height = -1;
            setLayoutParams(params);
        }

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(mVideoWrapper, "width", mVideoWrapper.getWidth(), originalWidth),
                ObjectAnimator.ofFloat(mVideoWrapper, "height", mVideoWrapper.getHeight(), originalHeight),
                ObjectAnimator.ofInt(mVideoWrapper, "marginTop", mVideoWrapper.getMarginTop(), 0),
                ObjectAnimator.ofInt(mVideoWrapper, "marginRight", mVideoWrapper.getMarginRight(), 0),
                ObjectAnimator.ofInt(mVideoWrapper, "detailMargin", mVideoWrapper.getDetailMargin(), 0),
                ObjectAnimator.ofFloat(mVideoWrapper, "z", mVideoWrapper.getZ(), 0),
                ObjectAnimator.ofFloat(mDetailView, "alpha", mDetailView.getAlpha(), 1f)//,
//                ObjectAnimator.ofInt(this.getBackground(), "alpha", this.getBackground().getAlpha(), 255)
        );
        set.setDuration(200).start();
        mNowStateScale = 1.0f;
        mCanHide = false;
    }

    public void goMin() {
        Log.d(TAG, "goMin: ");
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(mVideoWrapper, "width", mVideoWrapper.getWidth(), originalWidth * MIN_RATIO),
                ObjectAnimator.ofFloat(mVideoWrapper, "height", mVideoWrapper.getHeight(), originalHeight * MIN_RATIO),
                ObjectAnimator.ofInt(mVideoWrapper, "marginTop", mVideoWrapper.getMarginTop(), (int) mMaxScrollY),
                ObjectAnimator.ofInt(mVideoWrapper, "marginRight", mVideoWrapper.getMarginRight(), mMarginPx),
                ObjectAnimator.ofInt(mVideoWrapper, "detailMargin", mVideoWrapper.getDetailMargin(), mMarginPx),
                ObjectAnimator.ofFloat(mVideoWrapper, "z", mVideoWrapper.getZ(), mMarginPx / 2),

                ObjectAnimator.ofFloat(mDetailView, "alpha", mDetailView.getAlpha(), 0)
        );
        //ObjectAnimator.ofInt(this.getBackground(), "alpha", this.getBackground().getAlpha(), 0),
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCanHide = true;

                ViewGroup.LayoutParams p = getLayoutParams();
                p.width = -2;
                p.height = -2;
                setLayoutParams(p);

                mNowStateScale = MIN_RATIO;
            }
        });
        set.setDuration(200).start();
    }
    // - -- -- - - -- - - - -- - - - ---- -- -


    private class VideoViewWrapper {


        private final LinearLayout.LayoutParams mLayoutParams;
        private LinearLayout.LayoutParams detailParams;

        public VideoViewWrapper() {
            mLayoutParams = ((LayoutParams) mVideoView.getLayoutParams());
            mLayoutParams.gravity = Gravity.END;
            detailParams = (LinearLayout.LayoutParams) mDetailView.getLayoutParams();
        }


        int getWidth() {
            return mLayoutParams.width < 0 ? originalWidth : mLayoutParams.width;
        }

        int getHeight() {
            return mLayoutParams.height < 0 ? originalHeight : mLayoutParams.height;
        }

        void setWidth(float width) {
            if (width == originalWidth) {
                mLayoutParams.width = MATCH_PARENT;
                mLayoutParams.setMargins(0, 0, 0, 0);
            } else
                mLayoutParams.width = (int) width;

            mVideoView.setLayoutParams(mLayoutParams);
        }

        void setHeight(float height) {
            mLayoutParams.height = (int) height;
            mVideoView.setLayoutParams(mLayoutParams);
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

        void setZ(float z) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mVideoView.setTranslationZ(z);
            }

        }

        float getZ() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return mVideoView.getTranslationZ();
            } else {
                return 0;
            }
        }

        void setDetailMargin(int t) {
            detailParams.topMargin = t;
            mDetailView.setLayoutParams(detailParams);
        }

        int getDetailMargin() {
            return detailParams.topMargin;
        }

        void setMarginLeft(int ml) {
            mLayoutParams.leftMargin = ml;
            mVideoView.setLayoutParams(mLayoutParams);
        }

        int getMarginLeft(int ml){
            return mLayoutParams.leftMargin;
        }

    }

    //单击以及消失时的回调
    private Callback mCallback;

    interface Callback {
        void onVideoViewHide();

        void onVideoClick();
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }
    //--------
}
