package me.longerian.abcandroid.indexview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import android.widget.TextView;
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
        TextView button1 = (TextView)findViewById(R.id.button1);
        TextView button2 = (TextView)findViewById(R.id.button2);
        TextView button3 = (TextView)findViewById(R.id.button3);
        TextView button4 = (TextView)findViewById(R.id.button4);
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Longer", "click 1");
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Longer", "click 2");
            }
        });
        button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Longer", "click 3");
            }
        });
        button4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Longer", "click 4");
            }
        });


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
