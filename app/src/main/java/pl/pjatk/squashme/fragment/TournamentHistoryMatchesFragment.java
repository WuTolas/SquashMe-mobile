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

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.pjatk.squashme.R;
import pl.pjatk.squashme.activity.TournamentInfo;
import pl.pjatk.squashme.di.component.DaggerTournamentHistoryMatchesFragmentComponent;
import pl.pjatk.squashme.di.module.RoomModule;
import pl.pjatk.squashme.model.custom.TournamentMatchSimple;
import pl.pjatk.squashme.service.MatchService;

public class TournamentHistoryMatchesFragment extends Fragment {

    private long tournamentId;

    @Inject
    public MatchService matchService;
    private CompositeDisposable disposables;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tournamentId = ((TournamentInfo) requireActivity()).getTournamentId();
        DaggerTournamentHistoryMatchesFragmentComponent.builder()
                .roomModule(new RoomModule(requireActivity().getApplication()))
                .build()
                .inject(this);
        disposables = new CompositeDisposable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tournament_history_matches, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        disposables.add(Observable.fromSingle(matchService.searchTournamentMatches(tournamentId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(r -> prepareMatchesTable(r, view)));
    }

    private void prepareMatchesTable(List<TournamentMatchSimple> matches, View view) {
        TableLayout tl = view.findViewById(R.id.tbl_tournament_matches_history);
        ViewGroup container = getRootContainer();
        addTableHeader(tl, container);
        Integer currentRound = matches.stream().filter(m -> !m.isFinished()).map(TournamentMatchSimple::getTournamentRound).findFirst().orElse(null);
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

        TextView tv4 = (TextView) getLayoutInflater().inflate(R.layout.header_col, container, false);
        tv4.setText(R.string.result);
        headerRow.addView(tv4);

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