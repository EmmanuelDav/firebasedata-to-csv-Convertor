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
                    String name = "", serialN = "", phoneN = "", locationN = "";
                    for (DocumentSnapshot i : li) {
                        if (i.exists()) {
                            name = (String) i.get("name");
                            serialN = (String) i.get("serialNumber");
                            locationN = (String) i.get("location");
                            phoneN = (String) i.get("phoneNumber");

                            mAdapter = new Adapter(AdminActivity.this, mDemiClassArrayList);
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    }
                }
            }
        });
    }
}