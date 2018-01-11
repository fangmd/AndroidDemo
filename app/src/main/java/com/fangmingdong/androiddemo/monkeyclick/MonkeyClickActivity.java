package com.fangmingdong.androiddemo.monkeyclick;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fangmingdong.androiddemo.R;

public class MonkeyClickActivity extends AppCompatActivity {

    private Button mBtn;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MonkeyClickActivity.class);

        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monkey_click_activity);


        mBtn = (Button) findViewById(R.id.btn_monkey_click);


        // 0 performClick
//        mBtn.performClick();

        // 1 MotionEvent
//        touch(mBtn, 0, 0);

        // 2
    }

    private void touch(View view, float x, float y) {

        long downTime = SystemClock.uptimeMillis();

        //装疯
        MotionEvent downEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_DOWN, x, y, 0);
        downTime += 1000;

        //卖傻
        MotionEvent upEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_UP, x, y, 0);

        view.onTouchEvent(downEvent);
        view.onTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }


    public void showToast(View view) {
        Toast.makeText(this, "Haha", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ClickService.isRunning()) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent("auto.click");
                    intent.putExtra("flag",1);
                    intent.putExtra("id","btn_monkey_click");

                    sendBroadcast(intent);
                }
            },1500);

        } else {
            showOpenAccessibilityServiceDialog();
        }
    }


    // 2

    /** 显示未开启辅助服务的对话框*/
    private void showOpenAccessibilityServiceDialog() {
//        if(mTipsDialog != null && mTipsDialog.isShowing()) {
//            return;
//        }
//        View view = getLayoutInflater().inflate(R.layout.dialog_tips_layout, null);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openAccessibilityServiceSettings();
//            }
//        });
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("需要开启辅助服务正常使用");
//        builder.setView(view);
//        builder.setPositiveButton("打开辅助服务", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                openAccessibilityServiceSettings();
//            }
//        });
//        mTipsDialog = builder.show();
        openAccessibilityServiceSettings();
    }

    /** 打开辅助服务的设置*/
    private void openAccessibilityServiceSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, "找[AutoClick],然后开启服务", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
