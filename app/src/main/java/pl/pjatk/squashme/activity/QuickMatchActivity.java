package pl.pjatk.squashme.activity;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.pjatk.squashme.R;
import pl.pjatk.squashme.di.component.DaggerQuickMatchActivityComponent;
import pl.pjatk.squashme.di.module.RoomModule;
import pl.pjatk.squashme.fragment.CreateQuickMatchFragment;
import pl.pjatk.squashme.fragment.RefereeModeFragment;
import pl.pjatk.squashme.model.custom.MatchWithPlayers;
import pl.pjatk.squashme.service.MatchService;

/**
 * Quick match activity class - decides which fragment should be visible based on quick match data.
 */
public class QuickMatchActivity extends BaseActivity {

    @Inject
    public MatchService matchService;
    private CompositeDisposable disposables;

    /**
     * Gets current quick match from the match service.
     *
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_match);

        DaggerQuickMatchActivityComponent.builder()
                .roomModule(new RoomModule(getApplication()))
                .build()
                .inject(this);

        disposables = new CompositeDisposable();
        disposables.add(Observable.fromCallable(matchService::getCurrentQuickMatchWithPlayers)
                .subscribeOn(Schedulers.io())
                .subscribe(m -> prepareFragment(m.orElse(null))));
    }

    /**
     * Prepares and puts new fragment in the container.
     *
     * If there is no active quick match then it's forwarded to create quick match form.
     * If there is current quick match then it's forwarded to referee mode view.
     *
     * @param currentMatch current quick match - can be null when there is no active quick match
     */
    private void prepareFragment(MatchWithPlayers currentMatch) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (currentMatch == null) {
            fragmentTransaction.add(R.id.fragment_quick_match, CreateQuickMatchFragment.class, null);
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable("match", currentMatch);
            if (currentMatch.getMatch().isRefereeMode()) {
                fragmentTransaction.add(R.id.fragment_quick_match, RefereeModeFragment.class, bundle);
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