package com.fangmingdong.touchhandler.print;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.fangmingdong.touchhandler.R;

public class PrintEventActivity extends AppCompatActivity {

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
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });

    }


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





//            mTouchView.setX(x);
//            mTouchView.setY(y);

//            mTouchView.setTranslationX(x);
//            mTouchView.setTranslationY(y);


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
