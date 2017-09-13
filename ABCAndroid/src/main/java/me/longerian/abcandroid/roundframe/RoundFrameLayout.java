package me.longerian.abcandroid.roundframe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.os.Build;
import android.os.Build.VERSION;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by longerian on 2017/9/13.
 *
 * @author longerian
 * @date 2017/09/13
 */

public class RoundFrameLayout extends FrameLayout {

    private static RectF rectF;
    private static Path path;

    public RoundFrameLayout(@NonNull Context context) {
        super(context);
    }

    public RoundFrameLayout(@NonNull Context context,
        @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs,
        @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void draw(Canvas canvas) {
        if (VERSION.SDK_INT < 21) {
            //canvas.save();
            int width = this.getWidth();
            int height = this.getHeight();
            clipCorner(this, canvas, width, height, width / 2);
            super.draw(canvas);
            //canvas.restore();
        } else {
            super.draw(canvas);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (VERSION.SDK_INT < 21) {
        //    canvas.save();
            clipCorner(this, canvas, canvas.getWidth(), canvas.getHeight(), canvas.getWidth() / 2);
            super.dispatchDraw(canvas);
            //canvas.restore();
        } else {
            super.dispatchDraw(canvas);
        }
    }

    public static void clipCorner(View view, Canvas canvas, int viewWidth, int viewHeight, int borderRadius) {
        if(borderRadius > 0 && canvas != null) {
            if (Build.VERSION.SDK_INT >= 11) {
                if (canvas.isHardwareAccelerated()
                    && (Build.VERSION.SDK_INT < 18)) {
                    //硬件加速开启时,canvas.clipPath支持的最低版本为18
                    view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                }
            }

            if(rectF == null) {
                rectF = new RectF();
            }
            if(path == null) {
                path = new Path();
            }
            rectF.set(0, 0, viewWidth, viewHeight);
            path.reset();
            path.addRoundRect(rectF, borderRadius, borderRadius, Path.Direction.CW);
            path.close();
            canvas.clipPath(path, Op.REPLACE);
        }
    }

}
