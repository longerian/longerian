package me.longerian.abcandroid;

import com.facebook.testing.screenshot.Screenshot;
import com.facebook.testing.screenshot.ViewHelpers;

import android.test.InstrumentationTestCase;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by longerian on 16/10/23.
 */

public class ScreenShotTest extends InstrumentationTestCase {

    public void testActivityIndexView() {
        LayoutInflater inflater = LayoutInflater.from(getInstrumentation().getTargetContext());
        View view = inflater.inflate(R.layout.activity_index_view, null, false);
        ViewHelpers.setupView(view).setExactWidthPx(1080).layout();
        Screenshot.snap(view).record();
    }

}
