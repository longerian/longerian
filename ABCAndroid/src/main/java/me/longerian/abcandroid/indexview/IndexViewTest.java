package me.longerian.abcandroid.indexview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import me.longerian.abcandroid.R;

/**
 * Created by longerian on 15/8/28.
 */
public class IndexViewTest extends Activity {

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.activity_index_view);
        OrderLayout orderLayout = (OrderLayout)findViewById(R.id.order_test);
        orderLayout.setDrawOrder(2);

        final IndexView indexView = (IndexView) findViewById(R.id.index);
        indexView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (count) {
                    case 0:
                        indexView.setIndex(50);
                        break;
                    case 1:
                        indexView.setStartIndexColor(Color.parseColor("#1052CB"));
                        break;
                    case 2:
                        indexView.setEndIndexColor(Color.parseColor("#89E35E"));
                        break;
                    case 3:
                        indexView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));
                        break;
                }
                count++;
            }
        });
        MaskView maskView = (MaskView) findViewById(R.id.image1);

        ArrayMap<String, String> testMap = new ArrayMap<String, String>();
        testMap.put(null, "null string");
        Log.d("Longer", testMap.toString());

        TimerView tips = (TimerView) findViewById(R.id.tips);
        tips.startCountdown("结束", 1486279920000l);

        Bitmap sampleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendar);
        calendarView.setTimestamp(System.currentTimeMillis()).setDebug(false)
                .appendDateIcon(1, sampleBitmap).appendDateIcon(3, sampleBitmap).appendDateIcon(19, sampleBitmap).show();
    }
}
