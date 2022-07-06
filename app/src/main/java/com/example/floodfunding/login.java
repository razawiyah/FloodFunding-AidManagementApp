package com.example.floodfunding;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class login extends AppCompatActivity {

    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mRegisterText;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    String userID;

    boolean valid=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail          = findViewById(R.id.editTextEmail);
        mPassword       = findViewById(R.id.editTextPassword);
        mLoginBtn       = findViewById(R.id.loginBtn);
        mRegisterText   = findViewById(R.id.registerText);
        progressBar     = findViewById(R.id.progressBar3);

        fAuth           = FirebaseAuth.getInstance();
        fStore          = FirebaseFirestore.getInstance();

        //login button function
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validation data
                checkField(mEmail);
                checkField(mPassword);

                if (valid){
                    //text to string
                    String email    = mEmail.getText().toString().trim();
                    String password = mPassword.getText().toString().trim();

                    //check length of password
                    if(password.length() < 6){
                        mPassword.setError("Password must be >=6 characters.");
                        return;
                    }

                    progressBar.setVisibility(View.VISIBLE);

                    //authenticate the user
                    fAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(login.this, "User logged in.", Toast.LENGTH_SHORT).show();
                            //open user's menu
                            checkUserType(authResult.getUser().getUid());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(login.this, "Error!"+ e.getMessage() , Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        //register text function
        mRegisterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open victim register activity
                progressBar.setVisibility(View.VISIBLE);
                startActivity(new Intent(getApplicationContext(), register.class));
            }
        });
    }

    // check user type function
    private void checkUserType(String uid) {
        DocumentReference df = fStore.collection("user").document(uid);
        //extract data from the document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(TAG, "onSuccess"+ documentSnapshot.getData());

                //identify user type
                //if victim
                if (Objects.equals(documentSnapshot.getString("userType"), "Victim")){
                    Log.d(TAG, "onSuccess: victim");
                    startActivity(new Intent(getApplicationContext(), v_menu.class));
                    finish();
                }

                //if contributor
                if (Objects.equals(documentSnapshot.getString("userType"), "Contributor")){
                    Log.d(TAG, "onSuccess: contributor");
                    startActivity(new Intent(getApplicationContext(), c_menu.class));
                    finish();
                }

                //if Administrator
                if (Objects.equals(documentSnapshot.getString("userType"), "Administrator")){
                    Log.d(TAG, "onSuccess: admin");
                    startActivity(new Intent(getApplicationContext(), a_menu.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(login.this, "Error!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
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