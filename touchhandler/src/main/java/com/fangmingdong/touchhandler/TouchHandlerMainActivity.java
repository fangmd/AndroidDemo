package com.fangmingdong.touchhandler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.fangmingdong.touchhandler.print.PrintEventActivity;

public class TouchHandlerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.touch_handler_main_activity);
    }

    public void printTouchEvent(View view) {
        PrintEventActivity.actionStart(this);
    }


}
