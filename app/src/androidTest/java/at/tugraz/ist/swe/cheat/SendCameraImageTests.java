package at.tugraz.ist.swe.cheat;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static at.tugraz.ist.swe.cheat.BitmapMatcher.withBitmap;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SendCameraImageTests {

    @Rule
    public IntentsTestRule<MainActivity> mainActivityTestRule = new IntentsTestRule<>(MainActivity.class);

    BluetoothDeviceManager bluetoothDeviceManager;

    @Before
    public void setUp() {

        bluetoothDeviceManager = mainActivityTestRule.getActivity().bluetoothDeviceManager;

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                bluetoothDeviceManager.getBluetoothDeviceProvider().connectToDevice("00:11:22:AA:BB:CC");
                bluetoothDeviceManager.getBluetoothDeviceProvider().connected();
            }
        });

        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(getMockedImageResult());
    }

    private Instrumentation.ActivityResult getMockedImageResult() {
        Intent resultData = new Intent();
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
    }


    @Test  // Test if the button is displayed
    public void testButtonVisible() {
        onView(withId(R.id.bt_sendCameraImage)).check(matches(isDisplayed()));
    }

    @Test  // Test if gallery intend is shown
    public void testCameraIntendWasShown() {
        onView(withId(R.id.bt_sendCameraImage)).perform(click());
        intended(hasAction(MediaStore.ACTION_IMAGE_CAPTURE));
    }
}