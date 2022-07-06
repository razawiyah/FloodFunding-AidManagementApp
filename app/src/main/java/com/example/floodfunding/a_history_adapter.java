package com.example.floodfunding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class a_history_adapter extends FirestoreRecyclerAdapter<a_HistoryModel, a_history_adapter.holder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public a_history_adapter(@NonNull FirestoreRecyclerOptions<a_HistoryModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull a_history_adapter.holder holder, int position, @NonNull a_HistoryModel model) {
        holder.amount.setText(model.getAmount());
        holder.payDate.setText(model.getPayDate());
        holder.ic.setText(model.getIc());
    }

    @NonNull
    @Override
    public a_history_adapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ah_data_cont, parent, false);
        return new a_history_adapter.holder(view);
    }

    public class holder extends RecyclerView.ViewHolder {

        TextView amount, payDate, ic;

        public holder(@NonNull View itemView) {
            super(itemView);

            View view = itemView;

            amount = view.findViewById(R.id.amount);
            payDate =  view.findViewById(R.id.payDate);
            ic = view.findViewById(R.id.ic);
        }
    }
}
