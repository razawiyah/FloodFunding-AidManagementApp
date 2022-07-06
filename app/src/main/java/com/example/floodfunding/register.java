package com.example.floodfunding;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class register extends AppCompatActivity {

    EditText mFullName, mIC, mPhone, mEmail, mPassword;
    Button mRegisterBtn;
    TextView mLoginText;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID, sUser;

    String[] userType = {"Victim","Contributor","Administrator"};
    AutoCompleteTextView mUser;
    ArrayAdapter<String> adapterItems;

    boolean valid=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //fetch drawable data
        mFullName       = findViewById(R.id.editTextFullName);
        mIC             = findViewById(R.id.editTextIC);
        mPhone          = findViewById(R.id.editTextPhone);
        mEmail          = findViewById(R.id.editTextEmail);
        mPassword       = findViewById(R.id.editTextPassword);
        mRegisterBtn    = findViewById(R.id.registerBtn);
        mLoginText      = findViewById(R.id.loginText);
        mUser           = findViewById(R.id.userType);

        progressBar     = findViewById(R.id.progressBar2);

        fAuth           = FirebaseAuth.getInstance();
        fStore          = FirebaseFirestore.getInstance();


        //dropdown user type
        adapterItems = new ArrayAdapter<String>(register.this, R.layout.dropdown_item,userType);
        mUser.setAdapter(adapterItems);
        mUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

                //show success selection
                Toast.makeText(register.this, "User Type: "+ item, Toast.LENGTH_SHORT).show();
                sUser = item;
            }
        });

        //register button function
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validation data
                checkField(mEmail);
                checkField(mPassword);
                checkField(mFullName);
                checkField(mPhone);
                checkField(mIC);

                if (valid){
                    //text to string
                    String email        = mEmail.getText().toString().trim();
                    String password     = mPassword.getText().toString().trim();
                    String fullName     = mFullName.getText().toString();
                    String phone        = mPhone.getText().toString();
                    String ic           = mIC.getText().toString();
                    String selUserType  =sUser;

                    //check length of password
                    if(password.length() < 6){
                        mPassword.setError("Password must be >=6 characters.");
                        return;
                    }

                    progressBar.setVisibility(View.VISIBLE);

                    //register user in firebase
                    fAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            //toast for user created alert
                            Toast.makeText(register.this, "User Created", Toast.LENGTH_SHORT).show();

                            //store data in fireStore
                            userID = fAuth.getCurrentUser().getUid();

                            DocumentReference documentReference = fStore.collection("user").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fullname", fullName);
                            user.put("ic", ic);
                            user.put("phone", phone);
                            user.put("email", email);
                            user.put("userType", selUserType);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "onSuccess: user profile is created for" + userID);
                                }
                            });

                            //open user's menu
                            checkUserType(authResult.getUser().getUid());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(register.this, "Error!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        //login text function
        mLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open victim login activity
                startActivity(new Intent(getApplicationContext(), login.class));
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
                Log.d(ContentValues.TAG, "onSuccess"+ documentSnapshot.getData());

                //identify user type
                //if victim
                if (Objects.equals(documentSnapshot.getString("userType"), "Victim")){
                    Log.d(ContentValues.TAG, "onSuccess: victim");
                    startActivity(new Intent(getApplicationContext(), v_menu.class));
                    finish();
                }

                //if contributor
                if (Objects.equals(documentSnapshot.getString("userType"), "Contributor")){
                    Log.d(ContentValues.TAG, "onSuccess: contributor");
                    startActivity(new Intent(getApplicationContext(), c_menu.class));
                    finish();
                }

                //if Administrator
                if (Objects.equals(documentSnapshot.getString("userType"), "Administrator")){
                    Log.d(ContentValues.TAG, "onSuccess: admin");
                    startActivity(new Intent(getApplicationContext(), a_menu.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(register.this, "Error!", Toast.LENGTH_SHORT).show();
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