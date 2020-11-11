package com.UEMEnrollment.projectx;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Details extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 222;
    TextView mSerialN, mDeviceID, mDeviceAuthEmail, mIMEI, mComments, mTradePartners;
    TextView mState, mStatus;
    String csv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        checkLocationPermission();
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
         csv = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/MTN.csv");
        findViewById(R.id.saveTocsv).setOnClickListener(view -> {
            writeCsv(serialNum, deviceId, deviceAE, comment, tradeP, state, status, Ime, csv);
        });
//        findViewById(R.id.sendToMail).setOnClickListener(view ->{
//            Intent emailIntent = new Intent(Intent.ACTION_SEND);
//            emailIntent.setType("text/plain");
//            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"email@example.com"});
//            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject here");
//            emailIntent.putExtra(Intent.EXTRA_TEXT, "body text");
//            File file = new File(csv);
//            Uri uri = Uri.fromFile(file);
//            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
//            startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
//        });

    }

    private void writeCsv(String pSerialNum, String pDeviceId, String pDeviceAE, String pComment, String pTradeP, String pState, String pStatus, String pIme, String pCsv) {
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(pCsv));
            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[]{"SerialNumber", "DeviceID", "DeviceAltEmail", "Comment", "tradePartners", "state", "status", "IME"});
            data.add(new String[]{pSerialNum, pDeviceId, pDeviceAE, pComment, pTradeP, pState, pStatus, pIme});
            writer.writeAll(data);
            writer.close();
            Toast.makeText(this, "Successful", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TAG", e.getMessage());
            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();

        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_LOCATION);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    }
                } else {
                    Toast.makeText(this, "permission denied",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


}