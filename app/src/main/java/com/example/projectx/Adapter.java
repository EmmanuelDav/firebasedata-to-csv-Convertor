package com.example.projectx;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.viewHolder> {
    private Context context;
    public List<UserInfo> entryList;

    public Adapter(Context pContext, List<UserInfo> pEntryList) {
        context = pContext;
        entryList = pEntryList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_details, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, int position) {
        final UserInfo e = entryList.get(position);
        holder.mComments.setText("Comment = "+e.comment);
        holder.mDeviceAuthEmail.setText("Device Authentication Email = "+e.deviceAltEmail);
        holder.mIMEI.setText("IMEI = "+e.miMEI);
        holder.mDeviceID.setText("Device ID = "+e.deviceId);
        holder.mTradePartners.setText("Trade Partners = "+e.tradePartners);
        holder.mState.setText("State = "+e.state);
        holder.mStatus.setText("Status = "+e.status);
        holder.mName.setText(e.username);
        holder.mSerialN.setText((position + 1) + ".");

    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView mSerialN, mDeviceID, mDeviceAuthEmail, mIMEI, mComments, mTradePartners;
        TextView mState, mStatus,mName;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            mSerialN = itemView.findViewById(R.id.sn);
            mDeviceID =itemView. findViewById(R.id.Did);
            mDeviceAuthEmail = itemView.findViewById(R.id.dae);
            mIMEI =itemView. findViewById(R.id.IMEI);
            mComments =itemView. findViewById(R.id.cm);
            mTradePartners = itemView.findViewById(R.id.TP);
            mState = itemView.findViewById(R.id.state);
            mStatus = itemView.findViewById(R.id.status);
            mName = itemView.findViewById(R.id.name);
        }
    }
}
