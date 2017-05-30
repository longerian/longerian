package me.longerian.abcandroid.indexview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;

import me.longerian.abcandroid.R;

/**
 * Created by longerian on 17/2/7.
 */

public class CalendarView extends View {

    private final int[] DAY_OF_WEEK = new int[]{
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY,
            Calendar.SATURDAY,
            Calendar.SUNDAY
    };

    private final String[] MONTH_LABEL = new String[]{
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December",
    };

    private final String[] DAY_OF_WEEK_LABEL = new String[]{
            "MON",
            "TUE",
            "WED",
            "THU",
            "FRI",
            "SAT",
            "SUN",
    };

    private final String[] DATE_OF_STRING = new String[]{
            "01",
            "02",
            "03",
            "04",
            "05",
            "06",
            "07",
            "08",
            "09",
            "10",
            "11",
            "12",
            "13",
            "14",
            "15",
            "16",
            "17",
            "18",
            "19",
            "20",
            "21",
            "22",
            "23",
            "24",
            "25",
            "26",
            "27",
            "28",
            "29",
            "30",
            "31"
    };

    private final float CORNER_RADIUS = 16.0f;

    private final DateInfo[] VISIBLE_DATE_INFO = new DateInfo[35];

    /**
     * 本月第一天是周几
     */
    private int firstDayOfWeekOfMonth;

    /**
     * 本周第一天是周几（周日或者周一，本类中设置为周一）
     */
    private int firstDayOfWeek;

    private int totalDaysOfMonth;

    private final int TOTAL_DAYS_OF_WEEK = 7;

//    private final int WEEK_FRAME_ROWS = 6;

//    private int weekFrameHeight;

    private final int WEEK_ROWS = 5;

    private int weekRowHeight;

    private RectF[] touchableLocationList;

    private String title;

    private int year;

    private int month;

    private int today;

    private TextPaint titlePaint;

    private Paint.FontMetrics titleFontMetrics;

    private int titlePaddingLeft;

    private int titlePaddingTop;

    private TextPaint dayOfWeekPaint;

    private Paint.FontMetrics dayOfWeekFontMetrics;

    private int dayOfWeekWidth;

    private int dayOfWeekHeight;

    private int dayOfWeekGap;

    private TextPaint datePaint;

    private TextPaint datePaint2;

    private int dateWidth;

    private int dateHeight;

    private int dateHGap;

    private int dateVGap;

    private Paint.FontMetrics datePaintFontMetrics;

    private int datePaddingLeft;

    private Paint maskPaint;

    private RectF maskRect;

    private int maskHeight;

    private Paint todayPaint;

    private Paint planDayPaint;

    private int calendarDaySize;

    private Calendar mCalendar;

    private OnDateClickListener mOnDateClickListener;

    private boolean debug;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        titlePaint = new TextPaint();
        titlePaint.setAntiAlias(true);
        titlePaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.calendar_title));
        titlePaint.setColor(Color.rgb(248, 231, 28));
        titleFontMetrics = titlePaint.getFontMetrics();

        titlePaddingLeft = getResources()
                .getDimensionPixelOffset(R.dimen.calendar_title_padding_left);
        titlePaddingTop = getResources()
                .getDimensionPixelOffset(R.dimen.calendar_title_padding_top);

        dayOfWeekPaint = new TextPaint();
        dayOfWeekPaint.setAntiAlias(true);
        dayOfWeekPaint
                .setTextSize(getResources().getDimensionPixelSize(R.dimen.calendar_week_label));
        dayOfWeekPaint.setColor(Color.argb(102, 255, 255, 255));
        dayOfWeekFontMetrics = dayOfWeekPaint.getFontMetrics();

        dayOfWeekWidth = (int) (Layout.getDesiredWidth("ABC", dayOfWeekPaint) + 0.5f);
        dayOfWeekHeight = (int) (dayOfWeekFontMetrics.descent - dayOfWeekFontMetrics.ascent + 0.5f);

        datePaint = new TextPaint();
        datePaint.setAntiAlias(true);
        datePaint
                .setTextSize(getResources().getDimensionPixelSize(R.dimen.calendar_date));
        datePaint.setColor(Color.argb(255, 255, 255, 255));
        datePaintFontMetrics = datePaint.getFontMetrics();

        datePaint2 = new TextPaint();
        datePaint2.setAntiAlias(true);
        datePaint2
                .setTextSize(getResources().getDimensionPixelSize(R.dimen.calendar_date));
        datePaint2.setColor(Color.argb(50, 255, 255, 255));

        dateWidth = (int) (Layout.getDesiredWidth("00", datePaint) + 0.5f);
        dateHeight = (int) (datePaintFontMetrics.descent - datePaintFontMetrics.ascent + 0.5f);

        datePaddingLeft = getResources()
                .getDimensionPixelOffset(R.dimen.calendar_date_padding_left);

//        dateVGap = titlePaddingTop / 2;

        maskPaint = new Paint();
        maskPaint.setAntiAlias(true);
        maskPaint.setColor(Color.argb(25, 0, 0, 0));

        maskRect = new RectF();

        maskHeight = getResources().getDimensionPixelOffset(R.dimen.calendar_mask_height);

        todayPaint = new Paint();
        todayPaint.setAntiAlias(true);
        todayPaint.setColor(Color.argb(255, 255, 17, 17));

        planDayPaint = new Paint();
        planDayPaint.setAntiAlias(true);
        planDayPaint.setColor(Color.argb(82, 0, 0, 0));

        calendarDaySize = getResources().getDimensionPixelSize(R.dimen.calendar_grid_size);

        mCalendar = Calendar.getInstance();
        mCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        firstDayOfWeek = mCalendar.getFirstDayOfWeek();
    }

    public CalendarView setTimestamp(long timestamp) {
        mCalendar.setTimeInMillis(timestamp);
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        setTitle(MONTH_LABEL[month] + " " + year);
        setYearMonth(year, month + 1);
        return this;
    }

    /**
     * 在构造view的时候传入图标，不会立即绘制，必须要调用了{@link this#show()}方法
     * @param title
     * @return
     */
    public CalendarView setTitle(String title) {
        if (title == null) {
            this.title = "";
        } else {
            this.title = title;
        }
        return this;
    }

    /**
     * 在构造view的时候传入图标，不会立即绘制，必须要调用了{@link this#show()}方法
     * @param year 年份 如 2017
     * @param month 月份，1-12，内部会减一再传给calendar对象
     * @return
     */
    public CalendarView setYearMonth(int year, int month) {
        this.year = year;
        this.month = month - 1;
        this.today = mCalendar.get(Calendar.DATE);
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, this.month);
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        firstDayOfWeekOfMonth = mCalendar.get(Calendar.DAY_OF_WEEK);
        totalDaysOfMonth = getDaysInMonth(this.month, year);
//        WEEK_ROWS = calculateWeekRows();
//        weekFrameHeight = WEEK_FRAME_ROWS * calendarDaySize + dateVGap * (WEEK_FRAME_ROWS + 1);
//        weekRowHeight = WEEK_ROWS * calendarDaySize + dateVGap * (WEEK_ROWS + 1);
        touchableLocationList = new RectF[VISIBLE_DATE_INFO.length];
        for (int i = 0; i < VISIBLE_DATE_INFO.length; i++) {
            touchableLocationList[i] = new RectF();
        }
        buildVisibleDateInfo();
        return this;
    }

    private void buildVisibleDateInfo() {
        clearVisibleDateInfo();
        int firstDayOffset = findDayOffset();
        int lastDayOffset = firstDayOffset + totalDaysOfMonth - 1;
        for (int i = 0; i < VISIBLE_DATE_INFO.length; i++) {
            DateInfo info = new DateInfo();
            if (i < firstDayOffset) {
                info.range = DateInfo.LAST_MONTH;
                int lastMonth = month - 1 <= -1 ? 12 : month - 1;
                int lastYear = month - 1 <= -1 ? year - 1 : year;
                info.year = lastYear;
                info.month = lastMonth;
                info.date = getDaysInMonth(lastMonth, lastYear) - (firstDayOffset - i) + 1;
            } else if (i >= firstDayOffset && i <= lastDayOffset) {
                info.range = DateInfo.CURRENT_MONTH;
                info.year = year;
                info.month = month;
                info.date = i - firstDayOffset + 1;
            } else {
                info.range = DateInfo.NEXT_MONTH;
                int nextMonth = month + 1 >= 12 ? 1 : month + 1;
                int nextYear = month + 1 >= 12 ? year + 1 : year;
                info.year = nextYear;
                info.month = nextMonth;
                info.date = (i - lastDayOffset);
            }
            VISIBLE_DATE_INFO[i] = info;
        }

        //35个位置如果显示不下当月，判断今天日期是否一过第一周，是的话将第一周隐去
        if (lastDayOffset >= VISIBLE_DATE_INFO.length) {
            int todayOffset = today - 1 + firstDayOffset;
            if (todayOffset >= TOTAL_DAYS_OF_WEEK) {
                //shift left for a week
                for (int i = 0; i < VISIBLE_DATE_INFO.length - TOTAL_DAYS_OF_WEEK; i++) {
                    VISIBLE_DATE_INFO[i] = VISIBLE_DATE_INFO[i + TOTAL_DAYS_OF_WEEK];
                }
                for (int i = VISIBLE_DATE_INFO.length - TOTAL_DAYS_OF_WEEK; i < VISIBLE_DATE_INFO.length; i++) {
                    VISIBLE_DATE_INFO[i] = increase(VISIBLE_DATE_INFO[i], TOTAL_DAYS_OF_WEEK);
                }
            }
        }
    }

    private DateInfo increase(DateInfo info, int dayCount) {
        DateInfo newDateInfo = new DateInfo();
        int newDate = info.date + dayCount;
        int newMonth = info.month;
        int newYear = info.year;
        int newRange = info.range;
        if (newDate > totalDaysOfMonth) {
            newMonth = info.month + 1 >= 12 ? 1 : info.month + 1;
            newYear = info.month + 1 >= 12 ? info.year + 1 : info.year;
            newDate = newDate - totalDaysOfMonth;
            newRange = DateInfo.NEXT_MONTH;
        }
        newDateInfo.year = newYear;
        newDateInfo.month = newMonth;
        newDateInfo.date = newDate;
        newDateInfo.range = newRange;
        return newDateInfo;
    }

    private DateInfo decrease(DateInfo info, int dayCount) {
        DateInfo newDateInfo = new DateInfo();
        int newDate = info.date - dayCount;
        int newMonth = info.month;
        int newYear = info.year;
        int newRange = info.range;
        if (newDate <= 0) {
            newMonth = info.month - 1 <= -1 ? 12 : info.month - 1;
            newYear = info.month - 1 <= -1 ? info.year - 1 : info.year;
            int lastMonthTotalDay = getDaysInMonth(newMonth, newYear);
            newDate = lastMonthTotalDay + newDate;
            newRange = DateInfo.LAST_MONTH;
        }
        newDateInfo.year = newYear;
        newDateInfo.month = newMonth;
        newDateInfo.date = newDate;
        newDateInfo.range = newRange;
        return newDateInfo;
    }


    private void clearVisibleDateInfo() {
        for (int i = 0; i < VISIBLE_DATE_INFO.length; i++) {
            VISIBLE_DATE_INFO[i] = null;
        }
    }

    /**
     * 在构造view的时候传入图标，不会立即绘制，必须要调用了{@link this#show()}方法
     * @param dayOfMonth 日期， 1-31，根据当月的天数来决定最大天值，{@link this#setYearMonth(int, int)}
     * @param bitmap 日期上显示的图标
     * @return
     */
    public CalendarView appendDateIcon(int dayOfMonth, Bitmap bitmap) {
        for (int i = 0; i < VISIBLE_DATE_INFO.length; i++) {
            DateInfo info = VISIBLE_DATE_INFO[i];
            if (info.isCurrentMonth() && dayOfMonth == info.date) {
                info.icon = bitmap;
            }
        }
        return this;
    }

    /**
     * 开启调试模式，会绘制每个日期格子的范围
     * @param debug
     * @return
     */
    public CalendarView setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public CalendarView setOnDateClickListener(OnDateClickListener listener) {
        this.mOnDateClickListener = listener;
        return this;
    }

    /**
     * 在构造完view的时候传入图标，会立即绘制
     * @param dayOfMonth 日期， 1-31，根据当月的天数来决定最大天值，{@link this#setYearMonth(int, int)}
     * @param bitmap
     */
    public void setDateIcon(int dayOfMonth, Bitmap bitmap) {
        appendDateIcon(dayOfMonth, bitmap);
        invalidate();
    }

    public void show() {
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int availableWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
                - getPaddingRight();
        dayOfWeekGap = (int) ((availableWidth - datePaddingLeft * 2 - calendarDaySize * TOTAL_DAYS_OF_WEEK) / 6.0f + 0.5);
        dateHGap = (int) ((availableWidth - datePaddingLeft * 2 - calendarDaySize * TOTAL_DAYS_OF_WEEK) / 6.0f + 0.5f);
//        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), /*weekFrameHeight*/weekRowHeight + maskHeight);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (width * 0.635f + 0.5f);
        dateVGap = (int) ((height - maskHeight - calendarDaySize * WEEK_ROWS) * 1.0f / (WEEK_ROWS + 1) + 0.5f);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //1. draw day of week
        int left = 0;
        for (int i = 0; i < DAY_OF_WEEK.length; i++) {
            left = datePaddingLeft + i * (calendarDaySize + dayOfWeekGap) + (calendarDaySize - dayOfWeekWidth) / 2;
            canvas.drawText(DAY_OF_WEEK_LABEL[i], left,
                    maskHeight - titlePaddingTop - dayOfWeekFontMetrics.descent, dayOfWeekPaint);
        }


        //2. draw mask
        maskRect.left = 0;
        maskRect.top = 0;
        maskRect.right = canvas.getWidth();
        maskRect.bottom = maskHeight;
        canvas.drawRect(maskRect, maskPaint);

        //3. draw title
        canvas.drawText(title, titlePaddingLeft,
                titlePaddingTop + (titleFontMetrics.descent - titleFontMetrics.ascent)
                        - titleFontMetrics.descent,
                titlePaint);

        //4. draw days
        int top = 0;
        int dayOffset = 0;
        int weekOffset = 1;

        for (int i = 0; i < VISIBLE_DATE_INFO.length; i++) {
            left = datePaddingLeft + dayOffset * (calendarDaySize + dateHGap) + (calendarDaySize - dateWidth) / 2;

            top = (int) (
                    maskHeight + /*(weekFrameHeight - weekRowHeight) / 2*/dateVGap + (calendarDaySize + dateVGap) * (weekOffset
                            - 1) + calendarDaySize / 2 + dateHeight / 2
                            - datePaintFontMetrics.descent + 0.5f);

            if (touchableLocationList != null) {
                touchableLocationList[i].left = datePaddingLeft + dayOffset * (calendarDaySize + dateHGap);
                touchableLocationList[i].top = maskHeight + /*(weekFrameHeight - weekRowHeight) / 2*/dateVGap + (weekOffset - 1) * (calendarDaySize + dateVGap);
                touchableLocationList[i].right = touchableLocationList[i].left + calendarDaySize;
                touchableLocationList[i].bottom = touchableLocationList[i].top + calendarDaySize;
            }
            DateInfo info = VISIBLE_DATE_INFO[i];
            if (info.icon != null) {
                if (debug) {
                    canvas.drawRoundRect(touchableLocationList[i], CORNER_RADIUS, CORNER_RADIUS, maskPaint);
                }
                if (info.isCurrentMonth() && today == info.date) {
                    canvas.drawRoundRect(touchableLocationList[i], CORNER_RADIUS, CORNER_RADIUS, todayPaint);
                } else {
                    canvas.drawRoundRect(touchableLocationList[i], CORNER_RADIUS, CORNER_RADIUS, planDayPaint);
                }
            } else {
                if (info.isCurrentMonth()) {
                    canvas.drawText(DATE_OF_STRING[info.date - 1], left, top, datePaint);
                } else {
                    canvas.drawText(DATE_OF_STRING[info.date - 1], left, top, datePaint2);
                }
            }
            dayOffset++;
            if (dayOffset == TOTAL_DAYS_OF_WEEK) {
                dayOffset = 0;
                weekOffset++;
            }
        }


        //5. draw events img
        for (int i = 0; i < VISIBLE_DATE_INFO.length; i++) {
            if (VISIBLE_DATE_INFO[i].icon != null) {
                canvas.drawBitmap(VISIBLE_DATE_INFO[i].icon, null, touchableLocationList[i], null);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (debug) {
            Log.d("View", "touch " + x + ", " + y);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (touchableLocationList != null) {
                for (int i = 0; i < touchableLocationList.length; i++) {
                    RectF location = touchableLocationList[i];
                    Bitmap icon = VISIBLE_DATE_INFO[i].icon;
                    if (location != null && icon != null) {
                        if (location.contains(x, y)) {
                            if (debug) {
                                Log.d("View", "click " + VISIBLE_DATE_INFO[i].date);
                            }
                            if (mOnDateClickListener != null) {
                                mOnDateClickListener.onDateClick(VISIBLE_DATE_INFO[i].date);
                            }
                            break;
                        }
                    }
                }
            }
        }
        return true;
    }

    private int getDaysInMonth(int month, int year) {
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.MARCH:
            case Calendar.MAY:
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.OCTOBER:
            case Calendar.DECEMBER:
                return 31;
            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:
                return 30;
            case Calendar.FEBRUARY:
                return (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) ? 29 : 28;
            default:
                return 0;
        }
    }

    public int getCalendarDaySize() {
        return calendarDaySize;
    }

    private int calculateWeekRows() {
        int offset = findDayOffset();
        int dividend = (offset + totalDaysOfMonth) / TOTAL_DAYS_OF_WEEK;
        int remainder = (offset + totalDaysOfMonth) % TOTAL_DAYS_OF_WEEK;
        return (dividend + (remainder > 0 ? 1 : 0));
    }

    private int findDayOffset() {
        return (firstDayOfWeekOfMonth < firstDayOfWeek ? (firstDayOfWeekOfMonth + TOTAL_DAYS_OF_WEEK)
                : firstDayOfWeekOfMonth) - firstDayOfWeek;
    }

    public interface OnDateClickListener {

        /**
         *
         * @param dayOfMonth 日期， 1-31，根据当月的天数来决定最大天值，{@link this#setYearMonth(int, int)}
         */
        void onDateClick(int dayOfMonth);
    }

    public static class DateInfo {

        public static final int LAST_MONTH = 0;
        public static final int CURRENT_MONTH = 1;
        public static final int NEXT_MONTH = 2;

        int year;

        /** 1-31 */
        int date;

        /** 1-12 */
        int month;

        /**
         * {@link #LAST_MONTH}<br/>
         * {@link #CURRENT_MONTH}<br/>
         * {@link #NEXT_MONTH}<br/>
         */
        int range;

        Bitmap icon;

        boolean isCurrentMonth() {
            return range == CURRENT_MONTH;
        }

    }

}
