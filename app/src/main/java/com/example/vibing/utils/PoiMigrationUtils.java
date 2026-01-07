package com.example.vibing.utils;

import com.example.vibing.models.Poi;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utilitaire pour migrer les POI existants vers le nouveau système avec mots-clés
 */
public class PoiMigrationUtils {
    private static final String TAG = "PoiMigration";
    
    // Mots-clés par défaut basés sur les types de POI existants
    private static final Map<String, List<String>> DEFAULT_KEYWORDS = new HashMap<>();
    
    static {
        // Capitole/Théâtre
        DEFAULT_KEYWORDS.put("capitole", Arrays.asList("building", "architecture", "monument", "government", "historic"));
        DEFAULT_KEYWORDS.put("théâtre", Arrays.asList("building", "architecture", "theater", "culture", "entertainment"));
        
        // Ponts
        DEFAULT_KEYWORDS.put("pont", Arrays.asList("bridge", "water", "river", "architecture", "structure"));
        DEFAULT_KEYWORDS.put("pont neuf", Arrays.asList("bridge", "water", "river", "historic", "stone"));
        
        // Lieux religieux
        DEFAULT_KEYWORDS.put("basilique", Arrays.asList("church", "religious", "architecture", "worship", "historic"));
        DEFAULT_KEYWORDS.put("église", Arrays.asList("church", "religious", "architecture", "worship", "building"));
        
        // Parcs/Jardins
        DEFAULT_KEYWORDS.put("jardin", Arrays.asList("garden", "park", "nature", "tree", "plants", "green"));
        DEFAULT_KEYWORDS.put("parc", Arrays.asList("park", "nature", "tree", "recreation", "green"));
        
        // Stades/Zénith
        DEFAULT_KEYWORDS.put("zénith", Arrays.asList("building", "stadium", "architecture", "entertainment", "venue"));
        DEFAULT_KEYWORDS.put("stade", Arrays.asList("building", "stadium", "architecture", "sports", "venue"));
    }
    
    /**
     * Migre tous les POI existants pour ajouter les mots-clés appropriés
     */
    public static void migrateAllPoisWithKeywords() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        
        db.collection("pois")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d(TAG, "Début de la migration de " + queryDocumentSnapshots.size() + " POI");
                    
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Poi poi = doc.toObject(Poi.class);
                        poi.setId(doc.getId());
                        
                        List<String> keywords = generateKeywordsForPoi(poi);
                        
                        // Mettre à jour le POI avec les mots-clés
                        doc.getReference().update("keywords", keywords)
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "POI migré avec succès: " + poi.getName()))
                                .addOnFailureListener(e -> Log.e(TAG, "Erreur lors de la migration du POI: " + poi.getName(), e));
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Erreur lors de la récupération des POI", e));
    }
    
    /**
     * Génère les mots-clés appropriés pour un POI en fonction de son nom
     */
    public static List<String> generateKeywordsForPoi(Poi poi) {
        String poiName = poi.getName().toLowerCase();
        List<String> keywords = new ArrayList<>();
        
        // Chercher une correspondance exacte ou partielle dans les mots-clés par défaut
        for (Map.Entry<String, List<String>> entry : DEFAULT_KEYWORDS.entrySet()) {
            String key = entry.getKey();
            if (poiName.contains(key)) {
                keywords.addAll(entry.getValue());
                break; // Prendre la première correspondance trouvée
            }
        }
        
        // Si aucun mot-clé par défaut n'est trouvé, ajouter des mots-clés génériques
        if (keywords.isEmpty()) {
            keywords.addAll(Arrays.asList("building"));
        }
        
        // Ajouter le nom du POI en minuscules comme mot-clé
        keywords.add(poiName.replaceAll("[^a-z0-9\\s]", "").trim());
        
        Log.d(TAG, "Mots-clés générés pour " + poi.getName() + ": " + keywords);
        return keywords;
    }
    
    /**
     * Met à jour les mots-clés d'un POI spécifique
     */
    public static void updatePoiKeywords(String poiId, List<String> keywords) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        
        db.collection("pois").document(poiId)
                .update("keywords", keywords)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Mots-clés mis à jour pour le POI: " + poiId))
                .addOnFailureListener(e -> Log.e(TAG, "Erreur lors de la mise à jour des mots-clés du POI: " + poiId, e));
    }
    
    /**
     * Vérifie si un POI a déjà des mots-clés
     */
    public static void checkPoiKeywords(String poiId, KeywordCheckCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        
        db.collection("pois").document(poiId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> keywords = (List<String>) documentSnapshot.get("keywords");
                        callback.onResult(keywords != null && !keywords.isEmpty());
                    } else {
                        callback.onResult(false);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Erreur lors de la vérification des mots-clés du POI: " + poiId, e);
                    callback.onResult(false);
                });
    }
    
    public interface KeywordCheckCallback {
        void onResult(boolean hasKeywords);
    }
}