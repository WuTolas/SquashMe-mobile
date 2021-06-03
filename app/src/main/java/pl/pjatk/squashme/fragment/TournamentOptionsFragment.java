package pl.pjatk.squashme.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.pjatk.squashme.R;
import pl.pjatk.squashme.activity.TournamentDashboardNavigation;

public class TournamentOptionsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (requireActivity() instanceof TournamentDashboardNavigation) {
            ((TournamentDashboardNavigation) requireActivity()).showBottomNavigation();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tournament_options, container, false);
    }
}