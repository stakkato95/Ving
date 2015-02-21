package com.github.stakkato95.ving.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Artyom on 18.02.2015.
 */
public class DividerDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;

    public DividerDecoration(Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(new int[]{android.R.attr.listDivider});
        mDivider = typedArray.getDrawable(0);
        typedArray.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //parent.getChildCount() == 1 case with glimpsing Footer
        if (mDivider == null || parent.getChildCount() == 1) {
            super.onDrawOver(c, parent, state);
            return;
        }

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int top, bottom;

        for (int i = 0; i < parent.getChildCount(); i++) {
            View childItem = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childItem.getLayoutParams();
            top = childItem.getBottom() + layoutParams.bottomMargin - mDivider.getIntrinsicHeight();
            bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

}