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
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class HighlightMessageEspressoTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test  // Test if the button is displayed
    public void testMessageHighlight(){
        final String message1 = "I am important";
        final String message2 = "I am important too";

        onView(withId(R.id.tf_input)).perform(replaceText(message1));
        onView(withId(R.id.bt_send)).perform(click());
        onView(withId(R.id.tf_input)).perform(replaceText(message2));
        onView(withId(R.id.bt_send)).perform(click());
        onView(allOf(withId(R.id.tv_message), withText(message1))).perform(click());
        onView(allOf(withId(R.id.tv_message), withText(message2))).perform(click());

        onView(withChild(allOf(withId(R.id.tv_message), withText(message1)))).check(
                matches(withResourceName("rv_message_sent_highlighted")));
        onView(withChild(allOf(withId(R.id.tv_message), withText(message2)))).check(
                matches(withResourceName("rv_message_recieved_highlighted")));
    }
}
