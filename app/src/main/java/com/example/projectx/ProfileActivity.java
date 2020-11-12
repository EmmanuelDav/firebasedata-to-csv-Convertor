package com.example.projectx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity  implements EditDialog.getEditedData{
    FirebaseFirestore mFirebaseFirestore;
    String username;
    TextView mName, mEmail, mLocation, totalPush, totalPushPerUsers;
    int totalPushPerUser = 0, totalPerDay = 0;
    AlertDialog.Builder builder;
    AlertDialog progressDialog;
    ArrayList<UserInfo> mDemiClassArrayList;
    Adapter mAdapter;
    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        progressDialog = getDialogProgressBar().create();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        mRecyclerView = findViewById(R.id.itemRecyclerview);
        mDemiClassArrayList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Intent mIntent = getIntent();
        username = (String) mIntent.getSerializableExtra("Username");
        mName = findViewById(R.id.username);
        totalPushPerUsers = findViewById(R.id.quizscore);
        totalPush = findViewById(R.id.flag_score);
        mEmail = findViewById(R.id.email);
        mLocation = findViewById(R.id.mLocation);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mFirebaseFirestore.collection("Users").document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot pDocumentSnapshot) {
                progressDialog.dismiss();
                if (pDocumentSnapshot != null) {
                    String name = pDocumentSnapshot.get("Name").toString();
                    String email = pDocumentSnapshot.get("email").toString();
                    String Location = pDocumentSnapshot.get("location").toString();
                    mEmail.setText(email);
                    mName.setText(name);
                    mLocation.setText(Location);
                }
            }
        });
        retrieveInfoFromfirebaseWithUserQuery();
        findViewById(R.id.AddItems).setOnClickListener(view -> {
            startActivity(new Intent(this, UsersActivity.class));
            finish();
        });
    }

    private void retrieveInfoFromfirebaseWithUserQuery() {
        mFirebaseFirestore.collection("data").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot pQueryDocumentSnapshots) {
                List<DocumentSnapshot> li;
                li = pQueryDocumentSnapshots.getDocuments();
                if (li != null) {
                    for (DocumentSnapshot i : li) {
                        String name = "", serialN = "", deviceId = "", state = "", status = "", comment = "", tradeP = "", imei = "", deviceAltEmail = "";
                        String sUsername = (String) i.get("username");
                        String sDate = (String) i.get("Date");
                        if (sUsername != null && sUsername.matches(username)) {
                            totalPushPerUser += 1;
                            name = (String) i.get("username");
                            serialN = (String) i.get("serialNum");
                            deviceId = (String) i.get("deviceId");
                            state = (String) i.get("state");
                            comment = (String) i.get("comment");
                            tradeP = (String) i.get("tradePartners");
                            imei = (String) i.get("miMEI");
                            deviceAltEmail = (String) i.get("device Alt ID");
                            status = (String) i.get("status");
                            mDemiClassArrayList.add(new UserInfo(serialN, deviceId, deviceAltEmail, imei, comment, tradeP, state, status, name, i.getId()));
                            mAdapter = new Adapter(ProfileActivity.this, mDemiClassArrayList, getSupportFragmentManager());
                            mRecyclerView.setAdapter(mAdapter);
                            if (sDate != null && sDate.matches(SignIn.date)) {
                                totalPerDay += 1;
                            }
                        }
                    }
                    progressDialog.dismiss();
                    totalPushPerUsers.setText(String.valueOf(totalPerDay));
                    String data = String.valueOf(li.size());
                    totalPush.setText(String.valueOf(totalPushPerUser));
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

    @Override
    public void refresh() {
        mDemiClassArrayList.clear();
        updateExistingData();
        mAdapter.notifyDataSetChanged();
    }
    private  void updateExistingData(){
        mFirebaseFirestore.collection("data").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot pQueryDocumentSnapshots) {
                List<DocumentSnapshot> li;
                li = pQueryDocumentSnapshots.getDocuments();
                if (li != null) {
                    for (DocumentSnapshot i : li) {
                        String name = "", serialN = "", deviceId = "", state = "", status = "", comment = "", tradeP = "", imei = "", deviceAltEmail = "";
                        String sUsername = (String) i.get("username");
                        String sDate = (String) i.get("Date");
                        if (sUsername != null && sUsername.matches(username)) {
                            name = (String) i.get("username");
                            serialN = (String) i.get("serialNum");
                            deviceId = (String) i.get("deviceId");
                            state = (String) i.get("state");
                            comment = (String) i.get("comment");
                            tradeP = (String) i.get("tradePartners");
                            imei = (String) i.get("miMEI");
                            deviceAltEmail = (String) i.get("device Alt ID");
                            status = (String) i.get("status");
                            mDemiClassArrayList.add(new UserInfo(serialN, deviceId, deviceAltEmail, imei, comment, tradeP, state, status, name, i.getId()));
                            mAdapter = new Adapter(ProfileActivity.this, mDemiClassArrayList, getSupportFragmentManager());
                            mRecyclerView.setAdapter(mAdapter);
                            if (sDate != null && sDate.matches(SignIn.date)) {
                            }
                        }
                    }
                    progressDialog.dismiss();
                    totalPushPerUsers.setText(String.valueOf(totalPerDay));
                    String data = String.valueOf(li.size());
                    totalPush.setText(String.valueOf(totalPushPerUser));
                }

            }
        });

    }
}
