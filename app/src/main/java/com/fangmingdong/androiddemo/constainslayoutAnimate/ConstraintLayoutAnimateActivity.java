package com.fangmingdong.androiddemo.constainslayoutAnimate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.fangmingdong.androiddemo.R;

public class ConstraintLayoutAnimateActivity extends AppCompatActivity {

    private static final String TAG = ConstraintLayoutAnimateActivity.class.getSimpleName();
    private ConstraintLayout mRoot;
    private boolean mChanged;
    private ConstraintSet mConstraintSet1;
    private ConstraintSet mConstraintSet2;
    private int mWidthMax;
    private int mWidthMin;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ConstraintLayoutAnimateActivity.class);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constaint_layout_animate);

        mRoot = (ConstraintLayout) findViewById(R.id.c_animate_root);

        mConstraintSet1 = new ConstraintSet();
        mConstraintSet1.clone(mRoot);
        mConstraintSet2 = new ConstraintSet();
        mConstraintSet2.clone(this, R.layout.activity_constaint_layout_animate_small);

        mWidthMax = getResources().getDisplayMetrics().widthPixels;
        mWidthMin = getResources().getDisplayMetrics().widthPixels * 2 / 3;
    }

    public void change(View view) {
        TransitionManager.beginDelayedTransition(mRoot);

        if (mChanged) {
            mConstraintSet2.applyTo(mRoot);
        } else {
            mConstraintSet1.applyTo(mRoot);
        }

        mChanged = !mChanged;
    }

    public void change2(View view) {
        TransitionManager.beginDelayedTransition(mRoot);

        if (mChanged) {
            mConstraintSet1.setMargin(R.id.view, ConstraintSet.START, 0);
            mConstraintSet1.constrainWidth(R.id.view, mWidthMax);
            mConstraintSet1.setMargin(R.id.view, ConstraintSet.TOP, 0);
            mConstraintSet1.applyTo(mRoot);
        } else {
            int margin = (mWidthMax - mWidthMin) / 2;
            Log.d(TAG, "change: " + margin);
            mConstraintSet1.constrainWidth(R.id.view, mWidthMin);
            mConstraintSet1.setMargin(R.id.view, ConstraintSet.LEFT, margin);
            mConstraintSet1.setMargin(R.id.view, ConstraintSet.TOP, 200);
            mConstraintSet1.applyTo(mRoot);
        }
        mChanged = !mChanged;
    }
}
