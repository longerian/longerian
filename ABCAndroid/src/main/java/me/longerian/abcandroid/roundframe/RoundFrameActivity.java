package me.longerian.abcandroid.roundframe;

import android.app.Activity;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Log;
import me.longerian.abcandroid.R;

/**
 * Created by longerian on 2017/9/13.
 *
 * @author longerian
 * @date 2017/09/13
 */

public class RoundFrameActivity extends Activity {

    RoundFrameLayout roundFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_layout);
        roundFrame = (RoundFrameLayout)findViewById(R.id.round_frame);
        if (VERSION.SDK_INT >= 21) {
            roundFrame.setClipToOutline(true);
        }
        Log.d("Longer", Build.MODEL);
    }

}
