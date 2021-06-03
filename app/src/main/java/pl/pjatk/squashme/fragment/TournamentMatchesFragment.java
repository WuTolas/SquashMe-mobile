package pl.pjatk.squashme.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.pjatk.squashme.R;
import pl.pjatk.squashme.di.component.DaggerTournamentMatchesFragmentComponent;
import pl.pjatk.squashme.di.module.RoomModule;
import pl.pjatk.squashme.model.MatchWithPlayers;
import pl.pjatk.squashme.model.complex.TournamentMatchSimple;
import pl.pjatk.squashme.service.MatchService;

public class TournamentMatchesFragment extends Fragment {

    private static final String TAG = TournamentMatchesFragment.class.getSimpleName();

    @Inject
    public MatchService matchService;
    private CompositeDisposable disposables;

    private static final String ARG_TOURNAMENT_ID = "tournamentId";
    private List<TournamentMatchSimple> matches;
    private long tournamentId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tournamentId = getArguments().getLong(ARG_TOURNAMENT_ID);
        }
        DaggerTournamentMatchesFragmentComponent.builder()
                .roomModule(new RoomModule(requireActivity().getApplication()))
                .build()
                .inject(this);
        disposables = new CompositeDisposable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tournament_matches, container, false);
    }

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

    private void prepareMatchesTable(List<TournamentMatchSimple> matches, View view) {
        TableLayout tl = view.findViewById(R.id.tbl_tournament_matches);
        ViewGroup container = getRootContainer();
        addTableHeader(tl, container);
        Integer currentRound = matches.stream().filter(m -> !m.getMatch().isFinished()).map(m -> m.getMatch().getTournamentRound()).findFirst().orElse(null);
        for (int i = 0; i < matches.size(); i++) {
            addTableRow(tl, matches.get(i), i, container, currentRound);
        }
    }

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

        tableLayout.addView(headerRow);
    }

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
        tv1.setText(match.getPlayer1().getName());
        tr.addView(tv1);

        TextView tv2 = (TextView) getLayoutInflater().inflate(R.layout.normal_col, container, false);
        tv2.setText(match.getPlayer2().getName());
        tr.addView(tv2);

        TextView tv3 = (TextView) getLayoutInflater().inflate(R.layout.normal_col, container, false);
        tv3.setText(String.valueOf(match.getMatch().getTournamentRound()));
        tr.addView(tv3);

        Button btn;

        if (match.getMatch().isFinished()) {
            btn = (Button) getLayoutInflater().inflate(R.layout.ok_button_col, container, false);
            tr.addView(btn);
        } else if (!match.getMatch().isFinished() && match.getMatch().isRefereeMode() != null) {
            btn = (Button) getLayoutInflater().inflate(R.layout.continue_botton_col, container, false);
            tr.addView(btn);
        } else if (!match.getMatch().isFinished() && currentRound.equals(match.getMatch().getTournamentRound())) {
            btn = (Button) getLayoutInflater().inflate(R.layout.play_button_col, container, false);
            btn.setOnClickListener(startMatchDialogListener);
            tr.addView(btn);
        }

        tableLayout.addView(tr);
    }

    private ViewGroup getRootContainer() {
        int rootId = ((ViewGroup) requireView().getParent()).getId();
        return (ViewGroup) requireView().findViewById(rootId);
    }

    private final OnClickListener startMatchDialogListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            TableRow row = (TableRow) v.getParent();
            TextView tv = (TextView) row.getChildAt(0);
            try {
                int index = Integer.parseInt(tv.getText().toString()) - 1;
                TournamentMatchSimple selectedMatch = matches.get(index);
                CharSequence[] choices = {getString(R.string.provide_score_mode), getString(R.string.referee_mode)};
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(selectedMatch.getPlayer1().getName() + getString(R.string.vs) + selectedMatch.getPlayer2().getName())
                        .setSingleChoiceItems(choices, 1, null)
                        .setNeutralButton(R.string.cancel, null)
                        .setPositiveButton(R.string.confirm, (dialog, which) -> {
                            ListView lv = ((AlertDialog) dialog).getListView();
                            disposables.add(Single.just(selectedMatch)
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(tournamentMatch -> {
                                        boolean refereeMode = lv.getCheckedItemPosition() == 1;
                                        matchService.updateRefereeMode(tournamentMatch.getMatch().getId(), refereeMode);
                                        MatchWithPlayers matchWithPlayers = matchService.getMatchWithResults(tournamentMatch.getMatch().getId());
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

    private void prepareMatchFragment(MatchWithPlayers match) {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("match", match);
        if (match.getMatch().isRefereeMode()) {
            fragmentTransaction.replace(R.id.fragment_tournament, RefereeModeFragment.class, bundle);
        } else {
            fragmentTransaction.replace(R.id.fragment_tournament, QuickScoreModeFragment.class, bundle);
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