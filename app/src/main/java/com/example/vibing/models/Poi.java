package com.example.vibing.models;

import java.util.Map;
import java.util.List;

public class Poi {
    private String name;
    private double latitude;
    private double longitude;
    private int score;
    private int owningTeam; // 0 = neutral, 1-4 = teams
    private String id;
    private double radius; // Rayon de la zone cliquable en mètres
    
    // For Firestore compatibility - handle both flat and nested location structures
    private Map<String, Object> location;
    private Integer currentScore;
    private String ownerTeamId;
    private java.util.Date captureTime;
    private java.util.Date lastUpdated;
    private java.util.Date freezeUntil;
    private List<String> keywords;
    
    // Raw data map for manual extraction
    private Map<String, Object> rawData;

    public Poi() {
        // Default constructor for Firestore
        this.radius = 100.0; // Rayon par défaut de 100m
    }

    public Poi(String name, double latitude, double longitude, int score, int owningTeam) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.score = score;
        this.owningTeam = owningTeam;
        this.radius = 100.0; // Rayon par défaut de 100m
    }
    
    public Poi(String name, double latitude, double longitude, int score, int owningTeam, double radius) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.score = score;
        this.owningTeam = owningTeam;
        this.radius = radius;
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
        android.util.Log.d("POI_DEBUG", "getOwningTeam() called for POI: " + name);
        android.util.Log.d("POI_DEBUG", "ownerTeamId: " + ownerTeamId + ", owningTeam: " + owningTeam);
        
        // Handle both owningTeam and ownerTeamId fields
        if (ownerTeamId != null) {
            if (ownerTeamId.equals("null") || ownerTeamId.isEmpty()) {
                android.util.Log.d("POI_DEBUG", "ownerTeamId is null or empty, returning 0 (neutral)");
                return 0; // neutral team
            }
            try {
                // Handle both "2" and "team_2" formats
                if (ownerTeamId.startsWith("team_")) {
                    int result = Integer.parseInt(ownerTeamId.substring(5));
                    // Convert team 5+ to neutral (0)
                    if (result >= 5) {
                        result = 0;
                        android.util.Log.d("POI_DEBUG", "Converted team 5+ to neutral (0)");
                    }
                    android.util.Log.d("POI_DEBUG", "Parsed team_ format, returning: " + result);
                    return result;
                }
                int result = Integer.parseInt(ownerTeamId);
                // Convert team 5+ to neutral (0)
                if (result >= 5) {
                    result = 0;
                    android.util.Log.d("POI_DEBUG", "Converted team 5+ to neutral (0)");
                }
                android.util.Log.d("POI_DEBUG", "Parsed direct format, returning: " + result);
                return result;
            } catch (NumberFormatException e) {
                android.util.Log.e("POI_DEBUG", "Failed to parse ownerTeamId: " + ownerTeamId + ", returning 0 (neutral)", e);
                // If ownerTeamId is not a number, return 0 (neutral)
                return 0;
            }
        }
        android.util.Log.d("POI_DEBUG", "ownerTeamId is null, returning owningTeam: " + owningTeam);
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

    public java.util.Date getCaptureTime() {
        // Try raw data map first for captureTime
        if (rawData != null && rawData.containsKey("captureTime")) {
            Object captureObj = rawData.get("captureTime");
            if (captureObj instanceof java.util.Date) {
                return (java.util.Date) captureObj;
            }
        }
        return captureTime;
    }

    public void setCaptureTime(java.util.Date captureTime) {
        this.captureTime = captureTime;
    }

    public java.util.Date getLastUpdated() {
        // Try raw data map first for lastUpdated
        if (rawData != null && rawData.containsKey("lastUpdated")) {
            Object lastUpdatedObj = rawData.get("lastUpdated");
            if (lastUpdatedObj instanceof java.util.Date) {
                return (java.util.Date) lastUpdatedObj;
            }
        }
        return lastUpdated;
    }

    public void setLastUpdated(java.util.Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    public double getRadius() {
        // Try raw data map first
        if (rawData != null && rawData.containsKey("radius")) {
            Object radiusObj = rawData.get("radius");
            if (radiusObj instanceof Number) {
                return ((Number) radiusObj).doubleValue();
            }
        }
        // Return default radius if not set
        return radius > 0 ? radius : 100.0;
    }
    
    public void setRadius(double radius) {
        this.radius = radius;
    }
    
    public String getOwnerTeamId() {
        return ownerTeamId;
    }
    
    public void setOwnerTeamId(String ownerTeamId) {
        this.ownerTeamId = ownerTeamId;
    }
    
    public List<String> getKeywords() {
        // Try raw data map first for keywords
        if (rawData != null && rawData.containsKey("keywords")) {
            Object keywordsObj = rawData.get("keywords");
            if (keywordsObj instanceof List) {
                return (List<String>) keywordsObj;
            }
        }
        return keywords;
    }
    
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
    
    public java.util.Date getFreezeUntil() {
        // Try raw data map first for freezeUntil
        if (rawData != null && rawData.containsKey("freezeUntil")) {
            Object freezeObj = rawData.get("freezeUntil");
            if (freezeObj instanceof java.util.Date) {
                return (java.util.Date) freezeObj;
            }
        }
        return freezeUntil;
    }
    
    public void setFreezeUntil(java.util.Date freezeUntil) {
        this.freezeUntil = freezeUntil;
    }
}