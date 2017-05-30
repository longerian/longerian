package me.longerian.abcandroid.objectanimation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by longerian on 15/4/30.
 */
public class TranslationShrinkAnimationImageView extends ImageView implements Animator.AnimatorListener {

    private int duration = 1000;

    public TranslationShrinkAnimationImageView(Context context) {
        super(context);
    }

    public TranslationShrinkAnimationImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TranslationShrinkAnimationImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void translationShrinkTo(View targetView) {
        if (targetView != null) {
            int targetWidth = targetView.getWidth();
            int targetHeight = targetView.getHeight();
            int selfWidth = getWidth();
            int selfHeight = getHeight();
            float endScaleX = targetWidth * 1.0f / selfWidth;
            float endScaleY = targetHeight * 1.0f / selfHeight;
            float positionX = targetView.getX();
            float positionY = targetView.getY();
            Log.d("Longer", "dst X " + positionX + " dst Y " + positionY);
            Log.d("Longer", "dst Left " + targetView.getLeft() + " dst Top " + targetView.getTop());
            translationShrinkTo(positionX, positionY, endScaleX, endScaleY);
        }
    }

    public void translationShrinkTo(float dstX, float dstY, float dstScaleX, float dstScaleY) {
       Log.d("Longer", "org pivotx " + getPivotX() + " org pivoty " + getPivotY());
        //在星Note2手机上，设置为0.0跟默认行为一样，这个api略奇怪，按文档上说，默认应该是View自己的中心，也就是0.5，但实际上设置和不设置的行为是不一样的
        this.setPivotX(0.5f);
        this.setPivotY(0.5f);
       Log.d("Longer", "new pivotx " + getPivotX() + " new pivoty " + getPivotY());
        ObjectAnimator alpha = ObjectAnimator.ofFloat(this, "alpha", 1.0f, 0.0f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(this, "scaleX", 1.0f, dstScaleX);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(this, "scaleY", 1.0f, dstScaleY);
        ObjectAnimator x = ObjectAnimator.ofFloat(this, "x", dstX);
        ObjectAnimator y = ObjectAnimator.ofFloat(this, "y", dstY);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(duration);
        animSet.playTogether(scaleX, scaleY, x, y);
//        animSet.play(scaleX).with(scaleY);
//        animSet.play(x).with(y).before(scaleX);
        animSet.addListener(this);
        animSet.start();

//        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("x", dstX);
//        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", dstY);
//        PropertyValuesHolder sx = PropertyValuesHolder.ofFloat("scaleX", dstScaleX);
//        PropertyValuesHolder sy = PropertyValuesHolder.ofFloat("scaleY", dstScaleY);
//        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(toString(), pvhX, pvhY, sx, sy).setDuration(duration);
//        animator.start();
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        Log.d("Longer", "animation end");
        Log.d("Longer", "this dst X " + getX() + " dst Y " + getY());
        Log.d("Longer", "this Left " + getLeft() + " Top " + getTop());
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
