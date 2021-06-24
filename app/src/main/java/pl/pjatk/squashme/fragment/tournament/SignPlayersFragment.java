package pl.pjatk.squashme.fragment.tournament;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.pjatk.squashme.R;
import pl.pjatk.squashme.activity.TournamentInfo;
import pl.pjatk.squashme.di.component.DaggerSignPlayersFragmentComponent;
import pl.pjatk.squashme.di.module.RoomModule;
import pl.pjatk.squashme.helper.GenericTextWatcher;
import pl.pjatk.squashme.service.TournamentService;

/**
 * Fragment class responsible for providing players in the tournament.
 */
public class SignPlayersFragment extends Fragment {

    @Inject
    public TournamentService tournamentService;
    private CompositeDisposable disposables;

    private static final String ARG_TOURNAMENT_ID = "tournamentId";
    private static final String ARG_MAX_PLAYERS = "maxPlayers";

    private long tournamentId;
    private int maxPlayers;

    private final List<TextInputLayout> playerNameEditList = new ArrayList<>();
    private final List<String> playerNames = new ArrayList<>();
    private Button addButton;
    private Button cancelButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tournamentId = getArguments().getLong(ARG_TOURNAMENT_ID);
            maxPlayers = getArguments().getInt(ARG_MAX_PLAYERS);
        }
        DaggerSignPlayersFragmentComponent.builder()
                .roomModule(new RoomModule(requireActivity().getApplication()))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_players, container, false);
        initializeComponents(view);
        disposables = new CompositeDisposable();
        addButton.setOnClickListener(V -> handleAddPlayers());
        cancelButton.setOnClickListener(cancelTournament);
        return view;
    }

    /**
     * Handles adding players. Calls tournament service in order to generate matches.
     */
    private void handleAddPlayers() {
        if (isValidated()) {
            disposables.add(Observable.just(playerNames)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .subscribe(players -> {
                        tournamentService.generateRoundRobinMatches(tournamentId, players);
                        ((TournamentInfo) requireActivity()).setTournamentId(tournamentId);
                        prepareFragment();
                    })
            );
        }
    }

    /**
     * Prepares and puts new fragment in the container.
     */
    private void prepareFragment() {
        if (isAdded()) {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_tournament, TournamentMatchesFragment.class, null);
            fragmentTransaction.commit();
        }
    }

    /**
     * Initializes view components.
     *
     * @param view View
     */
    private void initializeComponents(View view) {
        addButton = view.findViewById(R.id.btn_tournament_players_add);
        cancelButton = view.findViewById(R.id.btn_tournament_players_cancel);
        addPlayerInputs(view);
    }

    /**
     * Generates player name inputs - based on how many players are supposed to be in the tournament.
     *
     * @param view View
     */
    private void addPlayerInputs(View view) {
        LinearLayout layout = view.findViewById(R.id.sign_players_container);
        ViewGroup container = null;
        
        for (int i = 0; i < maxPlayers; i++) {
            TextInputLayout ti = (TextInputLayout) getLayoutInflater().inflate(R.layout.player_input, container, false);
            ti.setHint(String.format("%s " + (i + 1), getString(R.string.player)));
            if (ti.getEditText() != null) {
                ti.getEditText().addTextChangedListener(new GenericTextWatcher(ti));
            }
            playerNameEditList.add(ti);
            layout.addView(ti);
        }
    }

    /**
     * Validates form.
     *
     * @return boolean
     */
    private boolean isValidated() {
        AtomicInteger errorCount = new AtomicInteger(0);
        playerNames.clear();

        playerNameEditList.forEach(ti -> {
            String playerName = ti.getEditText() != null ? ti.getEditText().getText().toString().trim() : "";

            if (playerName.isEmpty()) {
                ti.setError(getString(R.string.error_name_empty));
                errorCount.incrementAndGet();
            } else if (playerName.length() > 40) {
                ti.setError(getString(R.string.error_max_length, 40));
                errorCount.incrementAndGet();
            } else if (playerNames.contains(playerName)) {
                ti.setError(getString(R.string.error_player_exists));
                errorCount.incrementAndGet();
            } else {
                playerNames.add(playerName);
            }
        });

        return errorCount.get() == 0;
    }

    /**
     * Listener responsible for removing tournament on cancel.
     */
    private final View.OnClickListener cancelTournament = new OnClickListener() {
        @Override
        public void onClick(View v) {
            disposables.add(Observable.just(tournamentId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(tId -> {
                        tournamentService.removeTournament(tId);
                        requireActivity().finish();
                    }));
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }
}