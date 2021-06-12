package pl.pjatk.squashme.fragment;

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
import pl.pjatk.squashme.model.complex.TournamentResults;
import pl.pjatk.squashme.service.TournamentService;

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
        if (requireActivity() instanceof TournamentDashboardNavigation) {
            ((TournamentDashboardNavigation) requireActivity()).showBottomNavigation();
        }
        return inflater.inflate(R.layout.fragment_tournament_results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        disposables.add(Observable.fromSingle(tournamentService.searchTournamentResults(tournamentId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(r -> prepareResultsTable(r, view)));
    }

    private void prepareResultsTable(List<TournamentResults> results, View view) {
        TableLayout tl = view.findViewById(R.id.tbl_tournament_results);
        ViewGroup container = getRootContainer();
        addTableHeader(tl, container);
        for (int i = 0; i < results.size(); i++) {
            addTableRow(tl, results.get(i), i, container);
        }
    }

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

    private ViewGroup getRootContainer() {
        int rootId = ((ViewGroup) requireView().getParent()).getId();
        return (ViewGroup) requireView().findViewById(rootId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }
}