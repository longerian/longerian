package me.longerian.abcandroid.layoutdemo;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import me.longerian.abcandroid.R;

/**
 * Created by longerian on 15/4/29.
 */
public class LineSearchSuggestionLayout extends ViewGroup {

    private int divider;

    private int minWidth;

    private int maxWidth;

    private int maxLine;

    public LineSearchSuggestionLayout(Context context) {
        super(context);
        init(context);
    }

    public LineSearchSuggestionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LineSearchSuggestionLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Resources resources = getResources();
        divider = resources.getDimensionPixelSize(R.dimen.mui_m0);
        minWidth = resources
                .getDimensionPixelSize(R.dimen.mini_width);
        maxWidth = resources
                .getDimensionPixelSize(R.dimen.max_width);
        maxLine = 2;
    }

    public void setDivider(int divider) {
        this.divider = divider;
        requestLayout();
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
        requestLayout();
    }

    public void setMaxLine(int maxLine) {
        this.maxLine = maxLine;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int childCount = getChildCount();
        final int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int leftCursor = 0;
        int topCursor = 0;
        int inlineHeight = 0;
        int lineCount = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int width = child.getMeasuredWidth();
            if (width > maxWidth) {
                measureChild(child, MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                width = maxWidth;
            }
            int height = child.getMeasuredHeight();
            leftCursor += divider;
            leftCursor += width;
            if (height > inlineHeight) {
                //统计行内一行的高度，用于计算整体高度
                inlineHeight = height;
            }
            int left = parentWidth - leftCursor;
            if (left < minWidth || i == childCount - 1) {
                //如果剩余宽度少于一个区域的最小宽度，或者已经是最后一个区块，重新measure
                left = left + divider + width;
                measureChild(child, MeasureSpec.makeMeasureSpec(left, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                //要换行了，重置leftCursor，inlineHeight；整体高度累加，同时要算上一个间距
                topCursor += divider;
                topCursor += inlineHeight;
                leftCursor = 0;
                inlineHeight = 0;
                lineCount++;
            }
            if (lineCount >= maxLine) {
                break;
            }
        }
        //减去一行divider高度
        topCursor -= divider;
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                topCursor);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childCount = getChildCount();
        int parentWidth = r - l;
        int leftCursor = 0;
        int topCursor = 0;
        int lineCount = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();
            child.layout(leftCursor, topCursor, leftCursor + width, topCursor + height);
            leftCursor += divider;
            leftCursor += width;
            if (leftCursor >= parentWidth) {
                leftCursor = 0;
                topCursor += divider;
                topCursor += height;
                lineCount++;
            }
            if (lineCount >= maxLine) {
                break;
            }
        }
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public static class LayoutParams extends MarginLayoutParams {

        /**
         * {@inheritDoc}
         */
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(int width, int height) {
            super(width, height);
        }


        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        /**
         * Copy constructor. Clones the width, height, margin values, and
         * gravity of the source.
         *
         * @param source The layout params to copy from.
         */
        public LayoutParams(LayoutParams source) {
            super(source);
        }

    }

}
