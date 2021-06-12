package pl.pjatk.squashme.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import pl.pjatk.squashme.R;

public class HistoryOptionsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_options, container, false);
        initializeComponents(view);
        return view;
    }

    private void initializeComponents(View view) {
        Button quickMatchHistoryButton = view.findViewById(R.id.btn_quickmatch_history);
        quickMatchHistoryButton.setOnClickListener(v -> loadFragment(new QuickMatchHistoryFragment()));

        Button tournamentHistoryButton = view.findViewById(R.id.btn_tournament_history);
        tournamentHistoryButton.setOnClickListener(v -> loadFragment(new TournamentHistoryFragment()));
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_history, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}