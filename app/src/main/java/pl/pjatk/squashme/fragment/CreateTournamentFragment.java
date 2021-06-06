package pl.pjatk.squashme.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.pjatk.squashme.R;
import pl.pjatk.squashme.activity.TournamentDashboardNavigation;
import pl.pjatk.squashme.di.component.DaggerCreateTournamentFragmentComponent;
import pl.pjatk.squashme.di.module.RoomModule;
import pl.pjatk.squashme.model.Tournament;
import pl.pjatk.squashme.model.TournamentStatus;
import pl.pjatk.squashme.model.TournamentType;
import pl.pjatk.squashme.service.TournamentService;

public class CreateTournamentFragment extends Fragment {

    @Inject
    public TournamentService tournamentService;
    private CompositeDisposable disposables;

    private Spinner tournamentType;
    private EditText maxPlayers;
    private EditText bestOf;
    private CheckBox twoPointsAdvantage;
    private Button createButton;
    private Button cancelButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerCreateTournamentFragmentComponent.builder()
                .roomModule(new RoomModule(requireActivity().getApplication()))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_tournament, container, false);
        disposables = new CompositeDisposable();
        initializeComponents(view);
        createButton.setOnClickListener(V -> handleCreateTournament());
        cancelButton.setOnClickListener(V -> requireActivity().finish());
        return view;
    }

    private void handleCreateTournament() {
        if (isValidated()) {
            Tournament tournament = prepareTournament();
            disposables.add(Single.just(tournament)
                    .subscribeOn(Schedulers.io())
                    .subscribe(t -> {
                        Tournament saved = tournamentService.save(t);
                        prepareFragment(saved);
                    })
            );
        }
    }

    private void prepareFragment(Tournament tournament) {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putLong("tournamentId", tournament.getId());
        bundle.putSerializable("tournamentType", tournament.getType());
        bundle.putInt("maxPlayers", tournament.getMaxPlayers());
        fragmentTransaction.replace(R.id.fragment_tournament, SignPlayersFragment.class, bundle);
        fragmentTransaction.commit();
    }

    private Tournament prepareTournament() {
        Tournament tournament = new Tournament();
        tournament.setType(TournamentType.getByName(tournamentType.getSelectedItem().toString()));
        tournament.setMaxPlayers(Integer.parseInt(maxPlayers.getText().toString()));
        tournament.setBestOf(Integer.parseInt(bestOf.getText().toString()));
        tournament.setTwoPointsAdvantage(twoPointsAdvantage.isChecked());
        tournament.setStatus(TournamentStatus.PICKING_PLAYERS);
        return tournament;
    }

    private void initializeComponents(View view) {
        tournamentType = view.findViewById(R.id.spinner_tournament_type);
        tournamentType.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, TournamentType.values()));
        maxPlayers = view.findViewById(R.id.inp_tournament_max_players);
        bestOf = view.findViewById(R.id.inp_tournament_bestOf);
        twoPointsAdvantage = view.findViewById(R.id.chk_tournament_twoPointsAdvantage);
        createButton = view.findViewById(R.id.btn_tournament_create);
        cancelButton = view.findViewById(R.id.btn_tournament_cancel);
    }

    private boolean isValidated() {
        int errorsCount = 0;

        TournamentType type = TournamentType.getByName(tournamentType.getSelectedItem().toString());

        if (type == null) {
            ((TextView) tournamentType.getSelectedView()).setError(getString(R.string.error_field_empty));
            errorsCount++;
        }

        try {
            if (!bestOf.getText().toString().isEmpty()) {
                int bestOfValue = Integer.parseInt(bestOf.getText().toString());
                if (bestOfValue <= 0) {
                    bestOf.setError(getString(R.string.error_best_of_games));
                    errorsCount++;
                } else if (bestOfValue % 2 == 0) {
                    bestOf.setError(getString(R.string.error_best_of_must_be_odd));
                    errorsCount++;
                } else if (bestOfValue > 11) {
                    bestOf.setError(getString(R.string.error_max_number, 11));
                    errorsCount++;
                }
            } else {
                bestOf.setError(getString(R.string.error_field_empty));
                errorsCount++;
            }

            if (maxPlayers.getText().toString().isEmpty()) {
                maxPlayers.setError(getString(R.string.error_field_empty));
                errorsCount++;
            } else {
                int playersValue = Integer.parseInt(maxPlayers.getText().toString());
                if (playersValue < 3) {
                    maxPlayers.setError(getString(R.string.error_min_players, 3));
                    errorsCount++;
                } else if (playersValue > 32) {
                    maxPlayers.setError(getString(R.string.error_max_number, 32));
                    errorsCount++;
                }
            }
        } catch (NumberFormatException ex) {
            bestOf.setError(getString(R.string.error_not_a_number));
            errorsCount++;
        }

        return errorsCount == 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }
}