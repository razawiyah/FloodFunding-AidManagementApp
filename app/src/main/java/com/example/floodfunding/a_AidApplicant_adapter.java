package com.example.floodfunding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class a_AidApplicant_adapter extends FirestoreRecyclerAdapter<a_AidApplicantModel, a_AidApplicant_adapter.holder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public a_AidApplicant_adapter(@NonNull FirestoreRecyclerOptions<a_AidApplicantModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull a_AidApplicant_adapter.holder holder, int position, @NonNull a_AidApplicantModel model) {
        holder.ic.setText(model.getIc());
        holder.date.setText(model.getDate());
        holder.approvalStat.setText(model.getApprovalStat());
    }

    @NonNull
    @Override
    public a_AidApplicant_adapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.a_data_cont, parent, false);
        return new holder(view);
    }

    public class holder extends RecyclerView.ViewHolder {

        TextView ic, date, approvalStat;

        public holder(@NonNull View itemView) {
            super(itemView);

            View view = itemView;

            ic = view.findViewById(R.id.aic);
            date =  view.findViewById(R.id.adate);
            approvalStat = view.findViewById(R.id.aapprovalStat);
        }
    }
}
