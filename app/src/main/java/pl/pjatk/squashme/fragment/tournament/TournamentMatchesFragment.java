package pl.pjatk.squashme.fragment.tournament;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.pjatk.squashme.R;
import pl.pjatk.squashme.activity.TournamentDashboardNavigation;
import pl.pjatk.squashme.activity.TournamentInfo;
import pl.pjatk.squashme.di.component.DaggerTournamentMatchesFragmentComponent;
import pl.pjatk.squashme.di.module.RoomModule;
import pl.pjatk.squashme.fragment.referee.RefereeModeFragment;
import pl.pjatk.squashme.model.custom.MatchWithPlayers;
import pl.pjatk.squashme.model.custom.TournamentMatchSimple;
import pl.pjatk.squashme.service.MatchService;

/**
 * Fragment class responsible for tournament matches table.
 */
public class TournamentMatchesFragment extends Fragment {

    private static final String TAG = TournamentMatchesFragment.class.getSimpleName();

    @Inject
    public MatchService matchService;
    private CompositeDisposable disposables;

    private List<TournamentMatchSimple> matches;
    private long tournamentId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tournamentId = ((TournamentInfo) requireActivity()).getTournamentId();
        DaggerTournamentMatchesFragmentComponent.builder()
                .roomModule(new RoomModule(requireActivity().getApplication()))
                .build()
                .inject(this);
        disposables = new CompositeDisposable();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (requireActivity() instanceof TournamentDashboardNavigation) {
            ((TournamentDashboardNavigation) requireActivity()).showBottomNavigation();
        }
        return inflater.inflate(R.layout.fragment_tournament_matches, container, false);
    }

    /**
     * Gets tournament matches from match service.
     *
     * @param view View
     * @param savedInstanceState Bundle
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        disposables.add(Observable.fromSingle(matchService.searchTournamentMatches(tournamentId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(r -> {
                    matches = r;
                    prepareMatchesTable(r, view);
                }));
    }

    /**
     * Prepares matches table.
     *
     * @param matches matches in the tournament
     * @param view View
     */
    private void prepareMatchesTable(List<TournamentMatchSimple> matches, View view) {
        TableLayout tl = view.findViewById(R.id.tbl_tournament_matches);
        ViewGroup container = getRootContainer();
        addTableHeader(tl, container);
        Integer currentRound = matches.stream().filter(m -> !m.isFinished()).map(TournamentMatchSimple::getTournamentRound).findFirst().orElse(null);
        for (int i = 0; i < matches.size(); i++) {
            addTableRow(tl, matches.get(i), i, container, currentRound);
        }
    }

    /**
     * Creates table header.
     *
     * @param tableLayout TableLayout
     * @param container ViewGroup
     */
    private void addTableHeader(TableLayout tableLayout, ViewGroup container) {
        TableRow headerRow = new TableRow(getContext());

        TextView tv0 = (TextView) getLayoutInflater().inflate(R.layout.header_col, container, false);
        tv0.setText("#");
        headerRow.addView(tv0);

        TextView tv1 = (TextView) getLayoutInflater().inflate(R.layout.header_col, container, false);
        tv1.setText(R.string.playerOne);
        headerRow.addView(tv1);

        TextView tv2 = (TextView) getLayoutInflater().inflate(R.layout.header_col, container, false);
        tv2.setText(R.string.playerTwo);
        headerRow.addView(tv2);

        TextView tv3 = (TextView) getLayoutInflater().inflate(R.layout.header_col, container, false);
        tv3.setText(R.string.round);
        headerRow.addView(tv3);

        TextView tv4 = (TextView) getLayoutInflater().inflate(R.layout.header_col, container, false);
        tv4.setText(R.string.result);
        headerRow.addView(tv4);

        tableLayout.addView(headerRow);
    }

    /**
     * Creates table row.
     *
     * @param tableLayout TableLayout
     * @param match match data needed for the row
     * @param rowId row id
     * @param container ViewGroup
     * @param currentRound current round - matches in the current round can be managed
     */
    private void addTableRow(TableLayout tableLayout, TournamentMatchSimple match, int rowId, ViewGroup container, Integer currentRound) {
        TableRow tr;

        if (rowId % 2 == 0) {
            tr = (TableRow) getLayoutInflater().inflate(R.layout.even_row, container, false);
        } else {
            tr = (TableRow) getLayoutInflater().inflate(R.layout.odd_row, container, false);
        }

        TextView tv0 = (TextView) getLayoutInflater().inflate(R.layout.normal_col, container, false);
        tv0.setText(String.valueOf(++rowId));
        tr.addView(tv0);

        TextView tv1 = (TextView) getLayoutInflater().inflate(R.layout.normal_col, container, false);
        tv1.setText(match.getPlayer1());
        tr.addView(tv1);

        TextView tv2 = (TextView) getLayoutInflater().inflate(R.layout.normal_col, container, false);
        tv2.setText(match.getPlayer2());
        tr.addView(tv2);

        TextView tv3 = (TextView) getLayoutInflater().inflate(R.layout.normal_col, container, false);
        tv3.setText(String.valueOf(match.getTournamentRound()));
        tr.addView(tv3);

        TextView tv4 = (TextView) getLayoutInflater().inflate(R.layout.normal_col, container, false);
        tv4.setText(getString(R.string.colon, match.getSets1(), match.getSets2()));
        tr.addView(tv4);

        Button btn;

        if (match.isFinished()) {
            btn = (Button) getLayoutInflater().inflate(R.layout.ok_button_col, container, false);
            tr.addView(btn);
        } else if (!match.isFinished() && match.isRefereeMode() != null) {
            btn = (Button) getLayoutInflater().inflate(R.layout.continue_botton_col, container, false);
            btn.setOnClickListener(resumeMatchListener);
            tr.addView(btn);
        } else if (!match.isFinished() && currentRound.equals(match.getTournamentRound())) {
            btn = (Button) getLayoutInflater().inflate(R.layout.play_button_col, container, false);
            btn.setOnClickListener(startMatchDialogListener);
            tr.addView(btn);
        }

        tableLayout.addView(tr);
    }

    /**
     * Gets root container.
     *
     * @return ViewGroup
     */
    private ViewGroup getRootContainer() {
        int rootId = ((ViewGroup) requireView().getParent()).getId();
        return (ViewGroup) requireView().findViewById(rootId);
    }

    /**
     * Listener responsible for showing confirmation dialog regarding match start.
     */
    private final OnClickListener startMatchDialogListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                TournamentMatchSimple selectedMatch = getMatchFromRow(v);
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(getString(R.string.vs, selectedMatch.getPlayer1(), selectedMatch.getPlayer2()))
                        .setMessage(R.string.start_match_prompt)
                        .setNeutralButton(R.string.cancel, null)
                        .setPositiveButton(R.string.confirm, (dialog, which) -> {
                            disposables.add(Single.just(selectedMatch)
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(tournamentMatch -> {
                                        matchService.updateRefereeMode(tournamentMatch.getMatchId(), true);
                                        MatchWithPlayers matchWithPlayers = matchService.getMatchWithResults(tournamentMatch.getMatchId());
                                        if (matchWithPlayers != null) {
                                            prepareMatchFragment(matchWithPlayers);
                                        }
                                    }));
                        })
                        .show();
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage(), ex);
            }
        }
    };

    /**
     * Listener responsible for resuming ongoing match.
     */
    private final OnClickListener resumeMatchListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                TournamentMatchSimple selectedMatch = getMatchFromRow(v);
                disposables.add(Single.just(selectedMatch)
                        .subscribeOn(Schedulers.io())
                        .subscribe(tournamentMatch -> {
                            MatchWithPlayers matchWithPlayers = matchService.getMatchWithResults(tournamentMatch.getMatchId());
                            if (matchWithPlayers != null) {
                                prepareMatchFragment(matchWithPlayers);
                            }
                        }));
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage(), ex);
            }
        }
    };

    /**
     * Gets selected match data.
     *
     * @param v View
     * @return tournament match data
     */
    private TournamentMatchSimple getMatchFromRow(View v) {
        TableRow row = (TableRow) v.getParent();
        TextView tv = (TextView) row.getChildAt(0);
        int index = Integer.parseInt(tv.getText().toString()) - 1;
        return matches.get(index);
    }

    /**
     * Prepares fragment responsible for providing score in the match.
     *
     * @param match with players and results data
     */
    private void prepareMatchFragment(MatchWithPlayers match) {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("match", match);
        if (match.getMatch().isRefereeMode()) {
            fragmentTransaction.replace(R.id.fragment_tournament, RefereeModeFragment.class, bundle);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }
}