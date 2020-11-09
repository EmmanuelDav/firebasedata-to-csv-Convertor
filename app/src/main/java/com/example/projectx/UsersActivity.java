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
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class UsersActivity extends AppCompatActivity {
    String username;
    EditText mSerialN, mDeviceID, mDeviceAuthEmail, mIMEI, mComments, mTradePartners;
    Spinner mState, mStatus;
    CardView mSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        Intent mIntent = getIntent();
        username = (String) mIntent.getSerializableExtra("Username");
        mSerialN = findViewById(R.id.sn);
        mDeviceID = findViewById(R.id.Did);
        mDeviceAuthEmail = findViewById(R.id.dae);
        mIMEI = findViewById(R.id.IMEI);
        mComments = findViewById(R.id.cm);
        mTradePartners = findViewById(R.id.TP);
        mSubmitButton = findViewById(R.id.SUBMIT);
        mState = findViewById(R.id.state);
        mStatus = findViewById(R.id.status);
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
                HashMap<String, String> map = new HashMap<>();
                map.put("deviceId", deviceId);
                map.put("serialNum", serialNum);
                map.put("device Alt ID", deviceAE);
                map.put("miMEI", miMEI);
                map.put("comment", comment);
                map.put("tradePartners", tradeP);
                map.put("status", status);
                map.put("state", state);
                map.put("username",username);
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
        });
    }
}