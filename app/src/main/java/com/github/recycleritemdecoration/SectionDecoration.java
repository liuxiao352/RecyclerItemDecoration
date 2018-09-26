package com.github.recycleritemdecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

/**
 * @data 2018-09-26
 * @desc
 */

public class SectionDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private DecorationCallback mDecorationCallback;
    private Paint mPaint;
    private Paint mTextPaint;
    private int mTopGap;

    public SectionDecoration(Context context, DecorationCallback decorationCallback) {
        mContext = context;
        mDecorationCallback = decorationCallback;
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#EEEEEE"));
        mTopGap = 80;

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(50);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        long groupId = mDecorationCallback.getGroupId(position);
        if (groupId < 0) return;
        if (position == 0 || isFirstGroup(position)) {
            outRect.top = mTopGap;
        }else{
            outRect.top = 0;
        }
    }

//    @Override
//    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        super.onDraw(c, parent, state);
//        int left = parent.getPaddingLeft();
//        int right = parent.getWidth() - parent.getPaddingRight();
//        int childCount = parent.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View view = parent.getChildAt(i);
//            int position = parent.getChildAdapterPosition(view);
//            long groupId = mDecorationCallback.getGroupId(position);
//            if (groupId < 0) return;
//            String groupFirstLine = mDecorationCallback.getGroupFirstLine(position);
//            if (position == 0 || isFirstGroup(position)) {
//                float top = view.getTop() - mTopGap;
//                float bottom = view.getTop();
//                c.drawRect(left, top, right, bottom, mPaint);//绘制红色矩形
//                Rect rect = new Rect();
//                mTextPaint.getTextBounds(groupFirstLine, 0, groupFirstLine.length(), rect);
//                c.drawText(groupFirstLine, left, bottom - mTopGap / 2 + rect.height() / 2, mTextPaint);//绘制文本
//            }
//        }
//    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int itemCount = state.getItemCount();
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        long preGroupId, groupId = -1;
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);

            preGroupId = groupId;
            groupId = mDecorationCallback.getGroupId(position);
            if (groupId < 0 || groupId == preGroupId) continue;

            String textLine = mDecorationCallback.getGroupFirstLine(position).toUpperCase();
            if (TextUtils.isEmpty(textLine)) continue;

            int viewBottom = view.getBottom();
            float textY = Math.max(mTopGap, view.getTop());
            if (position + 1 < itemCount) { //下一个和当前不一样移动当前
                long nextGroupId = mDecorationCallback.getGroupId(position + 1);
                if (nextGroupId != groupId && viewBottom < textY ) {//组内最后一个view进入了header
                    textY = viewBottom;
                }
            }
            c.drawRect(left, textY - mTopGap, right, textY, mPaint);
            Rect rect = new Rect();
            mTextPaint.getTextBounds(textLine, 0, textLine.length(), rect);
            c.drawText(textLine, left + 40, textY - rect.height() / 2, mTextPaint);
        }
    }

    private boolean isFirstGroup(int pos){
        if (pos == 0){
            return true;
        }else{
            long preGroupId = mDecorationCallback.getGroupId(pos - 1);
            long groupId = mDecorationCallback.getGroupId(pos);
            return preGroupId != groupId;
        }
    }
}
