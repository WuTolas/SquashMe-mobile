package pl.pjatk.squashme.fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.lang.ref.WeakReference;
import java.util.concurrent.ThreadPoolExecutor;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.pjatk.squashme.R;
import pl.pjatk.squashme.dao.MatchDao;
import pl.pjatk.squashme.database.AppDatabase;
import pl.pjatk.squashme.model.Match;

public class CreateQuickMatchFragment extends Fragment {

    private CompositeDisposable disposables;

    private EditText p1FullName;
    private EditText p2FullName;
    private EditText bestOf;
    private CheckBox twoPointsAdvantage;
    private CheckBox refereeMode;
    private Button createButton;
    private Button cancelButton;

    private MatchDao matchDao;
    private Match savedMatch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        matchDao = AppDatabase.getInstance(requireContext()).matchDao();
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
            disposables.add(Single.just(matchDao)
                    .subscribeOn(Schedulers.io())
                    .subscribe(mDao -> {
                       long savedId = matchDao.insert(match);
                       match.setId(savedId);
                       prepareFragment(match);
                    }));
        }
    }

    private void prepareFragment(Match savedMatch) {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("match", savedMatch);
        if (savedMatch.isRefereeMode()) {
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
        match.setPlayer1(p1FullName.getText().toString());
        match.setPlayer2(p2FullName.getText().toString());
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