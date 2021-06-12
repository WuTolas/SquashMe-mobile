package pl.pjatk.squashme.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.pjatk.squashme.R;
import pl.pjatk.squashme.di.component.DaggerTournamentActivityComponent;
import pl.pjatk.squashme.di.module.RoomModule;
import pl.pjatk.squashme.fragment.tournament.CreateTournamentFragment;
import pl.pjatk.squashme.fragment.tournament.SignPlayersFragment;
import pl.pjatk.squashme.fragment.tournament.TournamentMatchesFragment;
import pl.pjatk.squashme.fragment.tournament.TournamentOptionsFragment;
import pl.pjatk.squashme.fragment.common.TournamentResultsFragment;
import pl.pjatk.squashme.model.Tournament;
import pl.pjatk.squashme.model.TournamentStatus;
import pl.pjatk.squashme.service.TournamentService;

/**
 * Quick match activity class - decides which fragment should be visible based on tournament data.
 */
public class TournamentActivity extends BaseActivity implements TournamentInfo, BottomNavigationView.OnNavigationItemSelectedListener, TournamentDashboardNavigation {

    @Inject
    public TournamentService tournamentService;
    private CompositeDisposable disposables;
    private BottomNavigationView bottomNavigationView;
    private long tournamentId;

    /**
     * Instantiates bottom navigation view and hides it by default.
     *
     * Gets current tournament from the tournament service.
     *
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setVisibility(View.GONE);

        DaggerTournamentActivityComponent.builder()
                .roomModule(new RoomModule(getApplication()))
                .build()
                .inject(this);

        disposables = new CompositeDisposable();
        disposables.add(Observable.fromCallable(tournamentService::getCurrentTournament)
                .subscribeOn(Schedulers.io())
                .subscribe(t -> {
                    Tournament tournament = t.orElse(null);
                    if (tournament != null) {
                        tournamentId = tournament.getId();
                    }
                    prepareFragment(tournament);
                })
        );
    }

    /**
     * Prepares and puts new fragment in the container.
     *
     * If there is no ongoing tournament then it's forwarded to create tournament form.
     * If there is tournament with picking players status then it's forwarded to picking
     * players form.
     * If there is ongoing tournament then it's forwarded to tournament matches fragment.
     *
     * @param currentTournament current tournament - can be null
     */
    private void prepareFragment(Tournament currentTournament) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (currentTournament == null) {
            fragmentTransaction.add(R.id.fragment_tournament, CreateTournamentFragment.class, null);
        } else {
            Bundle bundle = new Bundle();
            if (currentTournament.getStatus() == TournamentStatus.PICKING_PLAYERS) {
                bundle.putLong("tournamentId", currentTournament.getId());
                bundle.putInt("maxPlayers", currentTournament.getMaxPlayers());
                fragmentTransaction.replace(R.id.fragment_tournament, SignPlayersFragment.class, bundle);
            } else {
                fragmentTransaction.replace(R.id.fragment_tournament, TournamentMatchesFragment.class, null);
            }
        }
        fragmentTransaction.commit();
    }

    /**
     * Puts new fragment in the container.
     *
     * @param fragment Fragment
     * @return boolean
     */
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_tournament, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        if (item.getItemId() == R.id.navigation_tournament_matches) {
            fragment = new TournamentMatchesFragment();
        } else if (item.getItemId() == R.id.navigation_tournament_leaderboard) {
            fragment = new TournamentResultsFragment();
        } else if (item.getItemId() == R.id.navigation_tournament_options) {
            fragment = new TournamentOptionsFragment();
        }

        return loadFragment(fragment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showBottomNavigation() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hideBottomNavigation() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getTournamentId() {
        return tournamentId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTournamentId(long tournamentId) {
        this.tournamentId = tournamentId;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }
}