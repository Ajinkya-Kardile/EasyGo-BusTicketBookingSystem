package com.ajinkya.easygo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajinkya.easygo.R;
import com.ajinkya.easygo.model.TicketModel;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {
    private ArrayList<TicketModel> myList;

    public TicketAdapter(ArrayList<TicketModel> List){
        this.myList = List;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.bus_ticket, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TicketModel ticket = myList.get(position);
        holder.BusNo.setText(ticket.getBusNo());
        holder.Date.setText(ticket.getDate());
        holder.From.setText(ticket.getFromLocation());
        holder.To.setText(ticket.getToLocation());
        holder.StartTime.setText(ticket.getStartTime());
        holder.EndTime.setText(ticket.getEndTime());
        holder.BusType.setText(ticket.getBusType());
        holder.SeatNo.setText(ticket.getSeatNo());
        holder.Passenger.setText(ticket.getPassengerName());
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView BusNo, Date, From, To, StartTime, EndTime, BusType, SeatNo,Passenger;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.BusNo = itemView.findViewById(R.id.ticketBusNumber);
            this.Date = itemView.findViewById(R.id.ticketDate);
            this.From = itemView.findViewById(R.id.ticketFrom);
            this.To = itemView.findViewById(R.id.ticketTo);
            this.StartTime = itemView.findViewById(R.id.ticketStart);
            this.EndTime = itemView.findViewById(R.id.ticketEnd);
            this.BusType = itemView.findViewById(R.id.ticketBusType);
            this.SeatNo = itemView.findViewById(R.id.ticketSeat);
            this.Passenger = itemView.findViewById(R.id.ticketPassenger);
        }
    }
}
