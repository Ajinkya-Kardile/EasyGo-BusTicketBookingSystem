package com.ajinkya.easygo.adapters;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajinkya.easygo.model.PassengerInfo;
import com.ajinkya.easygo.R;

import java.util.ArrayList;

public class PassengerListAdapter extends RecyclerView.Adapter<PassengerListAdapter.ViewHolder> {
    private final ArrayList<PassengerInfo> myList;

    public PassengerListAdapter(ArrayList<PassengerInfo> List){
        this.myList=List;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.passenger_info, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PassengerInfo passengerInfo = myList.get(position);
        holder.pName.setText(passengerInfo.getPassengerName());
        Log.e(TAG, "onBindViewHolder: "+ passengerInfo.getPassengerName());
        holder.pNumber.setText(passengerInfo.getPhoneNumber());
        Log.e(TAG, "onBindViewHolder: "+ passengerInfo.getPhoneNumber() );
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView pName,pNumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.pName = itemView.findViewById(R.id.P_name);
            this.pNumber = itemView.findViewById(R.id.P_number);
        }
    }
}
