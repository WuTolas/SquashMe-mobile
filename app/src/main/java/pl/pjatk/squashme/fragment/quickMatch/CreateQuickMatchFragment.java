package pl.pjatk.squashme.fragment.quickMatch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.pjatk.squashme.R;
import pl.pjatk.squashme.di.component.DaggerCreateQuickMatchFragmentComponent;
import pl.pjatk.squashme.di.module.RoomModule;
import pl.pjatk.squashme.fragment.referee.RefereeModeFragment;
import pl.pjatk.squashme.helper.GenericTextWatcher;
import pl.pjatk.squashme.model.Match;
import pl.pjatk.squashme.model.custom.MatchWithPlayers;
import pl.pjatk.squashme.service.MatchService;
import pl.pjatk.squashme.service.PlayerService;

/**
 * Fragment class responsible for creating quick match.
 */
public class CreateQuickMatchFragment extends Fragment {

    @Inject
    public MatchService matchService;
    @Inject
    public PlayerService playerService;
    private CompositeDisposable disposables;

    private TextInputLayout textP1FullName;
    private TextInputLayout textP2FullName;
    private TextInputLayout textBestOf;
    private EditText p1FullName;
    private EditText p2FullName;
    private EditText bestOf;
    private CheckBox twoPointsAdvantage;
    private CheckBox refereeMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerCreateQuickMatchFragmentComponent.builder()
                .roomModule(new RoomModule(requireActivity().getApplication()))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_quick_match, container, false);
        initializeComponents(view);
        disposables = new CompositeDisposable();
        return view;
    }

    /**
     * Handles quick match creation. Calls match service.
     */
    private void handleCreateQuickMatch() {
        if (isValidated()) {
            Match match = prepareMatch();
            disposables.add(Observable.just(match)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .subscribe(m -> {
                        String p1Name = p1FullName.getText().toString().trim();
                        String p2Name = p2FullName.getText().toString().trim();
                        long p1Id = playerService.getIdWithSave(p1Name);
                        long p2Id = playerService.getIdWithSave(p2Name);
                        m.setPlayer1Id(p1Id);
                        m.setPlayer2Id(p2Id);
                        MatchWithPlayers mwp = matchService.saveWithPlayersReturn(m);
                        prepareFragment(mwp);
                    }));
        }
    }

    /**
     * Prepares and puts new fragment in the container.
     *
     * @param match match with players info
     */
    private void prepareFragment(MatchWithPlayers match) {
        if (isAdded()) {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putSerializable("match", match);
            if (match.getMatch().isRefereeMode()) {
                fragmentTransaction.replace(R.id.fragment_quick_match, RefereeModeFragment.class, bundle);
            }
            fragmentTransaction.commit();
        }
    }

    /**
     * Initializes view components.
     *
     * @param view View
     */
    private void initializeComponents(View view) {
        textP1FullName = view.findViewById(R.id.text_player1_fullName);
        textP2FullName = view.findViewById(R.id.text_player2_fullName);
        textBestOf = view.findViewById(R.id.text_match_bestOf);
        p1FullName = view.findViewById(R.id.inp_player1_fullName);
        p2FullName = view.findViewById(R.id.inp_player2_fullName);
        bestOf = view.findViewById(R.id.inp_match_bestOf);
        twoPointsAdvantage = view.findViewById(R.id.chk_match_twoPointsAdvantage);
        refereeMode = view.findViewById(R.id.chk_match_refereeMode);

        p1FullName.addTextChangedListener(new GenericTextWatcher(textP1FullName));
        p2FullName.addTextChangedListener(new GenericTextWatcher(textP2FullName));
        bestOf.addTextChangedListener(new GenericTextWatcher(textBestOf));

        Button createButton = view.findViewById(R.id.btn_match_create);
        Button cancelButton = view.findViewById(R.id.btn_match_cancel);
        createButton.setOnClickListener(V -> handleCreateQuickMatch());
        cancelButton.setOnClickListener(V -> requireActivity().finish());
    }

    /**
     * Prepares match based on provided inputs.
     *
     * @return Match
     */
    private Match prepareMatch() {
        Match match = new Match();
        match.setBestOf(bestOf.getText().toString().isEmpty() ? null : Integer.parseInt(bestOf.getText().toString()));
        match.setTwoPointsAdvantage(twoPointsAdvantage.isChecked());
        match.setRefereeMode(refereeMode.isChecked());
        return match;
    }

    /**
     * Validates form.
     *
     * @return boolean
     */
    private boolean isValidated() {
        int errorsCount = 0;

        String p1Name = p1FullName.getText().toString().trim();
        String p2Name = p2FullName.getText().toString().trim();

        if (p1Name.isEmpty()) {
            textP1FullName.setError(getString(R.string.error_name_empty));
            errorsCount++;
        } else if (p1Name.length() > 40) {
            textP1FullName.setError(getString(R.string.error_max_length, 40));
            errorsCount++;
        }

        if (p2Name.isEmpty()) {
            textP2FullName.setError(getString(R.string.error_name_empty));
            errorsCount++;
        } else if (p2Name.length() > 40) {
            textP2FullName.setError(getString(R.string.error_max_length, 40));
            errorsCount++;
        }

        if (!p1Name.isEmpty() && !p2Name.isEmpty() && p1Name.equals(p2Name)) {
            textP2FullName.setError(getString(R.string.error_player_exists));
            errorsCount++;
        }

        try {
            if (!bestOf.getText().toString().isEmpty()) {
                int bestOfValue = Integer.parseInt(bestOf.getText().toString());
                if (bestOfValue <= 0) {
                    textBestOf.setError(getString(R.string.error_best_of_games));
                    errorsCount++;
                } else if (bestOfValue % 2 == 0) {
                    textBestOf.setError(getString(R.string.error_best_of_must_be_odd));
                    errorsCount++;
                } else if (bestOfValue > 11) {
                    textBestOf.setError(getString(R.string.error_max_number, 11));
                    errorsCount++;
                }
            }
        } catch (NumberFormatException ex) {
            textBestOf.setError(getString(R.string.error_not_a_number));
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