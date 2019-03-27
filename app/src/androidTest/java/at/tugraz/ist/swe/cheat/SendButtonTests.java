package at.tugraz.ist.swe.cheat;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SendButtonTests {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test  // Test if the button is displayed
    public void testButtonVisible() {
        onView(withId(R.id.bt_send)).check(matches(isDisplayed()));
    }

    @Test  // Test if the button is disabled when the input is empty
    public void testInputEmptyButtonDisabled() {
        onView(withId(R.id.tf_input)).perform(replaceText(""));
        onView(withId(R.id.bt_send)).check(matches(not(isEnabled())));
    }

    @Test  // Test if the button is enabled when the input is not empty
    public void testInputNotEmptyButtonEnabled() {
        onView(withId(R.id.tf_input)).perform(replaceText("Some Message!"));
        onView(withId(R.id.bt_send)).check(matches(isEnabled()));
    }

    @Test  // Test if the input is cleared when the button is clicked
    public void testButtonClickClearsInput() {
        onView(withId(R.id.bt_send)).perform(click());
        onView(withId(R.id.tf_input)).check(matches((withText(""))));
    }

    @Test  // Test that the button starts disabled
    public void testStartButtonDisabled() {
        onView(withId(R.id.bt_send)).check(matches(not(isEnabled())));
    }
}
