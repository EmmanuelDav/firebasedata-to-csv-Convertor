package com.example.projectx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
    EditText mName, mEmail, mPassword, mSerialNum, mPhoneNum, mLocation;
    FirebaseAuth mAuth;
    Button mSignUpButton;
    Spinner mUserSpinner;
    AlertDialog.Builder builder;
    boolean isAdmin = false;
    AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        progressDialog = getDialogProgressBar().create();
        progressDialog.setCanceledOnTouchOutside(false);
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
                if (!isAdmin) {
                    progressDialog.show();
                    if (mail.isEmpty() || password.isEmpty() || name.isEmpty() || phoneN.isEmpty() || location.isEmpty()) {
                        progressDialog.dismiss();
                        Toast.makeText(SignUp.this, "Blank Field Found", Toast.LENGTH_LONG).show();
                    } else {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Users").whereEqualTo("Username", mail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                progressDialog.dismiss();
                                if (task != null) {
                                    Toast.makeText(SignUp.this, "User Already registered", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(SignUp.this, SignIn.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });
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
                                    user.put("AccountType", "User");
                                    user.put("location", location);
                                    user.put("phoneN", phoneN);
                                    user.put("serialNum", seNum);
                                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                                    database.collection("Users").document(name).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            Toast.makeText(SignUp.this, "User SignUp Successful", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SignUp.this, SignIn.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(SignUp.this, "User SignUp Unsuccessful No Internet", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception pE) {
                                progressDialog.dismiss();
                                Toast.makeText(SignUp.this, "Sign up Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }else {
                    progressDialog.show();
                    if (mail.isEmpty() || password.isEmpty()){
                        progressDialog.dismiss();
                        Toast.makeText(SignUp.this, "Missing Field Found", Toast.LENGTH_LONG).show();
                    }else {
                        mAuth.createUserWithEmailAndPassword(mail,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult pAuthResult) {
                                FirebaseFirestore database = FirebaseFirestore.getInstance();
                                Map<String, String> user = new HashMap<>();
                                user.put("email", mail);
                                user.put("AccountType", "Admin");
                                database.collection("Users").document(mail).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void pVoid) {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignUp.this, "Admin  SignUp Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUp.this, SignIn.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });
        mSignUpButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View pView) {
                isAdmin = true;
                mName.setVisibility(View.GONE);
                mSerialNum.setVisibility(View.GONE);
                mPhoneNum.setVisibility(View.GONE);
                mLocation.setVisibility(View.GONE);
                findViewById(R.id.admin).setVisibility(View.VISIBLE);
                findViewById(R.id.layout).setVisibility(View.GONE);
                return false;
            }
        });
    }

    public AlertDialog.Builder getDialogProgressBar() {
        if (builder == null) {
            builder = new AlertDialog.Builder(this);
            builder.setView(R.layout.dialog);
        }
        return builder;
    }

}