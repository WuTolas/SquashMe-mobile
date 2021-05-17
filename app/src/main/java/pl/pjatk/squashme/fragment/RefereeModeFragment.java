package pl.pjatk.squashme.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.pjatk.squashme.R;
import pl.pjatk.squashme.model.Match;
import pl.pjatk.squashme.model.MatchWithPlayers;

public class RefereeModeFragment extends Fragment {

    private static final String MATCH_PARAM = "match";

    private MatchWithPlayers match;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            match = (MatchWithPlayers) getArguments().getSerializable(MATCH_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_referee_mode, container, false);
    }
}