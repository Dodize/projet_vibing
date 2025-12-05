package com.example.vibing.ui.score;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

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
    
    private static final long DECREMENT_RATE_MILLIS = 5 * 1000; // 1 point per 5 seconds
    private static final int MIN_SCORE = 10;

    public PoiScoreViewModel() {
        mCurrentScore = new MutableLiveData<>();
        mMoneyScore = new MutableLiveData<>();
        mOwningTeam = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
        mMoneyScore.setValue(0);
        mOwningTeam.setValue(0); // Default to neutral
    }
    
    public void initializePoi(String poiId, String poiName, int initialScore) {
        this.poiId = poiId;
        this.poiName = poiName;
        
        // Set initial score locally, don't load from Firebase yet
        // Firebase loading will be done on demand
        mCurrentScore.setValue(initialScore);
        mOwningTeam.setValue(0); // Default to neutral until loaded
    }
    
    public void loadFromFirebase() {
        loadAndUpdateScore();
    }
    
    private void loadAndUpdateScore() {
        android.util.Log.d("POI_SCORE", "loadAndUpdateScore called for POI: " + poiName + ", ID: " + poiId);
        
        if (poiId != null) {
            db.collection("pois").document(poiId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    android.util.Log.d("POI_SCORE", "Firebase query successful for POI: " + poiName + ", exists: " + documentSnapshot.exists());
                    
                    if (documentSnapshot.exists()) {
                        Map<String, Object> data = documentSnapshot.getData();
                        
                        if (data != null) {
                            int currentScore = 100; // default
                            long lastUpdatedTime = System.currentTimeMillis() - 60000; // Assume 1 minute ago if no timestamp
                            
                            // Get current score from Firebase FIRST
                            if (data.containsKey("currentScore")) {
                                Object scoreObj = data.get("currentScore");
                                if (scoreObj instanceof Number) {
                                    currentScore = ((Number) scoreObj).intValue();
                                    android.util.Log.d("POI_SCORE", "Retrieved currentScore from Firebase: " + currentScore);
                                }
                            } else {
                                android.util.Log.w("POI_SCORE", "No currentScore field found in Firebase, using default: " + currentScore);
                            }
                            
                            // Get owning team from Firebase
                            int owningTeam = 0; // Default to neutral
                            if (data.containsKey("ownerTeamId")) {
                                Object ownerObj = data.get("ownerTeamId");
                                android.util.Log.d("POI_SCORE", "ownerTeamId from Firebase: " + ownerObj);
                                if (ownerObj instanceof String) {
                                    String ownerTeamId = (String) ownerObj;
                                    if (ownerTeamId != null && !ownerTeamId.equals("null")) {
                                        try {
                                            owningTeam = Integer.parseInt(ownerTeamId.replace("team_", ""));
                                        } catch (NumberFormatException e) {
                                            android.util.Log.e("POI_SCORE", "Error parsing ownerTeamId: " + ownerTeamId);
                                            owningTeam = 0;
                                        }
                                    }
                                }
                            }
                            android.util.Log.d("POI_SCORE", "Final owningTeam for " + poiName + ": " + owningTeam);
                            mOwningTeam.setValue(owningTeam);
                            

                            
                            // Get capture time from Firebase (use captureTime instead of lastUpdated)
                            boolean foundCaptureTime = false;
                            if (data.containsKey("captureTime")) {
                                Object captureTimeObj = data.get("captureTime");
                                if (captureTimeObj instanceof Date) {
                                    lastUpdatedTime = ((Date) captureTimeObj).getTime();
                                    long timeSinceCapture = System.currentTimeMillis() - lastUpdatedTime;
                                    android.util.Log.d("POI_SCORE", "Retrieved captureTime from Firebase: " + new java.util.Date(lastUpdatedTime) + " (age: " + (timeSinceCapture / 1000) + " seconds ago)");
                                    
                                    // Detect corrupted captureTime (less than 2 minutes ago but score is not at minimum)
                                    if (timeSinceCapture < 120000 && currentScore > MIN_SCORE + 10) {
                                        android.util.Log.w("POI_SCORE", "DETECTED CORRUPTION: captureTime is only " + (timeSinceCapture/1000) + "s ago but score is " + currentScore + ". Ignoring captureTime and using much older time.");
                                        // Use a much older time (24 hours ago) to prevent immediate decrement
                                        lastUpdatedTime = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
                                    } else {
                                        foundCaptureTime = true;
                                    }
                                }
                            }
                            
                            if (!foundCaptureTime) {
                                if (data.containsKey("lastUpdated")) {
                                    // Fallback to lastUpdated if captureTime not available
                                    Object lastUpdatedObj = data.get("lastUpdated");
                                    if (lastUpdatedObj instanceof Date) {
                                        lastUpdatedTime = ((Date) lastUpdatedObj).getTime();
                                        android.util.Log.d("POI_SCORE", "Retrieved lastUpdated from Firebase: " + new java.util.Date(lastUpdatedTime) + " (age: " + ((System.currentTimeMillis() - lastUpdatedTime) / 1000) + " seconds ago)");
                                    }
                                } else {
                                    android.util.Log.w("POI_SCORE", "No captureTime or lastUpdated found in Firebase, using calculated time");
                                    // Use a very old time to prevent immediate decrement
                                    lastUpdatedTime = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
                                }
                            }
                            
                            // If we have a recent local capture time, use it instead
                            if (lastCaptureTime > 0 && (System.currentTimeMillis() - lastCaptureTime) < CAPTURE_GRACE_PERIOD_MILLIS) {
                                lastUpdatedTime = lastCaptureTime;
                                android.util.Log.d("POI_SCORE", "Using local captureTime instead: " + new java.util.Date(lastUpdatedTime) + " (age: " + ((System.currentTimeMillis() - lastUpdatedTime) / 1000) + " seconds ago)");
                            }
                            
                            // Calculate time-based decrement from capture timestamp
                            long currentTime = System.currentTimeMillis();
                            long timeElapsed = currentTime - lastUpdatedTime;
                            
                            android.util.Log.d("POI_SCORE", "Time calculation: current=" + currentTime + 
                                ", captureTime=" + lastUpdatedTime + 
                                ", elapsed=" + timeElapsed + "ms (" + (timeElapsed/1000) + " seconds)");
                            
                            // Check if we're in grace period after capture
                            boolean inGracePeriod = wasRecentlyCaptured();
                            
                            // Calculate decrement (1 point per 5 seconds) - only if not in grace period
                            int decrementedAmount = 0;
                            int newScore = currentScore;
                            
                            if (!inGracePeriod) {
                                decrementedAmount = (int) (timeElapsed / DECREMENT_RATE_MILLIS);
                                newScore = Math.max(MIN_SCORE, currentScore - decrementedAmount);
                            }
                            
                            android.util.Log.d("POI_SCORE", "Decrement calculation: timeElapsed=" + timeElapsed + 
                                "ms (" + (timeElapsed/1000) + "s), rate=" + DECREMENT_RATE_MILLIS + "ms, gracePeriod=" + inGracePeriod + 
                                ", decremented=" + decrementedAmount + 
                                " points, currentScore=" + currentScore + ", newScore=" + newScore);
                            
                            // Update local score ONLY - NO Firebase update for decrement
                            // The score will be calculated on-the-fly next time
                            // Firebase updates should ONLY happen on explicit capture
                            mCurrentScore.setValue(newScore);
                            
                            android.util.Log.d("POI_SCORE", "SCORE UPDATE: Local score set to " + newScore + 
                                " (NO Firebase update for decrement - only on capture)");
                            
                            android.util.Log.d("POI_SCORE", "LOCAL UPDATE: Score updated locally to " + newScore + 
                                " (time elapsed: " + timeElapsed + "ms, decremented: " + decrementedAmount + ")");
                            
                            // Reset justCaptured flag after processing
                            justCaptured = false;
                            
                            // Debug logging
                            android.util.Log.d("POI_SCORE", "POI: " + poiName + 
                                ", Original: " + currentScore + 
                                ", Time elapsed: " + timeElapsed + "ms" +
                                ", Decremented: " + decrementedAmount +
                                ", New score: " + newScore +
                                ", Just captured: " + justCaptured +
                                ", Will update Firebase: " + (decrementedAmount > 0 && !justCaptured) +
                                "" +
                                ", Firebase time: " + lastUpdatedTime +
                                ", Capture time: " + lastCaptureTime);
                        }
                    } else {
                        android.util.Log.w("POI_SCORE", "Document does not exist for POI: " + poiName);
                        mCurrentScore.setValue(100);
                    }
                })
                .addOnFailureListener(e -> {
                    // On error, use default score
                    mCurrentScore.setValue(100);
                    android.util.Log.e("POI_SCORE", "Error loading score for POI: " + poiName, e);
                });
        } else {
            android.util.Log.e("POI_SCORE", "POI ID is null for POI: " + poiName);
            mCurrentScore.setValue(100);
        }
    }
    
    private boolean wasRecentlyCaptured() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastCaptureTime) < CAPTURE_GRACE_PERIOD_MILLIS;
    }
    
    private void updateZoneInFirebase(int newScore, int teamId) {
        if (poiId != null) {
            long captureTime = System.currentTimeMillis();
            Map<String, Object> updates = new HashMap<>();
            updates.put("currentScore", newScore);
            updates.put("ownerTeamId", teamId > 0 ? "team_" + teamId : null);
            // Store capture timestamp for on-the-fly score calculation
            updates.put("captureTime", new java.util.Date(captureTime));
            
            android.util.Log.d("POI_SCORE", "Capturing POI with capture timestamp: " + new java.util.Date(captureTime));
            
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
        return mCurrentScore;
    }

    public LiveData<Integer> getOwningTeam() {
        return mOwningTeam;
    }

    public LiveData<Integer> getMoneyScore() {
        return mMoneyScore;
    }

    public void addMoneyBonus(int amount) {
        Integer currentMoney = mMoneyScore.getValue();
        if (currentMoney != null) {
            mMoneyScore.setValue(currentMoney + amount);
        }
    }
    
    public void setScore(int newScore) {
        mCurrentScore.setValue(newScore);
        // No automatic Firebase update - score is calculated on-the-fly
    }
    
    public void captureZone(int newScore, int teamId) {
        mCurrentScore.setValue(newScore);
        mOwningTeam.setValue(teamId);
        justCaptured = true; // Mark as just captured to prevent immediate decrement
        
        // Store capture time to prevent immediate time-based decrement
        lastCaptureTime = System.currentTimeMillis();
        lastKnownScoreTime = System.currentTimeMillis(); // Reset local timestamp
        
        android.util.Log.d("POI_SCORE", "CAPTURE: Setting score to " + newScore + " and team to " + teamId);
        
        // Update Firebase immediately with capture data
        updateZoneInFirebase(newScore, teamId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // No cleanup needed since we don't have ongoing listeners
    }
}