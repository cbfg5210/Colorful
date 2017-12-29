package com.ue.fingercoloring.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ue.fingercoloring.R;

public class TimeLineDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;
    private int distance;

    private Drawable drawable;

    private Paint linePaint;

    public TimeLineDecoration(Context context, int distance) {
        mContext = context;
        this.distance = distance;
        drawable = ContextCompat.getDrawable(mContext, R.drawable.svg_time);

        linePaint = new Paint();
        linePaint.setStrokeWidth(2);
        linePaint.setColor(0xFF979797);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = distance;
        outRect.right = distance;
        outRect.bottom = distance;

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = distance;
        } else if (parent.getChildAdapterPosition(view) == 1) {
            outRect.top = 2 * distance;
        }

        if (parent.getChildAdapterPosition(view) % 2 == 0) {
            outRect.left = 20;
        } else {
            outRect.right = 20;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildCount() == 0) return;
        drawVerticalLine(c, parent);
        drawVerticalViews(c, parent);
    }

    public void drawVerticalLine(Canvas c, RecyclerView parent) {
        final int x = parent.getMeasuredWidth() / 2;
        final int startY = parent.getChildAt(0).getTop();

        int itemCount = parent.getAdapter().getItemCount();
        View lastChild = parent.getChildAt(parent.getChildCount() - 1);

        int endY = parent.getChildAdapterPosition(lastChild) == itemCount - 1 ? lastChild.getTop() : lastChild.getBottom();

        c.drawLine(x, startY, x, endY, linePaint);
    }

    public void drawVerticalViews(Canvas c, RecyclerView parent) {
        final int parentWidth = parent.getMeasuredWidth();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {//-1最后一个不画
            final View child = parent.getChildAt(i);

            int horizontalLineTop = child.getTop() + drawable.getIntrinsicHeight() / 2;
            int horizontalLineLeft, horizontalLineRight;

            if (child.getLeft() > parentWidth / 2) {
                horizontalLineLeft = parentWidth / 2;
                horizontalLineRight = child.getLeft();
            } else {
                horizontalLineLeft = child.getRight();
                horizontalLineRight = parentWidth / 2;
            }

            c.drawLine(horizontalLineLeft, horizontalLineTop, horizontalLineRight, horizontalLineTop, linePaint);

            final int top = child.getTop();
            final int bottom = top + drawable.getIntrinsicHeight();

            int drawableLeft = parentWidth / 2 - drawable.getIntrinsicWidth() / 2;
            int drawableRight = parentWidth / 2 + drawable.getIntrinsicWidth() / 2;

            drawable.setBounds(drawableLeft, top, drawableRight, bottom);
            drawable.draw(c);
        }
    }
}