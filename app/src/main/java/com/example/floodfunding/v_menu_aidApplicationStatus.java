package com.example.floodfunding;

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

public class v_menu_aidApplicationStatus extends AppCompatActivity {

    TextView mUserName, mUserIC, mUserEmail, mUserPhone;
    TextView mtextViewAddress, mtextViewDate, mtextViewHousehold, mtextViewHouseStat, mtextViewWaterLevel, mtextViewAidApproval;
    Button mDoneBtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmenu_aid_application_status);

        mUserName               = findViewById(R.id.userName);
        mUserIC                 = findViewById(R.id.userIC);
        mUserEmail              = findViewById(R.id.userEmail);
        mUserPhone              = findViewById(R.id.userPhone);

        mDoneBtn                = findViewById(R.id.doneRevBtn);

        mtextViewAddress        = findViewById(R.id.textViewAddress);
        mtextViewDate           = findViewById(R.id.textViewDate);
        mtextViewHousehold      = findViewById(R.id.textViewHousehold);
        mtextViewHouseStat      = findViewById(R.id.textViewHouseStat);
        mtextViewWaterLevel     = findViewById(R.id.textViewWaterLevel);
        mtextViewAidApproval    = findViewById(R.id.textViewAidApproval);

        progressBar             = findViewById(R.id.progressBar4);

        fAuth                   = FirebaseAuth.getInstance();
        fStore                  = FirebaseFirestore.getInstance();

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
        ((DocumentReference) documentReference).addSnapshotListener(v_menu_aidApplicationStatus.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException error) {

                if (documentSnapshot == null || error != null) return;

                mUserName.setText(documentSnapshot.getString("fullname"));
                mUserIC.setText(documentSnapshot.getString("ic"));
                mUserEmail.setText(documentSnapshot.getString("email"));
                mUserPhone.setText(documentSnapshot.getString("phone"));
            }
        });

        //fetch data for aid application
        DocumentReference documentReference2 = fStore.collection("aid_application").document(userID);
        ((DocumentReference) documentReference2).addSnapshotListener(v_menu_aidApplicationStatus.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot2, FirebaseFirestoreException error) {

                if (documentSnapshot2 == null || error != null) return;

                mtextViewAddress.setText(documentSnapshot2.getString("address"));
                mtextViewDate.setText(documentSnapshot2.getString("date"));
                mtextViewHousehold.setText(documentSnapshot2.getString("household"));
                mtextViewHouseStat.setText(documentSnapshot2.getString("houseStat"));
                mtextViewWaterLevel.setText(documentSnapshot2.getString("waterLevel"));
                mtextViewAidApproval.setText(documentSnapshot2.getString("approvalStat"));
            }
        });

        //done review button
        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                //open application status's login activity
                startActivity(new Intent(getApplicationContext(), v_menu.class));
            }
        });
    }
}