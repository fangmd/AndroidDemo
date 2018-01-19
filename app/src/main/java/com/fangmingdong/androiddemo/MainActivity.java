package com.fangmingdong.androiddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.fangmingdong.androiddemo.constainslayoutAnimate.ConstraintLayoutAnimateActivity;
import com.fangmingdong.androiddemo.coordinatorlayout.CoordinatorLayoutActivity;
import com.fangmingdong.androiddemo.monkeyclick.MonkeyClickActivity;
import com.fangmingdong.androiddemo.textDraw.TextDrawActivity;
import com.fangmingdong.androiddemo.weixinxiaochengxu.WeiXinActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toCoordinatorLayout(View view) {
        CoordinatorLayoutActivity.actionStart(this);
    }

    public void toMonkeyClick(View view) {
        MonkeyClickActivity.actionStart(this);
    }

    public void toConstraintLayoutAnimate(View view) {
        ConstraintLayoutAnimateActivity.actionStart(this);
    }

    public void weixin(View view) {
        WeiXinActivity.actionStart(this);
    }


    public void toTextDraw(View view) {
        TextDrawActivity.actionStart(this);
    }
}
