package at.tugraz.ist.swe.cheat;

import android.support.test.annotation.UiThreadTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
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
    public void testRecyclerViewVisibility() {
        onView(withId(R.id.rv_messages)).check(matches(isDisplayed()));
    }

    // Check if RecyclerView contains elements
    @Test
    public void testRecyclerAddMessage() {
        onView(withId(R.id.tf_input)).perform(replaceText("Dude where's my car?!"));
        onView(withId(R.id.bt_send)).perform(click());
        onView(withId(R.id.tv_message)).check(matches(isDisplayed()));
    }

    // Check if Click on RecyclerView element triggers an action
    @Test
    public void testRecyclerViewElementClick() {
        final int id = 1;
        final String senderAddress = "00:11:22:33:44";
        final String message = "Hello World";
        final Date timeStamp = new Date();
        ChatMessage messageObj = new ChatMessage(id, senderAddress, message, timeStamp);

        mainActivityTestRule.getActivity().adapter.addMessage(messageObj);

        onView(allOf(withId(R.id.tv_message), withText(message))).perform(click());
        onView(withId(R.id.tf_input)).check(matches((withText(message))));
    }

    // Check if RecyclerView elements have different background colors
    @Test
    public void testRecyclerViewElementBackgroundColors() {
        final String text_1 = "Hola";
        final String text_2 = "Bonjour";

        ChatMessage message_1 =
                new ChatMessage(1, "00:11:22:33:44", text_1, new Date());
        ChatMessage message_2 =
                new ChatMessage(2, "00:00:00:00:00", text_2, new Date());

        mainActivityTestRule.getActivity().adapter.addMessage(message_1);
        mainActivityTestRule.getActivity().adapter.addMessage(message_2);

        onView(withChild(allOf(withId(R.id.tv_message), withText(text_1)))).check(
                matches(withResourceName("rv_message_recieved")));
        onView(withChild(allOf(withId(R.id.tv_message), withText(text_2)))).check(
                matches(withResourceName("rv_message_sent")));
    }

}
