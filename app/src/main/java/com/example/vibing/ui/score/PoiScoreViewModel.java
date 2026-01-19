package com.example.vibing.ui.score;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.example.vibing.models.Bonus;
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
    
    // Bonus management
    private Bonus activeFreezeBonus = null;

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
        this.poiId = poiId;
        this.poiName = poiName;
        
        // Don't set initial score yet - wait for Firebase data to calculate dynamic score
        // This prevents showing incorrect score before calculation
        mOwningTeam.setValue(0); // Default to neutral until loaded
        // Immediately load from Firebase to get correct dynamic score
        loadAndUpdateScore();
    }
    
    public void loadFromFirebase() {
        loadAndUpdateScore();
    }
    
    private void loadAndUpdateScore() {
        if (poiId != null) {
            android.util.Log.d("POI_SCORE", "Making Firebase query for document: " + poiId);
            db.collection("pois").document(poiId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> data = documentSnapshot.getData();
                        // Log specific timestamp objects with more detail
                        android.util.Log.d("POI_SCORE", "=== DETAILED TIMESTAMP INSPECTION ===");
                        Object captureTimeRaw = data.get("captureTime");
                        Object lastUpdatedRaw = data.get("lastUpdated");
                        
                        
                        if (data != null) {
                            int baseScore = 100; // default base score from Firebase
                            long lastUpdatedTime = System.currentTimeMillis() - 60000; // Assume 1 minute ago if no timestamp
                            
                            // Get base score from Firebase (NEVER display this directly)
                            if (data.containsKey("currentScore")) {
                                Object scoreObj = data.get("currentScore");
                                if (scoreObj instanceof Number) {
                                    baseScore = ((Number) scoreObj).intValue();
                                }
                            } else {
                                android.util.Log.w("POI_SCORE", "❌ No currentScore field found in Firebase data. Available fields: " + data.keySet());
                            }
                            
                            // Get owning team from Firebase
                            int owningTeam = 0; // Default to neutral
                            if (data.containsKey("ownerTeamId")) {
                                Object ownerObj = data.get("ownerTeamId");
                                if (ownerObj instanceof String) {
                                    String ownerTeamId = (String) ownerObj;
                                    if (ownerTeamId != null && !ownerTeamId.equals("null")) {
                                        try {
                                            owningTeam = Integer.parseInt(ownerTeamId.replace("team_", ""));
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
                            mOwningTeam.setValue(owningTeam);
                            

                            
                             // Get timestamps from Firebase - prioritize captureTime over lastUpdated for accuracy
                            boolean foundUpdateTime = false;
                            long captureTimeMs = 0;
                            long lastUpdatedMs = 0;
                            
                            // First, get captureTime
                            if (data.containsKey("captureTime")) {
                                Object captureTimeObj = data.get("captureTime");
                                
                                if (captureTimeObj instanceof Date) {
                                    captureTimeMs = ((Date) captureTimeObj).getTime();
                                } else if (captureTimeObj != null && captureTimeObj.getClass().getSimpleName().equals("Timestamp")) {
                                    try {
                                        java.lang.reflect.Method getSecondsMethod = captureTimeObj.getClass().getMethod("getSeconds");
                                        java.lang.reflect.Method getNanosecondsMethod = captureTimeObj.getClass().getMethod("getNanoseconds");
                                        
                                        long seconds = ((Long) getSecondsMethod.invoke(captureTimeObj));
                                        int nanos = ((Integer) getNanosecondsMethod.invoke(captureTimeObj));
                                        
                                        captureTimeMs = seconds * 1000L + nanos / 1000000L;
                                    } catch (Exception e) {
                                        android.util.Log.e("POI_SCORE", "Error converting captureTime Timestamp", e);
                                        // Try alternative method - use toDate() if available
                                        try {
                                            java.lang.reflect.Method toDateMethod = captureTimeObj.getClass().getMethod("toDate");
                                            java.util.Date date = (java.util.Date) toDateMethod.invoke(captureTimeObj);
                                            captureTimeMs = date.getTime();
                                        } catch (Exception e2) {
                                            android.util.Log.e("POI_SCORE", "Error with toDate() method too", e2);
                                        }
                                    }
                                }
                            }
                            
                            // Then, get lastUpdated
                            if (data.containsKey("lastUpdated")) {
                                Object lastUpdatedObj = data.get("lastUpdated");
                                if (lastUpdatedObj instanceof Date) {
                                    lastUpdatedMs = ((Date) lastUpdatedObj).getTime();
                                } else if (lastUpdatedObj != null && lastUpdatedObj.getClass().getSimpleName().equals("Timestamp")) {
                                    try {
                                        java.lang.reflect.Method getSecondsMethod = lastUpdatedObj.getClass().getMethod("getSeconds");
                                        java.lang.reflect.Method getNanosecondsMethod = lastUpdatedObj.getClass().getMethod("getNanoseconds");
                                        
                                        long seconds = ((Long) getSecondsMethod.invoke(lastUpdatedObj));
                                        int nanos = ((Integer) getNanosecondsMethod.invoke(lastUpdatedObj));
                                        
                                        lastUpdatedMs = seconds * 1000L + nanos / 1000000L;
                                        
                                    } catch (Exception e) {
                                        // Try alternative method - use toDate() if available
                                        try {
                                            java.lang.reflect.Method toDateMethod = lastUpdatedObj.getClass().getMethod("toDate");
                                            java.util.Date date = (java.util.Date) toDateMethod.invoke(lastUpdatedObj);
                                            lastUpdatedMs = date.getTime();
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
                            
                            // FORCE: Always use captureTime if it exists (it's the correct one from our script)
                            if (captureTimeMs > 0) {
                                lastUpdatedTime = captureTimeMs;
                            } else if (lastUpdatedMs > 0) {
                                lastUpdatedTime = lastUpdatedMs;
                                // Fallback to 24 hours ago
                                lastUpdatedTime = currentTimeForComparison - (24 * 60 * 60 * 1000);
                            }
                            
                            if (lastUpdatedTime > 0) {
                                long timeSinceUpdate = currentTimeForComparison - lastUpdatedTime;
                                foundUpdateTime = true;
                            }
                            
                            // If we have a recent local capture time, use it instead
                            if (lastCaptureTime > 0 && (System.currentTimeMillis() - lastCaptureTime) < CAPTURE_GRACE_PERIOD_MILLIS) {
                                lastUpdatedTime = lastCaptureTime;
                            }
                            
                             // Calculate time-based decrement from capture timestamp
                            long currentTime = System.currentTimeMillis();
                            long timeElapsed = currentTime - lastUpdatedTime;
                            
// Check if we're in grace period after capture
                            boolean inGracePeriod = wasRecentlyCaptured();
                            
                            // Check if freeze bonus is active (both local and Firebase)
                            boolean freezeBonusActive = (activeFreezeBonus != null && activeFreezeBonus.isActive() && !activeFreezeBonus.isExpired());
                            
                            // Check if POI has freeze bonus from Firebase
                            java.util.Date freezeBonusUntil = null;
                            if (data.containsKey("freezeBonusUntil")) {
                                Object freezeBonusObj = data.get("freezeBonusUntil");
                                if (freezeBonusObj instanceof Date) {
                                    freezeBonusUntil = (Date) freezeBonusObj;
                                } else if (freezeBonusObj != null && freezeBonusObj.getClass().getSimpleName().equals("Timestamp")) {
                                    try {
                                        java.lang.reflect.Method toDateMethod = freezeBonusObj.getClass().getMethod("toDate");
                                        freezeBonusUntil = (java.util.Date) toDateMethod.invoke(freezeBonusObj);
                                    } catch (Exception e) {
                                        android.util.Log.e("POI_SCORE", "Error converting freezeBonusUntil Timestamp", e);
                                    }
                                }
                            }
                            boolean firebaseFreezeBonusActive = (freezeBonusUntil != null && freezeBonusUntil.after(new java.util.Date()));
                            
                            // Check if POI is frozen by timestamp
                            java.util.Date freezeUntil = null;
                            if (data.containsKey("freezeUntil")) {
                                Object freezeObj = data.get("freezeUntil");
                                if (freezeObj instanceof Date) {
                                    freezeUntil = (Date) freezeObj;
                                } else if (freezeObj != null && freezeObj.getClass().getSimpleName().equals("Timestamp")) {
                                    try {
                                        java.lang.reflect.Method toDateMethod = freezeObj.getClass().getMethod("toDate");
                                        freezeUntil = (java.util.Date) toDateMethod.invoke(freezeObj);
                                    } catch (Exception e) {
                                        android.util.Log.e("POI_SCORE", "Error converting freezeUntil Timestamp", e);
                                    }
                                }
                            }
                            boolean timestampFreezeActive = (freezeUntil != null && freezeUntil.after(new java.util.Date()));
                             
                             // ALWAYS calculate dynamic score - NEVER use Firebase score directly
                             int decrementedAmount = 0;
                             int dynamicScore = baseScore;
                             
                             if (!inGracePeriod && !freezeBonusActive && !firebaseFreezeBonusActive && !timestampFreezeActive) {
                                 decrementedAmount = (int) (timeElapsed / DECREMENT_RATE_MILLIS);
                                 dynamicScore = Math.max(MIN_SCORE, baseScore - decrementedAmount);
                                 
} else {
                                  if (freezeBonusActive) {
                                      android.util.Log.d("POI_SCORE", "LOCAL FREEZE BONUS ACTIVE: No decrement applied");
                                  } else if (firebaseFreezeBonusActive) {
                                      android.util.Log.d("POI_SCORE", "FIREBASE FREEZE BONUS ACTIVE: No decrement applied until " + freezeBonusUntil);
                                  } else if (timestampFreezeActive) {
                                      android.util.Log.d("POI_SCORE", "TIMESTAMP FREEZE ACTIVE: No decrement applied until " + freezeUntil);
                                  } else {
                                      android.util.Log.d("POI_SCORE", "IN GRACE PERIOD: No decrement applied, using base score");
                                  }
                              }
                            
// ALWAYS update local score with calculated dynamic score
                             // NEVER display raw Firebase score
                             mCurrentScore.setValue(dynamicScore);
                             
                             // Special logging for Capitole-related POIs
                             if (poiName != null && (poiName.toLowerCase().contains("capitole") || 
                                 poiName.toLowerCase().contains("théâtre") || 
                                 poiName.toLowerCase().contains("place"))) {
                                 android.util.Log.i("CAPITOLE_DEBUG", "🏛️ " + poiName + " - Score: " + dynamicScore + 
                                     " | Base: " + baseScore + " | Decremented: " + decrementedAmount + 
                                     " | Time elapsed: " + (timeElapsed / 1000) + "s" +
                                     " | Frozen: " + timestampFreezeActive);
                             }
                             
                             // Reset justCaptured flag after processing
                             justCaptured = false;
                            
                        }
                    } else {
                        android.util.Log.w("POI_SCORE", "❌ Document does not exist for POI: " + poiName);
                        android.util.Log.d("POI_SCORE", "Setting default dynamic score: 100");
                        mCurrentScore.setValue(100); // Default dynamic score
                    }
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
            
            // Also update freeze bonus if active
            if (activeFreezeBonus != null && activeFreezeBonus.isActive() && !activeFreezeBonus.isExpired()) {
                long freezeEndTime = System.currentTimeMillis() + activeFreezeBonus.getRemainingTime();
                updates.put("freezeBonusUntil", new java.util.Date(freezeEndTime));
            }
            
            db.collection("pois").document(poiId)
                .update(updates);
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

public void addMoneyBonus(int amount, android.content.Context context) {
        Integer currentMoney = mMoneyScore.getValue();
        if (currentMoney != null) {
            int newMoney = currentMoney + amount;
            mMoneyScore.setValue(newMoney);
            
            // Save to Firebase
            saveUserMoneyToFirebase(newMoney, context);
            
        }
    }
    
    public void addMoneyPenalty(int amount, android.content.Context context) {
        Integer currentMoney = mMoneyScore.getValue();
        if (currentMoney != null) {
            int newMoney = Math.max(0, currentMoney - amount); // Empêtrer de passer en négatif
            mMoneyScore.setValue(newMoney);
            
            // Save to Firebase
            saveUserMoneyToFirebase(newMoney, context);
            
        }
    }
    
    public void loadUserMoneyFromFirebase(android.content.Context context) {
        // Get current user ID from SharedPreferences        
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
    
    public void saveUserMoneyToFirebase(int money, android.content.Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("VibingPrefs", android.content.Context.MODE_PRIVATE);
            String userId = prefs.getString("user_id", null);
            
            if (userId != null) {
                db.collection("users").document(userId)
                    .update("money", money);
            } else {
                android.util.Log.w("POI_SCORE", "No user ID found, cannot save money to Firebase");
            }
        } catch (Exception e) {
            android.util.Log.e("POI_SCORE", "Exception saving user money", e);
        }
    }
    
    public void setMoney(int money) {
        mMoneyScore.setValue(money);
    }
    
    public void setScore(int newScore) {
        mCurrentScore.setValue(newScore);
        // No automatic Firebase update - score is calculated on-the-fly
    }
    
public void captureZone(int newScore, String teamId) {
        
        // Capture grace period to prevent multiple captures in quick succession
        if (wasRecentlyCaptured()) {
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
        // Set justCaptured flag and last capture time
        justCaptured = true;
        lastCaptureTime = System.currentTimeMillis();
        
        // Update in Firebase with full team string
        updateZoneInFirebase(newScore, teamId);
        
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
                            
                            mCurrentScore.setValue(dynamicScore);
                        }
                    }
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

    // Bonus management methods
    public void activateFreezeBonus() {
        if (activeFreezeBonus == null) {
            activeFreezeBonus = new Bonus(com.example.vibing.models.BonusType.FREEZE_SCORE);
            activeFreezeBonus.activate();
            
            // Synchronize immediately with Firebase
            syncFreezeBonusToFirebase();
        }
    }
    
    private void syncFreezeBonusToFirebase() {
        if (poiId != null && activeFreezeBonus != null && activeFreezeBonus.isActive() && !activeFreezeBonus.isExpired()) {
            long freezeEndTime = System.currentTimeMillis() + activeFreezeBonus.getRemainingTime();
            Map<String, Object> updates = new HashMap<>();
            updates.put("freezeBonusUntil", new java.util.Date(freezeEndTime));
            
            db.collection("pois").document(poiId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    android.util.Log.d("POI_SCORE", "Freeze bonus synchronized to Firebase until: " + new java.util.Date(freezeEndTime));
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("POI_SCORE", "Failed to sync freeze bonus to Firebase", e);
                });
        }
    }
    
    public boolean isFreezeBonusActive() {
        return activeFreezeBonus != null && activeFreezeBonus.isActive() && !activeFreezeBonus.isExpired();
    }
    
    public void deactivateFreezeBonus() {
        if (activeFreezeBonus != null) {
            activeFreezeBonus.deactivate();
            activeFreezeBonus = null;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // No cleanup needed since we don't have ongoing listeners
    }
}