package com.nerc.youtubeview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by nerc on 2017/12/1.
 */

public class YoutubeView extends LinearLayout {


    private View mVideoView;

    public YoutubeView(Context context) {
        this(context, null);
    }

    public YoutubeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YoutubeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);



    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initView();
    }

    private void initView() {
        mVideoView = findViewById(R.id.videoView);
        mVideoView.setOnTouchListener(new VideoTouchListener());
    }

    private class VideoTouchListener implements View.OnTouchListener {


        @Override
        public boolean onTouch(View v, MotionEvent event) {


            return false;
        }

    }


    private class VideoViewWrapper{


        private final ViewGroup.LayoutParams mLayoutParams;

        public VideoViewWrapper() {
            mLayoutParams = mVideoView.getLayoutParams();

        }


    }
}
