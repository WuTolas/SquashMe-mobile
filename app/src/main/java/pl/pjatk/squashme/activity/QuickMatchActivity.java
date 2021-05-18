package pl.pjatk.squashme.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.pjatk.squashme.R;
import pl.pjatk.squashme.di.component.DaggerQuickMatchActivityComponent;
import pl.pjatk.squashme.di.module.RoomModule;
import pl.pjatk.squashme.fragment.CreateQuickMatchFragment;
import pl.pjatk.squashme.fragment.QuickScoreModeFragment;
import pl.pjatk.squashme.fragment.RefereeModeFragment;
import pl.pjatk.squashme.model.MatchWithPlayers;
import pl.pjatk.squashme.service.MatchService;

public class QuickMatchActivity extends AppCompatActivity {

    @Inject
    public MatchService matchService;
    private CompositeDisposable disposables;

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
                .subscribe(m -> {
                    prepareFragment(m.orElse(null));
                }));
    }

    private void prepareFragment(MatchWithPlayers currentMatch) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (currentMatch == null) {
            fragmentTransaction.add(R.id.fragment_quick_match, CreateQuickMatchFragment.class, null);
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable("match", currentMatch);
            if (currentMatch.getMatch().isRefereeMode()) {
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