package com.example.floodfunding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Date;

public class c_menu_donationReceipt extends AppCompatActivity {

    TextView mPayDate, mPayStat, mTotalPaid;
    Button mDoneBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmenu_donation_receipt);

        mPayDate    = findViewById(R.id.payDate);
        mPayStat    = findViewById(R.id.payStat);
        mTotalPaid  = findViewById(R.id.totalPaid);

        mDoneBtn    = findViewById(R.id.doneBtn);

        fAuth           = FirebaseAuth.getInstance();
        fStore          = FirebaseFirestore.getInstance();
        userID          = fAuth.getCurrentUser().getUid();

        //fetch current userID
        if (fAuth.getCurrentUser() == null) {
            //Go to login
            startActivity(new Intent(getApplicationContext(), login.class));
        }
        else{
            userID = fAuth.getCurrentUser().getUid();
        }

        Log.i("user", String.valueOf(fAuth.getCurrentUser()));

        //fetch data for receipt
        DocumentReference documentReference = fStore.collection("donation").document(userID);
        ((DocumentReference) documentReference).addSnapshotListener(c_menu_donationReceipt.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException error) {

                if (documentSnapshot == null || error != null) return;

                mPayDate.setText(documentSnapshot.getString("payDate"));
                mPayStat.setText(documentSnapshot.getString("payStat"));
                mTotalPaid.setText(documentSnapshot.getString("amount"));
            }
        });

        //doneBtn function
        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open application status's login activity
                startActivity(new Intent(getApplicationContext(), c_menu.class));
            }
        });
    }
}