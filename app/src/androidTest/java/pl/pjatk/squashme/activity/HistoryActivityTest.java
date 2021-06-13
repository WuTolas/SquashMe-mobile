package pl.pjatk.squashme.activity;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.pjatk.squashme.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class HistoryActivityTest {

    @Rule
    public ActivityScenarioRule<HistoryActivity> rule = new ActivityScenarioRule<>(HistoryActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @Test
    public void activity_should_beOnHistoryOptionsFragment() {
        onView(withId(R.id.btn_quickmatch_history)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_tournament_history)).check(matches(isDisplayed()));
    }

    @After
    public void destroy() {
        Intents.release();
    }
}