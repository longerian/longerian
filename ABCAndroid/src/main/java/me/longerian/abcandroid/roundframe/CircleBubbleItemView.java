package me.longerian.abcandroid.roundframe;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import me.longerian.abcandroid.R;

/**
 * Created by longerian on 2017/9/13.
 *
 * @author longerian
 * @date 2017/09/13
 */

public class CircleBubbleItemView extends View {

    private float itemSize = 100;

    private float itemOffset = 10;

    private float angleUnit;

    private float centerX;

    private float centerY;

    private float radius;

    private long duration = 200L;

    private boolean inHide;

    private float hideScale = 1.0f;

    private boolean inBubbble;

    private float bubbleScale = 0.01f;

    private int hideIndex = -1;

    private int bubbleIndex = -1;

    private RectF rect;

    List<Bitmap> items = new ArrayList<>();

    public CircleBubbleItemView(Context context) {
        this(context, null);
    }

    public CircleBubbleItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleBubbleItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        startHide(5);
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

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void startHide(final int index) {
        if (index >= 0 && index < items.size()) {
            this.hideIndex = index;
        } else {
            this.hideIndex = -1;
            invalidate();
            return;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.f, 0.01f).setDuration(duration);
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                hideScale = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                inHide = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                inHide = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                inHide = false;
                startBubble(index, ((BitmapDrawable) getResources().getDrawable(R.drawable.cling)).getBitmap());
            }
        });
        valueAnimator.setStartDelay(2000L);
        valueAnimator.start();
    }

    public void startBubble(int index, final Bitmap bitmap) {
        if (index >= 0 && index < items.size() && bitmap != null) {
            this.bubbleIndex = index;
        } else {
            this.bubbleIndex = -1;
            invalidate();
            return;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.01f, 1.f).setDuration(duration * 5);
        valueAnimator.setInterpolator(new SpringInterpolator());
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bubbleScale = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                items.set(bubbleIndex, bitmap);
                inBubbble = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                inBubbble = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                inBubbble = false;
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
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
            canvas.rotate(i * angleUnit, centerX, centerY);
            if (inHide && i == hideIndex) {
                canvas.scale(hideScale, hideScale, (rect.left + rect.right) / 2.0f, (rect.top + rect.bottom) / 2.0f);
            }
            if (inBubbble && i == bubbleIndex) {
                canvas.scale(bubbleScale, bubbleScale, (rect.left + rect.right) / 2.0f, (rect.top + rect.bottom) / 2.0f);
            }
            canvas.drawBitmap(items.get(i), null, rect, null);
            canvas.restore();
        }
    }
}
