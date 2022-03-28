package com.example.androideasybussro.models;

import java.util.List;

public class Route {
    public List<Station> stations;
    public String routeID;

    public Route() {}

    public Route(List<Station> stations, String routeID) {
        this.stations = stations; this.routeID = routeID;
    }
}
