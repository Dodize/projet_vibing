package com.example.vibing.ui.score;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import android.content.SharedPreferences;

public class PoiScoreViewModel extends ViewModel {

    private final MutableLiveData<Integer> mCurrentScore;
    private final MutableLiveData<Integer> mMoneyScore;
    private final FirebaseFirestore db;
    
    // POI-specific data
    private String poiId;
    private String poiName;
    private MutableLiveData<Integer> mOwningTeam;
    private boolean justCaptured = false;
    private long lastCaptureTime = 0;
    private long lastKnownScoreTime = 0; // Local timestamp for score calculation
    private static final long CAPTURE_GRACE_PERIOD_MILLIS = 30 * 1000; // 30 seconds grace period after capture
    
    private static final long DECREMENT_RATE_MILLIS = 60 * 60 * 1000; // 1 point per hour
    private static final int MIN_SCORE = 10;

    public PoiScoreViewModel() {
        mCurrentScore = new MutableLiveData<>();
        mMoneyScore = new MutableLiveData<>();
        mOwningTeam = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
        mMoneyScore.setValue(0);
        mOwningTeam.setValue(0); // Default to neutral
        
        // Note: loadUserMoneyFromFirebase() will be called with Context from Fragment
    }
    
    public void initializePoi(String poiId, String poiName, int initialScore) {
        android.util.Log.d("POI_SCORE", "=== INITIALIZE POI START ===");
        android.util.Log.d("POI_SCORE", "POI ID: " + poiId);
        android.util.Log.d("POI_SCORE", "POI Name: " + poiName);
        android.util.Log.d("POI_SCORE", "Initial Score (from HomeFragment): " + initialScore);
        
        this.poiId = poiId;
        this.poiName = poiName;
        
        // Don't set initial score yet - wait for Firebase data to calculate dynamic score
        // This prevents showing incorrect score before calculation
        mOwningTeam.setValue(0); // Default to neutral until loaded
        
        android.util.Log.d("POI_SCORE", "About to call loadAndUpdateScore()...");
        // Immediately load from Firebase to get correct dynamic score
        loadAndUpdateScore();
        android.util.Log.d("POI_SCORE", "=== INITIALIZE POI END ===");
    }
    
    public void loadFromFirebase() {
        loadAndUpdateScore();
    }
    
    private void loadAndUpdateScore() {
        android.util.Log.d("POI_SCORE", "=== LOAD AND UPDATE SCORE START ===");
        android.util.Log.d("POI_SCORE", "loadAndUpdateScore called for POI: " + poiName + ", ID: " + poiId);
        android.util.Log.d("POI_SCORE", "Current mCurrentScore value before Firebase call: " + mCurrentScore.getValue());
        
        if (poiId != null) {
            android.util.Log.d("POI_SCORE", "Making Firebase query for document: " + poiId);
            db.collection("pois").document(poiId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    android.util.Log.d("POI_SCORE", "=== FIREBASE SUCCESS CALLBACK START ===");
                    android.util.Log.d("POI_SCORE", "Firebase query successful for POI: " + poiName + ", exists: " + documentSnapshot.exists());
                    android.util.Log.d("POI_SCORE", "Document ID: " + documentSnapshot.getId());
                    android.util.Log.d("POI_SCORE", "Document reference: " + documentSnapshot.getReference().getPath());
                    
                    if (documentSnapshot.exists()) {
                        Map<String, Object> data = documentSnapshot.getData();
                        android.util.Log.d("POI_SCORE", "DEBUG: Raw Firebase data keys: " + data.keySet());
                        android.util.Log.d("POI_SCORE", "DEBUG: Raw Firebase data: " + data.toString());
                        
                        // Log specific timestamp objects with more detail
                        android.util.Log.d("POI_SCORE", "=== DETAILED TIMESTAMP INSPECTION ===");
                        Object captureTimeRaw = data.get("captureTime");
                        Object lastUpdatedRaw = data.get("lastUpdated");
                        
                        android.util.Log.d("POI_SCORE", "captureTime raw: " + captureTimeRaw);
                        android.util.Log.d("POI_SCORE", "captureTime class: " + (captureTimeRaw != null ? captureTimeRaw.getClass().getName() : "null"));
                        if (captureTimeRaw != null) {
                            android.util.Log.d("POI_SCORE", "captureTime toString(): " + captureTimeRaw.toString());
                        }
                        
                        android.util.Log.d("POI_SCORE", "lastUpdated raw: " + lastUpdatedRaw);
                        android.util.Log.d("POI_SCORE", "lastUpdated class: " + (lastUpdatedRaw != null ? lastUpdatedRaw.getClass().getName() : "null"));
                        if (lastUpdatedRaw != null) {
                            android.util.Log.d("POI_SCORE", "lastUpdated toString(): " + lastUpdatedRaw.toString());
                        }
                        android.util.Log.d("POI_SCORE", "=== DETAILED TIMESTAMP INSPECTION END ===");
                        
                        if (data != null) {
                            int baseScore = 100; // default base score from Firebase
                            long lastUpdatedTime = System.currentTimeMillis() - 60000; // Assume 1 minute ago if no timestamp
                            
                            // Get base score from Firebase (NEVER display this directly)
                            android.util.Log.d("POI_SCORE", "Checking for currentScore field in Firebase data...");
                            if (data.containsKey("currentScore")) {
                                Object scoreObj = data.get("currentScore");
                                android.util.Log.d("POI_SCORE", "Found currentScore object: " + scoreObj + " (type: " + (scoreObj != null ? scoreObj.getClass().getSimpleName() : "null") + ")");
                                if (scoreObj instanceof Number) {
                                    baseScore = ((Number) scoreObj).intValue();
                                    android.util.Log.d("POI_SCORE", "✅ Retrieved BASE score from Firebase: " + baseScore + " (will be calculated dynamically)");
                                } else {
                                    android.util.Log.w("POI_SCORE", "❌ currentScore is not a Number: " + scoreObj);
                                }
                            } else {
                                android.util.Log.w("POI_SCORE", "❌ No currentScore field found in Firebase data. Available fields: " + data.keySet());
                            }
                            
                            // Get owning team from Firebase
                            int owningTeam = 0; // Default to neutral
                            android.util.Log.d("POI_SCORE", "Extracting owning team for POI: " + poiName);
                            android.util.Log.d("POI_SCORE", "Raw Firebase data keys: " + data.keySet());
                            
                            if (data.containsKey("ownerTeamId")) {
                                Object ownerObj = data.get("ownerTeamId");
                                android.util.Log.d("POI_SCORE", "Found ownerTeamId: " + ownerObj);
                                if (ownerObj instanceof String) {
                                    String ownerTeamId = (String) ownerObj;
                                    android.util.Log.d("POI_SCORE", "ownerTeamId string: " + ownerTeamId);
                                    if (ownerTeamId != null && !ownerTeamId.equals("null")) {
                                        try {
                                            owningTeam = Integer.parseInt(ownerTeamId.replace("team_", ""));
                                            android.util.Log.d("POI_SCORE", "Parsed owningTeam: " + owningTeam);
                                        } catch (NumberFormatException e) {
                                            android.util.Log.e("POI_SCORE", "Error parsing ownerTeamId: " + ownerTeamId);
                                            owningTeam = 0;
                                        }
                                    } else {
                                        android.util.Log.d("POI_SCORE", "ownerTeamId is null or 'null'");
                                    }
                                }
                            } else {
                                android.util.Log.d("POI_SCORE", "No ownerTeamId field found");
                            }
                            android.util.Log.d("POI_SCORE", "Final owningTeam for " + poiName + ": " + owningTeam);
                            mOwningTeam.setValue(owningTeam);
                            

                            
                             // Get timestamps from Firebase - prioritize captureTime over lastUpdated for accuracy
                            boolean foundUpdateTime = false;
                            long captureTimeMs = 0;
                            long lastUpdatedMs = 0;
                            
                            // First, get captureTime
                            if (data.containsKey("captureTime")) {
                                Object captureTimeObj = data.get("captureTime");
                                android.util.Log.d("POI_SCORE", "DEBUG: captureTime object from Firebase: " + captureTimeObj + " (type: " + (captureTimeObj != null ? captureTimeObj.getClass().getSimpleName() : "null") + ")");
                                
                                if (captureTimeObj instanceof Date) {
                                    captureTimeMs = ((Date) captureTimeObj).getTime();
                                    android.util.Log.d("POI_SCORE", "DEBUG: captureTime is Date object: " + captureTimeMs);
                                } else if (captureTimeObj != null && captureTimeObj.getClass().getSimpleName().equals("Timestamp")) {
                                    try {
                                        java.lang.reflect.Method getSecondsMethod = captureTimeObj.getClass().getMethod("getSeconds");
                                        java.lang.reflect.Method getNanosecondsMethod = captureTimeObj.getClass().getMethod("getNanoseconds");
                                        
                                        long seconds = ((Long) getSecondsMethod.invoke(captureTimeObj));
                                        int nanos = ((Integer) getNanosecondsMethod.invoke(captureTimeObj));
                                        
                                        captureTimeMs = seconds * 1000L + nanos / 1000000L;
                                        android.util.Log.d("POI_SCORE", "DEBUG: captureTime Timestamp - seconds: " + seconds + ", nanos: " + nanos);
                                        android.util.Log.d("POI_SCORE", "DEBUG: captureTime conversion math: " + seconds + " * 1000 + " + nanos + "/1000000 = " + captureTimeMs);
                                        android.util.Log.d("POI_SCORE", "DEBUG: captureTime converted to timestamp: " + captureTimeMs);
                                        android.util.Log.d("POI_SCORE", "DEBUG: captureTime converted date: " + new java.util.Date(captureTimeMs));
                                    } catch (Exception e) {
                                        android.util.Log.e("POI_SCORE", "Error converting captureTime Timestamp", e);
                                        // Try alternative method - use toDate() if available
                                        try {
                                            java.lang.reflect.Method toDateMethod = captureTimeObj.getClass().getMethod("toDate");
                                            java.util.Date date = (java.util.Date) toDateMethod.invoke(captureTimeObj);
                                            captureTimeMs = date.getTime();
                                            android.util.Log.d("POI_SCORE", "DEBUG: captureTime converted using toDate(): " + captureTimeMs + " (" + date + ")");
                                        } catch (Exception e2) {
                                            android.util.Log.e("POI_SCORE", "Error with toDate() method too", e2);
                                        }
                                    }
                                }
                            }
                            
                            // Then, get lastUpdated
                            if (data.containsKey("lastUpdated")) {
                                Object lastUpdatedObj = data.get("lastUpdated");
                                android.util.Log.d("POI_SCORE", "DEBUG: lastUpdated object from Firebase: " + lastUpdatedObj + " (type: " + (lastUpdatedObj != null ? lastUpdatedObj.getClass().getSimpleName() : "null") + ")");
                                
                                if (lastUpdatedObj instanceof Date) {
                                    lastUpdatedMs = ((Date) lastUpdatedObj).getTime();
                                    android.util.Log.d("POI_SCORE", "DEBUG: lastUpdated is Date object: " + lastUpdatedMs);
                                } else if (lastUpdatedObj != null && lastUpdatedObj.getClass().getSimpleName().equals("Timestamp")) {
                                    try {
                                        java.lang.reflect.Method getSecondsMethod = lastUpdatedObj.getClass().getMethod("getSeconds");
                                        java.lang.reflect.Method getNanosecondsMethod = lastUpdatedObj.getClass().getMethod("getNanoseconds");
                                        
                                        long seconds = ((Long) getSecondsMethod.invoke(lastUpdatedObj));
                                        int nanos = ((Integer) getNanosecondsMethod.invoke(lastUpdatedObj));
                                        
                                        lastUpdatedMs = seconds * 1000L + nanos / 1000000L;
                                        android.util.Log.d("POI_SCORE", "DEBUG: lastUpdated Timestamp - seconds: " + seconds + ", nanos: " + nanos);
                                        android.util.Log.d("POI_SCORE", "DEBUG: lastUpdated conversion math: " + seconds + " * 1000 + " + nanos + "/1000000 = " + lastUpdatedMs);
                                        android.util.Log.d("POI_SCORE", "DEBUG: lastUpdated converted to timestamp: " + lastUpdatedMs);
                                        android.util.Log.d("POI_SCORE", "DEBUG: lastUpdated converted date: " + new java.util.Date(lastUpdatedMs));
                                    } catch (Exception e) {
                                        android.util.Log.e("POI_SCORE", "Error converting lastUpdated Timestamp", e);
                                        // Try alternative method - use toDate() if available
                                        try {
                                            java.lang.reflect.Method toDateMethod = lastUpdatedObj.getClass().getMethod("toDate");
                                            java.util.Date date = (java.util.Date) toDateMethod.invoke(lastUpdatedObj);
                                            lastUpdatedMs = date.getTime();
                                            android.util.Log.d("POI_SCORE", "DEBUG: lastUpdated converted using toDate(): " + lastUpdatedMs + " (" + date + ")");
                                        } catch (Exception e2) {
                                            android.util.Log.e("POI_SCORE", "Error with toDate() method too", e2);
                                        }
                                    }
                                }
                            }
                            
                             // Choose the best timestamp - prioritize captureTime if it's older and more realistic
                            long currentTimeForComparison = System.currentTimeMillis();
                            long captureAge = captureTimeMs > 0 ? currentTimeForComparison - captureTimeMs : 0;
                            long lastUpdatedAge = lastUpdatedMs > 0 ? currentTimeForComparison - lastUpdatedMs : 0;
                            
                            android.util.Log.d("POI_SCORE", "TIMESTAMP COMPARISON:");
                            android.util.Log.d("POI_SCORE", "  captureTime: " + captureTimeMs + " (age: " + (captureAge / (1000 * 60 * 60)) + " hours)");
                            android.util.Log.d("POI_SCORE", "  lastUpdated: " + lastUpdatedMs + " (age: " + (lastUpdatedAge / (1000 * 60 * 60)) + " hours)");
                            
                            // FORCE: Always use captureTime if it exists (it's the correct one from our script)
                            if (captureTimeMs > 0) {
                                lastUpdatedTime = captureTimeMs;
                                android.util.Log.d("POI_SCORE", "FORCING CAPTURE TIME: " + captureTimeMs + " (" + new java.util.Date(captureTimeMs) + ")");
                                android.util.Log.d("POI_SCORE", "IGNORING lastUpdated: " + lastUpdatedMs + " (" + (lastUpdatedMs > 0 ? new java.util.Date(lastUpdatedMs) : "null") + ")");
                            } else if (lastUpdatedMs > 0) {
                                lastUpdatedTime = lastUpdatedMs;
                                android.util.Log.d("POI_SCORE", "USING LAST UPDATED TIME (no captureTime available)");
                            } else {
                                // Fallback to 24 hours ago
                                lastUpdatedTime = currentTimeForComparison - (24 * 60 * 60 * 1000);
                                android.util.Log.d("POI_SCORE", "USING FALLBACK TIME (24 hours ago)");
                            }
                            
                            if (lastUpdatedTime > 0) {
                                long timeSinceUpdate = currentTimeForComparison - lastUpdatedTime;
                                android.util.Log.d("POI_SCORE", "FINAL: Using timestamp: " + new java.util.Date(lastUpdatedTime) + " (age: " + (timeSinceUpdate / 1000) + " seconds ago)");
                                foundUpdateTime = true;
                            }
                            
                            // Timestamp selection already done above, just log for debugging
                            android.util.Log.d("POI_SCORE", "TIMESTAMP SELECTION COMPLETED - Using: " + new java.util.Date(lastUpdatedTime));
                            
                            // If we have a recent local capture time, use it instead
                            if (lastCaptureTime > 0 && (System.currentTimeMillis() - lastCaptureTime) < CAPTURE_GRACE_PERIOD_MILLIS) {
                                lastUpdatedTime = lastCaptureTime;
                                android.util.Log.d("POI_SCORE", "Using local captureTime instead: " + new java.util.Date(lastUpdatedTime) + " (age: " + ((System.currentTimeMillis() - lastUpdatedTime) / 1000) + " seconds ago)");
                            }
                            
                             // Calculate time-based decrement from capture timestamp
                            long currentTime = System.currentTimeMillis();
                            long timeElapsed = currentTime - lastUpdatedTime;
                            
                            android.util.Log.d("POI_SCORE", "=== TIME CALCULATION DEBUG ===");
                            android.util.Log.d("POI_SCORE", "Current system time: " + currentTime + " (" + new java.util.Date(currentTime) + ")");
                            android.util.Log.d("POI_SCORE", "Reference time from Firebase: " + lastUpdatedTime + " (" + new java.util.Date(lastUpdatedTime) + ")");
                            android.util.Log.d("POI_SCORE", "Time elapsed: " + timeElapsed + "ms (" + (timeElapsed/1000) + " seconds, " + (timeElapsed/(60*60*1000)) + " hours)");
                            android.util.Log.d("POI_SCORE", "=== TIME CALCULATION DEBUG END ===");
                            
                            // Check if we're in grace period after capture
                            boolean inGracePeriod = wasRecentlyCaptured();
                            
                             // ALWAYS calculate dynamic score - NEVER use Firebase score directly
                            int decrementedAmount = 0;
                            int dynamicScore = baseScore;
                            
                            if (!inGracePeriod) {
                                decrementedAmount = (int) (timeElapsed / DECREMENT_RATE_MILLIS);
                                dynamicScore = Math.max(MIN_SCORE, baseScore - decrementedAmount);
                                
                                android.util.Log.d("POI_SCORE", "DYNAMIC SCORE CALCULATION: " +
                                    "baseScore=" + baseScore +
                                    ", timeElapsed=" + (timeElapsed/1000) + "s" +
                                    ", hoursElapsed=" + (timeElapsed/(60*60*1000)) +
                                    ", decrementedAmount=" + decrementedAmount +
                                    ", dynamicScore=" + dynamicScore +
                                    ", MIN_SCORE=" + MIN_SCORE);
                            } else {
                                android.util.Log.d("POI_SCORE", "IN GRACE PERIOD: No decrement applied, using base score");
                            }
                            
                            // ALWAYS update local score with calculated dynamic score
                            // NEVER display raw Firebase score
                            android.util.Log.d("POI_SCORE", "About to set mCurrentScore to dynamicScore: " + dynamicScore);
                            android.util.Log.d("POI_SCORE", "Previous mCurrentScore value: " + mCurrentScore.getValue());
                            
                            mCurrentScore.setValue(dynamicScore);
                            
                            android.util.Log.d("POI_SCORE", "✅ DYNAMIC SCORE SET: " + dynamicScore + 
                                " (base=" + baseScore + ", decremented=" + decrementedAmount + ")");
                            
                            android.util.Log.d("POI_SCORE", "✅ FINAL: Displaying dynamic score " + dynamicScore + 
                                " instead of Firebase score " + baseScore);
                            
                            android.util.Log.d("POI_SCORE", "New mCurrentScore value after setValue: " + mCurrentScore.getValue());
                            
                            // Reset justCaptured flag after processing
                            justCaptured = false;
                            
                            // Debug logging
                            android.util.Log.d("POI_SCORE", "POI: " + poiName + 
                                ", Base score: " + baseScore + 
                                ", Dynamic score: " + dynamicScore + 
                                ", Time elapsed: " + timeElapsed + "ms" +
                                ", Decremented: " + decrementedAmount +
                                ", Just captured: " + justCaptured +
                                ", Firebase time: " + lastUpdatedTime +
                                ", Capture time: " + lastCaptureTime);
                        }
                    } else {
                        android.util.Log.w("POI_SCORE", "❌ Document does not exist for POI: " + poiName);
                        android.util.Log.d("POI_SCORE", "Setting default dynamic score: 100");
                        mCurrentScore.setValue(100); // Default dynamic score
                    }
                    android.util.Log.d("POI_SCORE", "=== FIREBASE SUCCESS CALLBACK END ===");
                })
                .addOnFailureListener(e -> {
                    android.util.Log.d("POI_SCORE", "=== FIREBASE FAILURE CALLBACK START ===");
                    // On error, use default dynamic score
                    android.util.Log.d("POI_SCORE", "Setting default dynamic score due to error: 100");
                    mCurrentScore.setValue(100);
                    android.util.Log.e("POI_SCORE", "Error loading score for POI: " + poiName, e);
                    android.util.Log.d("POI_SCORE", "=== FIREBASE FAILURE CALLBACK END ===");
                });
        } else {
            android.util.Log.e("POI_SCORE", "❌ POI ID is null for POI: " + poiName);
            android.util.Log.d("POI_SCORE", "Setting default dynamic score due to null ID: 100");
            mCurrentScore.setValue(100); // Default dynamic score
        }
        android.util.Log.d("POI_SCORE", "=== LOAD AND UPDATE SCORE END ===");
    }
    
    private boolean wasRecentlyCaptured() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastCaptureTime) < CAPTURE_GRACE_PERIOD_MILLIS;
    }
    
private void updateZoneInFirebase(int newScore, String teamId) {
        if (poiId != null) {
            long updateTime = System.currentTimeMillis();
            Map<String, Object> updates = new HashMap<>();
            updates.put("currentScore", newScore);
            updates.put("ownerTeamId", teamId != null && !teamId.isEmpty() ? teamId : null);
            // Store both capture timestamp and last updated timestamp
            updates.put("captureTime", new java.util.Date(updateTime));
            updates.put("lastUpdated", new java.util.Date(updateTime));
            
            android.util.Log.d("POI_SCORE", "Capturing POI with update timestamp: " + new java.util.Date(updateTime));
            
            db.collection("pois").document(poiId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    android.util.Log.d("POI_SCORE", "Successfully captured POI: " + poiName + " by team " + teamId + " with score " + newScore);
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("POI_SCORE", "Failed to capture POI: " + poiName, e);
                });
        }
    }

    public LiveData<Integer> getCurrentScore() {
        android.util.Log.d("POI_SCORE", "getCurrentScore() called, current value: " + mCurrentScore.getValue());
        return mCurrentScore;
    }

    public LiveData<Integer> getOwningTeam() {
        return mOwningTeam;
    }

    public LiveData<Integer> getMoneyScore() {
        return mMoneyScore;
    }

public void addMoneyBonus(int amount, android.content.Context context) {
        Integer currentMoney = mMoneyScore.getValue();
        if (currentMoney != null) {
            int newMoney = currentMoney + amount;
            mMoneyScore.setValue(newMoney);
            
            // Save to Firebase
            saveUserMoneyToFirebase(newMoney, context);
            
            android.util.Log.d("POI_SCORE", "Money bonus added: " + amount + ", new total: " + newMoney);
        }
    }
    
    public void addMoneyPenalty(int amount, android.content.Context context) {
        Integer currentMoney = mMoneyScore.getValue();
        if (currentMoney != null) {
            int newMoney = Math.max(0, currentMoney - amount); // Empêtrer de passer en négatif
            mMoneyScore.setValue(newMoney);
            
            // Save to Firebase
            saveUserMoneyToFirebase(newMoney, context);
            
            android.util.Log.d("POI_SCORE", "Money penalty applied: " + amount + ", new total: " + newMoney);
        }
    }
    
    public void loadUserMoneyFromFirebase(android.content.Context context) {
        // Get current user ID from SharedPreferences
        android.util.Log.d("POI_SCORE", "Loading user money from Firebase");
        
        try {
            SharedPreferences prefs = context.getSharedPreferences("VibingPrefs", android.content.Context.MODE_PRIVATE);
            String userId = prefs.getString("user_id", null);
            
            if (userId != null) {
                db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Integer money = documentSnapshot.getLong("money") != null ? 
                                documentSnapshot.getLong("money").intValue() : 0;
                            mMoneyScore.setValue(money);
                            android.util.Log.d("POI_SCORE", "Loaded user money from Firebase: " + money);
                        } else {
                            android.util.Log.w("POI_SCORE", "User document not found in Firebase");
                        }
                    })
                    .addOnFailureListener(e -> {
                        android.util.Log.e("POI_SCORE", "Error loading user money from Firebase", e);
                    });
            } else {
                android.util.Log.w("POI_SCORE", "No user ID found, using default money: 0");
            }
        } catch (Exception e) {
            android.util.Log.e("POI_SCORE", "Exception loading user money", e);
        }
    }
    
    private void saveUserMoneyToFirebase(int money, android.content.Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("VibingPrefs", android.content.Context.MODE_PRIVATE);
            String userId = prefs.getString("user_id", null);
            
            if (userId != null) {
                db.collection("users").document(userId)
                    .update("money", money)
                    .addOnSuccessListener(aVoid -> {
                        android.util.Log.d("POI_SCORE", "Successfully saved money to Firebase: " + money);
                    })
                    .addOnFailureListener(e -> {
                        android.util.Log.e("POI_SCORE", "Error saving money to Firebase", e);
                    });
            } else {
                android.util.Log.w("POI_SCORE", "No user ID found, cannot save money to Firebase");
            }
        } catch (Exception e) {
            android.util.Log.e("POI_SCORE", "Exception saving user money", e);
        }
    }
    
    public void setScore(int newScore) {
        android.util.Log.w("POI_SCORE", "setScore() called with score: " + newScore + " - This should only be used for dynamic scores, not raw Firebase scores!");
        mCurrentScore.setValue(newScore);
        // No automatic Firebase update - score is calculated on-the-fly
    }
    
public void captureZone(int newScore, String teamId) {
        android.util.Log.d("POI_SCORE", "=== CAPTURE ZONE START ===");
        android.util.Log.d("POI_SCORE", "captureZone called with newScore: " + newScore + ", teamId: " + teamId);
        android.util.Log.d("POI_SCORE", "Current mCurrentScore before update: " + mCurrentScore.getValue());
        android.util.Log.d("POI_SCORE", "Current mOwningTeam before update: " + mOwningTeam.getValue());
        android.util.Log.d("POI_SCORE", "POI: " + poiName);
        android.util.Log.d("POI_SCORE", "POI ID: " + poiId);
        
        // Capture grace period to prevent multiple captures in quick succession
        if (wasRecentlyCaptured()) {
            android.util.Log.d("POI_SCORE", "Capture ignored - recently captured within grace period");
            return;
        }
        
        // Extract team number for display (convert from "team_X" format)
        int teamNumber = 0;
        if (teamId != null && !teamId.isEmpty() && teamId.startsWith("team_")) {
            try {
                teamNumber = Integer.parseInt(teamId.substring(5));
            } catch (NumberFormatException e) {
                android.util.Log.e("POI_SCORE", "Error parsing teamId: " + teamId, e);
                teamNumber = 0;
            }
        }
        
        // Update LiveData with new values immediately
        mCurrentScore.setValue(newScore);
        mOwningTeam.setValue(teamNumber);
        
        android.util.Log.d("POI_SCORE", "Current mCurrentScore after setValue: " + mCurrentScore.getValue());
        android.util.Log.d("POI_SCORE", "Current mOwningTeam after setValue: " + mOwningTeam.getValue());
        
        // Set justCaptured flag and last capture time
        justCaptured = true;
        lastCaptureTime = System.currentTimeMillis();
        
        // Update in Firebase with full team string
        updateZoneInFirebase(newScore, teamId);
        
        android.util.Log.d("POI_SCORE", "=== CAPTURE ZONE END ===");
    }
    
    /**
     * Calcule le score dynamique actuel de la zone en fonction du temps écoulé
     * @return Le score décrémenté en fonction du temps
     */
    public int calculateDynamicScore() {
        Integer currentScore = mCurrentScore.getValue();
        if (currentScore == null) {
            return 100; // Score par défaut
        }
        
        // Récupérer le timestamp de dernière mise à jour depuis Firebase
        if (poiId != null) {
            db.collection("pois").document(poiId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> data = documentSnapshot.getData();
                        if (data != null) {
                            long lastUpdatedTime = System.currentTimeMillis() - (24 * 60 * 60 * 1000); // 24h par défaut
                            
                            // Prioriser lastUpdated, puis captureTime
                            if (data.containsKey("lastUpdated")) {
                                Object lastUpdatedObj = data.get("lastUpdated");
                                if (lastUpdatedObj instanceof Date) {
                                    lastUpdatedTime = ((Date) lastUpdatedObj).getTime();
                                }
                            } else if (data.containsKey("captureTime")) {
                                Object captureTimeObj = data.get("captureTime");
                                if (captureTimeObj instanceof Date) {
                                    lastUpdatedTime = ((Date) captureTimeObj).getTime();
                                }
                            }
                            
                            // Calculer le décrément (1 point par heure)
                            long currentTime = System.currentTimeMillis();
                            long timeElapsed = currentTime - lastUpdatedTime;
                            int decrementedAmount = (int) (timeElapsed / DECREMENT_RATE_MILLIS);
                            int dynamicScore = Math.max(MIN_SCORE, currentScore - decrementedAmount);
                            
                            android.util.Log.d("POI_SCORE", "Dynamic score calculation: original=" + currentScore + 
                                ", timeElapsed=" + (timeElapsed/(60*60*1000)) + " hours, decremented=" + decrementedAmount + 
                                ", dynamicScore=" + dynamicScore);
                            
                            mCurrentScore.setValue(dynamicScore);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("POI_SCORE", "Error calculating dynamic score", e);
                });
        }
        
        return currentScore;
    }
    
    /**
     * Gère le QCM: compare le score du joueur au score dynamique de la zone
     * @param playerScore Le score obtenu par le joueur au QCM
     * @param playerTeam L'équipe du joueur
     * @return true si le joueur gagne, false sinon
     */
public boolean handleQcmResult(int playerScore, String playerTeam) {
        // Utiliser le score dynamique déjà calculé et affiché
        Integer dynamicZoneScore = mCurrentScore.getValue();
        if (dynamicZoneScore == null) {
            dynamicZoneScore = 100; // Default dynamic score
        }
        
        android.util.Log.d("POI_SCORE", "QCM Result: Player score=" + playerScore + 
            ", Zone dynamic score=" + dynamicZoneScore + 
            ", Player team=" + playerTeam);
        
        // Le joueur gagne si son score est supérieur au score dynamique de la zone
        if (playerScore > dynamicZoneScore) {
            android.util.Log.d("POI_SCORE", "Player WINS QCM - Capturing zone with score " + playerScore);
            captureZone(playerScore, playerTeam);
            return true;
        } else {
            android.util.Log.d("POI_SCORE", "Player LOSES QCM - Zone not captured (player: " + playerScore + " < zone: " + dynamicZoneScore + ")");
            return false;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // No cleanup needed since we don't have ongoing listeners
    }
}