package pl.pjatk.squashme.adapter;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import pl.pjatk.squashme.R;
import pl.pjatk.squashme.model.custom.MatchHistory;

import java.util.List;

public class MyQuickMatchHistoryRecyclerViewAdapter extends RecyclerView.Adapter<MyQuickMatchHistoryRecyclerViewAdapter.ViewHolder> {

    private final List<MatchHistory> mValues;
    private final Context context;

    public MyQuickMatchHistoryRecyclerViewAdapter(List<MatchHistory> items, Context context) {
        mValues = items;
        this.context = context;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_quick_match_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mSets1View.setText(String.valueOf(mValues.get(position).getSets1()));
        holder.mSets2View.setText(String.valueOf(mValues.get(position).getSets2()));
        holder.mPlayer1View.setText(mValues.get(position).getPlayer1());
        holder.mPlayer2View.setText(mValues.get(position).getPlayer2());

        if (mValues.get(position).getSets2() > mValues.get(position).getSets1()) {
            holder.mSets2View.setTextColor(ContextCompat.getColor(context, R.color.winning_score));
            holder.mSets1View.setTextColor(ContextCompat.getColor(context, R.color.losing_score));
        } else if (mValues.get(position).getSets2() < mValues.get(position).getSets1()) {
            holder.mSets1View.setTextColor(ContextCompat.getColor(context, R.color.winning_score));
            holder.mSets2View.setTextColor(ContextCompat.getColor(context, R.color.losing_score));
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mSets1View;
        public final TextView mSets2View;
        public final TextView mPlayer1View;
        public final TextView mPlayer2View;
        public MatchHistory mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mSets1View = view.findViewById(R.id.quick_match_history_sets1);
            mSets2View = view.findViewById(R.id.quick_match_history_sets2);
            mPlayer1View = view.findViewById(R.id.quick_match_history_player1);
            mPlayer2View = view.findViewById(R.id.quick_match_history_player2);
        }
    }
}