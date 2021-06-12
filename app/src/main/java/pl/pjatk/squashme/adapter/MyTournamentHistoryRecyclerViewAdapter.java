package pl.pjatk.squashme.adapter;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import pl.pjatk.squashme.R;
import pl.pjatk.squashme.fragment.RefereeModeFragment;
import pl.pjatk.squashme.fragment.TournamentHistoryMatchesFragment;
import pl.pjatk.squashme.model.custom.TournamentHistory;

import java.util.List;

public class MyTournamentHistoryRecyclerViewAdapter extends RecyclerView.Adapter<MyTournamentHistoryRecyclerViewAdapter.ViewHolder> {

    private final FragmentManager fragmentManager;
    private final List<TournamentHistory> mValues;

    public MyTournamentHistoryRecyclerViewAdapter(List<TournamentHistory> items, FragmentManager fragmentManager) {
        mValues = items;
        this.fragmentManager = fragmentManager;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tournament_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTournamentNameView.setText(mValues.get(position).getTournamentName());
        holder.mView.setOnClickListener(V -> prepareTournamentDetailsFragment(mValues.get(position).getTournamentId()));
    }

    private void prepareTournamentDetailsFragment(long tournamentId) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putLong("tournamentId", tournamentId);
        fragmentTransaction.replace(R.id.fragment_history, TournamentHistoryMatchesFragment.class, bundle);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTournamentNameView;
        public TournamentHistory mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTournamentNameView = view.findViewById(R.id.tournament_history_name);
        }
    }
}