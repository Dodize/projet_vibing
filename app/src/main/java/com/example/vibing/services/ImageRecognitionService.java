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

public class ImageRecognitionService {
    private static final String TAG = "ImageRecognitionService";
    private ImageLabeler imageLabeler;
    private ObjectDetector objectDetector;
    
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
                        
                        boolean isCorrect = validateLocation(labelNames, expectedLocation);
                        callback.onSuccess(labelNames, isCorrect);
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