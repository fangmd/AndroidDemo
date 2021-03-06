package com.fangmingdong.androiddemo.recyclerViewSideScroll;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.fangmingdong.androiddemo.R;

public class RecyclerViewSideScrollActivity extends AppCompatActivity {

    private RecyclerView mRv;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, RecyclerViewSideScrollActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_side_scroll);

        mRv = (RecyclerView) findViewById(R.id.rv);

//        mRv.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                super.getItemOffsets(outRect, view, parent, state);
//                outRect.set(0, 0, 0, 10);
//            }
//        });


        RAdapter adapter = new RAdapter();
        mRv.setAdapter(adapter);


        TextView tv = (TextView) findViewById(R.id.tv_slide_scroll_item);
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(RecyclerViewSideScrollActivity.this, "Click", Toast.LENGTH_SHORT).show();
//            }
//        });

    }


}
