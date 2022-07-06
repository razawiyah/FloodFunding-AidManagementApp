package com.example.floodfunding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class a_menu_postAidApproval extends AppCompatActivity {

    TextView mUserName, mUserIC, mUserEmail, mUserPhone;
    TextView mtextViewAddress, mtextViewDate, mtextViewHousehold, mtextViewHouseStat, mtextViewWaterLevel, mtextViewAidApproval;

    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amenu_post_aid_approval);

            mUserName               = findViewById(R.id.userName);
            mUserIC                 = findViewById(R.id.userIC);
            mUserEmail              = findViewById(R.id.userEmail);
            mUserPhone              = findViewById(R.id.userPhone);

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
                //Go to application table
                startActivity(new Intent(getApplicationContext(), a_menu.class));
            }
            else{
                //along
                //how to get id of aid_applicator
                DocumentReference df = fStore.collection("aid_application").document();
                df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        //Log.d(ContentValues.TAG, "onSuccess"+ documentSnapshot.getData());
                        //.put("fullname", documentSnapshot.getString("fullname"));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(a_menu_postAidApproval.this, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });

                userID = fAuth.getCurrentUser().getUid();
            }

            Log.i("user", String.valueOf(fAuth.getCurrentUser()));

            //fetch data for user profile
            DocumentReference documentReference = fStore.collection("user").document(userID);
            ((DocumentReference) documentReference).addSnapshotListener(a_menu_postAidApproval.this, new EventListener<DocumentSnapshot>() {
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
            ((DocumentReference) documentReference2).addSnapshotListener(a_menu_postAidApproval.this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(DocumentSnapshot documentSnapshot2, FirebaseFirestoreException error) {

                    if (documentSnapshot2 == null || error != null) return;

                    mtextViewAddress.setText(documentSnapshot2.getString("address"));
                    mtextViewDate.setText(documentSnapshot2.getString("date"));
                    mtextViewHousehold.setText(documentSnapshot2.getString("household"));
                    mtextViewHouseStat.setText(documentSnapshot2.getString("houseStat"));
                    mtextViewWaterLevel.setText(documentSnapshot2.getString("waterLevel"));
                    mtextViewAidApproval.setText(documentSnapshot2.getString("ApprovalStat"));
                }
            });
    }
}