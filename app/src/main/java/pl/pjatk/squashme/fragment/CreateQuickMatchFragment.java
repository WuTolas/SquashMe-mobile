package pl.pjatk.squashme.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.pjatk.squashme.R;
import pl.pjatk.squashme.di.component.DaggerCreateQuickMatchFragmentComponent;
import pl.pjatk.squashme.di.module.RoomModule;
import pl.pjatk.squashme.model.Match;
import pl.pjatk.squashme.model.MatchWithPlayers;
import pl.pjatk.squashme.service.MatchService;
import pl.pjatk.squashme.service.PlayerService;

public class CreateQuickMatchFragment extends Fragment {

    @Inject
    public MatchService matchService;
    @Inject
    public PlayerService playerService;
    private CompositeDisposable disposables;

    private EditText p1FullName;
    private EditText p2FullName;
    private EditText bestOf;
    private CheckBox twoPointsAdvantage;
    private CheckBox refereeMode;
    private Button createButton;
    private Button cancelButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerCreateQuickMatchFragmentComponent.builder()
                .roomModule(new RoomModule(getActivity().getApplication()))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_quick_match, container, false);
        initializeComponents(view);
        disposables = new CompositeDisposable();
        createButton.setOnClickListener(V -> handleCreateQuickMatch());
        cancelButton.setOnClickListener(V -> requireActivity().finish());
        return view;
    }

    private void handleCreateQuickMatch() {
        if (isValidated()) {
            Match match = prepareMatch();
            disposables.add(Single.just(match)
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

    private void prepareFragment(MatchWithPlayers match) {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("match", match);
        if (match.getMatch().isRefereeMode()) {
            fragmentTransaction.replace(R.id.fragment_quick_match, RefereeModeFragment.class, bundle);
        } else {
            fragmentTransaction.replace(R.id.fragment_quick_match, QuickScoreModeFragment.class, bundle);
        }
        fragmentTransaction.commit();
    }

    private void initializeComponents(View view) {
        p1FullName = view.findViewById(R.id.inp_player1_fullName);
        p2FullName = view.findViewById(R.id.inp_player2_fullName);
        bestOf = view.findViewById(R.id.inp_match_bestOf);
        twoPointsAdvantage = view.findViewById(R.id.chk_match_twoPointsAdvantage);
        refereeMode = view.findViewById(R.id.chk_match_refereeMode);
        createButton = view.findViewById(R.id.btn_match_create);
        cancelButton = view.findViewById(R.id.btn_match_cancel);
    }

    private Match prepareMatch() {
        Match match = new Match();
        match.setBestOf(bestOf.getText().toString().isEmpty() ? null : Integer.parseInt(bestOf.getText().toString()));
        match.setTwoPointsAdvantage(twoPointsAdvantage.isChecked());
        match.setRefereeMode(refereeMode.isChecked());
        return match;
    }

    private boolean isValidated() {
        int errorsCount = 0;

        if (p1FullName.getText().toString().isEmpty()) {
            p1FullName.setError(getString(R.string.error_name_empty));
            errorsCount++;
        } else if (p1FullName.getText().toString().length() > 128) {
            p1FullName.setError(getString(R.string.error_max_length, 128));
            errorsCount++;
        }

        if (p2FullName.getText().toString().isEmpty()) {
            p2FullName.setError(getString(R.string.error_name_empty));
            errorsCount++;
        }

        try {
            if (!bestOf.getText().toString().isEmpty() && Integer.parseInt(bestOf.getText().toString()) <= 0) {
                bestOf.setError(getString(R.string.error_best_of_games));
                errorsCount++;
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