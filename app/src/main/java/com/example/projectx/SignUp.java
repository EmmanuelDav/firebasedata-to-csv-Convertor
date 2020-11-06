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

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    EditText mName, mEmail, mPassword;
    FirebaseAuth mAuth;
    Button mSignUpButton;
    Spinner mUserSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mName = findViewById(R.id.name);
        mEmail = findViewById(R.id.mail);
        mPassword = findViewById(R.id.rpassword);
        mSignUpButton = findViewById(R.id.signup);
        mUserSpinner = findViewById(R.id.Atype);
        mAuth = FirebaseAuth.getInstance();
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                final String mail = mEmail.getText().toString();
                String password = mPassword.getText().toString().trim();
                final String name = mName.getText().toString();
                if (mail.isEmpty() || password.isEmpty() || name.isEmpty()) {
                    Toast.makeText(SignUp.this, "Blank Field Found", Toast.LENGTH_LONG).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> pTask) {
                            if (pTask.isSuccessful()) {
                                String User = "User";
                                String userType = mUserSpinner.getSelectedItem().toString();
                                if (userType.equals("Hostel Owner")) {
                                    User = "Owner";
                                }
                                Map<String, String> user = new HashMap<>();
                                user.put("Name", name);
                                user.put("email", mail);
                                user.put("AccountType", userType);
                                FirebaseFirestore database = FirebaseFirestore.getInstance();
                                database.collection("Users").document(name).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SignUp.this, "User SignUp Successful", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignUp.this, "User SignUp Unsuccessful", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Intent intent = new Intent(SignUp.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception pE) {
                            Toast.makeText(SignUp.this, "User SignUp Unsuccessful", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }

}