package me.longerian.abcandroid.indexview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import me.longerian.abcandroid.R;

/**
 * Created by longerian on 16/8/25.
 */

public class HoleView extends View {

    private int mStartIndexColor;

    private int mEndIndexColor;

    private int mIndex;

    private int mOffset;

    private int mRadius = 50;

    private Rect mPaintRect;

    private Paint mPaint;

    private Path mPath;

    private RectF mCorner;

    private LinearGradient mLinearGradient;

    private boolean mInvalid;

    public HoleView(Context context) {
        this(context, null);
    }

    public HoleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HoleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.IndexView, defStyleAttr, 0);
        if (a != null) {
            mStartIndexColor = a.getColor(
                    R.styleable.IndexView_startIndexColor,
                    Color.RED);
            mEndIndexColor = a.getColor(R.styleable.IndexView_endIndexColor, Color.RED);
            mIndex = a.getInt(R.styleable.IndexView_index, 50);
            a.recycle();
        } else {
            mStartIndexColor = Color.RED;
            mEndIndexColor = Color.RED;
            mIndex = 50;
        }

        if (mIndex > 100) {
            mIndex = 100;
        }
        if (mIndex < 0) {
            mIndex = 0;
        }

        mOffset = 100;
        mRadius = 50;

        mPaintRect = new Rect();
        mPaint = new Paint();
        mPath = new Path();
        mCorner = new RectF();
    }

    public void setStartIndexColor(int startIndexColor) {
        mStartIndexColor = startIndexColor;
        mInvalid = true;
        invalidate();
    }

    public void setEndIndexColor(int endIndexColor) {
        mEndIndexColor = endIndexColor;
        mInvalid = true;
        invalidate();
    }

    public void setIndex(int index) {
        if (index > 100) {
            mIndex = 100;
        } else if (index < 0) {
            mIndex = 0;
        } else {
            mIndex = index;
        }
        mInvalid = true;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        mPath.reset();
        mPath.moveTo(0, 0);
        mPath.lineTo(width, 0);
        mPath.lineTo(width, height);
        mPath.lineTo(0, height);
        mPath.close();

        mCorner.left = mOffset - mRadius;
        mCorner.top = height / 2 - mRadius;
        mCorner.right = mOffset + mRadius;
        mCorner.bottom = height / 2 + mRadius;
        mPath.arcTo(mCorner, 90, 180, true);

        mPath.lineTo(width - mOffset, height / 2 - mRadius);

        mCorner.left = width - mOffset - mRadius;
        mCorner.top = height / 2 - mRadius;
        mCorner.right = width - mOffset + mRadius;
        mCorner.bottom = height / 2 + mRadius;
        mPath.arcTo(mCorner, -90, 180, false);

        mPath.close();

        mPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);

        if (mLinearGradient == null || mInvalid) {
            mLinearGradient = new LinearGradient(0, 0, 0, height, mStartIndexColor, mEndIndexColor,
                    Shader.TileMode.CLAMP);
            mInvalid = false;
        }
        mPaint.setShader(mLinearGradient);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mInvalid = true;
    }
}
