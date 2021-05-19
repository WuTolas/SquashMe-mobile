package pl.pjatk.squashme.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.pjatk.squashme.R;
import pl.pjatk.squashme.di.component.DaggerTournamentActivityComponent;
import pl.pjatk.squashme.di.module.RoomModule;
import pl.pjatk.squashme.fragment.CreateTournamentFragment;
import pl.pjatk.squashme.fragment.SignPlayersFragment;
import pl.pjatk.squashme.fragment.TournamentHubFragment;
import pl.pjatk.squashme.model.Tournament;
import pl.pjatk.squashme.model.TournamentStatus;
import pl.pjatk.squashme.service.TournamentService;

public class TournamentActivity extends AppCompatActivity {

    @Inject
    public TournamentService tournamentService;
    private CompositeDisposable disposables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);

        DaggerTournamentActivityComponent.builder()
                .roomModule(new RoomModule(getApplication()))
                .build()
                .inject(this);

        disposables = new CompositeDisposable();
        disposables.add(Observable.fromCallable(tournamentService::getCurrentTournament)
                .subscribeOn(Schedulers.io())
                .subscribe(t -> prepareFragment(t.orElse(null)))
        );
    }

    private void prepareFragment(Tournament currentTournament) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (currentTournament == null) {
            fragmentTransaction.add(R.id.fragment_tournament, CreateTournamentFragment.class, null);
        } else {
            Bundle bundle = new Bundle();
            if (currentTournament.getStatus() == TournamentStatus.PICKING_PLAYERS) {
                bundle.putLong("tournamentId", currentTournament.getId());
                bundle.putSerializable("tournamentType", currentTournament.getType());
                bundle.putInt("maxPlayers", currentTournament.getMaxPlayers());
                fragmentTransaction.replace(R.id.fragment_tournament, SignPlayersFragment.class, bundle);
            } else {
                fragmentTransaction.replace(R.id.fragment_tournament, TournamentHubFragment.class, null);
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