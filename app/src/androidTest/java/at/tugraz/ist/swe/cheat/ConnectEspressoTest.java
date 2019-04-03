package at.tugraz.ist.swe.cheat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasBackground;
import static android.support.test.espresso.matcher.ViewMatchers.hasTextColor;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

/**aa
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ConnectEspressoTest {


    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    BluetoothDeviceManager bluetoothDeviceManager;
    @Before
    public void setUp() {
        bluetoothDeviceManager = new BluetoothDeviceManager(new DummyBluetoothDeviceProvider());
    }

    //there must be a connect button
    @Test
    public void connectButtonVisible() {
        onView(withId(R.id.bt_connect)).check(matches(isDisplayed()));
    }

    //bluetooth must be in state ON if connect button is clicked
    @Test
    public void connectButtonBluetoothOn() {
        Toolbar myToolbar = (Toolbar) mainActivityTestRule.getActivity().findViewById(R.id.menu);
        MenuItem btConnect =  (MenuItem)myToolbar.getMenu().findItem(R.id.bt_connect);

        onView(withId(R.id.bt_connect)).perform(click());
        assertEquals(BluetoothDeviceManager.BtState.ON, bluetoothDeviceManager.getBtState());
    }

    //there color of the app bar must change if a connection is established
    @Test
    public void appBarColorChange() {

        Toolbar myToolbar = (Toolbar) mainActivityTestRule.getActivity().findViewById(R.id.menu);
        MenuItem btConnect =  (MenuItem)myToolbar.getMenu().findItem(R.id.bt_connect);

        // toolbar color should be white at first (not connected)
        assertEquals(((ColorDrawable)myToolbar.getBackground()).getColor(), 0xffffffff);

        onView(withId(R.id.bt_connect)).perform(click());

        Context context = myToolbar.findViewById(R.id.menu).getContext();
        Drawable connected = context.getResources().getDrawable(R.drawable.ic_wifi_tethering_black_24dp);
        Drawable notConnected = context.getResources().getDrawable(R.drawable.ic_portable_wifi_off_black_24dp);

        // after click on connect button toolbar should be green and connected icon should be shown
        assertEquals(((ColorDrawable)myToolbar.getBackground()).getColor(), 0xff66bb6a);
        assertEquals(btConnect.getIcon().getConstantState(), connected.getConstantState());

        onView(withId(R.id.bt_connect)).perform(click());

        // after click on connect button toolbar should be white and notConnected button shown again
        assertEquals(((ColorDrawable)myToolbar.getBackground()).getColor(), 0xffffffff);
        assertEquals(btConnect.getIcon().getConstantState(), notConnected.getConstantState());
    }

}
