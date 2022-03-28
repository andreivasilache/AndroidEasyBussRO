package com.example.androideasybussro.models;

public class Station {
    public String stationName;
    public String arrivalTime;
    public int index;

    public Station(){}

    public Station(String stationName, String arrivalTime, int index) {
        this.stationName = stationName;
        this.arrivalTime = arrivalTime;
        this.index = index;
    }
}
