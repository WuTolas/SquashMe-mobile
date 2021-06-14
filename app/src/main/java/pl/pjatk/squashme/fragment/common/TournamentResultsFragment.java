package pl.pjatk.squashme.fragment.common;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.pjatk.squashme.R;
import pl.pjatk.squashme.activity.TournamentDashboardNavigation;
import pl.pjatk.squashme.activity.TournamentInfo;
import pl.pjatk.squashme.di.component.DaggerTournamentResultsFragmentComponent;
import pl.pjatk.squashme.di.module.RoomModule;
import pl.pjatk.squashme.model.custom.TournamentResults;
import pl.pjatk.squashme.service.TournamentService;

/**
 * Fragment class responsible for displaying leaderboard in the tournament.
 */
public class TournamentResultsFragment extends Fragment {

    @Inject
    public TournamentService tournamentService;
    private CompositeDisposable disposables;

    private long tournamentId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tournamentId = ((TournamentInfo) requireActivity()).getTournamentId();
        DaggerTournamentResultsFragmentComponent.builder()
                .roomModule(new RoomModule((requireActivity().getApplication())))
                .build()
                .inject(this);
        disposables = new CompositeDisposable();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tournament_results, container, false);
        if (requireActivity() instanceof TournamentDashboardNavigation) {
            ((TournamentDashboardNavigation) requireActivity()).showBottomNavigation();
        }
        return view;
    }

    /**
     * Gets tournament results from tournament service.
     *
     * @param view View
     * @param savedInstanceState Bundle
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        disposables.add(Observable.fromSingle(tournamentService.searchTournamentResults(tournamentId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(r -> prepareResultsTable(r, view)));
    }

    /**
     * Prepares results table.
     *
     * @param results tournament results data needed for leaderboard
     * @param view View
     */
    private void prepareResultsTable(List<TournamentResults> results, View view) {
        TableLayout tl = view.findViewById(R.id.tbl_tournament_results);
        ViewGroup container = null;
        addTableHeader(tl, container);
        for (int i = 0; i < results.size(); i++) {
            addTableRow(tl, results.get(i), i, container);
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
        tv1.setText(R.string.name);
        headerRow.addView(tv1);

        TextView tv2 = (TextView) getLayoutInflater().inflate(R.layout.header_col, container, false);
        tv2.setText(R.string.games);
        headerRow.addView(tv2);

        TextView tv3 = (TextView) getLayoutInflater().inflate(R.layout.header_col, container, false);
        tv3.setText(R.string.sets);
        headerRow.addView(tv3);

        TextView tv4 = (TextView) getLayoutInflater().inflate(R.layout.header_col, container, false);
        tv4.setText(R.string.points);
        headerRow.addView(tv4);

        tableLayout.addView(headerRow);
    }

    /**
     * Creates table row.
     *
     * @param tableLayout TableLayout
     * @param result tournament result data needed for leaderboard row
     * @param rowId row id
     * @param container ViewGroup
     */
    private void addTableRow(TableLayout tableLayout, TournamentResults result, int rowId, ViewGroup container) {
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
        tv1.setText(result.getName());
        tr.addView(tv1);

        TextView tv2 = (TextView) getLayoutInflater().inflate(R.layout.normal_col, container, false);
        tv2.setText(getString(R.string.slash, result.getWinning(), result.getLosing()));
        tr.addView(tv2);

        TextView tv3 = (TextView) getLayoutInflater().inflate(R.layout.normal_col, container, false);
        tv3.setText(getString(R.string.slash, result.getSetsWinning(), result.getSetsLosing()));
        tr.addView(tv3);

        TextView tv4 = (TextView) getLayoutInflater().inflate(R.layout.normal_col, container, false);
        tv4.setText(getString(R.string.slash, result.getPointsWinning(), result.getPointsLosing()));
        tr.addView(tv4);

        tableLayout.addView(tr);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }
}