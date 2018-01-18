package com.fangmingdong.androiddemo.weixinxiaochengxu.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

/**
 * Created by nerc on 2018/1/11.
 */

public class WeiXinLinearLayout extends LinearLayout {

    private static final String TAG = WeiXinLinearLayout.class.getSimpleName();
    /**
     * 表示是否拦截了触摸事件
     * 是否正在处理触摸事件
     */
    private boolean mIsHandledTouchEvent;
    private float mLastMotionY;
    private float mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    /**
     * 是否允许下拉刷新
     * 默认 true 允许
     */
    private boolean mPullRefreshEnabled = true;
    private float mOffsetRadio = 1f;
    private State mPullDownState;
    private IHeadView mHeaderLayout;
    /**
     * 头部 最大动画处理的滑动距离
     * 在这个滑动距离之内，HeadView 会跟随滑动作出变化
     * 大于这个滑动距离，HeadView 就不会有新的变化
     * 大于这个距离，增加 mOffsetRadio阻尼，
     */
    private int mHeaderListHeight;
    /**
     * 底部，这里没有设置底部布局
     */
    private int mFooterViewHeight;
    /**
     * HeadView 的高度
     */
    private int mHeaderHeight;

    /**
     * 回滚的时间
     */
    private static final int SCROLL_DURATION = 150;
    /**
     * 平滑滑动动画执行
     */
    private SmoothScrollRunnable mSmoothScrollRunnable;


    public WeiXinLinearLayout(Context context) {
        super(context, null);
    }

    public WeiXinLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }


    public WeiXinLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {

        // 得到Header的高度，这个高度需要用这种方式得到，在onLayout方法里面得到的高度始终是0
//        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//
//                int pLeft = getPaddingLeft();
//                int pTop = -mHeaderListHeight;
//                int pRight = getPaddingRight();
//                int pBottom = -mFooterViewHeight;
//                setPadding(pLeft, pTop, pRight, pBottom);
//
//                getViewTreeObserver().removeGlobalOnLayoutListener(this);
//            }
//        });
    }

    //-- -temp
    public void setHeaderLayout(IHeadView headView) {
        mHeaderLayout = headView;


        mHeaderListHeight = (int) (getContext().getResources().getDisplayMetrics().density * 80);
        mHeaderHeight = headView.getContentHeight();

        int pLeft = getPaddingLeft();
        int pTop = -mHeaderListHeight;
        int pRight = getPaddingRight();
        int pBottom = -mHeaderListHeight;
        setPadding(pLeft, pTop, pRight, pBottom);
    }
    //


    /**
     * 拦截触摸事件
     * <p>
     * 在已知子控件不会处理触摸事件的时候可以不处理这个方法
     *
     * @param ev 触摸事件对象
     * @return true： 表示拦截， false：不拦截
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();

        //不拦截 cancel up 事件, 重置拦截状态
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mIsHandledTouchEvent = false;
            return false;
        }

        // 如果不是重新开始触摸且已经判断需要拦截，就一直拦截整套触摸事件
        // 减少判断次数
        if (action != MotionEvent.ACTION_DOWN && mIsHandledTouchEvent) {
            return true;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = ev.getY();
                mIsHandledTouchEvent = false;
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getY() - mLastMotionY;
                final float absDiff = Math.abs(deltaY);
                // 位移差大于mTouchSlop（TouchSlop是系统所能识别出的被认为是滑动的最小距离）
                //这是为了防止快速拖动引发刷新
                if ((absDiff > mTouchSlop)) {
                    mLastMotionY = ev.getY();
                    // 第一个显示出来，Header已经显示或拉下
                    if (isPullRefreshEnabled() && isReadyForPullDown()) {
                        // 1，Math.abs(getScrollY()) > 0：表示当前滑动的偏移量的绝对值大于0，表示当前HeaderView滑出来了或完全
                        // 不可见，存在这样一种case，当正在刷新时并且RefreshableView已经滑到顶部，向上滑动，那么我们期望的结果是
                        // 依然能向上滑动，直到HeaderView完全不可见
                        // 2，deltaY > 0.5f：表示下拉的值大于0.5f
                        mIsHandledTouchEvent = (Math.abs(getScrollYValue()) > 0 || deltaY > 0.5f);
                    }
                }
                break;
            default:
                break;
        }
        return mIsHandledTouchEvent;
    }

    /**
     * 得到当前Y的滚动值
     *
     * @return 滚动值
     */
    private int getScrollYValue() {
        return getScrollY();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean handled = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = event.getY();
                mIsHandledTouchEvent = false;
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = event.getY() - mLastMotionY;
                mLastMotionY = event.getY();
                if (isPullRefreshEnabled() && isReadyForPullDown()) {
                    pullHeaderLayout(deltaY / mOffsetRadio);
                    handled = true;
                } else {
                    mIsHandledTouchEvent = false;
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mIsHandledTouchEvent) {
                    mIsHandledTouchEvent = false;
                    // 当第一个显示出来时
                    if (isReadyForPullDown()) {
                        // 调用刷新
                        if (mPullRefreshEnabled && (mPullDownState == State.RELEASE_TO_REFRESH)) {
                            startRefreshing();
                            handled = true;
                        }
                        resetHeaderLayout();
                    }
                }
                break;

            default:
                break;
        }
        return handled;
    }

    /**
     * 下拉过程处理
     */
    private void pullHeaderLayout(float value) {
//        Log.d(TAG, "pullHeaderLayout: update head value=" + value);
        // 向上滑动，并且当前scrollY为0时，不滑动
        int oldScrollY = getScrollYValue();
        if (value < 0 && (oldScrollY - value) >= 0) {
            setScrollTo(0, 0);
            if (null != mHeaderLayout && 0 != mHeaderHeight) {
                mHeaderLayout.setState(State.RESET);
                mHeaderLayout.onPull(0);
            }
            return;
        }

        // 滑动布局 把 head 显示出来
        setScrollBy(0, -(int) value);

        int scrollY = Math.abs(getScrollYValue());
        if (null != mHeaderLayout && 0 != mHeaderHeight) {
            if (scrollY >= mHeaderListHeight) {
                mHeaderLayout.setState(State.arrivedListHeight);
                setOffsetRadio(3.0f); // 内容列表完全展开后阻尼值变大
            } else {
                setOffsetRadio(1.0f);
            }
            mHeaderLayout.onPull(scrollY);// 将滑动距离实时传给头部，以实现出我们需要的动画
        }

        // 未处于刷新状态，更新箭头
        if (isPullRefreshEnabled() && !isPullRefreshing()) {
            if (scrollY > mHeaderHeight) {
                mPullDownState = State.RELEASE_TO_REFRESH;
            } else {
                mPullDownState = State.PULL_TO_REFRESH;
            }
            mHeaderLayout.setState(mPullDownState);
        }
    }


    /**
     * 重置 Head 状态
     */
    private void resetHeaderLayout() {
        final int scrollY = Math.abs(getScrollYValue());
        final boolean refreshing = isPullRefreshing();

        if (refreshing && scrollY <= mHeaderHeight) {
            mHeaderLayout.setState(State.RESET);
            smoothScrollTo(0);
            return;
        }

        if (refreshing) {
            mHeaderLayout.setState(State.REFRESHING);
            smoothScrollTo(-mHeaderHeight);
        } else {
            mHeaderLayout.setState(State.RESET);
            smoothScrollTo(0);
        }
    }


    private void startRefreshing() {
        mHeaderLayout.setState(State.REFRESHING);
        mPullDownState = State.REFRESHING;

        // 使用接口回调 调用 刷新接口
        if (mRefreshListener != null) {
            mRefreshListener.refresh();
        }
    }


    /**
     * 平滑滚动
     *
     * @param newScrollValue 滚动的值
     */
    private void smoothScrollTo(int newScrollValue) {
        smoothScrollTo(newScrollValue, getSmoothScrollDuration(), 0);
    }

    private long getSmoothScrollDuration() {
        return SCROLL_DURATION;
    }

    /**
     * 平滑滚动
     *
     * @param newScrollValue 滚动的值
     * @param duration       滚动时候
     * @param delayMillis    延迟时间，0代表不延迟
     */
    private void smoothScrollTo(int newScrollValue, long duration, long delayMillis) {
        if (null != mSmoothScrollRunnable) {
            mSmoothScrollRunnable.stop();
        }

        int oldScrollValue = this.getScrollYValue();
        boolean post = (oldScrollValue != newScrollValue);
        if (post) {
            mSmoothScrollRunnable = new SmoothScrollRunnable(oldScrollValue, newScrollValue, duration);
        }

        if (post) {
            if (delayMillis > 0) {
                postDelayed(mSmoothScrollRunnable, delayMillis);
            } else {
                post(mSmoothScrollRunnable);
            }
        }
    }

    /**
     * 判断是否正在下拉刷新
     *
     * @return true正在刷新，否则false
     */
    protected boolean isPullRefreshing() {
        return (mPullDownState == State.REFRESHING);
    }

    /**
     * 设置 阻尼
     *
     * @param v
     */
    private void setOffsetRadio(float v) {
        mOffsetRadio = v;
    }

    /**
     * 设置滚动位置
     *
     * @param x 滚动到的x位置
     * @param y 滚动到的y位置
     */
    private void setScrollTo(int x, int y) {
        scrollTo(x, y);
    }

    /**
     * 设置滚动的偏移
     *
     * @param x 滚动x位置
     * @param y 滚动y位置
     */
    private void setScrollBy(int x, int y) {
        scrollBy(x, y);
    }


    private boolean isReadyForPullDown() {
        return true;
    }

    private boolean isPullRefreshEnabled() {
        return mPullRefreshEnabled;
    }

    public void setPullRefreshEnabled(boolean pullRefreshEnabled) {
        mPullRefreshEnabled = pullRefreshEnabled;
    }


    /**
     * 实现了平滑滚动的 Runnable
     *
     * @author Li Hong
     * @since 2013-8-22
     */
    final class SmoothScrollRunnable implements Runnable {
        /**
         * 动画效果
         */
        private final Interpolator mInterpolator;
        /**
         * 结束Y
         */
        private final int mScrollToY;
        /**
         * 开始Y
         */
        private final int mScrollFromY;
        /**
         * 滑动时间
         */
        private final long mDuration;
        /**
         * 是否继续运行
         */
        private boolean mContinueRunning = true;
        /**
         * 开始时刻
         */
        private long mStartTime = -1;
        /**
         * 当前Y
         */
        private int mCurrentY = -1;

        /**
         * 构造方法
         *
         * @param fromY    开始Y
         * @param toY      结束Y
         * @param duration 动画时间
         */
        public SmoothScrollRunnable(int fromY, int toY, long duration) {
            mScrollFromY = fromY;
            mScrollToY = toY;
            mDuration = duration;
            mInterpolator = new DecelerateInterpolator();
        }

        @Override
        public void run() {
            /**
             * If the duration is 0, we scroll the view to target y directly.
             */
            if (mDuration <= 0) {
                setScrollTo(0, mScrollToY);
                return;
            }

            /**
             * Only set mStartTime if this is the first time we're starting,
             * else actually calculate the Y delta
             */
            if (mStartTime == -1) {
                mStartTime = System.currentTimeMillis();
            } else {

                /**
                 * We do do all calculations in long to reduce software float
                 * calculations. We use 1000 as it gives us good accuracy and
                 * small rounding errors
                 */
                final long oneSecond = 1000;    // SUPPRESS CHECKSTYLE
                long normalizedTime = (oneSecond * (System.currentTimeMillis() - mStartTime)) / mDuration;
                normalizedTime = Math.max(Math.min(normalizedTime, oneSecond), 0);

                final int deltaY = Math.round((mScrollFromY - mScrollToY)
                        * mInterpolator.getInterpolation(normalizedTime / (float) oneSecond));
                mCurrentY = mScrollFromY - deltaY;

                setScrollTo(0, mCurrentY);
            }

            // If we're not at the target Y, keep going...
            if (mContinueRunning && mScrollToY != mCurrentY) {
                WeiXinLinearLayout.this.postDelayed(this, 16);// SUPPRESS CHECKSTYLE
            }
        }

        /**
         * 停止滑动
         */
        public void stop() {
            mContinueRunning = false;
            removeCallbacks(this);
        }
    }


    // 下拉刷新接口
    private IWeiXinLinearLayoutListener mRefreshListener;

    public void setRefreshListener(IWeiXinLinearLayoutListener refreshListener) {
        mRefreshListener = refreshListener;
    }
}
