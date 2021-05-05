package pl.pjatk.squashme.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.pjatk.squashme.R;
import pl.pjatk.squashme.dao.MatchDao;
import pl.pjatk.squashme.database.AppDatabase;
import pl.pjatk.squashme.fragment.CreateQuickMatchFragment;
import pl.pjatk.squashme.fragment.QuickScoreModeFragment;
import pl.pjatk.squashme.fragment.RefereeModeFragment;
import pl.pjatk.squashme.model.Match;

public class QuickMatchActivity extends AppCompatActivity {

    private CompositeDisposable disposables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_match);

        disposables = new CompositeDisposable();
        MatchDao matchDao = AppDatabase.getInstance(getApplicationContext()).matchDao();
        disposables.add(Observable.fromCallable(matchDao::getCurrentQuickMatch)
                .subscribeOn(Schedulers.io())
                .subscribe(m -> {
                    prepareFragment(m.orElse(null));
                }));
    }

    private void prepareFragment(Match currentMatch) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (currentMatch == null) {
            fragmentTransaction.add(R.id.fragment_quick_match, CreateQuickMatchFragment.class, null);
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable("match", currentMatch);
            if (currentMatch.isRefereeMode()) {
                fragmentTransaction.add(R.id.fragment_quick_match, RefereeModeFragment.class, bundle);
            } else {
                fragmentTransaction.add(R.id.fragment_quick_match, QuickScoreModeFragment.class, bundle);
            }
        }
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }
}