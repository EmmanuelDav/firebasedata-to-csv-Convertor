package com.example.projectx;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class EditDialog extends DialogFragment {
    String deviceId, state, status, comment, tradeP, imei, deviceAltEmail, key, srN;
    EditText mSerialN, mDeviceID, mDeviceAuthEmail, mIMEI, mComments;
    AutoCompleteTextView mTradePartners;
    Spinner mState, mStatus;
    CardView mSubmitButton;
    AlertDialog progressDialog;
    AlertDialog.Builder builder;
    DatabaseReference mDatabaseRef;
    getEditedData info;
    private ArrayAdapter<String> mAdapterData;
    public EditDialog() {}

    public interface getEditedData
    {
        void refresh();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle sBundle = getArguments();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        assert sBundle != null;
        comment = sBundle.getString("comment");
        deviceId = sBundle.getString("Device Id");
        state = sBundle.getString("State");
        status = sBundle.getString("Status");
        tradeP = sBundle.getString("TradeP");
        imei = sBundle.getString("Imei");
        key = sBundle.getString("Key");
        deviceAltEmail = sBundle.getString("DeviceEmail");
        srN = sBundle.getString("SrN");
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.edit_item_dialog, null);
        mDeviceID = dialogView.findViewById(R.id.Did);
        mDeviceAuthEmail = dialogView.findViewById(R.id.dae);
        mIMEI = dialogView.findViewById(R.id.IMEI);
        mComments = dialogView.findViewById(R.id.cm);
        mTradePartners = dialogView.findViewById(R.id.TP);
        mSerialN = dialogView.findViewById(R.id.sn);
        mState = dialogView.findViewById(R.id.state);
        mStatus = dialogView.findViewById(R.id.status);
        mDeviceAuthEmail.setText(deviceAltEmail);
        mDeviceID.requestFocus();
        mDeviceID.setText(deviceId);
        mIMEI.setText(imei);
        mComments.setText(comment);
        mSerialN.setText(srN);
        mTradePartners.setText(tradeP);
        mAdapterData = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item);
        mSubmitButton = dialogView.findViewById(R.id.SUBMIT);
        tradPartnersInfo();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Edit Record");
        alertDialogBuilder.setView(dialogView);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                progressDialog.show();
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
                    Toast.makeText(getContext(), "Empty blank found", Toast.LENGTH_LONG).show();
                } else {
                    HashMap<String, Object> map = new HashMap<>();
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
                    db.collection("data").document(key).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> pTask) {
                            progressDialog.dismiss();
                            mSerialN.requestFocus();
                            Toast.makeText(getContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                            info.refresh();
                            getDialog().dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception pE) {

                        }
                    });
                }
            }
        });
        alertDialogBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface pDialogInterface, int pI) {
                getDialog().dismiss();
            }
        });
        return alertDialogBuilder.create();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = getDialogProgressBar().create();
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public AlertDialog.Builder getDialogProgressBar() {
        if (builder == null) {
            builder = new AlertDialog.Builder(getContext());
            builder.setView(R.layout.dialog);
        }
        return builder;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ProfileActivity sProfileActivity;
        if (context instanceof ProfileActivity) {
            sProfileActivity = (ProfileActivity) context;
            info = (getEditedData) sProfileActivity;
        }
    }
    private void tradPartnersInfo() {
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot sDataSnapshot : snapshot.getChildren()) {
                    String data = sDataSnapshot.child("Partner Name").getValue(String.class);
                    mAdapterData.add(data);
                }
                Log.d("TAG", "BackGround Thread Active");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mTradePartners.setThreshold(3);
        mTradePartners.setAdapter(mAdapterData);
        mTradePartners.setTextSize(14);

    }

}
