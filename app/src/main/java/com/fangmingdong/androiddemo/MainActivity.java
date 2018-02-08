package com.fangmingdong.androiddemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fangmingdong.androiddemo.constainslayoutAnimate.ConstraintLayoutAnimateActivity;
import com.fangmingdong.androiddemo.coordinatorlayout.CoordinatorLayoutActivity;
import com.fangmingdong.androiddemo.monkeyclick.MonkeyClickActivity;
import com.fangmingdong.androiddemo.recyclerViewSideScroll.RecyclerViewSideScrollActivity;
import com.fangmingdong.androiddemo.scrollView.ScrollViewActivity;
import com.fangmingdong.androiddemo.textDraw.TextDrawActivity;
import com.fangmingdong.androiddemo.weixinxiaochengxu.WeiXinActivity;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedOutputStream;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        OverScroller mOverScroller = new OverScroller(this);
//
//        // Start scrolling by providing a starting point and the distance to travel.
//        mOverScroller.startScroll(0, 0,100, 100, 500);




//        try {
//            diskLruCache();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e(TAG, "onCreate: ", e);
//        }

    }

    private void diskLruCache() throws Exception {
        File directory = getCacheDir();
        int appVersion = 1;
        int valueCount = 1;
        long maxSize = 10 * 1024;
        DiskLruCache diskLruCache = DiskLruCache.open(directory, appVersion, valueCount, maxSize);

        DiskLruCache.Editor editor = diskLruCache.edit(String.valueOf(System.currentTimeMillis()));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(editor.newOutputStream(0));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);

        editor.commit();
        diskLruCache.flush();
        diskLruCache.close();





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

    public void scrollView(View view) {
        ScrollViewActivity.actionStart(this);
    }

    public void recyclerViewSideScroller(View view) {
        RecyclerViewSideScrollActivity.actionStart(this);
    }

    public void jobSchedule(View view) {

    }
}
