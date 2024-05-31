package com.example.revalocator;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

public class LocationStorage {
    private Map<String, LatLng> locations = new HashMap<>();

    // Method to add a location to the HashMap
    public void addLocation(String name, double latitude, double longitude) {
        locations.put(name, new LatLng(latitude, longitude));
    }

    // Method to get all stored locations
    public Map<String, LatLng> getLocations() {
        return locations;
    }
}
