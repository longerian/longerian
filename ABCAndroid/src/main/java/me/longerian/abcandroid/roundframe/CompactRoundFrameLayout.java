package me.longerian.abcandroid.roundframe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.FrameLayout;

/**
 * Created by longerian on 2017/9/13.
 *
 * @author longerian
 * @date 2017/09/13
 */

public class CompactRoundFrameLayout extends FrameLayout {

    private Bitmap maskBitmap;
    private Paint paint, maskPaint;
    private float cornerRadius;

    public CompactRoundFrameLayout(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public CompactRoundFrameLayout(@NonNull Context context,
        @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CompactRoundFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs,
        @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        setWillNotDraw(false);
    }

    @Override
    public void draw(Canvas canvas) {
        Bitmap offscreenBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas offscreenCanvas = new Canvas(offscreenBitmap);
        cornerRadius = canvas.getWidth() / 2;
        super.draw(offscreenCanvas);
        if (maskBitmap == null) {
            maskBitmap = createMask(canvas.getWidth(), canvas.getHeight());
        }
        offscreenCanvas.drawBitmap(maskBitmap, 0f, 0f, maskPaint);
        canvas.drawBitmap(offscreenBitmap, 0f, 0f, paint);
    }

    private Bitmap createMask(int width, int height) {
        Bitmap mask = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8);
        Canvas canvas = new Canvas(mask);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, width, height, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawRoundRect(new RectF(0, 0, width, height), cornerRadius, cornerRadius, paint);
        return mask;
    }

}
