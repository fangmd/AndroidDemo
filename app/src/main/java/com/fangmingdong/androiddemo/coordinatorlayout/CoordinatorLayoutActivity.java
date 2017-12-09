package com.fangmingdong.androiddemo.coordinatorlayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fangmingdong.androiddemo.R;
import com.fangmingdong.androiddemo.coordinatorlayout.WangYiYun.YouDaoMineActivity;

public class CoordinatorLayoutActivity extends AppCompatActivity {


    public static void actionStart(Context context) {
        Intent intent = new Intent(context, CoordinatorLayoutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coordinator_layout_activity);
    }

    public void toYouDaoMine(View view) {
        YouDaoMineActivity.actionStart(this);
    }
}
