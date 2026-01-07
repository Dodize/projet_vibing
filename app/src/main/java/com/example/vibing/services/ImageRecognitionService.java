package com.example.vibing.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.example.vibing.models.Poi;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ImageRecognitionService {
    private static final String TAG = "ImageRecognitionService";
    private ImageLabeler imageLabeler;
    private ObjectDetector objectDetector;
    private FirebaseFirestore db;
    
    public ImageRecognitionService(Context context) {
        // Initialize image labeler with default options
        ImageLabelerOptions options = ImageLabelerOptions.DEFAULT_OPTIONS;
        imageLabeler = ImageLabeling.getClient(options);
        
        // Initialize object detector
        ObjectDetectorOptions detectorOptions = new ObjectDetectorOptions.Builder()
                .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
                .enableClassification()
                .build();
        objectDetector = ObjectDetection.getClient(detectorOptions);
        
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
    }
    
    public interface RecognitionCallback {
        void onSuccess(List<String> labels, boolean isCorrectLocation);
        void onError(String error);
    }
    
    public void recognizeImage(Bitmap bitmap, String expectedLocation, RecognitionCallback callback) {
        try {
            InputImage image = InputImage.fromBitmap(bitmap, 0);
            
            // First try image labeling
            imageLabeler.process(image)
                    .addOnSuccessListener(labels -> {
                        List<String> labelNames = new ArrayList<>();
                        for (ImageLabel label : labels) {
                            labelNames.add(label.getText());
                        }
                        
                        // Validate using keywords from database
                        validateLocationWithKeywords(labelNames, expectedLocation, callback);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Image labeling failed", e);
                        callback.onError("Image labeling failed: " + e.getMessage());
                    });
                    
        } catch (Exception e) {
            Log.e(TAG, "Error processing image", e);
            callback.onError("Error processing image: " + e.getMessage());
        }
    }
    
    private void validateLocationWithKeywords(List<String> labels, String expectedLocation, RecognitionCallback callback) {
        // Find POI by name
        db.collection("pois")
                .whereEqualTo("name", expectedLocation)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // POI found, get its keywords
                        Poi poi = queryDocumentSnapshots.getDocuments().get(0).toObject(Poi.class);
                        poi.setId(queryDocumentSnapshots.getDocuments().get(0).getId());
                        
                        List<String> keywords = poi.getKeywords();
                        if (keywords != null && !keywords.isEmpty()) {
                            boolean isCorrect = validateWithKeywords(labels, keywords);
                            callback.onSuccess(labels, isCorrect);
                        } else {
                            // POI without keywords - use default "building" keyword
                            Log.w(TAG, "No keywords found for POI: " + expectedLocation + ", using default 'building'");
                            List<String> defaultKeywords = Arrays.asList("building");
                            boolean isCorrect = validateWithKeywords(labels, defaultKeywords);
                            callback.onSuccess(labels, isCorrect);
                        }
                    } else {
                        // POI not found, try to find by partial name match
                        findPoiByPartialName(labels, expectedLocation, callback);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching POI from database", e);
                    // Use default "building" keyword on error
                    List<String> defaultKeywords = Arrays.asList("building");
                    boolean isCorrect = validateWithKeywords(labels, defaultKeywords);
                    callback.onSuccess(labels, isCorrect);
                });
    }
    
    private void findPoiByPartialName(List<String> labels, String expectedLocation, RecognitionCallback callback) {
        db.collection("pois")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Poi matchingPoi = null;
                    
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Poi poi = doc.toObject(Poi.class);
                        poi.setId(doc.getId());
                        
                        // Check if expected location contains POI name or vice versa
                        String poiName = poi.getName().toLowerCase();
                        String expectedLower = expectedLocation.toLowerCase();
                        
                        if (poiName.contains(expectedLower) || expectedLower.contains(poiName)) {
                            matchingPoi = poi;
                            break;
                        }
                    }
                    
                    if (matchingPoi != null) {
                        List<String> keywords = matchingPoi.getKeywords();
                        if (keywords != null && !keywords.isEmpty()) {
                            boolean isCorrect = validateWithKeywords(labels, keywords);
                            callback.onSuccess(labels, isCorrect);
                        } else {
                            // POI without keywords - use default "building" keyword
                            Log.w(TAG, "No keywords found for POI, using default 'building'");
                            List<String> defaultKeywords = Arrays.asList("building");
                            boolean isCorrect = validateWithKeywords(labels, defaultKeywords);
                            callback.onSuccess(labels, isCorrect);
                        }
                    } else {
                        // No matching POI found, use default "building" keyword
                        Log.w(TAG, "No matching POI found for: " + expectedLocation + ", using default 'building'");
                        List<String> defaultKeywords = Arrays.asList("building");
                        boolean isCorrect = validateWithKeywords(labels, defaultKeywords);
                        callback.onSuccess(labels, isCorrect);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error searching POI by partial name", e);
                    boolean isCorrect = validateLocation(labels, expectedLocation);
                    callback.onSuccess(labels, isCorrect);
                });
    }
    
    private boolean validateWithKeywords(List<String> labels, List<String> keywords) {
        if (labels == null || keywords == null || keywords.isEmpty()) {
            return false;
        }
        
        // Convert all to lowercase for comparison
        List<String> labelsLower = new ArrayList<>();
        for (String label : labels) {
            labelsLower.add(label.toLowerCase());
        }
        
        List<String> keywordsLower = new ArrayList<>();
        for (String keyword : keywords) {
            keywordsLower.add(keyword.toLowerCase());
        }
        
        // Check if any label matches any keyword
        for (String label : labelsLower) {
            for (String keyword : keywordsLower) {
                if (label.contains(keyword) || keyword.contains(label)) {
                    Log.d(TAG, "Match found: label='" + label + "' matches keyword='" + keyword + "'");
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private boolean validateLocation(List<String> labels, String expectedLocation) {
        if (expectedLocation == null || labels == null) {
            return false;
        }
        
        // Convert to lowercase for comparison
        String expectedLower = expectedLocation.toLowerCase();
        
        // Check if any label matches the expected location
        for (String label : labels) {
            String labelLower = label.toLowerCase();
            
            // Direct match
            if (labelLower.contains(expectedLower) || expectedLower.contains(labelLower)) {
                return true;
            }
            
            // Location-specific keywords
            if (containsLocationKeywords(labelLower, expectedLower)) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean containsLocationKeywords(String label, String expectedLocation) {
        // Define keywords for common location types
        if (expectedLocation.contains("capitole") || expectedLocation.contains("théâtre")) {
            return label.contains("building") || label.contains("architecture") || label.contains("monument");
        }
        if (expectedLocation.contains("pont") || expectedLocation.contains("pont neuf")) {
            return label.contains("bridge") || label.contains("water") || label.contains("river");
        }
        if (expectedLocation.contains("basilique") || expectedLocation.contains("église")) {
            return label.contains("church") || label.contains("religious") || label.contains("architecture");
        }
        if (expectedLocation.contains("jardin") || expectedLocation.contains("parc")) {
            return label.contains("garden") || label.contains("park") || label.contains("nature") || label.contains("tree");
        }
        if (expectedLocation.contains("zénith") || expectedLocation.contains("stade")) {
            return label.contains("building") || label.contains("stadium") || label.contains("architecture");
        }
        
        return false;
    }
    
    public void close() {
        if (imageLabeler != null) {
            imageLabeler.close();
        }
        if (objectDetector != null) {
            objectDetector.close();
        }
    }
}