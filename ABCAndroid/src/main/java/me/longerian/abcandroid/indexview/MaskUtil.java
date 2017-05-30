package me.longerian.abcandroid.indexview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by longerian on 16/8/31.
 */

public class MaskUtil {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MODE_LEFT_RIGHT, MODE_RIGHT_LEFT, MODE_TOP_BOTTOM, MODE_BOTTOM_TOP})
    public @interface GradientOrientation {}

    public static final int MODE_LEFT_RIGHT = 0;
    public static final int MODE_RIGHT_LEFT = 1;
    public static final int MODE_TOP_BOTTOM = 2;
    public static final int MODE_BOTTOM_TOP = 3;

    private float leftTopRadius;

    private float rightTopRadius;

    private float leftBottomRadius;

    private float rightBottomRadius;

    private int outCornerLayerColor;

    private int maskLayerColor;

    private int orientation;

    private int colors[];

    private float positions[];

    private LinearGradient mLinearGradient;

    private Paint mPaint;

    private Path mPath;

    private RectF mCorner;

    public MaskUtil() {
        mPaint = new Paint();
        mPath = new Path();
        mCorner = new RectF();
        outCornerLayerColor = Color.WHITE;
        maskLayerColor = Color.parseColor("#40000000");
        leftTopRadius = 20.0f;
        rightTopRadius = 20.0f;
        leftBottomRadius = 20.0f;
        rightBottomRadius = 20.0f;
    }

    public void setRadius(float leftTopRadius, float rightTopRadius, float leftBottomRadius, float rightBottomRadius) {
        this.leftTopRadius = leftTopRadius;
        this.rightTopRadius = rightTopRadius;
        this.leftBottomRadius = leftBottomRadius;
        this.rightBottomRadius = rightBottomRadius;
    }

    public void setLinearGradient(@GradientOrientation int orientation, int colors[], float positions[]) {
        this.orientation = orientation;
        this.colors = colors;
        this.positions = positions;
    }

    public void setOutCornerLayerColor(int outCornerLayerColor) {
        this.outCornerLayerColor = outCornerLayerColor;
    }

    public void setMaskLayerColor(int maskLayerColor) {
        this.maskLayerColor = maskLayerColor;
    }

    public void onDraw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        //先画渐变
        if (this.colors != null && this.positions != null) {
            if (mLinearGradient == null) {
                switch (orientation) {
                    case MODE_LEFT_RIGHT:
                        mLinearGradient = new LinearGradient(0, 0, width, height,
                                this.colors, this.positions,
                                Shader.TileMode.CLAMP);
                        break;
                    case MODE_RIGHT_LEFT:
                        mLinearGradient = new LinearGradient(width, 0, 0, 0,
                                this.colors, this.positions,
                                Shader.TileMode.CLAMP);
                        break;
                    case MODE_TOP_BOTTOM:
                        mLinearGradient = new LinearGradient(0, 0, 0, height,
                                this.colors, this.positions,
                                Shader.TileMode.CLAMP);
                        break;
                    case MODE_BOTTOM_TOP:
                        mLinearGradient = new LinearGradient(0, height, 0, 0,
                                this.colors, this.positions,
                                Shader.TileMode.CLAMP);
                        break;
                }
            }
            mPaint.setShader(mLinearGradient);
            canvas.drawRect(0, 0, width, height, mPaint);
        }

        //再画蒙层
        mPaint.reset();
        mPaint.setColor(maskLayerColor);
        canvas.drawRect(0, 0, width, height, mPaint);

        //再盖圆角
        mPaint.reset();
        mPaint.setColor(outCornerLayerColor);
        mPath.reset();
        mPath.moveTo(0, 0);
        mPath.lineTo(width, 0);
        mPath.lineTo(width, height);
        mPath.lineTo(0, height);
        mPath.close();

        mCorner.left = 0;
        mCorner.top = 0;
        mCorner.right = 2 * leftTopRadius;
        mCorner.bottom = 2 * leftTopRadius;
        mPath.arcTo(mCorner, 180, 90, true);

        mPath.lineTo(width - rightTopRadius, 0);

        mCorner.left = width - 2 * rightTopRadius;
        mCorner.top = 0;
        mCorner.right = width;
        mCorner.bottom = 2 * rightTopRadius;
        mPath.arcTo(mCorner, -90, 90, false);

        mPath.lineTo(width, height - rightBottomRadius);

        mCorner.left = width - 2 * rightBottomRadius;
        mCorner.top = height - 2 * rightBottomRadius;
        mCorner.right = width;
        mCorner.bottom = height;
        mPath.arcTo(mCorner, 0, 90, false);

        mPath.lineTo(leftBottomRadius, height);

        mCorner.left = 0;
        mCorner.top = height - 2 * leftBottomRadius;
        mCorner.right = 2 * leftBottomRadius;
        mCorner.bottom = height;
        mPath.arcTo(mCorner, 90, 90, false);

        mPath.close();
        mPath.setFillType(Path.FillType.EVEN_ODD);

//        canvas.save();
//        canvas.clipPath(mPath);
        canvas.drawPath(mPath, mPaint);
//        canvas.restore();
    }
}
