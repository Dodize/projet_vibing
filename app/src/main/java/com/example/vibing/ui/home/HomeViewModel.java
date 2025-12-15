package com.example.vibing.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vibing.models.Poi;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
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
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<Poi> poiList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // Create POI manually from document data
                            Poi poi = createPoiFromDocument(document);
                            poiList.add(poi);
                        }
                        pois.setValue(poiList);
                    })
                    .addOnFailureListener(e -> {
                        // Fallback to test data
                        loadTestPois();
                    });
        } catch (Exception e) {
            // Fallback to test data
            loadTestPois();
        }
    }
    
    private void loadTestPois() {
        List<Poi> testPois = new ArrayList<>();
        
        // Add some test POIs with correct Toulouse coordinates
        Poi capitole = new Poi();
        capitole.setId("poi_capitole");
        capitole.setName("Capitole de Toulouse");
        capitole.setLatitude(43.6047);
        capitole.setLongitude(1.4442);
        capitole.setScore(500);
        capitole.setOwningTeam(0);
        testPois.add(capitole);
        
        Poi zenith = new Poi();
        zenith.setId("poi_zenith");
        zenith.setName("Zénith de Toulouse");
        zenith.setLatitude(43.5819);
        zenith.setLongitude(1.4337);
        zenith.setScore(250);
        zenith.setOwningTeam(0);
        testPois.add(zenith);
        
        Poi basilique = new Poi();
        basilique.setId("poi_basilique_saint_sernin");
        basilique.setName("Basilique Saint-Sernin");
        basilique.setLatitude(43.6082);
        basilique.setLongitude(1.4408);
        basilique.setScore(600);
        basilique.setOwningTeam(0);
        testPois.add(basilique);
        
        Poi pontNeuf = new Poi();
        pontNeuf.setId("poi_pont_neuf");
        pontNeuf.setName("Pont Neuf de Toulouse");
        pontNeuf.setLatitude(43.6032);
        pontNeuf.setLongitude(1.4302);
        pontNeuf.setScore(350);
        pontNeuf.setOwningTeam(0);
        testPois.add(pontNeuf);
        
        // POI de test avec les coordonnées spécifiées
        Poi testPoi = new Poi();
        testPoi.setId("poi_test_coordinates");
        testPoi.setName("POI Test Coordonnées");
        testPoi.setLatitude(43.60236021729008);
        testPoi.setLongitude(1.4557650816708745);
        testPoi.setScore(100);
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
        
        // Extract score
        if (data.containsKey("currentScore")) {
            Object score = data.get("currentScore");
            if (score instanceof Number) {
                poi.setScore(((Number) score).intValue());
            }
        } else if (data.containsKey("score")) {
            Object score = data.get("score");
            if (score instanceof Number) {
                poi.setScore(((Number) score).intValue());
            }
        }
        
        // Extract owning team
        if (data.containsKey("ownerTeamId")) {
            String teamId = (String) data.get("ownerTeamId");
            if (teamId != null && !teamId.equals("null") && !teamId.isEmpty()) {
                try {
                    // Extract number from "team_X" format
                    if (teamId.startsWith("team_")) {
                        String teamNumber = teamId.substring(5); // Remove "team_" prefix
                        int teamIdInt = Integer.parseInt(teamNumber);
                        poi.setOwningTeam(teamIdInt);
                    } else {
                        // Try direct parsing as fallback
                        int teamIdInt = Integer.parseInt(teamId);
                        poi.setOwningTeam(teamIdInt);
                    }
                } catch (NumberFormatException e) {
                    poi.setOwningTeam(0); // neutral
                    android.util.Log.d("TEAM_DEBUG", "POI '" + poi.getName() + "' failed to parse team ID, setting to neutral");
                }
            } else {
                poi.setOwningTeam(0); // neutral
            }
        } else if (data.containsKey("owningTeam")) {
            Object team = data.get("owningTeam");
            if (team instanceof Number) {
                int teamId = ((Number) team).intValue();
                poi.setOwningTeam(teamId);
            }
        } else {
            // No team field found, setting to neutral
        }
        
        return poi;
    }
}