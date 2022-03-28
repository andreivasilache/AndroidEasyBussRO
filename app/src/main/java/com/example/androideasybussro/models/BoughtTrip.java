package com.example.androideasybussro.models;

import java.util.List;

public class BoughtTrip {
    public String userID;
    public String label;
    public long dateTimestamp;
    public List<Station> stations;

    public BoughtTrip(String label, long dateTimestamp, List<Station> stations, String userID) {
        this.label = label;
        this.dateTimestamp = dateTimestamp;
        this.stations = stations;
        this.userID = userID;
    }
}
