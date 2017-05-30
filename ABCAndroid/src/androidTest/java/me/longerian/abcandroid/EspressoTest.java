package me.longerian.abcandroid;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.View;

import me.longerian.abcandroid.indexview.IndexViewTest;
import me.longerian.abcandroid.indexview.MaskView;

/**
 * Created by longerian on 16/10/26.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EspressoTest {

    private String mStringToBetyped;

    @Before
    public void initValidString() {
        // Specify a valid string.
        mStringToBetyped = "Espresso";
    }

    @Rule
    public ActivityTestRule<IndexViewTest> mActivityTestRule = new ActivityTestRule<IndexViewTest>(IndexViewTest.class);

    @Test
    public void testCheckVisiblity() {
        Log.i("EspressoTest", mStringToBetyped);
        Espresso.onView(ViewMatchers.withId(R.id.image2)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void checkContentDescription() {
        Log.i("EspressoTest", mStringToBetyped);
        Espresso.onView(ViewMatchers.withId(R.id.image1)).check(ViewAssertions.matches(ViewMatchers.hasContentDescription()));
    }

    @Test
    public void checkWidthHeight() {
        Espresso.onView(ViewMatchers.withId(R.id.image1)).check(ViewAssertions.matches(
                new Matcher<View>() {
                    @Override
                    public boolean matches(Object o) {
                        Log.i("EspressoTest", "matches " + o);
                        if (! (o instanceof MaskView)) {
                            return false;
                        }
                        MaskView view = (MaskView) o;
                        int width = view.getWidth();
                        int height = view.getHeight();
                        Log.i("EspressoTest", "matches width height " + width + "/" + height);
                        return width == 450 && height == 450;
                    }

                    @Override
                    public void describeMismatch(Object o, Description description) {
                        Log.i("EspressoTest", "describeMismatch " + o + " " + description);

                    }

                    @Override
                    public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
                        Log.i("EspressoTest", "_dont_implement_Matcher___instead_extend_BaseMatcher_");

                    }

                    @Override
                    public void describeTo(Description description) {
                        Log.i("EspressoTest", "describeTo");

                    }
                }));
    }

}
