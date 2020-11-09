package com.example.projectx;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Details extends AppCompatActivity {
    TextView mSerialN, mDeviceID, mDeviceAuthEmail, mIMEI, mComments, mTradePartners;
    TextView mState, mStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mSerialN = findViewById(R.id.sn);
        mDeviceID = findViewById(R.id.Did);
        mDeviceAuthEmail = findViewById(R.id.dae);
        mIMEI = findViewById(R.id.IMEI);
        mComments = findViewById(R.id.cm);
        mTradePartners = findViewById(R.id.TP);
        mState = findViewById(R.id.state);
        mStatus = findViewById(R.id.status);
        Intent i = getIntent();
        String serialNum = i.getStringExtra("serialN");
        String deviceId = i.getStringExtra("deviceId");
        String deviceAE = i.getStringExtra("deviceAltEmail");
        String comment = i.getStringExtra("comment");
        String tradeP = i.getStringExtra("tradePartners");
        String state = i.getStringExtra("state");
        String status = i.getStringExtra("status");
        String Ime = i.getStringExtra("Ime");
        mSerialN.setText(serialNum);
        mDeviceAuthEmail.setText(deviceAE);
        mTradePartners.setText(tradeP);
        mComments.setText(comment);
        mState.setText(state);
        mState.setText(status);
        mIMEI.setText(Ime);
        mDeviceID.setText(deviceId);
    }
}