package com.fangmingdong.androiddemo.recyclerViewSideScroll;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

        ViewGroup.LayoutParams layoutParams = holder.mSFL.getLayoutParams();
        layoutParams.width = mContext.getResources().getDisplayMetrics().widthPixels;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        holder.mSFL.setLayoutParams(layoutParams);

        holder.mTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class VH extends RecyclerView.ViewHolder {

        private final TextView mTv;
        private final SlideFrameLayoutTranslation mSFL;
        private final RelativeLayout mFL;

        public VH(View itemView) {
            super(itemView);

            mSFL = ((SlideFrameLayoutTranslation) itemView.findViewById(R.id.sfl));
            mTv = ((TextView) itemView.findViewById(R.id.tv_slide_scroll_item));
            mFL = ((RelativeLayout) itemView.findViewById(R.id.rl_content));
        }
    }
}
