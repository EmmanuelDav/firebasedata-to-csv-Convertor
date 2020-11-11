package com.UEMEnrollment.projectx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    EditText mName, mEmail, mPassword, retPasword, mPhoneNum, mLocation;
    FirebaseAuth mAuth;
    Button mSignUpButton;
    Spinner nLocation;
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
       retPasword = findViewById(R.id.checkPassword);
        mPassword = findViewById(R.id.rpassword);
        mPhoneNum = findViewById(R.id.phoneNum);
        nLocation = findViewById(R.id.mLocation);
        mSignUpButton = findViewById(R.id.signup);
        mAuth = FirebaseAuth.getInstance();
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                final String mail = mEmail.getText().toString();
                String password = mPassword.getText().toString().trim();
                final String name = mName.getText().toString();
                final String phoneN = mPhoneNum.getText().toString();
                final String location = nLocation.getSelectedItem().toString();
                final String rtPassword = retPasword.getText().toString();
                    progressDialog.show();
                {
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
    }

    public AlertDialog.Builder getDialogProgressBar() {
        if (builder == null) {
            builder = new AlertDialog.Builder(this);
            builder.setView(R.layout.dialog);
        }
        return builder;
    }

}