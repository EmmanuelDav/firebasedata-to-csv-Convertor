package com.example.projectx;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    FirebaseFirestore mFirebaseFirestore;
    String username;
    TextView mName, mEmail, mLocation, totalPush, totalPushPerUsers;
    int totalPushPerUser = 0, totalPerDay = 0;
    AlertDialog.Builder builder;
    AlertDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        progressDialog = getDialogProgressBar().create();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        Intent mIntent = getIntent();
        username = (String) mIntent.getSerializableExtra("Username");
        mName = findViewById(R.id.username);

        totalPush = findViewById(R.id.flag_score);
        mEmail = findViewById(R.id.email);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mFirebaseFirestore.collection("Users").document(SignIn.username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot pDocumentSnapshot) {
                progressDialog.dismiss();
                if (pDocumentSnapshot != null) {
                    String email = pDocumentSnapshot.get("email").toString();
                    mEmail.setText(email);

                }
            }
        });
        mFirebaseFirestore.collection("data").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot pQueryDocumentSnapshots) {
                List<DocumentSnapshot> li;
                li = pQueryDocumentSnapshots.getDocuments();
                if (li != null) {
                    for (DocumentSnapshot i : li) {
                        String sUsername = (String) i.get("username");
                        String sDate = (String) i.get("Date");
                        if (sUsername != null && sUsername.matches(SignIn.username)) {
                            totalPushPerUser += 1;
                            if (sDate != null && sDate.matches(SignIn.date)) {
                               totalPerDay += 1;
                            }
                        }
                    }
                    progressDialog.dismiss();
                    String data = String.valueOf(li.size());
                    totalPush.setText(data);
                }

            }
        });
        findViewById(R.id.AddItems).setOnClickListener(view -> {
            startActivity(new Intent(this, AdminActivity.class));
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
