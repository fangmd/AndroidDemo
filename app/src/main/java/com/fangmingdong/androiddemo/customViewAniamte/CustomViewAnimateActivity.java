package com.fangmingdong.androiddemo.customViewAniamte;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fangmingdong.androiddemo.R;

public class CustomViewAnimateActivity extends AppCompatActivity {

    private static final String TAG = CustomViewAnimateActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view_animate);


        AnimateView animateView = (AnimateView) findViewById(R.id.animateView);

        Log.d(TAG, "onCreate: ");

        new Thread(() -> {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            runOnUiThread(()->{
                animateView.startAnimate();
            });

        }).start();


    }
}
