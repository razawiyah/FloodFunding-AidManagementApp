package com.example.floodfunding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class a_menu_DonationHistory extends AppCompatActivity {

    RecyclerView mDonationHistA;

    Button mDoneBtn;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    a_history_adapter a_history_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amenu_donation_history);

        mDonationHistA  = findViewById(R.id.donationHistA);
        mDonationHistA.setLayoutManager(new LinearLayoutManager(a_menu_DonationHistory.this));

        mDoneBtn        = findViewById(R.id.aDoneBtn2);

        fAuth           = FirebaseAuth.getInstance();
        fStore          = FirebaseFirestore.getInstance();

        Query query = fStore.collection("donation");

        FirestoreRecyclerOptions<a_HistoryModel> options =
                new FirestoreRecyclerOptions.Builder<a_HistoryModel>()
                        .setQuery(query, a_HistoryModel.class)
                        .build();

        a_history_adapter = new a_history_adapter(options);
        mDonationHistA.setAdapter(a_history_adapter);

        a_history_adapter.startListening();

        //done review button
        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open a_menu activity
                startActivity(new Intent(getApplicationContext(), a_menu.class));
            }
        });
    }
}