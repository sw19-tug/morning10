package at.tugraz.ist.swe.cheat;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

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

    //there must be a connect button
    @Test
    public void connectButtonVisible() {
        onView(withId(R.id.bt_connect)).check(matches(isDisplayed()));
    }

    //there color of the app bar must change if a connection is established
    @Test
    public void appBarColorChange() {

        Toolbar myToolbar = (Toolbar) mainActivityTestRule.getActivity().findViewById(R.id.menu);

        assertEquals(((ColorDrawable)myToolbar.getBackground()).getColor(), 0xffffffff);

        onView(withId(R.id.bt_connect)).perform(click());

        assertEquals(((ColorDrawable)myToolbar.getBackground()).getColor(), 0xff66bb6a);

        onView(withId(R.id.bt_connect)).perform(click());

        assertEquals(((ColorDrawable)myToolbar.getBackground()).getColor(), 0xffffffff);
    }

}
