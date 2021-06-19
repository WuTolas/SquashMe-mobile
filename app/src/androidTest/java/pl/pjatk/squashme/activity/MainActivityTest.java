package pl.pjatk.squashme.activity;


import android.app.Activity;
import android.widget.Button;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicReference;

import pl.pjatk.squashme.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);
    private Activity activity;

    @Before
    public void setup() {
        activity = getActivity(rule);
        Intents.init();
    }

    private <T extends Activity> T getActivity(ActivityScenarioRule<T> activityScenarioRule) {
        AtomicReference<T> activityRef = new AtomicReference<>();
        activityScenarioRule.getScenario().onActivity(activityRef::set);
        return activityRef.get();
    }

    @Test
    public void buttons_should_containSpecificText() {
        Button tournamentButton = activity.findViewById(R.id.tournament);
        Button historyButton = activity.findViewById(R.id.history);
        Button settingsButton = activity.findViewById(R.id.settings);
        Button quickMatchButton = activity.findViewById(R.id.quick_match);

        assertThat(tournamentButton.getText().toString(), is(activity.getString(R.string.tournament)));
        assertThat(historyButton.getText().toString(), is(activity.getString(R.string.match_history)));
        assertThat(settingsButton.getText().toString(), is(activity.getString(R.string.settings)));
        assertThat(quickMatchButton.getText().toString(), is(activity.getString(R.string.quick_match)));
    }

    @Test
    public void quickMatchButton_should_startQuickMatchActivity() {
        onView(withId(R.id.quick_match)).perform(click());
        intended(hasComponent(QuickMatchActivity.class.getName()));
    }

    @Test
    public void tournamentButton_should_startTournamentActivity() {
        onView(withId(R.id.tournament)).perform(click());
        intended(hasComponent(TournamentActivity.class.getName()));
    }

    @Test
    public void matchHistoryButton_should_startHistoryActivity() {
        onView(withId(R.id.history)).perform(click());
        intended(hasComponent(HistoryActivity.class.getName()));
    }

    @Test
    public void settingsButton_should_startSettingsActivity() {
        onView(withId(R.id.settings)).perform(click());
        intended(hasComponent(SettingsActivity.class.getName()));
    }

    @After
    public void destroy() {
        Intents.release();
    }
}
