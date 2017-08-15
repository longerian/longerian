package me.longerian.abcandroid.indexview;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

/**
 * Created by longerian on 2017/8/15.
 *
 * @author longerian
 * @date 2017/08/15
 */

public class OrderLayout extends FrameLayout {
    public OrderLayout(@NonNull Context context) {
        super(context);
        setChildrenDrawingOrderEnabled(true);
    }

    public OrderLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
    }

    public OrderLayout(@NonNull Context context, @Nullable AttributeSet attrs,
        @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setChildrenDrawingOrderEnabled(true);
    }

    private static final int[][] DRAW_ORDERS = new int[][]{
        {0, 1, 2, 3},
        {3, 2, 1, 0},
        {1, 2, 3, 0}
    };

    private int currentOrder;

    public void setDrawOrder(int order) {
        currentOrder = order;
        invalidate();
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        Log.d("Longer", "i " + i + " --> " + DRAW_ORDERS[currentOrder][i]);
        return DRAW_ORDERS[currentOrder][i];
    }

}
