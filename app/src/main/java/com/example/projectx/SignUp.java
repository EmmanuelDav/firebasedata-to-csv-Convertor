package com.example.projectx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    EditText mName, mEmail, mPassword,mSerialNum,mPhoneNum,mLocation;
    FirebaseAuth mAuth;
    Button mSignUpButton;
    Spinner mUserSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mName = findViewById(R.id.namee);
        mEmail = findViewById(R.id.mail);
        mPassword = findViewById(R.id.rpassword);
        mSerialNum = findViewById(R.id.serialNum);
        mPhoneNum = findViewById(R.id.phoneNum);
        mLocation = findViewById(R.id.mLocation);
        mSignUpButton = findViewById(R.id.signup);
        mUserSpinner = findViewById(R.id.Atype);
        mAuth = FirebaseAuth.getInstance();
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                final String mail = mEmail.getText().toString();
                String password = mPassword.getText().toString().trim();
                final String name = mName.getText().toString();
                final String phoneN = mPhoneNum.getText().toString();
                final String location = mLocation.getText().toString();
                final String seNum = mSerialNum.getText().toString();
                if (mail.isEmpty() || password.isEmpty() || name.isEmpty()) {
                    Toast.makeText(SignUp.this, "Blank Field Found", Toast.LENGTH_LONG).show();
                } else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Users").whereEqualTo("Username", mail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task != null) {
//                                Toast.makeText(SignUp.this, "User Already registered", Toast.LENGTH_SHORT).show();
//                                Intent i = new Intent(SignUp.this, SignIn.class);
//                                startActivity(i);
//                                finish();
//                            }
                        }
                    });
                    mAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> pTask) {
                            if (pTask.isSuccessful()) {
                                Toast.makeText(SignUp.this, "User SignUp Successful", Toast.LENGTH_SHORT).show();
                                String User = "User";
                                String userType = mUserSpinner.getSelectedItem().toString();
                                if (userType.equals("Hostel Owner")) {
                                    User = "Owner";
                                }
                                Map<String, String> user = new HashMap<>();
                                user.put("Name", name);
                                user.put("email", mail);
                                user.put("AccountType", userType);
                                user.put("location", location);
                                user.put("phoneN", phoneN);
                                user.put("serialNum", seNum);
                                FirebaseFirestore database = FirebaseFirestore.getInstance();
                                database.collection("Users").document(name).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SignUp.this, "User SignUp Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUp.this, SignIn.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignUp.this, "User SignUp Unsuccessful No Internet", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception pE) {
                            Toast.makeText(SignUp.this, " No Internet", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

}