package com.example.projectx;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class UsersActivity extends AppCompatActivity {
    EditText mSerialN, mDeviceID, mDeviceAuthEmail, mIMEI, mComments;
    AutoCompleteTextView mTradePartners;
    Spinner mState, mStatus;
    CardView mSubmitButton;
    DatabaseReference mDatabaseRef;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Intent mIntent = getIntent();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mSerialN = findViewById(R.id.sn);
        mDeviceID = findViewById(R.id.Did);
        mDeviceAuthEmail = findViewById(R.id.dae);
        mIMEI = findViewById(R.id.IMEI);
        mComments = findViewById(R.id.cm);
        mTradePartners = findViewById(R.id.TP);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);
        mSubmitButton = findViewById(R.id.SUBMIT);
        mState = findViewById(R.id.state);
        mStatus = findViewById(R.id.status);
        populateDataFromFirebase();
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                String serialNum = mSerialN.getText().toString();
                String deviceId = mDeviceID.getText().toString();
                String deviceAE = mDeviceAuthEmail.getText().toString();
                String miMEI = mIMEI.getText().toString();
                String comment = mComments.getText().toString();
                String tradeP = mTradePartners.getText().toString();
                String state = mState.getSelectedItem().toString();
                String status = mStatus.getSelectedItem().toString();
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                if (serialNum.isEmpty() || deviceAE.isEmpty() || miMEI.isEmpty() || comment.isEmpty() || state.isEmpty() || status.isEmpty()) {
                    Toast.makeText(UsersActivity.this, "Empty blank found", Toast.LENGTH_LONG).show();
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("deviceId", deviceId);
                    map.put("serialNum", serialNum);
                    map.put("device Alt ID", deviceAE);
                    map.put("miMEI", miMEI);
                    map.put("comment", comment);
                    map.put("tradePartners", tradeP);
                    map.put("status", status);
                    map.put("state", state);
                    map.put("username", SignIn.username);
                    map.put("Date", SignIn.date);
                    db.collection("data").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> pTask) {
                            mSerialN.setText("");
                            mDeviceID.setText("");
                            mDeviceAuthEmail.setText("");
                            mIMEI.setText("");
                            mComments.setText("");
                            mTradePartners.setText("");
                            Toast.makeText(UsersActivity.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception pE) {

                        }
                    });
                }


            }
        });
       new MyTask().execute();
    }

    private void populateDataFromFirebase() {

    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... pVoids) {
            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot sDataSnapshot : snapshot.getChildren()) {
                        String data = sDataSnapshot.child("Partner Name").getValue(String.class);
                        mAdapter.add(data);
                    }
                    Log.d("TAG","BackGround Thread Active");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void pVoid) {
            mTradePartners.setThreshold(3);
            mTradePartners.setAdapter(mAdapter);
            mTradePartners.setTextSize(14);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Signout:
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent i = new Intent(this, SignIn.class);
                startActivity(i);
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent sIntent = new Intent(this, ProfileActivity.class);
        sIntent.putExtra("Username", SignIn.username);
        finish();
        super.onBackPressed();
    }
}