package com.fangmingdong.androiddemo.coordinatorlayout.WangYiYun;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fangmingdong.androiddemo.R;

public class YouDaoMineActivity extends AppCompatActivity {

    public static void actionStart(Context context){
        Intent intent = new Intent(context, YouDaoMineActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wang_yi_yun_mine);
    }
}
