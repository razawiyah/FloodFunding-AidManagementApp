package com.example.floodfunding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class a_menu extends AppCompatActivity {

    TextView mUserName, mUserIC, mUserEmail, mUserPhone;
    Button mAidApprovalBtn, mDonationHistBtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amenu);

        mUserName       = findViewById(R.id.userName);
        mUserIC         = findViewById(R.id.userIC);
        mUserEmail      = findViewById(R.id.userEmail);
        mUserPhone      = findViewById(R.id.userPhone);

        mAidApprovalBtn   = findViewById(R.id.aidApprovalBtn);
        mDonationHistBtn   = findViewById(R.id.donationHistBtn);
        progressBar     = findViewById(R.id.progressBar4);

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

        Log.i("user", String.valueOf(fAuth.getCurrentUser()));

        //fetch data for user profile
        DocumentReference documentReference = fStore.collection("user").document(userID);
        ((DocumentReference) documentReference).addSnapshotListener(a_menu.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException error) {
                if (documentSnapshot == null || error != null) return;

                mUserName.setText(documentSnapshot.getString("fullname"));
                mUserIC.setText(documentSnapshot.getString("ic"));
                mUserEmail.setText(documentSnapshot.getString("email"));
                mUserPhone.setText(documentSnapshot.getString("phone"));
            }
        });

        //start post-aid approval's activity
        mAidApprovalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                startActivity(new Intent(getApplicationContext(), a_menu_aidApplicationList.class));
            }
        });

        //start donation history's activity
        mDonationHistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                //open application status's login activity
                startActivity(new Intent(getApplicationContext(), a_menu_DonationHistory.class));
            }
        });


    }

    //logout function
    public void logout(View view) {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), login.class));
        finish();
    }
}