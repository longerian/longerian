package me.longerian.abcandroid.indexview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import me.longerian.abcandroid.R;

/**
 * Created by longerian on 15/8/28.
 */
public class IndexView extends View {

    private int mStartIndexColor;

    private int mEndIndexColor;

    private int mIndex;

    private Rect mPaintRect;

    private Paint mPaint;

    private LinearGradient mLinearGradient;

    private boolean mInvalid;

    public IndexView(Context context) {
        this(context, null);
    }

    public IndexView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexView(Context context, AttributeSet attrs, int defStyleAttr) {
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

        mPaintRect = new Rect();
        mPaint = new Paint();
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
        int indexWidth = (int) (width * mIndex * 1.0f / 100);
        mPaintRect.left  = 0;
        mPaintRect.top = 0;
        mPaintRect.right = indexWidth;
        mPaintRect.bottom = height;
        if (mLinearGradient == null || mInvalid) {
            mLinearGradient = new LinearGradient(0, height / 2, indexWidth, height / 2, mStartIndexColor, mEndIndexColor,
                    Shader.TileMode.CLAMP);
            mInvalid = false;
        }
        mPaint.setShader(mLinearGradient);
        canvas.drawRect(mPaintRect, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mInvalid = true;
    }
}
