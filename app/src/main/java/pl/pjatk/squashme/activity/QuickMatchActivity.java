package pl.pjatk.squashme.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.AsyncTask;
import android.os.Bundle;

import java.lang.ref.WeakReference;

import pl.pjatk.squashme.R;
import pl.pjatk.squashme.dao.MatchDao;
import pl.pjatk.squashme.database.AppDatabase;
import pl.pjatk.squashme.fragment.CreateQuickMatchFragment;
import pl.pjatk.squashme.fragment.QuickScoreModeFragment;
import pl.pjatk.squashme.fragment.RefereeModeFragment;
import pl.pjatk.squashme.model.Match;

public class QuickMatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_match);
        MatchDao matchDao = AppDatabase.getInstance(getApplicationContext()).matchDao();
        GetCurrentQuickMatchTask currentQuickMatchTask = new GetCurrentQuickMatchTask(this, matchDao);
        currentQuickMatchTask.execute();
    }

    private void afterMatchGet(Match currentMatch) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (currentMatch == null) {
            fragmentTransaction.add(R.id.fragment_quick_match, CreateQuickMatchFragment.class, null);
        } else {
            if (currentMatch.isRefereeMode()) {
                fragmentTransaction.add(R.id.fragment_quick_match, RefereeModeFragment.class, null);
            } else {
                fragmentTransaction.add(R.id.fragment_quick_match, QuickScoreModeFragment.class, null);
            }
        }
        fragmentTransaction.commit();
    }

    private static class GetCurrentQuickMatchTask extends AsyncTask<Void, Void, Match> {

        private final WeakReference<QuickMatchActivity> activityRef;
        private final MatchDao matchDao;

        public GetCurrentQuickMatchTask(QuickMatchActivity quickMatchActivity, MatchDao matchDao) {
            this.activityRef = new WeakReference<>(quickMatchActivity);
            this.matchDao = matchDao;
        }

        @Override
        protected Match doInBackground(Void... voids) {
            return matchDao.getCurrentQuickMatch();
        }

        @Override
        protected void onPostExecute(Match match) {
            super.onPostExecute(match);
            if (activityRef != null) {
                activityRef.get().afterMatchGet(match);
            }
        }
    }
}