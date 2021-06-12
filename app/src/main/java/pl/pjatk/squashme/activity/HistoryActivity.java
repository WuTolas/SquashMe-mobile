package pl.pjatk.squashme.activity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import pl.pjatk.squashme.R;
import pl.pjatk.squashme.fragment.HistoryOptionsFragment;

public class HistoryActivity extends BaseActivity implements TournamentInfo {

    private long tournamentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        HistoryOptionsFragment fragment = new HistoryOptionsFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_history, fragment);
        ft.commit();
    }

    @Override
    public long getTournamentId() {
        return tournamentId;
    }

    @Override
    public void setTournamentId(long tournamentId) {
        this.tournamentId = tournamentId;
    }
}