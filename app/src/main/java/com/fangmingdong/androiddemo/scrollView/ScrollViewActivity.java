package com.fangmingdong.androiddemo.scrollView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fangmingdong.androiddemo.R;

public class ScrollViewActivity extends AppCompatActivity {

    private CustomScrollView mCSV;

    public static void actionStart(Context context){
        Intent intent = new Intent(context, ScrollViewActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view);

        mCSV = (CustomScrollView) findViewById(R.id.csv);


        new Thread(()->{

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            runOnUiThread(()->{
                mCSV.startAutoScroll();
            });
        }).start();




    }
}
