package com.ajinkya.easygo;


import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class BusInfoModel extends RecyclerView.ViewHolder {
    View mView;
    TextView textDate,textTimeStart,textTimeEnd,textLocationPinStart,textLocationPinEnd, textBusNo,textSeatType,ticketPrice;
    CardView blCard;

    public BusInfoModel(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        textDate= mView.findViewById(R.id.tvDate);
        textTimeStart = mView.findViewById(R.id.tvStartTime);
        textTimeEnd = mView.findViewById(R.id.tvEndTime);
        textLocationPinStart = mView.findViewById(R.id.tvFrom);
        textLocationPinEnd = mView.findViewById(R.id.tvTo);
        textBusNo = mView.findViewById(R.id.tvBusNo);
        textSeatType = mView.findViewById(R.id.tvType);
        blCard = mView.findViewById(R.id.loadbusview);
        ticketPrice = mView.findViewById(R.id.tvPrice);

    }


    public void settextDate(String Textdate){
        textDate.setText(Textdate);
    }

    public void settextTimeStart(String TextTimeStart){
        textTimeStart.setText(TextTimeStart);
    }


    public void setTextTimeEnd(String TextTimeEnd){
        textTimeEnd.setText(TextTimeEnd);
    }


    public void settextLocationStart(String TextLocationPinStart){
        textLocationPinStart.setText(TextLocationPinStart);
    }


    public void settextLocationEnd(String TextLocationPinEnd){
        textLocationPinEnd.setText(TextLocationPinEnd);
    }


    public void settextBusno(String TextBusno){
        textBusNo.setText(TextBusno);
    }



    public void settextSeatType(String TextSeatType){
        textSeatType.setText(TextSeatType);
    }
    @SuppressLint("SetTextI18n")
    public void settextPrice(String TextPrice){
        ticketPrice.setText("Rs "+TextPrice);
    }



}
