package pl.pjatk.squashme.fragment.tournament;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.pjatk.squashme.R;
import pl.pjatk.squashme.activity.TournamentDashboardNavigation;
import pl.pjatk.squashme.activity.TournamentInfo;
import pl.pjatk.squashme.di.component.DaggerTournamentOptionsFragmentComponent;
import pl.pjatk.squashme.di.module.RoomModule;
import pl.pjatk.squashme.service.TournamentService;

/**
 * Fragment class responsible for tournament options view.
 */
public class TournamentOptionsFragment extends Fragment {

    @Inject
    public TournamentService tournamentService;
    private CompositeDisposable disposables;

    private long tournamentId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tournamentId = ((TournamentInfo) requireActivity()).getTournamentId();
        DaggerTournamentOptionsFragmentComponent.builder()
                .roomModule(new RoomModule((requireActivity().getApplication())))
                .build()
                .inject(this);
        disposables = new CompositeDisposable();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tournament_options, container, false);
        if (requireActivity() instanceof TournamentDashboardNavigation) {
            ((TournamentDashboardNavigation) requireActivity()).showBottomNavigation();
        }
        initializeComponents(view);
        return view;
    }

    /**
     * Initializes view components.
     *
     * @param view View
     */
    private void initializeComponents(View view) {
        Button endTournamentButton = view.findViewById(R.id.btn_tournament_end);
        endTournamentButton.setOnClickListener(endTournamentDialogListener);
    }

    /**
     * Listener responsible for displaying confirmation dialog and finishing current tournament.
     */
    private final OnClickListener endTournamentDialogListener = v -> new MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.end_tournament)
            .setMessage(R.string.tournament_close_prompt)
            .setNeutralButton(R.string.cancel, null)
            .setPositiveButton(R.string.confirm, (dialog, which) -> disposables.add(Single.just(tournamentId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(tournamentId -> {
                        tournamentService.endTournament(tournamentId);
                        requireActivity().finish();
                    })))
            .show();

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }
}