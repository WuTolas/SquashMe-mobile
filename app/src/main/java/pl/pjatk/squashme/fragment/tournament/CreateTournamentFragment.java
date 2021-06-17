package pl.pjatk.squashme.fragment.tournament;

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

import com.google.android.material.textfield.TextInputLayout;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.pjatk.squashme.R;
import pl.pjatk.squashme.di.component.DaggerCreateTournamentFragmentComponent;
import pl.pjatk.squashme.di.module.RoomModule;
import pl.pjatk.squashme.helper.GenericTextWatcher;
import pl.pjatk.squashme.model.Tournament;
import pl.pjatk.squashme.model.TournamentStatus;
import pl.pjatk.squashme.model.TournamentType;
import pl.pjatk.squashme.service.TournamentService;

/**
 * Fragment class responsible for creating tournament.
 */
public class CreateTournamentFragment extends Fragment {

    @Inject
    public TournamentService tournamentService;
    private CompositeDisposable disposables;

    private TextInputLayout textTournamentName;
    private TextInputLayout textMaxPlayers;
    private TextInputLayout textBestOf;
    private Spinner tournamentType;
    private EditText tournamentName;
    private EditText maxPlayers;
    private EditText bestOf;
    private CheckBox twoPointsAdvantage;

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
        return view;
    }

    /**
     * Handles tournament creation. Calls tournament service.
     */
    private void handleCreateTournament() {
        if (isValidated()) {
            Tournament tournament = prepareTournament();
            disposables.add(Observable.just(tournament)
                    .subscribeOn(Schedulers.io())
                    .subscribe(t -> {
                        Tournament saved = tournamentService.save(t);
                        prepareFragment(saved);
                    })
            );
        }
    }

    /**
     * Prepares and puts new fragment in the container.
     *
     * @param tournament tournament data
     */
    private void prepareFragment(Tournament tournament) {
        if (isAdded()) {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putLong("tournamentId", tournament.getId());
            bundle.putSerializable("tournamentType", tournament.getType());
            bundle.putInt("maxPlayers", tournament.getMaxPlayers());
            fragmentTransaction.replace(R.id.fragment_tournament, SignPlayersFragment.class, bundle);
            fragmentTransaction.commit();
        }
    }

    /**
     * Prepares tournament based on provided inputs.
     *
     * @return Tournament
     */
    private Tournament prepareTournament() {
        Tournament tournament = new Tournament();
        tournament.setName(tournamentName.getText().toString());
        tournament.setType(TournamentType.getByName(tournamentType.getSelectedItem().toString()));
        tournament.setMaxPlayers(Integer.parseInt(maxPlayers.getText().toString()));
        tournament.setBestOf(Integer.parseInt(bestOf.getText().toString()));
        tournament.setTwoPointsAdvantage(twoPointsAdvantage.isChecked());
        tournament.setStatus(TournamentStatus.PICKING_PLAYERS);
        return tournament;
    }

    /**
     * Initializes view components.
     *
     * @param view View
     */
    private void initializeComponents(View view) {
        textTournamentName = view.findViewById(R.id.text_tournament_name);
        textBestOf = view.findViewById(R.id.text_tournament_bestOf);
        textMaxPlayers = view.findViewById(R.id.text_tournament_players_number);
        tournamentName = view.findViewById(R.id.inp_tournament_name);
        tournamentType = view.findViewById(R.id.spinner_tournament_type);
        tournamentType.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, TournamentType.values()));
        maxPlayers = view.findViewById(R.id.inp_tournament_max_players);
        bestOf = view.findViewById(R.id.inp_tournament_bestOf);
        twoPointsAdvantage = view.findViewById(R.id.chk_tournament_twoPointsAdvantage);

        tournamentName.addTextChangedListener(new GenericTextWatcher(textTournamentName));
        bestOf.addTextChangedListener(new GenericTextWatcher(textBestOf));
        maxPlayers.addTextChangedListener(new GenericTextWatcher(textMaxPlayers));

        Button createButton = view.findViewById(R.id.btn_tournament_create);
        Button cancelButton = view.findViewById(R.id.btn_tournament_cancel);
        createButton.setOnClickListener(V -> handleCreateTournament());
        cancelButton.setOnClickListener(V -> requireActivity().finish());
    }

    /**
     * Validates form.
     *
     * @return boolean
     */
    private boolean isValidated() {
        int errorsCount = 0;

        TournamentType type = TournamentType.getByName(tournamentType.getSelectedItem().toString());

        if (tournamentName.getText().toString().isEmpty()) {
            textTournamentName.setError(getString(R.string.error_name_empty));
            errorsCount++;
        } else if (tournamentName.length() > 64) {
            textTournamentName.setError(getString(R.string.error_max_length, 64));
            errorsCount++;
        }

        if (type == null) {
            ((TextView) tournamentType.getSelectedView()).setError(getString(R.string.error_field_empty));
            errorsCount++;
        }

        try {
            if (!bestOf.getText().toString().isEmpty()) {
                int bestOfValue = Integer.parseInt(bestOf.getText().toString());
                if (bestOfValue <= 0) {
                    textBestOf.setError(getString(R.string.error_best_of_games_positive));
                    errorsCount++;
                } else if (bestOfValue % 2 == 0) {
                    textBestOf.setError(getString(R.string.error_best_of_must_be_odd));
                    errorsCount++;
                } else if (bestOfValue > 11) {
                    textBestOf.setError(getString(R.string.error_max_number, 11));
                    errorsCount++;
                }
            } else {
                textBestOf.setError(getString(R.string.error_field_empty));
                errorsCount++;
            }

        } catch (NumberFormatException ex) {
            bestOf.setError(getString(R.string.error_not_a_number));
            errorsCount++;
        }

        try {
            if (maxPlayers.getText().toString().isEmpty()) {
                textMaxPlayers.setError(getString(R.string.error_field_empty));
                errorsCount++;
            } else {
                int playersValue = Integer.parseInt(maxPlayers.getText().toString());
                if (playersValue < 3) {
                    textMaxPlayers.setError(getString(R.string.error_min_players, 3));
                    errorsCount++;
                } else if (playersValue > 32) {
                    textMaxPlayers.setError(getString(R.string.error_max_number, 32));
                    errorsCount++;
                }
            }
        } catch (NumberFormatException ex) {
            textMaxPlayers.setError(getString(R.string.error_not_a_number));
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