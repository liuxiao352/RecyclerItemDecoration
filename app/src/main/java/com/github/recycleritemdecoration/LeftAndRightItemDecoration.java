package com.github.recycleritemdecoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @data 2018-09-26
 * @desc
 */

public class LeftAndRightItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mLeftPaint;
    private Paint mRightPaint;
    private int mTagWidth;

    public LeftAndRightItemDecoration() {
        mLeftPaint = new Paint();
        mLeftPaint.setColor(Color.parseColor("#FF4081"));
        mRightPaint = new Paint();
        mRightPaint.setColor(Color.parseColor("#3F51B5"));
        mTagWidth = 40;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i); //得到每一个view
            int position = parent.getChildAdapterPosition(view);// 得到每一个view的位置
            boolean b = position % 2 == 0;
            if (b) { //左
                int left = view.getLeft();
                int right = view.getLeft() + mTagWidth;
                int top = view.getTop();
                int bottom = view.getBottom();
                c.drawRect(left, top, right, bottom, mLeftPaint);
            }else{ //右
                int left = view.getRight() - mTagWidth;
                int right = view.getRight();
                int top = view.getTop();
                int bottom = view.getBottom();
                c.drawRect(left, top, right, bottom, mRightPaint);
            }

        }
    }
}
