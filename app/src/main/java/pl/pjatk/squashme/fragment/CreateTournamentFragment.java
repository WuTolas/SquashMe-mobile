package pl.pjatk.squashme.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import pl.pjatk.squashme.R;
import pl.pjatk.squashme.di.component.DaggerCreateTournamentFragmentComponent;
import pl.pjatk.squashme.di.module.RoomModule;
import pl.pjatk.squashme.service.TournamentService;

public class CreateTournamentFragment extends Fragment {

    @Inject
    public TournamentService tournamentService;
    private CompositeDisposable disposables;

    private EditText bestOf;
    private CheckBox twoPointsAdvantage;
    private Button createButton;
    private Button cancelButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerCreateTournamentFragmentComponent.builder()
                .roomModule(new RoomModule(getActivity().getApplication()))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_tournament, container, false);
        disposables = new CompositeDisposable();
        initializeComponents(view);
        cancelButton.setOnClickListener(V -> requireActivity().finish());
        return view;
    }

    private void initializeComponents(View view) {
        bestOf = view.findViewById(R.id.inp_tournament_bestOf);
        twoPointsAdvantage = view.findViewById(R.id.chk_tournament_twoPointsAdvantage);
        createButton = view.findViewById(R.id.btn_match_create);
        cancelButton = view.findViewById(R.id.btn_tournament_cancel);
    }

    private boolean isValidated() {
        int errorsCount = 0;

        try {
            if (bestOf.getText().toString().isEmpty()) {
                bestOf .setError(getString(R.string.error_field_empty));
                errorsCount++;
            } else if (Integer.parseInt(bestOf.getText().toString()) <= 0) {
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