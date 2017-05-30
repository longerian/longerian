package me.longerian.abcandroid.indexview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by longerian on 16/8/31.
 */

public class MaskView extends ImageView {

    private MaskUtil mMaskUtil;

    public MaskView(Context context) {
        this(context, null);
    }

    public MaskView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMaskUtil = new MaskUtil();
        mMaskUtil.setLinearGradient(MaskUtil.MODE_BOTTOM_TOP, new int[]{Color.BLACK, Color.TRANSPARENT}, new float[]{0.0f, 0.3f});
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mMaskUtil.onDraw(canvas);
    }
}
