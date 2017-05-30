package me.longerian.abcandroid.objectanimation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import me.longerian.abcandroid.R;

/**
 * Created by longerian on 15/4/30.
 */
public class ObjectAnimationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_animation);

        final TranslationShrinkAnimationImageView animationImageView = (TranslationShrinkAnimationImageView) findViewById(R.id.animation);
        final ImageView target = (ImageView) findViewById(R.id.target);

        target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                int targetWidth = target.getWidth();
                int targetHeight = target.getHeight();
                int selfWidth = animationImageView.getWidth();
                int selfHeight = animationImageView.getHeight();
                float endScaleX = targetWidth * 1.0f / selfWidth;
                float endScaleY = targetHeight * 1.0f / selfHeight;

                float endTranslationX = target.getTranslationX();
                float endTranslationY = target.getTranslationY();

                float positionX = target.getX();
                float positionY = target.getY();

                animationImageView.setPivotX(0.0f);
                animationImageView.setPivotY(0.0f);

                Log.d("Longer", "position source X = " + animationImageView.getX() + " Y = " + animationImageView.getY());
                Log.d("Longer", "position target Y = " + positionX + " Y = " + positionY);


                Log.d("Longer", "translation source X = " + animationImageView.getTranslationX() + " Y = " + animationImageView.getTranslationY());
                Log.d("Longer", "translation target X = " + endTranslationX + " Y = " + endTranslationY);

                ObjectAnimator alpha = ObjectAnimator.ofFloat(animationImageView, "alpha", 1.0f, 0.0f);
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(animationImageView, "scaleX", 1.0f, endScaleX);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(animationImageView, "scaleY", 1.0f, endScaleY);
//                ObjectAnimator translationX = ObjectAnimator.ofFloat(animationImageView, "translationX", endTranslationX);
//                ObjectAnimator translationY = ObjectAnimator.ofFloat(animationImageView, "translationY", endTranslationY);
                ObjectAnimator x = ObjectAnimator.ofFloat(animationImageView, "x", positionX);
                ObjectAnimator y = ObjectAnimator.ofFloat(animationImageView, "y", positionY);


//                AnimatorSet animSet = new AnimatorSet();
//                animSet.setDuration(1000);
//                animSet.playTogether(scaleX, scaleY, x, y);
//                animSet.play(scaleX).with(scaleY);
//                animSet.play(x).with(y);
//                animSet.play(x).with(scaleX);
//                animSet.start();

//                PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("x", positionX);
//                PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", positionY);
//                PropertyValuesHolder sx = PropertyValuesHolder.ofFloat("scaleX", 1.0f, endScaleX);
//                PropertyValuesHolder sy = PropertyValuesHolder.ofFloat("scaleY", 1.0f, endScaleY);
//                ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(animationImageView, pvhX, pvhY, sx, sy).setDuration(1000);
//                animator.start();
                animationImageView.translationShrinkTo(positionX, positionY, endScaleX, endScaleY);
                 **/
                animationImageView.translationShrinkTo(target);
            }
        });


    }
}
