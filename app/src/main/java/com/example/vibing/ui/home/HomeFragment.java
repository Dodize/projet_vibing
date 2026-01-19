package com.example.vibing.ui.home;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vibing.R; // Import R for resources
import com.example.vibing.databinding.FragmentHomeBinding;
import com.example.vibing.models.Poi;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Marker.OnMarkerClickListener;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import android.view.MotionEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import android.os.Handler;
import android.os.Looper;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.XYTileSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import android.os.Handler;
import android.os.Looper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.content.Intent;
import java.util.Map;
import java.util.HashMap;

public class HomeFragment extends Fragment implements OnMarkerClickListener {

    private FragmentHomeBinding binding;
    private MapView mapView;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Marker userMarker;
    private static final int REQUEST_PERMISSIONS_PEDOMETER = 1001;
    private static final int REQUEST_PERMISSIONS_LOCATION = 1002;
    private RecyclerView poiRecyclerView;
    private PoiListAdapter poiListAdapter;
    private List<PoiItem> poiList;
    private GeoPoint currentUserLocation;
    private Random random = new Random();
    private TextView currentInfoBubble;
    private Marker currentSelectedMarker;
    private HomeViewModel homeViewModel;
    private List<Map<String, String>> visitedPois;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private Sensor stepDetectorSensor;
    private SensorEventListener stepListener;
    private int initialSteps;
    private int currentSteps;
    private boolean isStepCounterAvailable;
    
    // Step tracking for money calculation
    private int totalStepsSinceStart;
    private int lastMoneyStepCount;
    private static final int STEPS_PER_EURO = 20;
    
    // Cache for team colors loaded from Firebase
    private Map<Integer, Integer> teamColorsCache = new HashMap<>();
    private Map<Integer, Integer> teamBrightColorsCache = new HashMap<>();
    
    // Variables pour le suivi des zones
    private List<String> previouslyEnteredPoiNames = new ArrayList<>();
    private boolean hasShownPopupForCurrentLocation = false;
    
    // Easter egg: Admin access with 7 taps
    private static final int ADMIN_TAP_COUNT = 7;
    private static final long ADMIN_TAP_TIMEOUT_MS = 3000; // 3 seconds
    private int adminTapCount = 0;
    private long adminFirstTapTime = 0;
    private Handler adminTapHandler;
    
    // Method to detect if location is the emulator default (Mountain View, CA)
    private boolean isEmulatorDefaultLocation(GeoPoint location) {
        if (location == null) return false;
        
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        
        // Check if location matches Mountain View, CA (Google's default emulator location)
        // Allow small tolerance for floating point precision
        return Math.abs(lat - 37.4219983) < 0.001 && Math.abs(lon - (-122.084)) < 0.001;
    }
    


    // --- POI Model ---
    private static class PoiItem {
        String name;
        String id; // Firebase document ID
        double distance;
        double latitude;
        double longitude;
        int score;
         int owningTeam; // 0 = neutral, 1-4 = teams
        double radius; // Rayon de la zone cliquable en mètres
        Marker marker;

        PoiItem(String name, String id, double latitude, double longitude, int score, int owningTeam) {
            this.name = name;
            this.id = id;
            this.latitude = latitude;
            this.longitude = longitude;
            this.score = score;
            this.owningTeam = owningTeam;
            this.radius = 100.0; // Rayon par défaut de 100m
            this.distance = 0; // Will be calculated
        }
        
        PoiItem(String name, double latitude, double longitude, int score, int owningTeam, double radius) {
            this.name = name;
            this.id = null; // No ID available in this constructor
            this.latitude = latitude;
            this.longitude = longitude;
            this.score = score;
            this.owningTeam = owningTeam;
            this.radius = radius;
            this.distance = 0; // Will be calculated
        }
    }

    // --- Simple POI Adapter ---
    private class PoiListAdapter extends RecyclerView.Adapter<PoiListAdapter.PoiViewHolder> {
        private final List<PoiItem> poiList;

        public PoiListAdapter(List<PoiItem> poiList) {
            this.poiList = poiList;
        }

        @NonNull
        @Override
        public PoiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Assuming R.layout.list_item_poi exists
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_poi, parent, false); 
            return new PoiViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PoiViewHolder holder, int position) {
            PoiItem poi = poiList.get(position);
            
            holder.poiNameTextView.setText(poi.name);
            // Format distance for display
            String distanceText = String.format("%.2f km", poi.distance);
            holder.poiDistanceTextView.setText(distanceText);
            
            // Check if POI was visited today and update appearance
            android.util.Log.d("POI_VISIT_DEBUG", "LIST Checking POI: " + poi.name + " (ID: " + poi.id + ")");
            boolean isVisitedToday = isPoiVisitedToday(poi.id);
            android.util.Log.d("POI_VISIT_DEBUG", "LIST Result for " + poi.name + ": visitedToday=" + isVisitedToday);
            
            if (isVisitedToday) {
                holder.poiNameTextView.setTextColor(0xFF808080); // Gray text
                holder.poiDistanceTextView.setTextColor(0xFF808080); // Gray text
                holder.itemView.setAlpha(0.6f); // Semi-transparent
            } else {
                // Use proper theme colors for better visibility
                holder.poiNameTextView.setTextColor(getResources().getColor(R.color.text_dark, null));
                holder.poiDistanceTextView.setTextColor(getResources().getColor(R.color.text_secondary, null));
                holder.itemView.setAlpha(1.0f); // Fully opaque
            }
            
            // Set click listener
            holder.itemView.setOnClickListener(v -> {
                if (isVisitedToday) {
                    Toast.makeText(v.getContext(), "Vous avez déjà visité " + poi.name + " aujourd'hui. Revenez demain !", Toast.LENGTH_LONG).show();
                    return;
                }
                
                if (isUserInPoiZone(poi)) {
                    navigateToPoiScore(poi);
                } else {
                    String distanceInMeters = String.format("%.0f", poi.distance * 1000); // Convert km to m
                    Toast.makeText(v.getContext(), "Vous devez être à " + distanceInMeters + "m de " + poi.name + " pour lancer le quiz", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            int size = poiList.size();
            return size;
        }
        


        public class PoiViewHolder extends RecyclerView.ViewHolder {
            TextView poiNameTextView;
            TextView poiDistanceTextView;

            public PoiViewHolder(@NonNull View itemView) {
                super(itemView);
                // Assuming these IDs exist in list_item_poi.xml
                poiNameTextView = itemView.findViewById(R.id.poiNameTextView);
                poiDistanceTextView = itemView.findViewById(R.id.poiDistanceTextView);
                

            }
        }
    }
     // --- End Simple POI Adapter ---

    // Flag to track if visited POIs have been loaded
    private boolean visitedPoisLoaded = false;

    private void initializePOIsFromFirebase() {
        homeViewModel.getPois().observe(getViewLifecycleOwner(), pois -> {
            if (pois != null) {
                poiList = new ArrayList<>();
                
                // Convert Firebase POIs to local PoiItems
                for (Poi poi : pois) {
                    PoiItem poiItem = new PoiItem(poi.getName(), poi.getId(), poi.getLatitude(), poi.getLongitude(), poi.getScore(), poi.getOwningTeam());
                    poiList.add(poiItem);
                }
                
                // Clear existing markers from map
                if (mapView != null) {
                    mapView.getOverlays().clear();
                    // Re-add user marker if it exists
                    if (userMarker != null) {
                        mapView.getOverlays().add(userMarker);
                    }
                }
                
                // Add markers to map
                for (PoiItem poi : poiList) {
                    Marker poiMarker = new Marker(mapView);
                    GeoPoint poiLocation = new GeoPoint(poi.latitude, poi.longitude);
                    poiMarker.setPosition(poiLocation);
                    poiMarker.setTitle(poi.name);
                    poiMarker.setSnippet("Score: " + poi.score + " | Équipe: " + getTeamName(poi.owningTeam));
                    
                    // Check if user is in zone (don't gray out visited POIs on map)
                    boolean isInZone = isUserInPoiZone(poi);
                    poiMarker.setIcon(getTeamIcon(poi.owningTeam, isInZone, false));
                    
                    poiMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
                    poiMarker.setOnMarkerClickListener(this);
                    // Increase touch area for easier interaction on mobile
                    poiMarker.setInfoWindow(null); // Disable default info window
                    poiMarker.setPanToView(false); // Don't auto-pan on click
                    
                    poi.marker = poiMarker;
                    mapView.getOverlays().add(poiMarker);
                }
                
                updatePoiDistancesAndList();
                
                // Center map on user location if available, otherwise Toulouse as fallback
                if (currentUserLocation != null) {
                    mapView.getController().setCenter(currentUserLocation);
                    mapView.getController().setZoom(15.0);
                } else {
                    mapView.getController().setCenter(new GeoPoint(43.6047, 1.4442));
                    mapView.getController().setZoom(13.0);
                }
                
                // Force map refresh
                mapView.invalidate();
                
                // Check for POI zones after POIs are loaded, but only if visited POIs are loaded
                checkForPoiZonesWhenReady();
            }
        });
    }

    private void checkForPoiZonesWhenReady() {
        android.util.Log.d("POI_SYNC_DEBUG", "checkForPoiZonesWhenReady called - visitedPoisLoaded: " + visitedPoisLoaded);
        
        if (visitedPoisLoaded) {
            // Visited POIs are loaded, we can check for zones immediately
            checkForNewlyEnteredPoiZones();
        } else {
            // Visited POIs not loaded yet, wait and retry
            android.util.Log.d("POI_SYNC_DEBUG", "Visited POIs not loaded yet, waiting...");
            
            // Wait for visited POIs to be loaded (with timeout)
            waitForVisitedPoisAndCheckZones();
        }
    }

    private void waitForVisitedPoisAndCheckZones() {
        // Check every 500ms for up to 5 seconds
        final int maxRetries = 10;
        final long retryDelay = 500;
        
        final Handler handler = new Handler(Looper.getMainLooper());
        final Runnable[] runnable = new Runnable[1];
        final int[] retryCount = {0};
        
        runnable[0] = new Runnable() {
            @Override
            public void run() {
                retryCount[0]++;
                android.util.Log.d("POI_SYNC_DEBUG", "Checking visited POIs status, attempt " + retryCount[0] + "/10");
                
                if (visitedPoisLoaded || visitedPois != null) {
                    android.util.Log.d("POI_SYNC_DEBUG", "Visited POIs now loaded, checking zones");
                    checkForNewlyEnteredPoiZones();
                } else if (retryCount[0] < maxRetries) {
                    android.util.Log.d("POI_SYNC_DEBUG", "Still waiting for visited POIs, retrying in 500ms");
                    handler.postDelayed(this, retryDelay);
                } else {
                    android.util.Log.w("POI_SYNC_DEBUG", "Timeout waiting for visited POIs, proceeding anyway");
                    checkForNewlyEnteredPoiZones();
                }
            }
        };
        
        handler.postDelayed(runnable[0], retryDelay);
    }
    
    private String getTeamName(int teamId) {
        switch (teamId) {
            case 0: return "Neutre";
            case 1: return "Les Conquérants";
            case 2: return "Les Explorateurs";
            case 3: return "Les Stratèges";
            case 4: return "Les Gardiens";
            default:
                return "Neutre";
        }
    }
    
    private void initializeTeamColorsCache() {
        // Initialize with default values
        teamColorsCache.put(0, 0xFF808080); // Gray for neutral
        teamColorsCache.put(1, 0xFFFF0000); // Red
        teamColorsCache.put(2, 0xFF0000FF); // Blue
        teamColorsCache.put(3, 0xFF00FF00); // Green
        teamColorsCache.put(4, 0xFFFFFF00); // Yellow
        teamColorsCache.put(5, 0xFF800080); // Purple
        
        teamBrightColorsCache.put(0, 0xFFB0B0B0); // Brighter gray for neutral
        teamBrightColorsCache.put(1, 0xFFFF4444); // Brighter red
        teamBrightColorsCache.put(2, 0xFF4444FF); // Brighter blue
        teamBrightColorsCache.put(3, 0xFF44FF44); // Brighter green
        teamBrightColorsCache.put(4, 0xFFFFFF44); // Brighter yellow
        teamBrightColorsCache.put(5, 0xFFAA44AA); // Brighter purple
        
        // Load real team colors from Firebase and update cache
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("teams")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    android.util.Log.d("TEAM_COLOR_DEBUG", "Successfully loaded team colors from Firebase");
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String teamId = document.getId();
                        String teamColorHex = document.getString("color");
                        String teamColorHexField = document.getString("colorHex");
                        
                        // Extract team number from "team_X" format
                        if (teamId.startsWith("team_")) {
                            try {
                                int teamNumber = Integer.parseInt(teamId.substring(5));
                                
                                // Use color if available, otherwise try colorHex
                                String colorToUse = teamColorHex;
                                if (colorToUse == null || colorToUse.isEmpty()) {
                                    colorToUse = teamColorHexField;
                                }
                                
                                if (colorToUse != null && !colorToUse.isEmpty()) {
                                    int colorInt = android.graphics.Color.parseColor(colorToUse);
                                    teamColorsCache.put(teamNumber, colorInt);
                                    
                                    // Generate bright version
                                    int brightColorInt = makeColorBrighter(colorToUse);
                                    teamBrightColorsCache.put(teamNumber, brightColorInt);
                                    
                                    android.util.Log.d("TEAM_COLOR_DEBUG", "Updated team color: " + teamNumber + " -> " + colorToUse);
                                }
                            } catch (NumberFormatException e) {
                                android.util.Log.e("TEAM_COLOR_DEBUG", "Error parsing team ID: " + teamId, e);
                            } catch (IllegalArgumentException e) {
                                android.util.Log.e("TEAM_COLOR_DEBUG", "Error parsing color for team " + teamId, e);
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("TEAM_COLOR_DEBUG", "Error loading team colors from Firebase", e);
                });
        } catch (Exception e) {
            android.util.Log.e("TEAM_COLOR_DEBUG", "Exception initializing team colors cache", e);
        }
    }
    
    private int makeColorBrighter(String colorHex) {
        try {
            int color = android.graphics.Color.parseColor(colorHex);
            float[] hsv = new float[3];
            android.graphics.Color.colorToHSV(color, hsv);
            hsv[2] = Math.min(hsv[2] * 1.4f, 1.0f); // Increase brightness by 40%
            return android.graphics.Color.HSVToColor(hsv);
        } catch (Exception e) {
            android.util.Log.e("TEAM_COLOR_DEBUG", "Error making color brighter: " + colorHex, e);
            return 0xFFB0B0B0; // Default bright gray
        }
    }
    
    private int getTeamColor(int teamId) {
        // Return color from cache, fallback to default gray if not found
        return teamColorsCache.getOrDefault(teamId, 0xFF808080); // Gray fallback
    }
    
    private android.graphics.drawable.Drawable getTeamIcon(int teamId) {
        return getTeamIcon(teamId, false, false);
    }
    
    private android.graphics.drawable.Drawable getTeamIcon(int teamId, boolean isInZone) {
        return getTeamIcon(teamId, isInZone, false);
    }
    
    private android.graphics.drawable.Drawable getTeamIcon(int teamId, boolean isInZone, boolean isVisitedToday) {
        // Create a larger colored circle drawable for team markers
        android.graphics.drawable.ShapeDrawable shape = new android.graphics.drawable.ShapeDrawable(new android.graphics.drawable.shapes.OvalShape());
        
        int color;
        int strokeWidth;
        int size;
        
        if (isVisitedToday) {
            // Gray out visited POIs
            color = 0xFF808080; // Gray
            strokeWidth = 2;
            size = 35; // Smaller for visited POIs
        } else {
            // Use brighter color if in zone, normal color if not
            color = isInZone ? getBrightTeamColor(teamId) : getTeamColor(teamId);
            strokeWidth = isInZone ? 4 : 2; // Thicker border if in zone
            size = isInZone ? 45 : 40; // Slightly larger if in zone
        }
        
        shape.getPaint().setColor(color);
        shape.getPaint().setStrokeWidth(strokeWidth);
        shape.getPaint().setStyle(android.graphics.Paint.Style.FILL_AND_STROKE);
        shape.getPaint().setAntiAlias(true);
        shape.setIntrinsicWidth(size);
        shape.setIntrinsicHeight(size);
        return shape;
    }
    
    private int getBrightTeamColor(int teamId) {
        // Return bright color from cache, fallback to default bright gray if not found
        return teamBrightColorsCache.getOrDefault(teamId, 0xFFB0B0B0); // Bright gray fallback
    }
    
    private void updatePoiDistancesAndList() {
        GeoPoint referenceLocation = currentUserLocation;
        
        // Use user's real location if available, otherwise fallback to Toulouse
        if (referenceLocation == null) {
            referenceLocation = new GeoPoint(43.6047, 1.4442); // Toulouse Capitole fallback
        } else if (isEmulatorDefaultLocation(referenceLocation)) {
            // If we have emulator default location, use Toulouse instead
            referenceLocation = new GeoPoint(43.6047, 1.4442);
            currentUserLocation = referenceLocation; // Update to avoid repeated checks
        }
        
        // Final null check for referenceLocation
        if (referenceLocation == null) {
            return;
        }
        
        // Calculate distances from reference location
        if (poiList == null) {
            return;
        }
        
        for (PoiItem poi : poiList) {
            GeoPoint poiLocation = new GeoPoint(poi.latitude, poi.longitude);
            
            // Manual Haversine calculation (most reliable)
            double lat1 = Math.toRadians(referenceLocation.getLatitude());
            double lon1 = Math.toRadians(referenceLocation.getLongitude());
            double lat2 = Math.toRadians(poi.latitude);
            double lon2 = Math.toRadians(poi.longitude);
            
            double dLat = lat2 - lat1;
            double dLon = lon2 - lon1;
            double a = Math.sin(dLat/2) * Math.sin(dLat/2) + 
                       Math.cos(lat1) * Math.cos(lat2) * 
                       Math.sin(dLon/2) * Math.sin(dLon/2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            double distanceInMeters = 6371000 * c; // Earth radius in meters
            
            poi.distance = distanceInMeters / 1000.0; // Convert to km
            
            // Update marker icon based on zone status (don't gray out visited POIs on map)
            if (poi.marker != null) {
                boolean isInZone = isUserInPoiZone(poi);
                poi.marker.setIcon(getTeamIcon(poi.owningTeam, isInZone, false));
            }
        }
        
        // Sort by distance
        poiList.sort((a, b) -> Double.compare(a.distance, b.distance));
        
        // Update adapter with new data
        // Create new adapter with fresh data to ensure it's properly updated
        poiListAdapter = new PoiListAdapter(new ArrayList<>(poiList));
        poiRecyclerView.setAdapter(poiListAdapter);
        
        // Check for POI zones whenever distances are updated
        checkForNewlyEnteredPoiZones();
        
        mapView.invalidate();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Display user information from SharedPreferences
        displayUserInfo();
        
        // Initialize team colors cache with default values and load from Firebase
        initializeTeamColorsCache();

        // 1. Initialize MapView and OSMDroid configuration
        mapView = binding.mapView;
        
        Configuration.getInstance().setUserAgentValue(
            "VibingApp/1.0 (https://example.com/contact-if-needed)"
        );
        
        // Enable zoom controls and multi-touch
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        
        // Setup info bubble container
        setupInfoBubble();
        
        // Add map click listener to hide info bubble when clicking elsewhere
        mapView.setMapListener(new org.osmdroid.events.MapListener() {
            @Override
            public boolean onScroll(org.osmdroid.events.ScrollEvent event) {
                // Update bubble position when map is scrolled
                if (currentSelectedMarker != null) {
                    updateInfoBubblePosition(currentSelectedMarker);
                }
                return false;
            }
            
            @Override
            public boolean onZoom(org.osmdroid.events.ZoomEvent event) {
                // Update bubble position when map is zoomed
                if (currentSelectedMarker != null) {
                    updateInfoBubblePosition(currentSelectedMarker);
                }
                return false;
            }
        });
        
        // Set tile source - try satellite view first
        try {
            // Use Stamen Terrain with satellite-like appearance
            OnlineTileSourceBase satelliteTileSource = new XYTileSource(
                "OpenStreetMap",
                0,
                18,
                256,
                ".png",
                new String[] {"https://tile.openstreetmap.org/"},
                "© OpenStreetMap contributors"
            );
            mapView.setTileSource(satelliteTileSource);
        } catch (Exception e) {
            try {
                mapView.setTileSource(TileSourceFactory.MAPNIK);
            } catch (Exception e2) {
                // Silent fallback
            }
        }
        
        // Initialize Location Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        
        // Initialize admin tap handler
        adminTapHandler = new Handler(Looper.getMainLooper());
        
// Initialize Location Callback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                // Use real location from locationResult
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    GeoPoint realLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                    currentUserLocation = realLocation;
                    updatePoiDistancesAndList();
                }
            }
        };
        
        // Set up easter egg on username text view
        setupAdminEasterEgg();

        // Initialize User Marker (will be positioned on first location update)
        userMarker = new Marker(mapView);
        userMarker.setTitle("Localisation en cours...");
        userMarker.setIcon(getResources().getDrawable(R.drawable.ic_user_location)); 
        mapView.getOverlays().add(userMarker);
        
        // 2. Initialize POI List RecyclerView
        poiRecyclerView = binding.poiListRecyclerView;
        poiRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Create empty adapter initially to prevent "No adapter attached" error
        poiListAdapter = new PoiListAdapter(new ArrayList<>());
        poiRecyclerView.setAdapter(poiListAdapter);
        
        // 3. Load user's visited POIs
        loadUserVisitedPois();
        
        // 4. Initialize POIs from Firebase
        initializePOIsFromFirebase();
        
        // 5. Initialize Pedometer (after layout is ready)
        initializePedometer();
        
        // 6. Check Permissions and Start Location Logic
        checkLocationPermissions();
        
        // Center map on user location if available, otherwise Toulouse as fallback
        if (currentUserLocation != null) {
            mapView.getController().setCenter(currentUserLocation);
            mapView.getController().setZoom(15.0);
        } else {
            mapView.getController().setCenter(new GeoPoint(43.6047, 1.4442));
            mapView.getController().setZoom(13.0);
        }

        return root;
    }
    
    private void setupPoiList(View root) {
        // This method is now integrated directly into onCreateView for simplicity, 
        // but the logic is preserved in displayPlaceholderPOIs()
    }

    private void checkLocationPermissions() {
        // D'ABORD demander permission podomètre PUIS localisation
        requestPedometerPermission();
    }
    
    private void requestPedometerPermission() {
        android.util.Log.d("HomeFragment", "Début flux permissions - Demande podomètre (ACTIVITY_RECOGNITION)");
        
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            
            android.util.Log.d("HomeFragment", "Permission podomètre non accordée - Affichage popup");
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                    REQUEST_PERMISSIONS_PEDOMETER);
        } else {
            android.util.Log.d("HomeFragment", "Permission podomètre déjà accordée - Passage direct localisation");
            checkActualLocationPermissions();
        }
    }
    
    private void checkActualLocationPermissions() {
        android.util.Log.d("HomeFragment", "Demande localisation (ACCESS_FINE_LOCATION)");
        
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            
            android.util.Log.d("HomeFragment", "Permission localisation non accordée - Affichage popup");
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_LOCATION);
        } else {
            android.util.Log.d("HomeFragment", "Permission localisation déjà accordée - Démarrage immédiat");
            startLocationUpdates();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == REQUEST_PERMISSIONS_PEDOMETER) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                android.util.Log.d("HomeFragment", "Permission podomètre accordée");
            } else {
                android.util.Log.d("HomeFragment", "Permission podomètre refusée");
            }
            
            // Attendre 300ms avant de demander la localisation
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                android.util.Log.d("HomeFragment", "Délai écoulé - Demande localisation");
                checkActualLocationPermissions();
            }, 300);
            
        } else if (requestCode == REQUEST_PERMISSIONS_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                android.util.Log.d("HomeFragment", "Permission localisation accordée - Démarrage localisation");
                startLocationUpdates();
            } else {
                android.util.Log.d("HomeFragment", "Permission localisation refusée - Position par défaut");
                // Permission denied. Use Toulouse as fallback location.
                if (userMarker != null) {
                    userMarker.setTitle("Permission de localisation refusée");
                    currentUserLocation = new GeoPoint(43.6047, 1.4442); // Set fallback location
                    mapView.getController().setCenter(currentUserLocation);
                    mapView.getController().setZoom(13.0);
                    userMarker.setVisible(false); // Hide marker if we can't get location
                    updatePoiDistancesAndList(); // Update distances with fallback location
                }
            }
        }
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            
            LocationRequest locationRequest = LocationRequest.create()
                .setInterval(5000) // 5 seconds for more responsive updates
                .setFastestInterval(2000) // 2 seconds
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1); // Request fresh location updates

            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
            
            // Request an initial location update immediately if possible
            fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
                if (location != null) {
                    // Use real location
                    GeoPoint realLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                    currentUserLocation = realLocation;
                    mapView.getController().setCenter(realLocation);
                    mapView.getController().setZoom(15.0);
                    userMarker.setPosition(realLocation);
                    userMarker.setTitle("Vous êtes ici");
                    userMarker.setVisible(true);
                    
                    // Update POI distances with real location
                    updatePoiDistancesAndList();
                    mapView.invalidate();
                } else {
                    // Fallback to Toulouse Capitole if no location available
                    GeoPoint fallbackLocation = new GeoPoint(43.6047, 1.4442);
                    currentUserLocation = fallbackLocation;
                    mapView.getController().setCenter(fallbackLocation);
                    mapView.getController().setZoom(13.0);
                    userMarker.setPosition(fallbackLocation);
                    userMarker.setTitle("Position par défaut (Toulouse)");
                    userMarker.setVisible(true);
                    
                    // Update POI distances with fallback location
                    updatePoiDistancesAndList();
                    mapView.invalidate();
                }
            });
        }
    }
    


    private void stopLocationUpdates() {
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
        // Restart location updates when the fragment becomes visible
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        }
        // Refresh POI list when returning to fragment
        if (poiRecyclerView != null && poiRecyclerView.getAdapter() != null) {
            poiRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
        // Stop location updates to save battery
        stopLocationUpdates();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        
        // Stop location updates to save battery
        stopLocationUpdates();
        
        // Arrêter le podomètre
        stopPedometer();
        
        // Clean up info bubble
        if (currentInfoBubble != null && currentInfoBubble.getParent() != null) {
            ((ViewGroup) currentInfoBubble.getParent()).removeView(currentInfoBubble);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        // Find the POI associated with this marker
        for (PoiItem poi : poiList) {
            if (poi.marker == marker) {
                // Vérifier si le POI a déjà été visité aujourd'hui
                if (isPoiVisitedToday(poi.id)) {
                    Toast.makeText(getContext(), "Vous avez déjà visité " + poi.name + " aujourd'hui. Revenez demain !", Toast.LENGTH_LONG).show();
                    return true;
                }
                
                // Vérifier si le joueur est dans la zone du POI
                if (isUserInPoiZone(poi)) {
                    // Naviguer vers PoiScoreFragment comme dans la liste
                    navigateToPoiScore(poi);
                    
                    // Add visual feedback - briefly enlarge the marker
                    android.graphics.drawable.Drawable originalIcon = marker.getIcon();
                    android.graphics.drawable.Drawable enlargedIcon = getTeamIcon(poi.owningTeam);
                    enlargedIcon.setBounds(-25, -25, 25, 25);
                    marker.setIcon(enlargedIcon);
                    mapView.invalidate();
                    
                    // Reset icon after a short delay
                    new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                        marker.setIcon(originalIcon);
                        mapView.invalidate();
                    }, 200);
                    
                    return true; // Event handled
                } else {
                    // Joueur hors zone - afficher un message et ne pas lancer le quiz
                    String distanceText = String.format("%.0f", poi.distance * 1000); // Convert km to m
                    Toast.makeText(getContext(), "Vous devez être à " + distanceText + "m de " + poi.name + " pour lancer le quiz", Toast.LENGTH_LONG).show();
                    
                    // Feedback visuel - marker rouge temporaire
                    android.graphics.drawable.Drawable originalIcon = marker.getIcon();
                    android.graphics.drawable.ShapeDrawable redIcon = new android.graphics.drawable.ShapeDrawable(new android.graphics.drawable.shapes.OvalShape());
                    redIcon.getPaint().setColor(0xFFFF0000); // Rouge
                    redIcon.getPaint().setStrokeWidth(2);
                    redIcon.getPaint().setStyle(android.graphics.Paint.Style.FILL_AND_STROKE);
                    redIcon.setIntrinsicWidth(40);
                    redIcon.setIntrinsicHeight(40);
                    marker.setIcon(redIcon);
                    mapView.invalidate();
                    
                    // Reset icon après un court délai
                    new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                        marker.setIcon(originalIcon);
                        mapView.invalidate();
                    }, 500);
                    
                    return true; // Event handled (but quiz not launched)
                }
            }
        }
        return false; // Event not handled
    }
    
    private boolean isUserInPoiZone(PoiItem poi) {
        if (currentUserLocation == null) {
            return false;
        }
        
        // Calculer la distance entre l'utilisateur et le POI en mètres
        double lat1 = Math.toRadians(currentUserLocation.getLatitude());
        double lon1 = Math.toRadians(currentUserLocation.getLongitude());
        double lat2 = Math.toRadians(poi.latitude);
        double lon2 = Math.toRadians(poi.longitude);
        
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + 
                   Math.cos(lat1) * Math.cos(lat2) * 
                   Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distanceInMeters = 6371000 * c; // Earth radius in meters
        
        return distanceInMeters <= poi.radius;
    }
    
    private void checkForNewlyEnteredPoiZones() {
        android.util.Log.d("POPUP_DEBUG", "checkForNewlyEnteredPoiZones called");
        android.util.Log.d("POI_SYNC_DEBUG", "visitedPois status: " + (visitedPois != null ? "loaded (" + visitedPois.size() + " items)" : "null"));
        
        if (poiList == null) {
            android.util.Log.d("POPUP_DEBUG", "poiList is null");
            return;
        }
        
        if (currentUserLocation == null) {
            android.util.Log.d("POPUP_DEBUG", "currentUserLocation is null");
            return;
        }
        
        // Additional safety check - if visited POIs are still not loaded, wait
        if (!visitedPoisLoaded && visitedPois == null) {
            android.util.Log.d("POI_SYNC_DEBUG", "Visited POIs still not loaded, postponing zone check");
            waitForVisitedPoisAndCheckZones();
            return;
        }
        
        android.util.Log.d("POPUP_DEBUG", "Checking " + poiList.size() + " POIs");
        
        List<PoiItem> currentlyEnteredPois = new ArrayList<>();
        
        // Find all POIs the user is currently in zone for
        for (PoiItem poi : poiList) {
            boolean inZone = isUserInPoiZone(poi);
            if (inZone) {
                currentlyEnteredPois.add(poi);
            }
        }
        
        android.util.Log.d("POPUP_DEBUG", "Currently in " + currentlyEnteredPois.size() + " zones");
        android.util.Log.d("POPUP_DEBUG", "Previously in " + previouslyEnteredPoiNames.size() + " zones");
        android.util.Log.d("POPUP_DEBUG", "hasShownPopupForCurrentLocation: " + hasShownPopupForCurrentLocation);
        
        // Check if this is a new entry (user wasn't in this POI zone before)
        List<PoiItem> newlyEnteredPois = new ArrayList<>();
        for (PoiItem currentPoi : currentlyEnteredPois) {
            if (!previouslyEnteredPoiNames.contains(currentPoi.name)) {
                newlyEnteredPois.add(currentPoi);
                android.util.Log.d("POPUP_DEBUG", "Newly entered POI: " + currentPoi.name);
            }
        }
        
        // Show popup if user entered new POI zones
        if (!newlyEnteredPois.isEmpty()) {
            android.util.Log.d("POPUP_DEBUG", "Should show popup for " + newlyEnteredPois.size() + " POIs");
            android.util.Log.d("POI_VISIT_DEBUG", "About to call popup with visitedPois status: " + (visitedPois != null ? "not null" : "null"));
            showPoiQuizPopup(newlyEnteredPois);
        }
        

        
        // Update the previously entered POIs list
        previouslyEnteredPoiNames.clear();
        for (PoiItem poi : currentlyEnteredPois) {
            previouslyEnteredPoiNames.add(poi.name);
        }
    }
    
    private void showPoiQuizPopup(List<PoiItem> newlyEnteredPois) {
        if (newlyEnteredPois.isEmpty()) {
            return;
        }
        
        // Filter out POIs that have already been visited today
        List<PoiItem> eligiblePois = new ArrayList<>();
        for (PoiItem poi : newlyEnteredPois) {
            android.util.Log.d("POI_VISIT_DEBUG", "POPUP Checking POI: " + poi.name + " (ID: " + poi.id + ")");
            boolean visitedToday = isPoiVisitedToday(poi.id);
            android.util.Log.d("POI_VISIT_DEBUG", "POPUP Result for " + poi.name + ": visitedToday=" + visitedToday);
            if (!visitedToday) {
                eligiblePois.add(poi);
            }
        }
        
        if (eligiblePois.isEmpty()) {
            android.util.Log.d("POI_VISIT_DEBUG", "POPUP All POIs have been visited today, not showing popup");
            return;
        }
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog[] dialogRef = new AlertDialog[1]; // Array to hold dialog reference
        
        if (eligiblePois.size() == 1) {
            // Single POI entered
            PoiItem poi = eligiblePois.get(0);
            builder.setTitle("🎯 Zone de POI détectée!")
                  .setMessage("Vous êtes dans la zone de " + poi.name + " !\n\nScore actuel: " + poi.score + "\nVoulez-vous lancer le quiz pour capturer ce POI ?");
            
            builder.setPositiveButton("Lancer le quiz", (dialog, which) -> {
                navigateToPoiScore(poi);
                dialog.dismiss();
            });
            
        } else {
            // Multiple POIs entered - create custom layout with clickable POI items
            android.util.Log.d("POI_VISIT_DEBUG", "POPUP Creating custom layout with size: " + eligiblePois.size());
            
            // Create custom layout
            LinearLayout layout = new LinearLayout(getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(50, 40, 50, 20);
            
            // Add title text
            TextView titleText = new TextView(getContext());
            titleText.setText("Vous êtes dans la zone de plusieurs POIs !\n\nChoisissez celui que vous voulez capturer :");
            titleText.setTextSize(16);
            titleText.setPadding(0, 0, 0, 30);
            layout.addView(titleText);
            
            // Create clickable views for each POI
            for (int i = 0; i < eligiblePois.size(); i++) {
                PoiItem poi = eligiblePois.get(i);
                
                // Create container for this POI
                LinearLayout poiContainer = new LinearLayout(getContext());
                poiContainer.setOrientation(LinearLayout.HORIZONTAL);
                poiContainer.setPadding(20, 15, 20, 15);
                poiContainer.setBackgroundColor(Color.parseColor("#F5F5F5"));
                
                // Create POI text
                TextView poiText = new TextView(getContext());
                poiText.setText((i + 1) + ". " + poi.name + " (Score: " + poi.score + ")");
                poiText.setTextSize(14);
                poiText.setTextColor(Color.BLACK);
                poiText.setPadding(20, 0, 20, 0);
                
                // Create arrow button
                TextView arrowButton = new TextView(getContext());
                arrowButton.setText("▶");
                arrowButton.setTextSize(16);
                arrowButton.setTextColor(Color.parseColor("#2196F3"));
                arrowButton.setPadding(10, 0, 10, 0);
                arrowButton.setOnClickListener(v -> {
                    navigateToPoiScore(poi);
                    if (dialogRef[0] != null) dialogRef[0].dismiss();
                });
                
                // Make entire container clickable
                poiContainer.setOnClickListener(v -> {
                    navigateToPoiScore(poi);
                    if (dialogRef[0] != null) dialogRef[0].dismiss();
                });
                
                // Add to container
                poiContainer.addView(poiText);
                poiContainer.addView(arrowButton);
                
                // Add some margin between items
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 10, 0, 10);
                poiContainer.setLayoutParams(params);
                
                layout.addView(poiContainer);
                android.util.Log.d("POI_VISIT_DEBUG", "POPUP Added clickable POI: " + poi.name);
            }
            
            builder.setTitle("🎯 Plusieurs zones de POI détectées!")
                  .setView(layout);
        }
             
        builder.setNegativeButton("Plus tard", (dialog, which) -> {
            dialog.dismiss();
        });
        
        builder.setCancelable(false); // Prevent dismissal by clicking outside
        
        AlertDialog dialog = builder.create();
        dialogRef[0] = dialog; // Store reference for custom layout
        dialog.show();
    }

    private void setupInfoBubble() {
        // Create a custom info bubble that will be added to the map's parent layout
        ViewGroup mapContainer = (ViewGroup) binding.mapView.getParent();
        
        currentInfoBubble = new TextView(getContext());
        currentInfoBubble.setVisibility(View.GONE);
        currentInfoBubble.setPadding(20, 12, 20, 12);
        currentInfoBubble.setTextSize(14);
        currentInfoBubble.setTextColor(Color.WHITE);
        currentInfoBubble.setGravity(Gravity.CENTER);
        
        // Create background drawable with rounded corners
        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        background.setColor(Color.parseColor("#CC000000")); // Semi-transparent black
        background.setCornerRadius(12);
        background.setStroke(2, Color.WHITE);
        currentInfoBubble.setBackground(background);
        
        // Add to map container with proper layout params for any parent type
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        mapContainer.addView(currentInfoBubble, params);
    }

    private void showInfoBubble(Marker marker, PoiItem poi) {
        if (currentInfoBubble == null || poi == null) return;
        
        // Create info text
        String distanceText = String.format("%.1f km", poi.distance);
        String infoText = poi.name + "\n" +
                         "Distance: " + distanceText + "\n" +
                         "Score: " + poi.score + " | " + getTeamName(poi.owningTeam);
        
        currentInfoBubble.setText(infoText);
        currentInfoBubble.setVisibility(View.VISIBLE);
        
        // Position the bubble above the marker
        updateInfoBubblePosition(marker);
        
        currentSelectedMarker = marker;
    }

    private void updateInfoBubblePosition(Marker marker) {
        if (currentInfoBubble == null || marker == null) return;
        
        // Get marker position on screen
        GeoPoint markerPos = marker.getPosition();
        org.osmdroid.views.Projection projection = mapView.getProjection();
        android.graphics.Point screenPoint = new android.graphics.Point();
        projection.toPixels(markerPos, screenPoint);
        
        // Position bubble above marker
        int bubbleWidth = currentInfoBubble.getWidth();
        int bubbleHeight = currentInfoBubble.getHeight();
        
        if (bubbleWidth == 0) {
            // Measure the view first if not laid out yet
            currentInfoBubble.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            );
            bubbleWidth = currentInfoBubble.getMeasuredWidth();
            bubbleHeight = currentInfoBubble.getMeasuredHeight();
        }
        
        int x = screenPoint.x - bubbleWidth / 2;
        int y = screenPoint.y - bubbleHeight - 50; // 50px above marker
        
        // Ensure bubble stays within screen bounds
        x = Math.max(10, Math.min(x, mapView.getWidth() - bubbleWidth - 10));
        y = Math.max(10, y);
        
        // Position the bubble using translation instead of layout params
        currentInfoBubble.setTranslationX(x);
        currentInfoBubble.setTranslationY(y);
    }

    private void hideInfoBubble() {
        if (currentInfoBubble != null) {
            currentInfoBubble.setVisibility(View.GONE);
        }
        currentSelectedMarker = null;
    }
    
    private void navigateToPoiScore(PoiItem poi) {
        // Create bundle with POI data
        Bundle bundle = new Bundle();
        bundle.putString("poiName", poi.name);
        bundle.putString("poiId", poi.id); // Pass the actual POI ID from Firebase
        bundle.putFloat("poiLatitude", (float) poi.latitude);
        bundle.putFloat("poiLongitude", (float) poi.longitude);
        bundle.putInt("poiScore", poi.score);
        bundle.putInt("poiOwningTeam", poi.owningTeam);
        
// Get current user's team ID as string
        SharedPreferences prefs = requireContext().getSharedPreferences("VibingPrefs", android.content.Context.MODE_PRIVATE);
        String userTeamId = prefs.getString("team_id", "team_1"); // Default to team_1 if not set
        bundle.putString("userTeamId", userTeamId);
        
        // Navigate to PoiScoreFragment
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_homeFragment_to_poiScoreFragment, bundle);
    }
    
    private void displayUserInfo() {
        if (binding == null || !isAdded() || getContext() == null) {
            android.util.Log.w("HOME_FRAGMENT", "Fragment not attached or binding is null, skipping UI update");
            return;
        }
        
        SharedPreferences prefs = requireContext().getSharedPreferences("VibingPrefs", android.content.Context.MODE_PRIVATE);
        String username = prefs.getString("username", "Joueur");
        String teamName = prefs.getString("team_name", "Équipe inconnue");
        int money = prefs.getInt("money", 0);
        
        // Load current money from Firebase
        loadUserMoneyFromFirebase();
        
        TextView usernameTextView = binding.getRoot().findViewById(R.id.username_text_view);
        TextView teamTextView = binding.getRoot().findViewById(R.id.team_text_view);
        TextView moneyTextView = binding.getRoot().findViewById(R.id.money_text_view);
        TextView walkingTextView = binding.getRoot().findViewById(R.id.walking_text_view);
        
        if (usernameTextView != null) {
            usernameTextView.setText(username);
        }
        
        if (teamTextView != null) {
            teamTextView.setText("Équipe: " + teamName);
        }
        
        if (moneyTextView != null) {
            moneyTextView.setText("Argent: " + money + "€");
        }
        
        // Le podomètre sera initialisé après la création complète de la vue
    }
    private void loadUserMoneyFromFirebase() {
        try {
            SharedPreferences prefs = requireContext().getSharedPreferences("VibingPrefs", android.content.Context.MODE_PRIVATE);
            String userId = prefs.getString("user_id", null);
            
            if (userId != null) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Integer money = documentSnapshot.getLong("money") != null ? 
                                documentSnapshot.getLong("money").intValue() : 0;
                            
                            // Load step tracking data
                            Integer totalSteps = documentSnapshot.getLong("totalSteps") != null ? 
                                documentSnapshot.getLong("totalSteps").intValue() : 0;
                            Integer moneySteps = documentSnapshot.getLong("moneySteps") != null ? 
                                documentSnapshot.getLong("moneySteps").intValue() : 0;
                            
                            // Update local SharedPreferences with Firebase values
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putInt("money", money);
                            editor.putInt("totalSteps", totalSteps);
                            editor.putInt("moneySteps", moneySteps);
                            editor.apply();
                            
                            // Initialize step tracking
                            totalStepsSinceStart = totalSteps;
                            lastMoneyStepCount = moneySteps;
                            
                            // Update UI
                            updateMoneyDisplay(money);
                            
                        } else {
                            android.util.Log.w("HOME_FRAGMENT", "User document not found in Firebase");
                        }
                    })
                    .addOnFailureListener(e -> {
                        android.util.Log.e("HOME_FRAGMENT", "Error loading user money from Firebase", e);
                    });
            } else {
                android.util.Log.w("HOME_FRAGMENT", "No user ID found, using default money: 0");
            }
        } catch (Exception e) {
            android.util.Log.e("HOME_FRAGMENT", "Exception loading user money", e);
        }
    }
    
    private void updateMoneyDisplay(int money) {
        // Check if fragment is still attached and binding is not null
        if (binding == null || !isAdded() || getContext() == null) {
            android.util.Log.w("HOME_FRAGMENT", "Fragment not attached or binding is null, skipping UI update");
            return;
        }
        
        TextView moneyTextView = binding.getRoot().findViewById(R.id.money_text_view);
        if (moneyTextView != null) {
            moneyTextView.setText("Argent: " + money + "€");
        }
    }
    
    private void initializePedometer() {
        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        
        // Load step tracking data from SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("VibingPrefs", android.content.Context.MODE_PRIVATE);
        totalStepsSinceStart = prefs.getInt("totalSteps", 0);
        lastMoneyStepCount = prefs.getInt("moneySteps", 0);
        
        // Essayer d'abord le capteur STEP_COUNTER (compteur de pas total depuis le redémarrage)
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        
        // Si non disponible, essayer STEP_DETECTOR (détecte chaque pas individuellement)
        if (stepCounterSensor == null) {
            stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        }
        
        if (stepCounterSensor != null) {
            // Utiliser STEP_COUNTER
            isStepCounterAvailable = true;
            initialSteps = -1; // Sera défini lors de la première lecture
            currentSteps = 0;
            
            stepListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
                        android.util.Log.d("HomeFragment", "STEP_COUNTER event received: " + event.values[0]);
                        
                        // La première lecture définit notre point de départ
                        if (initialSteps == -1) {
                            initialSteps = (int) event.values[0];
                            currentSteps = 0;
                            android.util.Log.d("HomeFragment", "Initial steps set to: " + initialSteps);
                        } else {
                            // Calculer les pas depuis le démarrage de l'application
                            currentSteps = (int) event.values[0] - initialSteps;
                            android.util.Log.d("HomeFragment", "Current steps calculated: " + currentSteps);
                        }
                        
                        // Update total steps and calculate money
                        updateStepsAndMoney();
                    }
                }
                
                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    android.util.Log.d("HomeFragment", "Step sensor accuracy changed: " + accuracy);
                }
            };
            
            sensorManager.registerListener(stepListener, stepCounterSensor, SensorManager.SENSOR_DELAY_UI);
            android.util.Log.d("HomeFragment", "Podomètre STEP_COUNTER initialisé");
            
        } else if (stepDetectorSensor != null) {
            // Utiliser STEP_DETECTOR
            isStepCounterAvailable = false;
            currentSteps = 0;
            
            stepListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
                        currentSteps++;
                        
                        // Update total steps and calculate money
                        updateStepsAndMoney();
                    }
                }
                
                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {}
            };
            
            sensorManager.registerListener(stepListener, stepDetectorSensor, SensorManager.SENSOR_DELAY_UI);
            android.util.Log.d("HomeFragment", "Podomètre STEP_DETECTOR initialisé");
            
        } else {
            // Aucun capteur disponible
            android.util.Log.w("HomeFragment", "Aucun capteur de pas disponible sur cet appareil");
            updateWalkingDisplay();
        }
        
        // Forcer l'affichage initial après l'initialisation
        updateWalkingDisplay();
        
        // Forcer une petite pause puis réenregistrer le capteur pour s'assurer qu'il fonctionne
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            if (sensorManager != null && stepListener != null && stepCounterSensor != null) {
                android.util.Log.d("HomeFragment", "Re-registering step counter sensor to ensure it works");
                sensorManager.unregisterListener(stepListener);
                boolean registered = sensorManager.registerListener(stepListener, stepCounterSensor, SensorManager.SENSOR_DELAY_UI);
                android.util.Log.d("HomeFragment", "Step sensor re-registered: " + registered);
            }
        }, 2000); // 2 secondes de délai
    }
    
    private void updateWalkingDisplay() {
        if (binding == null || !isAdded()) {
            return;
        }
        TextView walkingTextView = binding.getRoot().findViewById(R.id.walking_text_view);
        if (walkingTextView != null) {
            int currentModulo = currentSteps % STEPS_PER_EURO;
            int cyclesCompleted = currentSteps / STEPS_PER_EURO;
            walkingTextView.setText("Marche: " + currentModulo + "/20 (" + cyclesCompleted + ")");
        }
    }
    
    private void updateStepsAndMoney() {
        // Update display
        updateWalkingDisplay();
        
        // Calculate new total steps
        int newTotalSteps = totalStepsSinceStart + currentSteps;
        
        // Calculate how many euros we've earned from new steps
        int stepsSinceLastMoney = newTotalSteps - lastMoneyStepCount;
        int eurosEarned = stepsSinceLastMoney / STEPS_PER_EURO;
        
        if (eurosEarned > 0) {
            // Update money step count
            lastMoneyStepCount += eurosEarned * STEPS_PER_EURO;
            
            // Update user money
            SharedPreferences prefs = requireContext().getSharedPreferences("VibingPrefs", android.content.Context.MODE_PRIVATE);
            int currentMoney = prefs.getInt("money", 0);
            int newMoney = currentMoney + eurosEarned;
            
            // Update UI immediately
            updateMoneyDisplay(newMoney);
            
            // Save to SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("money", newMoney);
            editor.putInt("totalSteps", newTotalSteps);
            editor.putInt("moneySteps", lastMoneyStepCount);
            editor.apply();
            
            // Save to Firebase
            saveStepsAndMoneyToFirebase(newMoney, newTotalSteps, lastMoneyStepCount);
            
            android.util.Log.d("HomeFragment", "Steps updated! Earned " + eurosEarned + "€ from " + stepsSinceLastMoney + " steps");
        } else {
            // Just save the total steps
            SharedPreferences prefs = requireContext().getSharedPreferences("VibingPrefs", android.content.Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("totalSteps", newTotalSteps);
            editor.apply();
            
            // Save total steps to Firebase
            saveStepsToFirebase(newTotalSteps);
        }
    }
    
    private void stopPedometer() {
        if (sensorManager != null && stepListener != null) {
            sensorManager.unregisterListener(stepListener);
            android.util.Log.d("HomeFragment", "Podomètre arrêté");
        }
    }
    
    private void saveStepsAndMoneyToFirebase(int money, int totalSteps, int moneySteps) {
        try {
            SharedPreferences prefs = requireContext().getSharedPreferences("VibingPrefs", android.content.Context.MODE_PRIVATE);
            String userId = prefs.getString("user_id", null);
            
            if (userId != null) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                
                // Create updates map for batch update
                java.util.Map<String, Object> updates = new java.util.HashMap<>();
                updates.put("money", money);
                updates.put("totalSteps", totalSteps);
                updates.put("moneySteps", moneySteps);
                updates.put("lastStepUpdate", new java.util.Date());
                
                db.collection("users").document(userId)
                    .update(updates)
                    .addOnSuccessListener(aVoid -> {
                        android.util.Log.d("HOME_FRAGMENT", "Successfully saved steps and money to Firebase");
                    })
                    .addOnFailureListener(e -> {
                        android.util.Log.e("HOME_FRAGMENT", "Error saving steps and money to Firebase", e);
                    });
            } else {
                android.util.Log.w("HOME_FRAGMENT", "No user ID found, cannot save steps and money to Firebase");
            }
        } catch (Exception e) {
            android.util.Log.e("HOME_FRAGMENT", "Exception saving steps and money to Firebase", e);
        }
    }
    
    private void saveStepsToFirebase(int totalSteps) {
        try {
            SharedPreferences prefs = requireContext().getSharedPreferences("VibingPrefs", android.content.Context.MODE_PRIVATE);
            String userId = prefs.getString("user_id", null);
            
            if (userId != null) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                
                java.util.Map<String, Object> updates = new java.util.HashMap<>();
                updates.put("totalSteps", totalSteps);
                updates.put("lastStepUpdate", new java.util.Date());
                
                db.collection("users").document(userId)
                    .update(updates)
                    .addOnSuccessListener(aVoid -> {
                        android.util.Log.d("HOME_FRAGMENT", "Successfully saved steps to Firebase");
                    })
                    .addOnFailureListener(e -> {
                        android.util.Log.e("HOME_FRAGMENT", "Error saving steps to Firebase", e);
                    });
            } else {
                android.util.Log.w("HOME_FRAGMENT", "No user ID found, cannot save steps to Firebase");
            }
        } catch (Exception e) {
            android.util.Log.e("HOME_FRAGMENT", "Exception saving steps to Firebase", e);
        }
    }
    
    private void saveUserMoneyToFirebase(int money) {
        try {
            SharedPreferences prefs = requireContext().getSharedPreferences("VibingPrefs", android.content.Context.MODE_PRIVATE);
            String userId = prefs.getString("user_id", null);
            
            if (userId != null) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").document(userId)
                    .update("money", money)
                    .addOnSuccessListener(aVoid -> {
                        android.util.Log.d("HOME_FRAGMENT", "Successfully saved money to Firebase: " + money);
                    })
                    .addOnFailureListener(e -> {
                        android.util.Log.e("HOME_FRAGMENT", "Error saving money to Firebase", e);
                    });
            } else {
                android.util.Log.w("HOME_FRAGMENT", "No user ID found, cannot save money to Firebase");
            }
        } catch (Exception e) {
            android.util.Log.e("HOME_FRAGMENT", "Exception saving user money", e);
        }
    }
    
    // Public method to allow other fragments to update money
    public void updateUserMoney(int money) {
        // Update local SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("VibingPrefs", android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("money", money);
        editor.apply();
        
        // Update Firebase
        saveUserMoneyToFirebase(money);
        
        // Update UI
        updateMoneyDisplay(money);
        
    }
    
    // Public method to get current steps for testing
    public int getCurrentSteps() {
        return currentSteps;
    }
    
    // Public method to get total steps since app install
    public int getTotalStepsSinceStart() {
        return totalStepsSinceStart + currentSteps;
    }
    
    private void loadUserVisitedPois() {
        try {
            SharedPreferences prefs = requireContext().getSharedPreferences("VibingPrefs", android.content.Context.MODE_PRIVATE);
            String userId = prefs.getString("user_id", null);
            
            if (userId != null) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            @SuppressWarnings("unchecked")
                            List<Map<String, String>> visited = (List<Map<String, String>>) documentSnapshot.get("visitedPois");
                            visitedPois = visited != null ? visited : new ArrayList<>();
                        } else {
                            visitedPois = new ArrayList<>();
                        }
                        
                        // Mark visited POIs as loaded
                        visitedPoisLoaded = true;
                        android.util.Log.d("POI_SYNC_DEBUG", "Visited POIs loaded, count: " + visitedPois.size());
                        
                        // Update POI display after loading visited POIs
                        updatePoiDistancesAndList();
                    })
                    .addOnFailureListener(e -> {
                        visitedPois = new ArrayList<>();
                        visitedPoisLoaded = true;
                        android.util.Log.w("POI_SYNC_DEBUG", "Failed to load visited POIs, using empty list");
                    });
            } else {
                visitedPois = new ArrayList<>();
                visitedPoisLoaded = true;
                android.util.Log.d("POI_SYNC_DEBUG", "No user ID found, marking visited POIs as loaded with empty list");
            }
        } catch (Exception e) {
            visitedPois = new ArrayList<>();
            visitedPoisLoaded = true;
            android.util.Log.e("POI_SYNC_DEBUG", "Exception loading visited POIs, using empty list", e);
        }
    }
    
// Easter egg: Handle admin tap detection
    private void handleAdminTap(MotionEvent e) {
        long currentTime = System.currentTimeMillis();
        
        // Reset if this is first tap or timeout occurred
        if (adminTapCount == 0) {
            adminFirstTapTime = currentTime;
        } else if (currentTime - adminFirstTapTime > ADMIN_TAP_TIMEOUT_MS) {
            adminTapCount = 0;
            adminFirstTapTime = currentTime;
        }
        
        adminTapCount++;
        android.util.Log.d("ADMIN_EASTER_EGG", "Admin tap count: " + adminTapCount);
        
        // Check if we've reached magic number
        if (adminTapCount >= ADMIN_TAP_COUNT) {
            adminTapCount = 0;
            adminFirstTapTime = 0;
            showAdminPasswordDialog();
        } else if (adminTapCount > ADMIN_TAP_COUNT - 3) {
            // Give visual feedback when getting close
            showToast("Admin access: " + adminTapCount + "/" + ADMIN_TAP_COUNT);
        }
    }
    
    // Show password dialog for admin access
    private void showAdminPasswordDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
        builder.setTitle("🔐 Accès Admin");
        
        // Create an input field
        final android.widget.EditText input = new android.widget.EditText(requireContext());
        input.setHint("Entrez le mot de passe");
        input.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        
        // Set up the buttons
        builder.setPositiveButton("Valider", (dialog, which) -> {
            String password = input.getText().toString().trim();
            if (password.equals("toto")) {
                showToast("🔓 Admin access granted!");
                launchAdminActivity();
            } else {
                showToast("❌ Mot de passe incorrect!");
            }
        });
        
        builder.setNegativeButton("Annuler", (dialog, which) -> {
            dialog.cancel();
        });
        
        builder.show();
    }
    
    // Launch admin activity
    private void launchAdminActivity() {
        try {
            android.util.Log.d("ADMIN_EASTER_EGG", "Launching admin activity");
            
            // Remove any pending tap reset
            adminTapHandler.removeCallbacksAndMessages(null);
            
            // Launch admin activity
            Intent intent = new Intent(requireContext(), com.example.vibing.activities.PoiAdminActivity.class);
            startActivity(intent);
            
        } catch (Exception e) {
            android.util.Log.e("ADMIN_EASTER_EGG", "Error launching admin activity", e);
            showToast("Error accessing admin panel");
        }
    }
    
    // Helper method to show toast
    private void showToast(String message) {
        android.widget.Toast.makeText(requireContext(), message, android.widget.Toast.LENGTH_SHORT).show();
    }
    
    // Setup easter egg on username text view
    private void setupAdminEasterEgg() {
        if (binding == null || !isAdded()) {
            return;
        }
        TextView usernameTextView = binding.usernameTextView;
        if (usernameTextView != null) {
            usernameTextView.setOnClickListener(v -> {
                MotionEvent event = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 0, 0, 0);
                handleAdminTap(event);
                event.recycle();
            });
        }
    }
    
    private boolean isPoiVisitedToday(String poiId) {
        if (visitedPois == null) {
            android.util.Log.d("POI_VISIT_DEBUG", "visitedPois is null, returning false for " + poiId);
            return false;
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = dateFormat.format(new Date());
        
        for (Map<String, String> visit : visitedPois) {
            String visitedPoiId = visit.get("poiId");
            String visitDate = visit.get("visitDate");
            
            if (poiId.equals(visitedPoiId) && today.equals(visitDate)) {
                android.util.Log.d("POI_VISIT_DEBUG", "Found match: POI " + poiId + " visited today");
                return true;
            }
        }
        
        android.util.Log.d("POI_VISIT_DEBUG", "No match found: POI " + poiId + " not visited today");
        return false;
    }
    
private void recordPoiVisit(String poiId) {
        if (visitedPois == null) {
            visitedPois = new ArrayList<>();
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = dateFormat.format(new Date());
        
        // Check if POI already exists and update visit date, or add new visit
        boolean poiFound = false;
        for (Map<String, String> visit : visitedPois) {
            if (poiId.equals(visit.get("poiId"))) {
                // Update existing visit date
                visit.put("visitDate", today);
                poiFound = true;
                break;
            }
        }
        
        if (!poiFound) {
            // Add new visit
            Map<String, String> visit = new HashMap<>();
            visit.put("poiId", poiId);
            visit.put("visitDate", today);
            visitedPois.add(visit);
        }
        
        // Save to Firebase
        saveVisitedPoisToFirebase();
    }
    
    private void saveVisitedPoisToFirebase() {
        try {
            SharedPreferences prefs = requireContext().getSharedPreferences("VibingPrefs", android.content.Context.MODE_PRIVATE);
            String userId = prefs.getString("user_id", null);
            
            if (userId != null) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").document(userId)
                    .update("visitedPois", visitedPois);
            } else {
                android.util.Log.w("HOME_FRAGMENT", "No user ID found, cannot save visited POIs");
            }
        } catch (Exception e) {
            android.util.Log.e("HOME_FRAGMENT", "Exception saving visited POIs", e);
        }
    }
    

}