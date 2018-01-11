package com.fangmingdong.touchhandler.print;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.TextView;

import com.fangmingdong.touchhandler.R;

public class PrintEventActivity extends AppCompatActivity {

    private static final String TAG = PrintEventActivity.class.getSimpleName();
    private TextView mTv;
    private View mTouchView;
    private GestureDetectorCompat mDetector;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PrintEventActivity.class);

        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.touch_handler_print_event_activity);

        mTv = (TextView) findViewById(R.id.tv_print);

        mTouchView = findViewById(R.id.view_print_touch);
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        mTouchView.setOnTouchListener(new View.OnTouchListener() {

            private float mStartY;
            private float mStartX;

            private float mDownViewX;
            private float mDownViewY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (mIsAnimating) {
                    return false;
                }

//                mDetector.onTouchEvent(event);

                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mDownViewX = mTouchView.getX();
                        mDownViewY = mTouchView.getY();

                        mStartX = event.getRawX();
                        mStartY = event.getRawY();
                        Log.d(TAG, "onTouch: ACTION_DOWN   getRawX=" + mStartX + ",getRawY=" + mStartY);
                        Log.d(TAG, "onTouch: ACTION_DOWN   x=" + mDownViewX + ",y=" + mDownViewY);
                        break;
                    case MotionEvent.ACTION_MOVE:

                        float newX = event.getRawX();
                        float newY = event.getRawY();
                        float offsetX = newX - mStartX;
                        float offsetY = newY - mStartY;

                        mTouchView.setX(mTouchView.getX() + offsetX);
                        mTouchView.setY(mTouchView.getY() + offsetY);

                        Log.d(TAG, "onTouch: ACTION_MOVE   offsetX=" + offsetX +
                                ",offsetY=" + offsetY);

                        mStartX = newX;
                        mStartY = newY;

                        break;
                    case MotionEvent.ACTION_UP:

                        //
                        autoScrollTo(mDownViewX, mDownViewY);

                        break;
                    case MotionEvent.ACTION_CANCEL:

                        break;
                }
                return true;
            }
        });
    }

    private boolean mIsAnimating;
    private void autoScrollTo(float x, float y) {
        Log.d(TAG, "autoScrollTo: x=" + x + ", y=" + y + ",, mTouchView.getX()" + mTouchView.getX() +
                ",,mTouchView.getY()" + mTouchView.getY());
        ViewPropertyAnimator animate = mTouchView.animate();
        animate.translationXBy(x - mTouchView.getX());
        animate.translationYBy(y - mTouchView.getY());
        animate.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mIsAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsAnimating = false;
                Log.d(TAG, "onAnimationEnd: x=" + mTouchView.getX() + ", y=" + mTouchView.getY());
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animate.setDuration(800);
        animate.start();
    }

    //
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final String DEBUG_TAG = "Gestures";


        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            if (distanceX < 4 && distanceY < 4) {
                return true;
            }

            float startX = mTouchView.getX();
            float startY = mTouchView.getY();

            int x = ((int) (startX - ((int) distanceX)));
            int y = ((int) (startY - ((int) distanceY)));

            mTouchView.setX(x);
            mTouchView.setY(y);


            mTv.setText("distanceX=" + distanceX + ", distanceY=" + distanceY + ", x=" + x + "&&y=" + y);

            Log.d(DEBUG_TAG, "onScroll: distanceX=" + distanceX + ", distanceY=" + distanceY + ", x=" + x + "&&y=" + y);
            return true;
        }

//        @Override
//        public boolean onDown(MotionEvent event) {
//            Log.d(DEBUG_TAG, "onDown: " + event.toString());
//            return true;
//        }
//
//
//        @Override
//        public boolean onFling(MotionEvent event1, MotionEvent event2,
//                               float velocityX, float velocityY) {
//            Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
//            return true;
//        }
    }


}
