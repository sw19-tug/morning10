package at.tugraz.ist.swe.cheat;

import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
public class TextToSpeechTest {

        @Rule
        public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);


        // Check if Read to me is visible
        @Test
        public void optionDisplayed() {
            final String message = "Hello World";
            onView(withId(R.id.tf_input)).perform(replaceText(message));
            onView(withId(R.id.bt_send)).perform(click());

            onView(allOf(withId(R.id.tv_message), withText(message))).perform(longClick());
            onView(withText("Read to me")).check(matches(isDisplayed()));
        }
/*
        // Check if reading a message works
        @Test
        public void testEditMessage () {
            final String message = "Hello World";

            onView(withId(R.id.tf_input)).perform(replaceText(message));
            onView(withId(R.id.bt_send)).perform(click());

            onView(allOf(withId(R.id.tv_message), withText(message))).perform(longClick());
            onView(withText("Read to me")).perform(click());


        }
*/

}
