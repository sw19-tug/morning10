package at.tugraz.ist.swe.cheat;

import android.os.SystemClock;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
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
public class RecyclerViewEspressoTest {

        @Rule
        public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

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

        // Check if LongClick triggers the context menu
        @Test
        public void optionsDialogVisible() {
            final String message = "Hello World";
            onView(withId(R.id.tf_input)).perform(replaceText(message));
            onView(withId(R.id.bt_send)).perform(click());

            onView(allOf(withId(R.id.tv_message), withText(message))).perform(longClick());
            onView(withText("Choose an action")).check(matches(isDisplayed()));
            onView(withText("Edit")).check(matches(isDisplayed()));
            onView(withText("Delete")).check(matches(isDisplayed()));

            onView(withText("CANCEL")).perform(click());
        }

        // Check if editing a message works
        @Test
        public void testEditMessage () {
            final String message = "Hello World";
            final String new_message = "Hola El Mundo";

            onView(withId(R.id.tf_input)).perform(replaceText(message));
            onView(withId(R.id.bt_send)).check(matches(withText("Send")));
            onView(withId(R.id.bt_send)).perform(click());

            onView(allOf(withId(R.id.tv_message), withText(message))).perform(longClick());
            onView(withText("Edit")).perform(click());

            onView(withId(R.id.bt_send)).check(matches(withText("Edit")));
            onView(withId(R.id.tf_input)).check(matches(withText(message)));
            onView(withId(R.id.tf_input)).perform(replaceText(new_message));
            onView(withId(R.id.bt_send)).perform(click());

            onView(withId(R.id.bt_send)).check(matches(withText("Send")));
            onView(allOf(withId(R.id.tv_message), withText(new_message))).check(matches(isDisplayed()));
            onView(allOf(withId(R.id.tv_message), withText(message))).check(doesNotExist());
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
        }

        // Check if RecyclerView elements have different background colors
        @Test
        public void testRecyclerViewElementBackgroundColors () {
            final String text_1 = "Hola";
            final String text_2 = "Bonjour";

            onView(withId(R.id.tf_input)).perform(replaceText(text_1));
            onView(withId(R.id.bt_send)).perform(click());
            onView(withId(R.id.tf_input)).perform(replaceText(text_2));
            onView(withId(R.id.bt_send)).perform(click());

            onView(withChild(allOf(withId(R.id.tv_message), withText(text_1)))).check(
                    matches(withResourceName("rv_message_sent")));
            onView(withChild(allOf(withId(R.id.tv_message), withText(text_2)))).check(
                    matches(withResourceName("rv_message_recieved")));
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
}
