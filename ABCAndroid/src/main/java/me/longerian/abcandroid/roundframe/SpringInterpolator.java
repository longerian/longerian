package me.longerian.abcandroid.roundframe;

import android.animation.TimeInterpolator;

import static java.lang.Math.PI;

/**
 * Created by longerian on 2017/9/14.
 *
 * @author longerian
 * @date 2017/09/14
 */

public class SpringInterpolator implements TimeInterpolator {

    private static final float FACTOR = 0.4f;

    @Override
    public float getInterpolation(float input) {
        return (float)(Math.pow(2, -10 * input) * Math.sin((input - FACTOR / 4) * (2 * PI) / FACTOR) + 1);
    }
}
