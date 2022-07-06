package com.example.floodfunding;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class a_menu_aidApplicationList extends AppCompatActivity {

    RecyclerView mApplicantList;

    Button mDoneBtn;

    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    a_AidApplicant_adapter a_AidApplicant_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amenu_aid_application_list);

        mApplicantList  = findViewById(R.id.applicantList);
        mApplicantList.setLayoutManager(new LinearLayoutManager(a_menu_aidApplicationList.this));

        mDoneBtn        = findViewById(R.id.aDoneBtn);

        progressBar     = findViewById(R.id.progressBar3);
        fAuth           = FirebaseAuth.getInstance();
        fStore          = FirebaseFirestore.getInstance();

        Query query = fStore.collection("aid_application");

        FirestoreRecyclerOptions<a_AidApplicantModel> options =
                new FirestoreRecyclerOptions.Builder<a_AidApplicantModel>()
                        .setQuery(query, a_AidApplicantModel.class)
                        .build();

        a_AidApplicant_adapter = new a_AidApplicant_adapter(options);
        mApplicantList.setAdapter(a_AidApplicant_adapter);

        a_AidApplicant_adapter.startListening();

        //done review button
        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                //open a_menu activity
                startActivity(new Intent(getApplicationContext(), a_menu.class));
            }
        });

        }
}