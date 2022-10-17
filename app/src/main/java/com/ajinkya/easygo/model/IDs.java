package com.ajinkya.easygo.model;

public class IDs {
    private String Location_pin,Place;

    public IDs(){

    }
    public IDs(String Location_pin, String Place ){
        this.Location_pin = Location_pin;
        this.Place = Place;

    }


    public String getLocation_pin() {
        return Location_pin;
    }

    public void setLocation_pin(String Location_pin) {
        this.Location_pin = Location_pin;
    }


    public String getPlace() {
        return Place;
    }

    public void setPlace(String Place) {
        this.Place = Place;
    }









}
