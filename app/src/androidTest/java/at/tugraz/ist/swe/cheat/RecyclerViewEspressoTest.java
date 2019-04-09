package at.tugraz.ist.swe.cheat;

import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
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
    public void testRecyclerViewElements() {
        onView(allOf(withId(R.id.tv_message), withText("Bonjour"))).check(matches(isDisplayed()));
    }

    // Check if Click on RecyclerView element triggers an action (toast)
    @Test
    public void testRecyclerViewElementClick() {
        onView(allOf(withId(R.id.tv_message), withText("Bonjour"))).perform(click());
        onView(withId(R.id.tf_input)).check(matches((withText("Bonjour"))));
    }

}
