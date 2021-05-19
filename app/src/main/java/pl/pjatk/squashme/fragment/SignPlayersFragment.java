package pl.pjatk.squashme.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.pjatk.squashme.R;
import pl.pjatk.squashme.di.component.DaggerSignPlayersFragmentComponent;
import pl.pjatk.squashme.di.module.RoomModule;
import pl.pjatk.squashme.model.TournamentType;
import pl.pjatk.squashme.service.TournamentService;

public class SignPlayersFragment extends Fragment {

    @Inject
    public TournamentService tournamentService;
    private CompositeDisposable disposables;

    private static final String ARG_TOURNAMENT_ID = "tournamentId";
    private static final String ARG_TOURNAMENT_TYPE = "tournamentType";
    private static final String ARG_MAX_PLAYERS = "maxPlayers";

    private long tournamentId;
    private TournamentType tournamentType;
    private int maxPlayers;

    private final List<EditText> playerNameEditList = new ArrayList<>();
    private final List<String> playerNames = new ArrayList<>();
    private Button addButton;
    private Button cancelButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tournamentId = getArguments().getLong(ARG_TOURNAMENT_ID);
            tournamentType = (TournamentType) getArguments().getSerializable(ARG_TOURNAMENT_TYPE);
            maxPlayers = getArguments().getInt(ARG_MAX_PLAYERS);
        }
        DaggerSignPlayersFragmentComponent.builder()
                .roomModule(new RoomModule(getActivity().getApplication()))
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
        cancelButton.setOnClickListener(V -> requireActivity().finish());
        return view;
    }

    private void handleAddPlayers() {
        if (isValidated()) {
            disposables.add(Single.just(playerNames)
                    .subscribeOn(Schedulers.io())
                    .subscribe(players -> {
                        tournamentService.savePlayers(tournamentId, players);
                    })
            );
        }
    }

    private void initializeComponents(View view) {
        addButton = view.findViewById(R.id.btn_tournament_players_add);
        cancelButton = view.findViewById(R.id.btn_tournament_players_cancel);
        addPlayerInputs(view);
    }

    private void addPlayerInputs(View view) {
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.sign_players_container);
        TextView nameLabel;
        EditText nameInput;

        for (int i = 0; i < maxPlayers; i++) {
            nameLabel = new TextView(getContext());
            nameLabel.setText(String.format("%s " + (i + 1), getString(R.string.player)));
            nameInput = new EditText(getContext());
            nameInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            playerNameEditList.add(nameInput);
            layout.addView(nameLabel);
            layout.addView(nameInput);
        }
    }

    private boolean isValidated() {
        AtomicInteger errorCount = new AtomicInteger(0);
        playerNames.clear();

        playerNameEditList.forEach(e -> {
            String playerName = e.getText().toString().trim();

            if (playerName.isEmpty()) {
                e.setError(getString(R.string.error_name_empty));
                errorCount.incrementAndGet();
            } else if (playerName.length() > 64) {
                e.setError(getString(R.string.error_max_length, 64));
                errorCount.incrementAndGet();
            } else if (playerNames.contains(playerName)) {
                e.setError(getString(R.string.error_player_exists));
                errorCount.incrementAndGet();
            } else {
                playerNames.add(playerName);
            }
        });

        return errorCount.get() == 0;
    }
}