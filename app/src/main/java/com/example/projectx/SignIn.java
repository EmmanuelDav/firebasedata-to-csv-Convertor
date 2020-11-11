package com.example.projectx;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SignIn extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passEditText;
    TextView newUserSignIn;
    Button signIn;
    AlertDialog.Builder builder;
    AlertDialog progressDialog;
    FirebaseAuth fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        progressDialog = getDialogProgressBar().create();
        progressDialog.setCanceledOnTouchOutside(false);
        fb = FirebaseAuth.getInstance();
        newUserSignIn = (TextView) findViewById(R.id.register);
        signIn = findViewById(R.id.button);
        emailEditText = findViewById(R.id.mail);
        passEditText = findViewById(R.id.password);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                final String email, password;
                email = emailEditText.getText().toString().trim();
                password = passEditText.getText().toString().trim();
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                if (email.length() == 0 || password.length() == 0) {
                    Toast.makeText(SignIn.this, "Login fields cannot be empty", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    fb.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        List<DocumentSnapshot> li;
                                        li = queryDocumentSnapshots.getDocuments();
                                        if (!queryDocumentSnapshots.isEmpty()) {
                                            int x = 0;
                                            for (DocumentSnapshot i : li) {
                                                String AA = (String) i.get("AccountType");
                                                String UU = (String) i.get("email");
                                                if (UU.equals(email)) {
                                                    if (AA.equals("Admin")) {
                                                        x = 1;
                                                    }
                                                    break;
                                                }
                                            }
                                            if (x == 0) {
                                                progressDialog.dismiss();
                                                Toast.makeText(SignIn.this, "User SignUp Successful", Toast.LENGTH_SHORT).show();
                                                Intent i1 = new Intent(SignIn.this, UsersActivity.class);
                                                i1.putExtra("Username", email);
                                                startActivity(i1);
                                            } else {
                                                progressDialog.dismiss();
                                                Intent i = new Intent(SignIn.this, AdminActivity.class);
                                                startActivity(i);
                                            }
                                        }
                                    }
                                });
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(SignIn.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception pE) {
                            progressDialog.dismiss();
                            Log.d("TAG","Failed because"+pE.getCause());

                        }
                    });

                }
            }
        });
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                startActivity(new Intent(SignIn.this, SignUp.class));
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