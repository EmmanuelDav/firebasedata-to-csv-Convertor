package com.example.projectx;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    ArrayList<UserInfo> mDemiClassArrayList;
    Adapter mAdapter;
    FirebaseFirestore mFirebaseFirestore;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mRecyclerView = findViewById(R.id.recyclerView);
        mDemiClassArrayList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mFirebaseFirestore.collection("data").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot pQueryDocumentSnapshots) {
                List<DocumentSnapshot> li;
                li = pQueryDocumentSnapshots.getDocuments();
                if (!pQueryDocumentSnapshots.isEmpty()) {
                    String name = "", serialN = "", deviceId = "", state = "", status = "", comment = "", tradeP = "", deviceAltId = "", imei = "", deviceAltEmail = "";
                    for (DocumentSnapshot i : li) {
                        if (i.exists()) {
                            name = (String) i.get("username");
                            serialN = (String) i.get("serialNum");
                            deviceId = (String) i.get("deviceId");
                            state = (String) i.get("state");
                            comment = (String) i.get("comment");
                            tradeP = (String) i.get("tradePartners");
                            imei = (String) i.get("miMEI");
                            deviceAltEmail = (String) i.get("device Alt ID");
                            status = (String) i.get("status");
                            mDemiClassArrayList.add(new UserInfo(serialN,deviceId,deviceAltEmail,imei,comment,tradeP,state,status,name));
                            mAdapter = new Adapter(AdminActivity.this, mDemiClassArrayList);
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    }
                }
            }
        });
    }
}