package com.example.floodfunding;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class c_menu_donationPage<Exceptione> extends AppCompatActivity {

    TextView mOptRM10, mOptRM25, mOptRM50, mOptRM100, mOptRM500, mOptRM1000;
    EditText mAmount;
    Button mPaymentBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID, sBank, mDate, mIc;

    String[] bankOpt = {"Maybank2U","CIMB Clicks","Public Bank", "RHB Now", "Hong Leong Connect", "Ambank", "MyBSN", "Bank Rakyat"};
    AutoCompleteTextView mBankOpt;
    ArrayAdapter<String> adapterBank;

    boolean valid=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmenu_donation_page);

        mOptRM10        = findViewById(R.id.optRM10);
        mOptRM25        = findViewById(R.id.optRM25);
        mOptRM50        = findViewById(R.id.optRM50);
        mOptRM100       = findViewById(R.id.optRM100);
        mOptRM500       = findViewById(R.id.optRM500);
        mOptRM1000      = findViewById(R.id.optRM1000);

        mBankOpt        = findViewById(R.id.bankOpt);
        mAmount         = findViewById(R.id.editTextDonation);
        mPaymentBtn     = findViewById(R.id.paymentBtn);

        fAuth           = FirebaseAuth.getInstance();
        fStore          = FirebaseFirestore.getInstance();
        userID          = fAuth.getCurrentUser().getUid();

        //set date
        mDate = getDate();

        //fetch ic
        DocumentReference documentReference = fStore.collection("user").document(userID);
        ((DocumentReference) documentReference).addSnapshotListener(c_menu_donationPage.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException error) {
                if (documentSnapshot == null || error != null) return;

                mIc = documentSnapshot.getString("ic");
                Log.d(TAG, "onSuccess: ic is " + mIc);
            }
        });

        //loading page
        c_loadingPage loadingPage = new c_loadingPage(c_menu_donationPage.this);

        //RM10 textview
        mOptRM10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAmount.setText("10");
            }
        });

        //RM25 textview
        mOptRM25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAmount.setText("25");
            }
        });

        //RM50 textview
        mOptRM50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAmount.setText("50");
            }
        });

        //RM100 textview
        mOptRM100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAmount.setText("100");
            }
        });

        //RM500 textview
        mOptRM500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAmount.setText("500");
            }
        });

        //RM1000 textview
        mOptRM1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAmount.setText("1000");
            }
        });

        //dropdown user type
        adapterBank = new ArrayAdapter<String>(c_menu_donationPage.this, R.layout.dropdown_item,bankOpt);
        mBankOpt.setAdapter(adapterBank);
        mBankOpt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

                //show success selection
                Toast.makeText(c_menu_donationPage.this, "Option: "+ item, Toast.LENGTH_SHORT).show();
                sBank = item;
            }
        });

        //paymentBtn function
        mPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //data validation
                checkField(mAmount);

                if (valid){
                    String ic = mIc;
                    String bankOpt  = sBank;
                    String amount   = mAmount.getText().toString();
                    String date     = mDate;
                    String sUserID  = userID;

                    //store data in firestore
                    DocumentReference documentReference = fStore.collection("donation").document(userID);
                    Map<String,Object> donation = new HashMap<>();

                    donation.put("amount", "RM" + amount);                      //donation amount
                    donation.put("payDate", date);                              //date
                    donation.put("payStat", "Successful");                      //payment status
                    donation.put("bankOpt", bankOpt);                           //Bank Option
                    donation.put("userID", sUserID);                            //userID
                    donation.put("ic", ic);                                     //ic

                    documentReference.set(donation).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "onSuccess: " + userID + " is processing.");
                            Toast.makeText(c_menu_donationPage.this, "donation has been processed", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(c_menu_donationPage.this, "process failed.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    //loading page start
                    loadingPage.startLoadingDialog();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingPage.dismissDialog();
                            startActivity(new Intent(getApplicationContext(), c_menu_donationReceipt.class));
                        }
                    }, 5000);
                }
            }
        });
    }

    //getDate
    private String getDate(){
        return new SimpleDateFormat("hh:mm a, dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }

    //checkField function
    public void checkField (@NonNull EditText textField){
        if (textField.getText().toString().isEmpty()){
            textField.setError("This field is Required.");
            valid=false;
        }
        else{
            valid=true;
        }
    }
}