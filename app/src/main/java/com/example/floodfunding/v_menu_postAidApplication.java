package com.example.floodfunding;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class v_menu_postAidApplication extends AppCompatActivity {

    EditText mIc, mAddress, mDate, mHousehold;
    Button mApplyBtn;
    ProgressBar progressBar;
    CheckBox mDataDeclaration;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID, sResidentialStat, sWaterlevel;

    String[] residentalStat = {"Rent","Owned"};
    String[] waterLevel = {"Navel","Knee","Ankle"};
    AutoCompleteTextView mResidentialStat,mWaterLevel;
    ArrayAdapter<String> adapterItems, adapterItems2;

    boolean valid=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmenu_post_aid_application);

        //fetch data from activity
        mIc                 = findViewById(R.id.editTextIc);
        mAddress            = findViewById(R.id.editTextAddress);
        mDate               = findViewById(R.id.editTextDate);
        mHousehold          = findViewById(R.id.editTextHousehold);

        mResidentialStat    = findViewById(R.id.residentalStat);
        mWaterLevel         = findViewById(R.id.waterLevel);

        mDataDeclaration    = findViewById(R.id.dataDeclaration);
        mApplyBtn           = findViewById(R.id.applyBtn);
        progressBar         = findViewById(R.id.progressBar3);

        fAuth               = FirebaseAuth.getInstance();
        fStore              = FirebaseFirestore.getInstance();
        userID              = fAuth.getCurrentUser().getUid();

        //set date
        mDate.setText(getDate());

        //dropdown residental stat
        adapterItems = new ArrayAdapter<String>(v_menu_postAidApplication.this, R.layout.dropdown_item,residentalStat);
        mResidentialStat.setAdapter(adapterItems);
        mResidentialStat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

                //showing success selection
                Toast.makeText(getApplicationContext(), "House Status: "+ item, Toast.LENGTH_SHORT).show();
                sResidentialStat = item;
            }
        });

        //dropdown water level
        adapterItems2 = new ArrayAdapter<String>(v_menu_postAidApplication.this, R.layout.dropdown_item,waterLevel);
        mWaterLevel.setAdapter(adapterItems2);
        mWaterLevel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item2 = parent.getItemAtPosition(position).toString();

                //showing success selection
                Toast.makeText(getApplicationContext(), "Water Level: "+ item2, Toast.LENGTH_SHORT).show();
                sWaterlevel = item2;
            }
        });

        //fetch ic
        DocumentReference documentReference = fStore.collection("user").document(userID);
        ((DocumentReference) documentReference).addSnapshotListener(v_menu_postAidApplication.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException error) {
                if (documentSnapshot == null || error != null) return;

                mIc.setText(documentSnapshot.getString("ic"));
                Log.d(TAG, "onSuccess: ic is " + mIc);
            }
        });

        //apply aid button function
        mApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //data validation
                checkField(mAddress);
                checkField(mResidentialStat);
                checkField(mHousehold);
                checkTick(mDataDeclaration);

                if (valid){
                    //text to string
                    String address              = mAddress.getText().toString();
                    String date                 = mDate.getText().toString();
                    String household            = mHousehold.getText().toString();
                    String selResidentialStat   = sResidentialStat;
                    String selWaterLevel        = sWaterlevel;
                    String ic                   = mIc.getText().toString();

                    progressBar.setVisibility(View.VISIBLE);

                    //store data in firestore for victim aid_approval status check
                    DocumentReference documentReference = fStore.collection("aid_application").document(userID);
                    Map<String,Object> aid_application = new HashMap<>();
                    aid_application.put("address", address);
                    aid_application.put("date", date);
                    aid_application.put("household", household);
                    aid_application.put("houseStat", selResidentialStat);
                    aid_application.put("waterLevel", selWaterLevel);
                    aid_application.put("approvalStat", "Processing");
                    aid_application.put("ic", ic);

                    if (mDataDeclaration.isChecked()){
                        aid_application.put("Agreement","Agreed");
                    }

                    documentReference.set(aid_application).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "onSuccess: aid_application is applied for " + userID);
                            Toast.makeText(v_menu_postAidApplication.this, "aid_application is applied", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(v_menu_postAidApplication.this, "process failed." + e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                    //open victim's menu
                    startActivity(new Intent(getApplicationContext(), v_menu_aidApplicationStatus.class));
                }
            }
        });
    }

    //getDate
    private String getDate(){
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
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


    //checkTick function
    public void checkTick (@NonNull CheckBox box){
        if (box.isChecked()){
            valid=true;
        }
        else{
            Toast.makeText(getApplicationContext(), "Cannot apply if user do not agree.", Toast.LENGTH_SHORT).show();
            valid=false;
        }
    }
}