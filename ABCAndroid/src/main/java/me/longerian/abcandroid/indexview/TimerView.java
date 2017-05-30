package me.longerian.abcandroid.indexview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import me.longerian.abcandroid.R;

/**
 * Created by longerian on 17/2/5.
 */

public class TimerView extends View {

    private boolean countdownMode;

    private TextPaint textPaint;

    private Paint timeBgPaint;

    private int textColor = Color.WHITE;

    private String text;

    private long until;

    private String day;

    private String hour;

    private String minute;

    private String second;

    private int textWidth;

    private int textHeight;

    private int timePadding;

    private int timeGap;

    private int timeMargin;

    private int timeRadius;

    private RectF timeBgRect;

    private Runnable countdownTask = new Runnable() {
        @Override
        public void run() {
            startCountdown(text, until);
        }
    };

    public TimerView(Context context) {
        this(context, null);
    }

    public TimerView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        textPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_szie));
        timeBgPaint = new Paint();
        timeBgPaint.setColor(Color.argb(20, 0, 0, 0));
        timePadding = getResources().getDimensionPixelSize(R.dimen.time_padding);
        timeGap = getResources().getDimensionPixelSize(R.dimen.time_gap);
        timeMargin = getResources().getDimensionPixelOffset(R.dimen.time_margin);
        timeRadius = getResources().getDimensionPixelOffset(R.dimen.time_radius);
        timeBgRect = new RectF();
    }

    public void startCountdown(String text, long until) {
        long now = System.currentTimeMillis();
        this.text = text;
        textWidth = (int) (Layout.getDesiredWidth(this.text, textPaint) + 0.5f);
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        textHeight = (int) ((fm.descent - fm.ascent) + 0.5f);
        if (now < until) {
            countdownMode = true;
            this.until = until;
            formatField(this.until - now);
            textWidth += (int) ((Layout.getDesiredWidth("00", textPaint) + timePadding * 2 + timeGap) * 4+ 0.5f);
        } else {
            countdownMode = false;
        }
        requestLayout();
        invalidate();
        if (countdownMode) {
            postDelayed(countdownTask, 1000l);
        }
    }

    private void formatField(long until) {
        if (until <= 0) {
            day = "00";
            hour = "00";
            minute = "00";
            second = "00";
            return;
        }
        int dayCount = (int) (until / 86400000);
        if (dayCount > 99) {
            day = "99";
        } else if (dayCount >= 0 && dayCount <= 9) {
            day = "0" + dayCount;
        } else {
            day = String.valueOf(dayCount);
        }
        until -= dayCount * 86400 * 1000;
        int hourCount = (int) (until / 3600000);
        if (hourCount >= 0 && hourCount <= 9) {
            hour = "0" + hourCount;
        } else {
            hour = String.valueOf(hourCount);
        }
        until -= hourCount * 3600 * 1000;
        int minuteCount = (int) (until / 60000);
        if (minuteCount >= 0 && minuteCount <= 9) {
            minute = "0" + minuteCount;
        } else {
            minute = String.valueOf(minuteCount);
        }
        until -= minuteCount * 60 * 1000;
        int secondCount = (int) (until / 1000);
        if (secondCount >= 0 && secondCount <= 9) {
            second = "0" + secondCount;
        } else {
            second = String.valueOf(secondCount);
        }
    }

    public void stopCoundDown() {
        removeCallbacks(countdownTask);
    }

    public void setText(String text) {
        if (this.text != text) {
            countdownMode = false;
            this.text = text;
            textWidth = (int) (Layout.getDesiredWidth(this.text, textPaint) + 0.5f);
            Paint.FontMetrics fm = textPaint.getFontMetrics();
            textHeight = (int) ((fm.descent - fm.ascent) + 0.5f);
            requestLayout();
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int left = getPaddingLeft();
        canvas.drawText(text, left,
                canvas.getHeight() - getPaddingBottom() - textPaint.getFontMetrics().descent,
                textPaint);
        if (countdownMode) {
            left += (int) (Layout.getDesiredWidth(this.text, textPaint) + 0.5f) + timeGap;

            timeBgRect.left = left;
            timeBgRect.top = timeMargin;
            timeBgRect.right = left + timePadding + timePadding + Layout.getDesiredWidth(day, textPaint);
            timeBgRect.bottom = canvas.getHeight() - timeMargin;
            canvas.drawRoundRect(timeBgRect, timeRadius, timeRadius, timeBgPaint);

            textPaint.setFakeBoldText(true);
            canvas.drawText(day, left + timePadding,
                    canvas.getHeight() - getPaddingBottom() - textPaint.getFontMetrics().descent,
                    textPaint);

            left = (int) (timeBgRect.right + timeGap + 0.5f);
            timeBgRect.left = left;
            timeBgRect.top = timeMargin;
            timeBgRect.right = left + timePadding + timePadding + Layout.getDesiredWidth(hour, textPaint);
            timeBgRect.bottom = canvas.getHeight() - timeMargin;
            canvas.drawRoundRect(timeBgRect, timeRadius, timeRadius, timeBgPaint);

            canvas.drawText(hour, left + timePadding,
                    canvas.getHeight() - getPaddingBottom() - textPaint.getFontMetrics().descent,
                    textPaint);

            left = (int) (timeBgRect.right + timeGap + 0.5f);
            timeBgRect.left = left;
            timeBgRect.top = timeMargin;
            timeBgRect.right = left + timePadding + timePadding + Layout.getDesiredWidth(minute, textPaint);
            timeBgRect.bottom = canvas.getHeight() - timeMargin;
            canvas.drawRoundRect(timeBgRect, timeRadius, timeRadius, timeBgPaint);

            canvas.drawText(minute, left + timePadding,
                    canvas.getHeight() - getPaddingBottom() - textPaint.getFontMetrics().descent,
                    textPaint);

            left = (int) (timeBgRect.right + timeGap + 0.5f);
            timeBgRect.left = left;
            timeBgRect.top = timeMargin;
            timeBgRect.right = left + timePadding + timePadding + Layout.getDesiredWidth(second, textPaint);
            timeBgRect.bottom = canvas.getHeight() - timeMargin;
            canvas.drawRoundRect(timeBgRect, timeRadius, timeRadius, timeBgPaint);

            canvas.drawText(second, left + timePadding,
                    canvas.getHeight() - getPaddingBottom() - textPaint.getFontMetrics().descent,
                    textPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(textWidth + getPaddingRight() + getPaddingLeft(),
                textHeight + getPaddingTop() + getPaddingBottom());
    }
}
