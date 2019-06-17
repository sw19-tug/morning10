package at.tugraz.ist.swe.cheat;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import at.tugraz.ist.swe.cheat.dto.CustomMessage;
import at.tugraz.ist.swe.cheat.dto.Device;
import at.tugraz.ist.swe.cheat.dto.ProxyBitmap;
import at.tugraz.ist.swe.cheat.util.ConverterClassByte;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static at.tugraz.ist.swe.cheat.BitmapMatcher.withBitmap;
import static at.tugraz.ist.swe.cheat.dto.Provider.STATE_CONNECTED;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SendImageTests {

    @Rule
    public IntentsTestRule<MainActivity> mainActivityTestRule = new IntentsTestRule<>(MainActivity.class);

    private Bitmap mockedImage;

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

        createMockedImage();
        intending(hasAction(Intent.ACTION_PICK)).respondWith(getMockedImageResult());
    }

    private void createMockedImage() {
        mockedImage = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888);
        mockedImage.eraseColor(Color.MAGENTA);
        File dir = mainActivityTestRule.getActivity().getExternalCacheDir();
        File file = new File(dir.getPath(), "mockedImage.jpeg");
        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(file);
            mockedImage.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Instrumentation.ActivityResult getMockedImageResult() {
        Intent resultData = new Intent();
        File dir = mainActivityTestRule.getActivity().getExternalCacheDir();
        File file = new File(dir.getPath(), "mockedImage.jpeg");
        Uri uri = Uri.fromFile(file);
        resultData.setData(uri);
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
    }



    @Test  // Test if the button is displayed
    public void testButtonVisible() {
        onView(withId(R.id.bt_sendImage)).check(matches(isDisplayed()));
    }

    @Test  // Test if gallery intend is shown
    public void testGalleryIntend() {
        onView(withId(R.id.bt_sendImage)).perform(click());
        intended(hasAction(Intent.ACTION_PICK));
    }

    @Test  // Test sent images displayed
    public void testSentImageDisplayed () {
        onView(withId(R.id.bt_sendImage)).perform(click());
        onView(withId(R.id.tv_message_img)).check(matches(withBitmap(mockedImage)));
    }

    @Test
    public void convertBitmapTest()
    {

        Bitmap mockedImage = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888);
        ChatMessage message = new ChatMessage(1, "User", mockedImage, new Date());
        CustomMessage fullmessage = new CustomMessage(STATE_CONNECTED,
                new Device("test", "00:00:00:00"),message);

        try {
            byte[] buffer = ConverterClassByte.toByteArray(fullmessage);
            CustomMessage customMessage = (CustomMessage) ConverterClassByte.toObject(buffer);

            assertEquals(fullmessage.getMessage().getImage().getHeight(), customMessage.getMessage().getImage().getHeight());
            assertEquals(fullmessage.getMessage().getImage().getWidth(), customMessage.getMessage().getImage().getWidth());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}