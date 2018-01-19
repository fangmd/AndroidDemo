package com.fangmingdong.androiddemo.textDraw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fangmingdong.androiddemo.R;

public class TextDrawActivity extends AppCompatActivity {

    private TextDrawView mTDV;

    public static void actionStart(Context context){
        Intent intent = new Intent(context, TextDrawActivity.class);

        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_draw);


        mTDV = (TextDrawView) findViewById(R.id.tdv);

        mTDV.setText("方明东");


    }
}
