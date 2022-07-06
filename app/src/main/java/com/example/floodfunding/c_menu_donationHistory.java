package com.example.floodfunding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class c_menu_donationHistory extends AppCompatActivity {

    RecyclerView mDonationHistC;

    Button mDoneBtn;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    c_history_adapter c_history_adapter;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmenu_donation_history);

        mDonationHistC  = findViewById(R.id.mDonationHistC);
        mDonationHistC.setLayoutManager(new LinearLayoutManager(c_menu_donationHistory.this));

        mDoneBtn        = findViewById(R.id.aDoneBtn3);

        fAuth           = FirebaseAuth.getInstance();
        fStore          = FirebaseFirestore.getInstance();

        //fetch current userID
        if (fAuth.getCurrentUser() == null) {
            //Go to login
            startActivity(new Intent(getApplicationContext(), login.class));
        }
        else{
            userID = fAuth.getCurrentUser().getUid();
        }

        Log.i("c_user", String.valueOf(fAuth.getCurrentUser()));

        Query query = fStore.collection("donation").whereEqualTo("userID", userID);

        FirestoreRecyclerOptions<c_HistoryModel> options =
                new FirestoreRecyclerOptions.Builder<c_HistoryModel>()
                        .setQuery(query, c_HistoryModel.class)
                        .build();

        c_history_adapter = new c_history_adapter(options);
        mDonationHistC.setAdapter(c_history_adapter);

        c_history_adapter.startListening();

        //done review button
        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open c_menu activity
                startActivity(new Intent(getApplicationContext(), c_menu.class));
            }
        });
    }
}