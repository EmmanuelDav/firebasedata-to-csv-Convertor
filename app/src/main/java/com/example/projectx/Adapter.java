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
        View view = LayoutInflater.from(context).inflate(R.layout.activity_listitem, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, int position) {
        final UserInfo e = entryList.get(position);
        holder.mName.setText(e.serialNum);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                int position = holder.getAdapterPosition();
                Intent i  = new Intent(context, Details.class);
                i.putExtra("location",entryList.get(position).location);
                i.putExtra("serialN",entryList.get(position).serialNum);
                i.putExtra("phoneN",entryList.get(position).phoneNum);
                i.putExtra("deviceAltEmail",entryList.get(position).deviceAltEmail);
                i.putExtra("comment",entryList.get(position).comment);
                i.putExtra("status",entryList.get(position).status);
                i.putExtra("state",entryList.get(position).state);
                i.putExtra("tradePartners",entryList.get(position).tradePartners);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView mName, email;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.name);
        }
    }
}
