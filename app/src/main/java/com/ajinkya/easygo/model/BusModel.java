package com.ajinkya.easygo.model;

public class BusModel {
    public String arrivalPin;
    public String arrivalTime;
    public String busNo;
    public String date;
    public String departurePin;
    public String departureTime;
    public String typeSit;
    public String ticketPrice;


    public BusModel(){


    }

    public BusModel(String arrivalPin, String arrivalTime, String busNo,
                    String date, String departurePin, String departureTime,
                    String typeSit,String ticketPrice) {
        this.arrivalPin = arrivalPin;
        this.arrivalTime = arrivalTime;
        this.busNo = busNo;
        this.date = date;
        this.departurePin = departurePin;
        this.departureTime = departureTime;
        this.typeSit = typeSit;
        this.ticketPrice = ticketPrice;
    }

    public String getArrivalPin() {
        return arrivalPin;
    }

    public void setArrivalPin(String arrivalPin) {
        this.arrivalPin = arrivalPin;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }


    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDeparturePin() {
        return departurePin;
    }

    public void setDeparturePin(String departurePin) {
        this.departurePin = departurePin;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getTypeSit() {
        return typeSit;
    }

    public void setTypeSit(String typeSit) {
        this.typeSit = typeSit;
    }

    public String getTicketPrice(){
        return ticketPrice;
    }

    public void setTicketPrice(String  ticketPrice){
        this.ticketPrice = ticketPrice;

    }

}
