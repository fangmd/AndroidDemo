package com.fangmingdong.androiddemo.recyclerViewSideScroll;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fangmingdong.androiddemo.R;

import java.util.ArrayList;


/**
 * Created by double on 2018/1/24.
 */

public class RAdapter extends RecyclerView.Adapter<RAdapter.VH> {


    private final ArrayList<String> mDatas;
    private Context mContext;

    public RAdapter() {
        mDatas = new ArrayList<>();

        initData();
    }

    private void initData() {
        for (int i = 0; i < 40; i++) {
            mDatas.add(i + " item ");
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_vew_side_scroll_item, parent, false);
        return new VH((view));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        String s = mDatas.get(position);
        holder.mTv.setText(s);

//        ViewGroup.LayoutParams layoutParams = holder.mTv.getLayoutParams();
//        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
//        holder.mTv.setLayoutParams(layoutParams);

        ViewGroup.LayoutParams layoutParams = holder.mSFL.getLayoutParams();
        layoutParams.width = mContext.getResources().getDisplayMetrics().widthPixels;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        holder.mSFL.setLayoutParams(layoutParams);

        holder.mSFL.resetState();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class VH extends RecyclerView.ViewHolder {

        private final TextView mTv;
        private final SlideFrameLayout mSFL;
        private final FrameLayout mFL;

        public VH(View itemView) {
            super(itemView);

            mSFL = ((SlideFrameLayout) itemView.findViewById(R.id.sfl));
            mTv = ((TextView) itemView.findViewById(R.id.tv_slide_scroll_item));
            mFL = ((FrameLayout) itemView.findViewById(R.id.fl_content));
        }
    }
}
