package me.longerian.abcandroid.roundframe;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import me.longerian.abcandroid.R;

/**
 * Created by longerian on 2017/9/13.
 *
 * @author longerian
 * @date 2017/09/13
 */

public class CircleItemView extends View {

    //360d / 15s = 24d / 1s = 0.4d / frame

    private float itemSize = 100;

    private float itemOffset = 10;

    private float angleUnit;

    private float rotateAngle = 0.0f;

    private float rotateSpeedAngle = 0.4f;

    private float centerX;

    private float centerY;

    private float radius;

    private RectF rect;

    private Runnable rotateAction = new Runnable() {
        @Override
        public void run() {
            rotateAngle += rotateSpeedAngle;
            invalidate();
            startRotate();
        }
    };

    List<Bitmap> items = new ArrayList<>();

    public CircleItemView(Context context) {
        this(context, null);
    }

    public CircleItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        itemSize = getResources().getDimensionPixelSize(R.dimen.item_size);
        itemOffset = getResources().getDimensionPixelSize(R.dimen.item_offset);
        for (int i = 0; i < 12; i++) {
            if (i == 1) {
                items.add(((BitmapDrawable) (context.getResources().getDrawable(R.drawable.ic_launcher))).getBitmap());
                continue;
            }
            items.add(((BitmapDrawable) (context.getResources().getDrawable(R.drawable.tencent_q))).getBitmap());
        }
        angleUnit = 360.0f / items.size();
        rect = new RectF();
        setRotateSpeed(15.0f);
    }

    public void addItems(List<Bitmap> item) {

    }

    public void setItemSize(float itemSize) {
        this.itemSize = itemSize;
        invalidate();
    }

    public void setItemOffset(float offset) {
        this.itemOffset = offset;
        invalidate();
    }

    public void setRotateSpeed(float secondPerCircle) {
        if (secondPerCircle == 0.0f) {
            this.rotateAngle = 0.0f;
            this.rotateSpeedAngle = 0.0f;
            removeCallbacks(rotateAction);
            invalidate();
        } else {
            this.rotateAngle = 0.0f;
            this.rotateSpeedAngle = 360.0f / secondPerCircle / 60;
            startRotate();
        }
    }

    public void startRotate() {
        ViewCompat.postOnAnimation(this, rotateAction);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(rotateAction);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        radius = width * 1.0f / 2;
        centerX = radius;
        centerY = radius;
        rect.set(centerX - itemSize / 2, height - itemSize + itemOffset, centerX + itemSize / 2, height + itemOffset);
        for (int i = 0, size = items.size(); i < size; i++) {
            canvas.save();
            canvas.rotate(i * angleUnit + rotateAngle, centerX, centerY);
            canvas.drawBitmap(items.get(i), null, rect, null);
            canvas.restore();
        }
    }
}
