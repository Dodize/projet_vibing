package com.example.vibing.ui.tutorial;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.vibing.R;
import com.example.vibing.models.User;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.List;
import java.util.Random;

public class TutorialDialog extends Dialog {

    public interface TutorialDialogListener {
        void onTutorialStarted();
        void onTutorialFinished();
        void onTutorialClosed();
        void onZoomToPoi(Marker poiMarker);
        void onHighlightMoneySection();
        void onClearHighlights();
    }

    private static final String PREFS_NAME = "tutorial_prefs";
    private static final String KEY_TUTORIAL_COMPLETED = "tutorial_completed";
    private static final String KEY_CURRENT_STEP = "current_step";

    private final Context context;
    private final TutorialDialogListener listener;
    private final FragmentManager fragmentManager;
    
    private TextView titleTextView;
    private TextView contentTextView;
    private TextView stepIndicatorTextView;
    private Button previousButton;
    private Button nextButton;
    private ImageView closeButton;
    private View tutorialOverlay;
    private View teamColorContainer;
    private View teamColorCircle;
    
    private int currentStep = 0;
    private static final int TOTAL_STEPS = 6;
    
    private MapView mapView;
    private List<Marker> poiMarkers;
    private Random random = new Random();
    private FirebaseFirestore db;

    public TutorialDialog(@NonNull Context context, TutorialDialogListener listener) {
        this(context, listener, null);
    }

    public TutorialDialog(@NonNull Context context, TutorialDialogListener listener, FragmentManager fragmentManager) {
        super(context, R.style.TutorialDialogTheme);
        this.context = context;
        this.listener = listener;
        this.fragmentManager = fragmentManager;
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_tutorial);
        
        setCancelable(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        
        initializeViews();
        setupClickListeners();
        loadTutorialState();
        
        // Preload team info if starting on step 2
        if (currentStep == 1) {
            preloadTeamInfo();
        }
        
        updateStepContent();
    }

    private void initializeViews() {
        titleTextView = findViewById(R.id.tutorial_title);
        contentTextView = findViewById(R.id.tutorial_content);
        stepIndicatorTextView = findViewById(R.id.step_indicator);
        previousButton = findViewById(R.id.previous_button);
        nextButton = findViewById(R.id.next_button);
        closeButton = findViewById(R.id.close_button);
        tutorialOverlay = findViewById(R.id.tutorial_overlay);
        teamColorContainer = findViewById(R.id.team_color_container);
        teamColorCircle = findViewById(R.id.team_color_circle);
    }

    private void setupClickListeners() {
        previousButton.setOnClickListener(v -> showPreviousStep());
        nextButton.setOnClickListener(v -> showNextStep());
        closeButton.setOnClickListener(v -> closeTutorial());
        
        // Allow clicking outside to close
        tutorialOverlay.setOnClickListener(v -> closeTutorial());
    }

    private void loadTutorialState() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        currentStep = prefs.getInt(KEY_CURRENT_STEP, 0);
    }

    private void saveTutorialState() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_CURRENT_STEP, currentStep);
        editor.apply();
    }

    public void showNextStep() {
        if (currentStep < TOTAL_STEPS - 1) {
            currentStep++;
            updateStepContent();
            saveTutorialState();
            
            // Execute step-specific actions
            executeStepActions(currentStep);
        } else {
            // Tutorial finished
            completeTutorial();
        }
    }

    public void showPreviousStep() {
        if (currentStep > 0) {
            currentStep--;
            updateStepContent();
            saveTutorialState();
            
            // Execute step-specific actions
            executeStepActions(currentStep);
        }
    }

    private void updateStepContent() {
        String[] titles = getStepTitles();
        String[] contents = getStepContents();
        
        titleTextView.setText(titles[currentStep]);
        contentTextView.setText(contents[currentStep]);
        stepIndicatorTextView.setText((currentStep + 1) + " / " + TOTAL_STEPS);
        
        // Show/hide team color circle based on step
        if (currentStep == 1) { // Step 2
            teamColorContainer.setVisibility(View.VISIBLE);
        } else {
            teamColorContainer.setVisibility(View.GONE);
        }
        
        // Update button states
        previousButton.setEnabled(currentStep > 0);
        
        if (currentStep == TOTAL_STEPS - 1) {
            nextButton.setText(R.string.tutorial_finish);
        } else {
            nextButton.setText(R.string.tutorial_next);
        }
    }

    private void executeStepActions(int step) {
        // Clear any previous highlights
        if (listener != null) {
            listener.onClearHighlights();
        }
        
        switch (step) {
            case 0: // Step 1: Welcome
                // No special actions needed
                break;
                
            case 1: // Step 2: Team and POI demonstration
                // Update content with user's team information
                updateStep2ContentWithTeamInfo();
                break;
                
            case 2: // Step 3: Capture mechanics
                // No special actions needed
                break;
                
            case 3: // Step 4: Photo validation
                // No special actions needed
                break;
                
            case 4: // Step 5: Money system
                if (listener != null) {
                    listener.onHighlightMoneySection();
                }
                break;
                
            case 5: // Step 6: Conclusion
                // No special actions needed
                break;
        }
    }
    
    private void updateStep2ContentWithTeamInfo() {
        if (context != null) {
            // Utiliser le même nom de SharedPreferences que HomeFragment
            SharedPreferences prefs = context.getSharedPreferences("VibingPrefs", Context.MODE_PRIVATE);
            String userName = prefs.getString("username", "Joueur");
            String teamId = prefs.getString("team_id", "");
            
            // Always fetch fresh data for step 2
            fetchTeamInfoFromFirebase(teamId, userName);
        }
    }

    private void completeTutorial() {
        // Mark tutorial as completed
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_TUTORIAL_COMPLETED, true);
        editor.apply();
        
        // Clear highlights
        if (listener != null) {
            listener.onClearHighlights();
        }
        
        // Notify listener
        if (listener != null) {
            listener.onTutorialFinished();
        }
        
        dismiss();
    }

    private void closeTutorial() {
        // Save current step for later resumption
        saveTutorialState();
        
        // Clear highlights
        if (listener != null) {
            listener.onClearHighlights();
        }
        
        // Notify listener
        if (listener != null) {
            listener.onTutorialClosed();
        }
        
        dismiss();
    }

    public static boolean isTutorialCompleted(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_TUTORIAL_COMPLETED, false);
    }

    public static void resetTutorial(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_TUTORIAL_COMPLETED);
        editor.remove(KEY_CURRENT_STEP);
        editor.apply();
    }

    // Getters for testing
    public int getCurrentStep() {
        return currentStep;
    }

    public int getTotalSteps() {
        return TOTAL_STEPS;
    }

    public void setCurrentStep(int step) {
        if (step >= 0 && step < TOTAL_STEPS) {
            currentStep = step;
            updateStepContent();
        }
    }

    public String[] getStepTitles() {
        return new String[]{
            context.getString(R.string.tutorial_step1_title),
            context.getString(R.string.tutorial_step2_title),
            context.getString(R.string.tutorial_step3_title),
            context.getString(R.string.tutorial_step4_title),
            context.getString(R.string.tutorial_step5_title),
            context.getString(R.string.tutorial_step6_title)
        };
    }

    public String[] getStepContents() {
        return new String[]{
            context.getString(R.string.tutorial_step1_content),
            context.getString(R.string.tutorial_step2_content),
            context.getString(R.string.tutorial_step3_content),
            context.getString(R.string.tutorial_step4_content),
            context.getString(R.string.tutorial_step5_content),
            context.getString(R.string.tutorial_step6_content)
        };
    }

    // Feature flags for step-specific functionality
    public boolean shouldShowMapZoom() {
        return currentStep == 1; // Step 2
    }

    public boolean shouldHighlightMoneySection() {
        return currentStep == 4; // Step 5
    }

    // Button state getters for testing
    public boolean isPreviousButtonEnabled() {
        return previousButton.isEnabled();
    }

    public boolean isNextButtonEnabled() {
        return nextButton.isEnabled();
    }

    public String getNextButtonText() {
        return nextButton.getText().toString();
    }

    // Setters for external dependencies
    public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }

    public void setPoiMarkers(List<Marker> poiMarkers) {
        this.poiMarkers = poiMarkers;
    }

    @Override
    public void show() {
        super.show();
        if (listener != null) {
            listener.onTutorialStarted();
        }
    }
    
    /**
     * Preload team info from Firebase (called when starting on step 2)
     */
    private void preloadTeamInfo() {
        SharedPreferences prefs = context.getSharedPreferences("VibingPrefs", Context.MODE_PRIVATE);
        String userName = prefs.getString("username", "Joueur");
        String teamId = prefs.getString("team_id", "");
        
        // Force refresh from Firebase even if cached
        fetchTeamInfoFromFirebase(teamId, userName, true);
    }
    
    /**
     * Fetch team info from Firebase and update the tutorial content
     */
    private void fetchTeamInfoFromFirebase(String teamId, String userName) {
        fetchTeamInfoFromFirebase(teamId, userName, false);
    }
    
    /**
     * Fetch team info from Firebase and update the tutorial content
     */
    private void fetchTeamInfoFromFirebase(String teamId, String userName, boolean forceRefresh) {
        if (teamId == null || teamId.isEmpty()) {
            // Use default values if no team ID
            showDefaultTeamInfo(userName);
            return;
        }
        
        SharedPreferences prefs = context.getSharedPreferences("VibingPrefs", Context.MODE_PRIVATE);
        
        // Check if we have cached info and are not forcing refresh
        if (!forceRefresh) {
            String cachedTeamName = prefs.getString("team_name", null);
            String cachedTeamColor = prefs.getString("team_color", null);
            String cachedTeamColorHex = prefs.getString("team_color_hex", null);
            
            if (cachedTeamName != null && cachedTeamColor != null) {
                // Use cached info immediately
                updateStep2ContentWithFetchedInfo(userName, cachedTeamName, cachedTeamColor, cachedTeamColorHex);
                return;
            }
        }
        
        // Fetch from Firebase
        db.collection("teams")
                .document(teamId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String teamName = documentSnapshot.getString("name");
                        String teamColorHex = documentSnapshot.getString("colorHex");
                        String teamColor = documentSnapshot.getString("color");
                        
                        // Cache the team info
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("team_name", teamName);
                        editor.putString("team_color", teamColor);
                        editor.putString("team_color_hex", teamColorHex);
                        editor.apply();
                        
                        // Update the tutorial content with fetched info
                        updateStep2ContentWithFetchedInfo(userName, teamName, teamColor, teamColorHex);
                    } else {
                        // Team not found, use default values
                        showDefaultTeamInfo(userName);
                    }
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("TutorialDialog", "Error fetching team info", e);
                    // Use default values on error
                    showDefaultTeamInfo(userName);
                });
    }
    
    /**
     * Update step 2 content with fetched team information
     */
    private void updateStep2ContentWithFetchedInfo(String userName, String teamName, String teamColor, String teamColorHex) {
        if (contentTextView == null) return;
        
        // Check if we're still on step 2 before updating
        if (currentStep != 1) return;
        
        // Update content with team name
        try {
            String step2Content = context.getString(R.string.tutorial_step2_content, teamName != null ? teamName : "Conquérants");
            contentTextView.setText(step2Content);
        } catch (Exception e) {
            // Fallback if string formatting fails
            contentTextView.setText("Vous faites partie de l'équipe " + (teamName != null ? teamName : "Conquérants") + ".\n\nLes zones sur la carte sont représentées par des points de couleur selon l'équipe qui les contrôle.");
        }
        
        // Set the circle color
        if (teamColorHex != null && !teamColorHex.isEmpty()) {
            try {
                int color = android.graphics.Color.parseColor(teamColorHex);
                teamColorCircle.setBackgroundColor(color);
            } catch (Exception e) {
                // Fallback to default color mapping if hex parsing fails
                teamColorCircle.setBackgroundColor(getDefaultTeamColor(teamColor));
            }
        } else {
            // Fallback to default color mapping
            teamColorCircle.setBackgroundColor(getDefaultTeamColor(teamColor));
        }
    }
    
    /**
     * Show default team info when Firebase fetch fails
     */
    private void showDefaultTeamInfo(String userName) {
        String defaultTeamName = "Conquérants";
        String defaultTeamColor = "Rouge";
        String defaultTeamColorHex = null;
        
        // Check if we're still on step 2 before updating
        if (currentStep != 1) return;
        
        // Update content with default team name
        try {
            String step2Content = context.getString(R.string.tutorial_step2_content, defaultTeamName);
            contentTextView.setText(step2Content);
        } catch (Exception e) {
            // Fallback if string formatting fails
            contentTextView.setText("Vous faites partie de l'équipe Conquérants.\n\nLes zones sur la carte sont représentées par des points de couleur selon l'équipe qui les contrôle.");
        }
        
        // Set default circle color
        teamColorCircle.setBackgroundColor(getDefaultTeamColor(defaultTeamColor));
    }
    
    /**
     * Get default team color based on color name
     */
    private int getDefaultTeamColor(String colorName) {
        if (colorName == null) return context.getResources().getColor(R.color.primary_blue, null);
        
        switch (colorName.toLowerCase()) {
            case "rouge":
            case "red":
                return context.getResources().getColor(android.R.color.holo_red_dark, null);
            case "bleu":
            case "blue":
                return context.getResources().getColor(android.R.color.holo_blue_dark, null);
            case "vert":
            case "green":
                return context.getResources().getColor(android.R.color.holo_green_dark, null);
            case "orange":
                return context.getResources().getColor(android.R.color.holo_orange_dark, null);
            case "violet":
            case "purple":
                return context.getResources().getColor(android.R.color.holo_purple, null);
            default:
                return context.getResources().getColor(R.color.primary_blue, null);
        }
    }
}