package com.fangmingdong.androiddemo.weixinxiaochengxu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.fangmingdong.androiddemo.R;
import com.fangmingdong.androiddemo.weixinxiaochengxu.widgets.IWeiXinLinearLayoutListener;
import com.fangmingdong.androiddemo.weixinxiaochengxu.widgets.TextViewHeadView;
import com.fangmingdong.androiddemo.weixinxiaochengxu.widgets.WeiXinLinearLayout;

/**
 * 仿 微信下拉显示小程序的效果
 */
public class WeiXinActivity extends AppCompatActivity {

    private static final String TAG = WeiXinActivity.class.getSimpleName();
    private WeiXinLinearLayout mLLRoot;
    private TextViewHeadView mTvHead;
    private View mViewBody;

    public static void actionStart(Context context){
        Intent intent = new Intent(context, WeiXinActivity.class);

        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wei_xin);

        mLLRoot = (WeiXinLinearLayout) findViewById(R.id.ll_root);
        mTvHead = (TextViewHeadView) findViewById(R.id.tv_head);
        mViewBody = findViewById(R.id.body);


        mLLRoot.setHeaderLayout(mTvHead);

        mLLRoot.setPullRefreshEnabled(true);
        mLLRoot.setRefreshListener(new IWeiXinLinearLayoutListener() {
            @Override
            public void refresh() {
                Log.d(TAG, "refresh: ");
            }
        });




    }
}
