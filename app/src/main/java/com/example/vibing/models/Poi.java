package com.example.vibing.models;

import java.util.Map;

public class Poi {
    private String name;
    private double latitude;
    private double longitude;
    private int score;
    private int owningTeam; // 0 = neutral, 1-5 = teams
    private String id;
    
    // For Firestore compatibility - handle both flat and nested location structures
    private Map<String, Object> location;
    private Integer currentScore;
    private String ownerTeamId;
    
    // Raw data map for manual extraction
    private Map<String, Object> rawData;

    public Poi() {
        // Default constructor for Firestore
    }

    public Poi(String name, double latitude, double longitude, int score, int owningTeam) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.score = score;
        this.owningTeam = owningTeam;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        // Try nested location object first
        if (location != null && location.containsKey("latitude")) {
            Object lat = location.get("latitude");
            if (lat instanceof Number) {
                return ((Number) lat).doubleValue();
            }
        }
        
        // Try raw data map
        if (rawData != null) {
            // Check if location is nested in raw data
            if (rawData.containsKey("location")) {
                Object locObj = rawData.get("location");
                if (locObj instanceof Map) {
                    Map<String, Object> locMap = (Map<String, Object>) locObj;
                    if (locMap.containsKey("latitude")) {
                        Object lat = locMap.get("latitude");
                        if (lat instanceof Number) {
                            return ((Number) lat).doubleValue();
                        }
                    }
                }
            }
            
            // Check if latitude is flat in raw data
            if (rawData.containsKey("latitude")) {
                Object lat = rawData.get("latitude");
                if (lat instanceof Number) {
                    return ((Number) lat).doubleValue();
                }
            }
        }
        
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        // Try nested location object first
        if (location != null && location.containsKey("longitude")) {
            Object lon = location.get("longitude");
            if (lon instanceof Number) {
                return ((Number) lon).doubleValue();
            }
        }
        
        // Try raw data map
        if (rawData != null) {
            // Check if location is nested in raw data
            if (rawData.containsKey("location")) {
                Object locObj = rawData.get("location");
                if (locObj instanceof Map) {
                    Map<String, Object> locMap = (Map<String, Object>) locObj;
                    if (locMap.containsKey("longitude")) {
                        Object lon = locMap.get("longitude");
                        if (lon instanceof Number) {
                            return ((Number) lon).doubleValue();
                        }
                    }
                }
            }
            
            // Check if longitude is flat in raw data
            if (rawData.containsKey("longitude")) {
                Object lon = rawData.get("longitude");
                if (lon instanceof Number) {
                    return ((Number) lon).doubleValue();
                }
            }
        }
        
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getScore() {
        // Handle both score and currentScore fields
        if (currentScore != null) {
            return currentScore;
        }
        
        // Try raw data map
        if (rawData != null && rawData.containsKey("currentScore")) {
            Object scoreObj = rawData.get("currentScore");
            if (scoreObj instanceof Number) {
                return ((Number) scoreObj).intValue();
            }
        }
        
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getOwningTeam() {
        // Handle both owningTeam and ownerTeamId fields
        if (ownerTeamId != null) {
            if (ownerTeamId.equals("null") || ownerTeamId.isEmpty()) {
                return 0; // neutral team
            }
            try {
                return Integer.parseInt(ownerTeamId);
            } catch (NumberFormatException e) {
                // If ownerTeamId is not a number, return 0 (neutral)
                return 0;
            }
        }
        return owningTeam;
    }

    public void setOwningTeam(int owningTeam) {
        this.owningTeam = owningTeam;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}