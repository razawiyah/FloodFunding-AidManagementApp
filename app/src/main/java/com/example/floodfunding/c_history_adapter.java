package com.example.floodfunding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class c_history_adapter extends FirestoreRecyclerAdapter<c_HistoryModel, c_history_adapter.holder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public c_history_adapter(@NonNull FirestoreRecyclerOptions<c_HistoryModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull c_history_adapter.holder holder, int position, @NonNull c_HistoryModel model) {
        holder.amount.setText(model.getAmount());
        holder.payDate.setText(model.getPayDate());
        holder.payStat.setText(model.getPayStat());
    }

    @NonNull
    @Override
    public c_history_adapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.c_data_cont, parent, false);
        return new c_history_adapter.holder(view);
    }

    public class holder extends RecyclerView.ViewHolder {

        TextView amount, payDate, payStat;

        public holder(@NonNull View itemView) {
            super(itemView);

            View view = itemView;

            amount = view.findViewById(R.id.amount);
            payDate =  view.findViewById(R.id.payDate);
            payStat = view.findViewById(R.id.payStat);
        }
    }
}
