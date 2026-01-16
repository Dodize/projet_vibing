package com.example.vibing.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vibing.models.Poi;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Poi>> pois;
    private FirebaseFirestore db;

    public HomeViewModel() {
        pois = new MutableLiveData<>();
        
        try {
            db = FirebaseFirestore.getInstance();
            loadPois();
        } catch (Exception e) {
            loadTestPois();
        }
    }

    public LiveData<List<Poi>> getPois() {
        return pois;
    }

    private void loadPois() {
        try {
            db.collection("pois")
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        if (e != null) {
                            android.util.Log.e("HOME_VIEWMODEL", "Error listening to POI updates", e);
                            return;
                        }
                        
                        if (queryDocumentSnapshots != null) {
                            android.util.Log.d("HOME_VIEWMODEL", "Successfully loaded " + queryDocumentSnapshots.size() + " POIs from Firebase");
                            List<Poi> poiList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                // Create POI manually from document data
                                Poi poi = createPoiFromDocument(document);
                                android.util.Log.d("HOME_VIEWMODEL", "POI: " + poi.getName() + " with score: " + poi.getScore());
                                poiList.add(poi);
                            }
                            pois.setValue(poiList);
                        }
                    });
        } catch (Exception e) {
            android.util.Log.e("HOME_VIEWMODEL", "Exception setting up POI listener", e);
            // Fallback to test data
            android.util.Log.w("HOME_VIEWMODEL", "Using test data fallback");
            loadTestPois();
        }
    }
    
    private void loadTestPois() {
        android.util.Log.w("HOME_VIEWMODEL", "Loading test POI data with realistic scores");
        List<Poi> testPois = new ArrayList<>();
        
        // Add some test POIs with correct Toulouse coordinates and realistic scores
        Poi capitole = new Poi();
        capitole.setId("poi_capitole");
        capitole.setName("Capitole de Toulouse");
        capitole.setLatitude(43.6047);
        capitole.setLongitude(1.4442);
        capitole.setScore(250);
        capitole.setOwningTeam(0);
        testPois.add(capitole);
        
        Poi zenith = new Poi();
        zenith.setId("poi_zenith");
        zenith.setName("Zénith de Toulouse");
        zenith.setLatitude(43.5819);
        zenith.setLongitude(1.4337);
        zenith.setScore(180);
        zenith.setOwningTeam(0);
        testPois.add(zenith);
        
        Poi basilique = new Poi();
        basilique.setId("poi_basilique_saint_sernin");
        basilique.setName("Basilique Saint-Sernin");
        basilique.setLatitude(43.6082);
        basilique.setLongitude(1.4408);
        basilique.setScore(320);
        basilique.setOwningTeam(0);
        testPois.add(basilique);
        
        Poi pontNeuf = new Poi();
        pontNeuf.setId("poi_pont_neuf");
        pontNeuf.setName("Pont Neuf de Toulouse");
        pontNeuf.setLatitude(43.6032);
        pontNeuf.setLongitude(1.4302);
        pontNeuf.setScore(150);
        pontNeuf.setOwningTeam(0);
        testPois.add(pontNeuf);
        
        // POI de test avec les coordonnées spécifiées
        Poi testPoi = new Poi();
        testPoi.setId("poi_test_coordinates");
        testPoi.setName("POI Test Coordonnées");
        testPoi.setLatitude(43.60236021729008);
        testPoi.setLongitude(1.4557650816708745);
        testPoi.setScore(200);
        testPoi.setOwningTeam(0);
        testPois.add(testPoi);
        
        pois.setValue(testPois);
    }
    
    private Poi createPoiFromDocument(QueryDocumentSnapshot document) {
        Poi poi = new Poi();
        Map<String, Object> data = document.getData();
        
        poi.setId(document.getId());
        
        // Extract name
        if (data.containsKey("name")) {
            poi.setName((String) data.get("name"));
        }
        
        // Extract coordinates from nested location object
        if (data.containsKey("location")) {
            Object locationObj = data.get("location");
            if (locationObj instanceof Map) {
                Map<String, Object> locationMap = (Map<String, Object>) locationObj;
                
                if (locationMap.containsKey("latitude")) {
                    Object lat = locationMap.get("latitude");
                    if (lat instanceof Number) {
                        poi.setLatitude(((Number) lat).doubleValue());
                    }
                }
                
                if (locationMap.containsKey("longitude")) {
                    Object lon = locationMap.get("longitude");
                    if (lon instanceof Number) {
                        poi.setLongitude(((Number) lon).doubleValue());
                    }
                }
            }
        }
        
        // Get currentScore from database and apply same dynamic calculation as PoiScoreViewModel
        int baseScore = 100; // default
        if (data.containsKey("currentScore")) {
            Object scoreValue = data.get("currentScore");
            if (scoreValue instanceof Number) {
                baseScore = ((Number) scoreValue).intValue();
            } else if (scoreValue instanceof String) {
                try {
                    baseScore = Integer.parseInt((String) scoreValue);
                } catch (NumberFormatException e) {
                    baseScore = 100; // Default fallback
                }
            } else {
                baseScore = 100; // Default fallback
            }
        }
        
        // Apply same time-based decrement calculation as PoiScoreViewModel
        long lastUpdatedTime = 0;
        long currentTime = System.currentTimeMillis();
        
        // Get captureTime first (priorité)
        if (data.containsKey("captureTime")) {
            Object captureTimeObj = data.get("captureTime");
            if (captureTimeObj instanceof Date) {
                lastUpdatedTime = ((Date) captureTimeObj).getTime();
            } else if (captureTimeObj != null && captureTimeObj.getClass().getSimpleName().equals("Timestamp")) {
                try {
                    java.lang.reflect.Method getSecondsMethod = captureTimeObj.getClass().getMethod("getSeconds");
                    java.lang.reflect.Method getNanosecondsMethod = captureTimeObj.getClass().getMethod("getNanoseconds");
                    long seconds = ((Long) getSecondsMethod.invoke(captureTimeObj));
                    int nanos = ((Integer) getNanosecondsMethod.invoke(captureTimeObj));
                    lastUpdatedTime = seconds * 1000L + nanos / 1000000L;
                } catch (Exception e) {
                    android.util.Log.e("HOME_VIEWMODEL", "Error converting captureTime", e);
                }
            }
        }
        
        // Fallback to lastUpdated if no captureTime
        if (lastUpdatedTime == 0 && data.containsKey("lastUpdated")) {
            Object lastUpdatedObj = data.get("lastUpdated");
            if (lastUpdatedObj instanceof Date) {
                lastUpdatedTime = ((Date) lastUpdatedObj).getTime();
            } else if (lastUpdatedObj != null && lastUpdatedObj.getClass().getSimpleName().equals("Timestamp")) {
                try {
                    java.lang.reflect.Method toDateMethod = lastUpdatedObj.getClass().getMethod("toDate");
                    java.util.Date date = (java.util.Date) toDateMethod.invoke(lastUpdatedObj);
                    lastUpdatedTime = date.getTime();
                } catch (Exception e) {
                    android.util.Log.e("HOME_VIEWMODEL", "Error converting lastUpdated", e);
                }
            }
        }
        
        // Calculate dynamic score (same logic as PoiScoreViewModel)
        int finalScore = baseScore;
        if (lastUpdatedTime > 0) {
            long timeElapsed = currentTime - lastUpdatedTime;
            long DECREMENT_RATE_MILLIS = 60 * 60 * 1000; // 1 point per hour
            int MIN_SCORE = 10;
            int decrementedAmount = (int) (timeElapsed / DECREMENT_RATE_MILLIS);
            finalScore = Math.max(MIN_SCORE, baseScore - decrementedAmount);
        }
        
        poi.setScore(finalScore);
        
        if (data.containsKey("ownerTeamId")) {
            String teamId = (String) data.get("ownerTeamId");
            if (teamId != null && !teamId.equals("null") && !teamId.isEmpty()) {
                try {
                    // Extract number from "team_X" format
                    if (teamId.startsWith("team_")) {
                        String teamNumber = teamId.substring(5); // Remove "team_" prefix
                        int teamIdInt = Integer.parseInt(teamNumber);
                        
                        // Convert team 5+ to neutral (0)
                        if (teamIdInt >= 5) {
                            teamIdInt = 0;
                        }
                        
                        poi.setOwningTeam(teamIdInt);
                        // Also set ownerTeamId for consistency
                        poi.setOwnerTeamId("team_" + teamIdInt);
                    } else {
                        // Handle direct number format
                        int teamIdInt = Integer.parseInt(teamId);
                        
                        // Convert team 5+ to neutral (0)
                        if (teamIdInt >= 5) {
                            teamIdInt = 0;
                        }
                        
                        poi.setOwningTeam(teamIdInt);
                        // Also set ownerTeamId for consistency
                        poi.setOwnerTeamId("team_" + teamIdInt);
                    }
                } catch (NumberFormatException e) {
                    poi.setOwningTeam(0); // neutral
                    android.util.Log.d("TEAM_DEBUG", "POI '" + poi.getName() + "' failed to parse team ID, setting to neutral");
                    // Also set ownerTeamId for consistency
                    poi.setOwnerTeamId("team_0");
                }
            } else {
                poi.setOwningTeam(0); // neutral by default
                poi.setOwnerTeamId("team_0");
                android.util.Log.d("TEAM_DEBUG", "POI '" + poi.getName() + "' has null ownerTeamId, setting to neutral");
            }
        } else {
            poi.setOwningTeam(0); // neutral by default
            poi.setOwnerTeamId("team_0");
            android.util.Log.d("TEAM_DEBUG", "POI '" + poi.getName() + "' has no ownerTeamId field, setting to neutral");
        }
        
        return poi;
    }
}



