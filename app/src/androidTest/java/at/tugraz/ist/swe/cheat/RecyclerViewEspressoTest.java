package at.tugraz.ist.swe.cheat;

import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import at.tugraz.ist.swe.cheat.dto.CustomMessage;
import at.tugraz.ist.swe.cheat.dto.Device;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static at.tugraz.ist.swe.cheat.dto.Provider.STATE_CONNECTED;
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
public class RecyclerViewEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);


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
    }
    // Check if RecyclerView is displayed
    @Test
    public void testRecyclerViewVisibility () {
        onView(withId(R.id.rv_chat_history)).check(matches(isDisplayed()));
    }

    // Check if RecyclerView contains elements
    @Test
    public void testRecyclerAddMessage () {
        onView(withId(R.id.tf_input)).perform(replaceText("Dude where's my car?!"));
        onView(withId(R.id.bt_send)).perform(click());
        onView(withId(R.id.tv_message)).check(matches(isDisplayed()));
    }

    // Check if deleting a message works
    @Test
    public void testDeleteMessage () {
        final String message = "Hello World";

        onView(withId(R.id.tf_input)).perform(replaceText(message));
        onView(withId(R.id.bt_send)).check(matches(withText("Send")));
        onView(withId(R.id.bt_send)).perform(click());

        onView(allOf(withId(R.id.tv_message), withText(message))).perform(longClick());
        onView(withText("Delete")).perform(click());
        SystemClock.sleep(100);
        onView(allOf(withId(R.id.tv_message), withText(message))).check(doesNotExist());
        onView(allOf(withId(R.id.tv_message), withText("This message was deleted"))).check(matches(isDisplayed()));
    }

    // Check if RecyclerView displays timestamp
    @Test
    public void testRecyclerViewElementTimeStamp () {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
        String date_string = formatter.format(date);

        onView(withId(R.id.tf_input)).perform(replaceText("Dude where's my car?!"));
        onView(withId(R.id.bt_send)).perform(click());

        onView(withChild(allOf(withId(R.id.tv_timestamp), withText(date_string)))).check(matches(isDisplayed()));
    }

    @Test
    public void testPartnerText() {

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {

                CustomMessage fullmessage = new CustomMessage(STATE_CONNECTED,
                        new Device("Dummy Device","00:11:22:AA:BB:CC"),
                        new ChatMessage("user", "Dummy Text", new Date()));

                bluetoothDeviceManager.getBluetoothDeviceProvider().received(fullmessage);
            }
        });


        onView(withChild(allOf(withId(R.id.tv_message), withText("Dummy Text")))).check(
                matches(withResourceName("rv_message_recieved")));
    }
}
