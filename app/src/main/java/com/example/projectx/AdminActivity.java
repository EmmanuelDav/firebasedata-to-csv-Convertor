package com.example.projectx;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 222;
    ArrayList<UserInfo> mDemiClassArrayList;
    Adapter mAdapter;
    FirebaseFirestore mFirebaseFirestore;
    RecyclerView mRecyclerView;
    String csv;
    DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        checkLocationPermission();
        mRecyclerView = findViewById(R.id.recyclerView);
        mDemiClassArrayList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        csv = (Environment.getExternalStorageDirectory().getAbsoluteFile() + "/tech.csv");
        mFirebaseFirestore.collection("data").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot pQueryDocumentSnapshots) {
                List<DocumentSnapshot> li;
                li = pQueryDocumentSnapshots.getDocuments();
                if (!pQueryDocumentSnapshots.isEmpty()) {
                    String name = "", serialN = "", deviceId = "", state = "", status = "", comment = "", tradeP = "", imei = "", deviceAltEmail = "", key ="";
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
                            key =(String) i.getId();
                            mDemiClassArrayList.add(new UserInfo(serialN, deviceId, deviceAltEmail, imei, comment, tradeP, state, status, name,key));
                            mAdapter = new Adapter(AdminActivity.this, mDemiClassArrayList,getSupportFragmentManager());
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    }
                }
            }
        });
        findViewById(R.id.saveTocsv).setOnClickListener(view -> {
            writeCSVFile(csv, mDemiClassArrayList);
        });
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

    public void writeCSVFile(String  csvFileName, List<UserInfo> listBooks) {
        ICsvBeanWriter beanWriter = null;
        CellProcessor[] processors = new CellProcessor[]{
                new NotNull(), // ISBNr
                new NotNull(), // publisher
                new NotNull(), // title
                new NotNull(), // author
                new NotNull(), // publisher
                new NotNull(), // publisher
                new NotNull(), // publisher
                new NotNull(), // publisher
                new NotNull(), // publisher
        };
        try {
            beanWriter = new CsvBeanWriter(new FileWriter(csvFileName), CsvPreference.EXCEL_PREFERENCE);
            String[] header = {"serialNum", "deviceId", "deviceAltEmail", "miMEI", "comment",
                    "tradePartners", "state", "status", "username"};
            beanWriter.writeHeader(header);
            for (UserInfo aBook : listBooks) {
                beanWriter.write(aBook, header, processors);
            }
            Toast.makeText(AdminActivity.this, "File saved in Internal Storage ", Toast.LENGTH_LONG).show();
        } catch (IOException ex) {
            System.err.println("Created Successfully " + ex);
            Toast.makeText(AdminActivity.this, "Failed  ", Toast.LENGTH_LONG).show();
        } finally {
            if (beanWriter != null) {
                try {
                    beanWriter.close();
                } catch (IOException ex) {
                    System.err.println("Error closing the writer: " + ex);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Signout: {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent i = new Intent(this, SignIn.class);
                startActivity(i);
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return  true;
    }

}