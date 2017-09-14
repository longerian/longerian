package me.longerian.abcandroid.roundframe;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import me.longerian.abcandroid.R;

/**
 * Created by longerian on 2017/9/14.
 *
 * @author longerian
 * @date 2017/09/14
 */

public class FallingItemView extends View {

    private static final float MIN = 0.9f;

    private static final float MAX = 1.0f;

    private long duration = 2000L;

    private float itemSize = 100;

    private RectF rect;

    List<Bitmap> items = new ArrayList<>();

    private long[] delays;

    private float[] targetPositionY;

    private float[] drawPositionX;

    private float[] drawPositionY;

    private boolean fallingIn;

    private boolean fallingOut;

    public FallingItemView(Context context) {
        this(context, null);
    }

    public FallingItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FallingItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        itemSize = getResources().getDimensionPixelSize(R.dimen.item_size);
        for (int i = 0; i < 8; i++) {
            if (i == 1) {
                items.add(((BitmapDrawable) (context.getResources().getDrawable(R.drawable.ic_launcher))).getBitmap());
                continue;
            }
            if (i == 2) {
                items.add(((BitmapDrawable) (context.getResources().getDrawable(R.drawable.system_installer))).getBitmap());
                continue;
            }
            items.add(((BitmapDrawable) (context.getResources().getDrawable(R.drawable.tencent_q))).getBitmap());
        }
        targetPositionY = new float[items.size()];
        delays = new long[items.size()];
        drawPositionX = new float[items.size()];
        drawPositionY = new float[items.size()];

        drawPositionX[0] = -0.05f;
        drawPositionX[1] = 0.1f;
        drawPositionX[2] = 0.2f;
        drawPositionX[3] = 0.65f;
        drawPositionX[4] = 0.5f;
        drawPositionX[5] = 0.3f;
        drawPositionX[6] = 0.70f;
        drawPositionX[7] = 0.8f;

        targetPositionY[0] = 0.85f;
        targetPositionY[1] = 0.90f;
        targetPositionY[2] = 0.95f;
        targetPositionY[3] = 1.0f;
        targetPositionY[4] = 1.0f;
        targetPositionY[5] = 0.95f;
        targetPositionY[6] = 0.90f;
        targetPositionY[7] = 0.8f;

        delays[0] = 120L;
        delays[1] = 1000L;
        delays[2] = 200L;
        delays[3] = 0L;
        delays[4] = 1500L;
        delays[5] = 150L;
        delays[6] = 700L;
        delays[7] = 500L;

        rect = new RectF();
        startFallingIn();
    }

    public void setItems(List<Bitmap> item) {

    }

    public void setDrawPositionX(float[] drawPositionX) {
        if (drawPositionX != null && drawPositionX.length == items.size()) {
            this.drawPositionX = drawPositionX;
        }
    }

    public void setTargetPositionY(float[] targetPositionY) {
        if (targetPositionY != null && targetPositionY.length == items.size()) {
            this.targetPositionY = targetPositionY;
        }
    }

    public void setDelays(long[] delays) {
        if (delays != null && delays.length == items.size()) {
            this.delays = delays;
        }
    }

    public void setItemSize(float itemSize) {
        this.itemSize = itemSize;
    }

    public void startFallingIn() {
        AnimatorSet animatorSet = new AnimatorSet();
        List<Animator> animators = new ArrayList<>();
        for (int i = 0, size = items.size() ; i < size; i++) {
            final int index = i;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, targetPositionY[i]).setDuration(duration);
            valueAnimator.setStartDelay(delays[i]);
            valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    drawPositionY[index] = (float)animation.getAnimatedValue();
                    invalidate();
                }
            });
            animators.add(valueAnimator);
        }
        animatorSet.playTogether(animators);
        animatorSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                fallingIn = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                fallingIn = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fallingIn = false;
                startFallingOut();
            }
        });
        animatorSet.setStartDelay(2000L);
        animatorSet.start();
    }

    public void startFallingOut() {
        AnimatorSet animatorSet = new AnimatorSet();
        List<Animator> animators = new ArrayList<>();
        for (int i = 0, size = items.size() ; i < size; i++) {
            final int index = i;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(targetPositionY[i], 2.0f).setDuration(duration);
            valueAnimator.setStartDelay(delays[i]);
            valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    targetPositionY[index] = (float)animation.getAnimatedValue();
                    invalidate();
                }
            });
            animators.add(valueAnimator);
        }
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                fallingOut = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fallingOut = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                fallingOut = true;
            }
        });
        animatorSet.playTogether(animators);
        animatorSet.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        for (int i = 0, size = items.size(); i < size; i++) {
            if (fallingIn) {
                float currentPositionY = height * drawPositionY[i];
                float target = height * targetPositionY[i];
                if (currentPositionY >= target) {
                    currentPositionY = target;
                }
                float currentPositionX = width * drawPositionX[i];
                rect.set(currentPositionX, currentPositionY - itemSize, currentPositionX + itemSize, currentPositionY);
            } else if (fallingOut) {
                float currentPositionY = height * targetPositionY[i];
                float currentPositionX = width * drawPositionX[i];
                rect.set(currentPositionX, currentPositionY - itemSize, currentPositionX + itemSize, currentPositionY);
            }
            canvas.drawBitmap(items.get(i), null, rect, null);
        }
    }
}
