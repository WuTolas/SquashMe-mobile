package pl.pjatk.squashme.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import pl.pjatk.squashme.R;
import pl.pjatk.squashme.model.custom.TournamentHistory;

import java.util.List;

public class MyTournamentHistoryRecyclerViewAdapter extends RecyclerView.Adapter<MyTournamentHistoryRecyclerViewAdapter.ViewHolder> {

    private final List<TournamentHistory> mValues;

    public MyTournamentHistoryRecyclerViewAdapter(List<TournamentHistory> items) {
        mValues = items;
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