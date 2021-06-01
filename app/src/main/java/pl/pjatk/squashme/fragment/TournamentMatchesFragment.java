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
import pl.pjatk.squashme.di.component.DaggerTournamentMatchesFragmentComponent;
import pl.pjatk.squashme.di.module.RoomModule;
import pl.pjatk.squashme.model.complex.TournamentMatchSimple;
import pl.pjatk.squashme.service.MatchService;

public class TournamentMatchesFragment extends Fragment {

    @Inject
    public MatchService matchService;
    private CompositeDisposable disposables;

    private static final String ARG_TOURNAMENT_ID = "tournamentId";
    private long tournamentId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tournamentId = getArguments().getLong(ARG_TOURNAMENT_ID);
        }
        DaggerTournamentMatchesFragmentComponent.builder()
                .roomModule(new RoomModule(getActivity().getApplication()))
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
                .subscribe(r -> prepareMatchesTable(r, view)));
    }

    private void prepareMatchesTable(List<TournamentMatchSimple> matches, View view) {
        TableLayout tl = (TableLayout) view.findViewById(R.id.tbl_tournament_matches);
        addTableHeader(tl);
        for (int i = 0; i < matches.size(); i++) {
            addTableRow(tl, matches.get(i), i);
        }
    }

    private void addTableHeader(TableLayout tableLayout) {
        TableRow headerRow = new TableRow(getContext());

        TextView tv0 = new TextView(getContext());
        tv0.setText("#");
        headerRow.addView(tv0);

        TextView tv1 = new TextView(getContext());
        tv1.setText(R.string.playerOne);
        headerRow.addView(tv1);

        TextView tv2 = new TextView(getContext());
        tv2.setText(R.string.playerTwo);
        headerRow.addView(tv2);

        TextView tv3 = new TextView(getContext());
        tv3.setText(R.string.round);
        headerRow.addView(tv3);

        tableLayout.addView(headerRow);
    }

    private void addTableRow(TableLayout tableLayout, TournamentMatchSimple match, int rowId) {
        TableRow tr = new TableRow(getContext());

        TextView tv0 = new TextView(getContext());
        tv0.setText(String.valueOf(++rowId));
        tr.addView(tv0);

        TextView tv1 = new TextView(getContext());
        tv1.setText(match.getPlayer1().getName());
        tr.addView(tv1);

        TextView tv2 = new TextView(getContext());
        tv2.setText(match.getPlayer2().getName());
        tr.addView(tv2);

        TextView tv3 = new TextView(getContext());
        tv3.setText(String.valueOf(match.getMatch().getTournament_round()));
        tr.addView(tv3);

        tableLayout.addView(tr);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }
}