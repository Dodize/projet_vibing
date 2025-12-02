package com.example.vibing.ui.home;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;

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
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.XYTileSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment implements OnMarkerClickListener {

    private FragmentHomeBinding binding;
    private MapView mapView;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Marker userMarker;
    private static final int REQUEST_PERMISSIONS_LOCATION = 1001;
    private RecyclerView poiRecyclerView;
    private PoiListAdapter poiListAdapter;
    private List<PoiItem> poiList;
    private GeoPoint currentUserLocation;
    private Random random = new Random();
    private TextView currentInfoBubble;
    private Marker currentSelectedMarker;
    private HomeViewModel homeViewModel;
    
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
        double distance;
        double latitude;
        double longitude;
        int score;
        int owningTeam; // 0 = neutral, 1-5 = teams
        double radius; // Rayon de la zone cliquable en mètres
        Marker marker;

        PoiItem(String name, double latitude, double longitude, int score, int owningTeam) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
            this.score = score;
            this.owningTeam = owningTeam;
            this.radius = 100.0; // Rayon par défaut de 100m
            this.distance = 0; // Will be calculated
        }
        
        PoiItem(String name, double latitude, double longitude, int score, int owningTeam, double radius) {
            this.name = name;
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
            
            // Set click listener
            holder.itemView.setOnClickListener(v -> {
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


    private void initializePOIsFromFirebase() {
        homeViewModel.getPois().observe(getViewLifecycleOwner(), pois -> {
            if (pois != null) {
                poiList = new ArrayList<>();
                
                // Convert Firebase POIs to local PoiItems
                for (Poi poi : pois) {
                    PoiItem poiItem = new PoiItem(poi.getName(), poi.getLatitude(), poi.getLongitude(), poi.getScore(), poi.getOwningTeam(), poi.getRadius());
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
                    
                    // Check if user is in zone to set appropriate icon
                    boolean isInZone = isUserInPoiZone(poi);
                    poiMarker.setIcon(getTeamIcon(poi.owningTeam, isInZone));
                    
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
            }
        });
    }
    
    private String getTeamName(int teamId) {
        switch (teamId) {
            case 0: return "Neutre";
            case 1: return "Équipe Rouge";
            case 2: return "Équipe Bleue";
            case 3: return "Équipe Verte";
            case 4: return "Équipe Jaune";
            case 5: return "Équipe Violette";
            default: return "Inconnue";
        }
    }
    
    private int getTeamColor(int teamId) {
        switch (teamId) {
            case 0: return 0xFF808080; // Gray for neutral
            case 1: return 0xFFFF0000; // Red
            case 2: return 0xFF0000FF; // Blue
            case 3: return 0xFF00FF00; // Green
            case 4: return 0xFFFFFF00; // Yellow
            case 5: return 0xFF800080; // Purple
            default: return 0xFF808080; // Gray
        }
    }
    
    private android.graphics.drawable.Drawable getTeamIcon(int teamId) {
        return getTeamIcon(teamId, false);
    }
    
    private android.graphics.drawable.Drawable getTeamIcon(int teamId, boolean isInZone) {
        // Create a larger colored circle drawable for team markers
        android.graphics.drawable.ShapeDrawable shape = new android.graphics.drawable.ShapeDrawable(new android.graphics.drawable.shapes.OvalShape());
        
        // Use brighter color if in zone, normal color if not
        int color = isInZone ? getBrightTeamColor(teamId) : getTeamColor(teamId);
        shape.getPaint().setColor(color);
        shape.getPaint().setStrokeWidth(isInZone ? 4 : 2); // Thicker border if in zone
        shape.getPaint().setStyle(android.graphics.Paint.Style.FILL_AND_STROKE);
        shape.getPaint().setAntiAlias(true);
        shape.setIntrinsicWidth(isInZone ? 45 : 40); // Slightly larger if in zone
        shape.setIntrinsicHeight(isInZone ? 45 : 40);
        return shape;
    }
    
    private int getBrightTeamColor(int teamId) {
        switch (teamId) {
            case 0: return 0xFFB0B0B0; // Brighter gray for neutral
            case 1: return 0xFFFF4444; // Brighter red
            case 2: return 0xFF4444FF; // Brighter blue
            case 3: return 0xFF44FF44; // Brighter green
            case 4: return 0xFFFFFF44; // Brighter yellow
            case 5: return 0xFFAA44AA; // Brighter purple
            default: return 0xFFB0B0B0; // Brighter gray
        }
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
            
            // Update marker icon based on zone status
            if (poi.marker != null) {
                boolean isInZone = isUserInPoiZone(poi);
                poi.marker.setIcon(getTeamIcon(poi.owningTeam, isInZone));
            }
        }
        
        // Sort by distance
        poiList.sort((a, b) -> Double.compare(a.distance, b.distance));
        
        // Update adapter with new data
        // Create new adapter with fresh data to ensure it's properly updated
        poiListAdapter = new PoiListAdapter(new ArrayList<>(poiList));
        poiRecyclerView.setAdapter(poiListAdapter);
        
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
                    mapView.getController().setCenter(realLocation);
                    mapView.getController().setZoom(15.0);

                    if (userMarker != null) {
                        userMarker.setPosition(realLocation);
                        userMarker.setTitle("Vous êtes ici");
                        userMarker.setVisible(true);
                    }
                }
                
                // Update POI distances when location changes
                updatePoiDistancesAndList();
                mapView.invalidate();
            }
        };

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
        
        // 3. Initialize POIs from Firebase
        initializePOIsFromFirebase();
        
        // 4. Check Permissions and Start Location Logic
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
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_LOCATION);
        } else {
            // Permission already granted, start location updates
            startLocationUpdates();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                startLocationUpdates();
            } else {
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
        stopLocationUpdates(); // Ensure updates are stopped on destruction
        if (mapView != null) {
            mapView.onDetach(); // Detach map resources
        }
        // Clean up info bubble
        if (currentInfoBubble != null && currentInfoBubble.getParent() != null) {
            ((ViewGroup) currentInfoBubble.getParent()).removeView(currentInfoBubble);
        }
        binding = null;
    }

    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        // Find the POI associated with this marker
        for (PoiItem poi : poiList) {
            if (poi.marker == marker) {
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
        
        // DEBUG: Afficher les informations de débogage
        android.util.Log.d("POI_ZONE_DEBUG", "POI: " + poi.name);
        android.util.Log.d("POI_ZONE_DEBUG", "User location: " + currentUserLocation.getLatitude() + ", " + currentUserLocation.getLongitude());
        android.util.Log.d("POI_ZONE_DEBUG", "POI location: " + poi.latitude + ", " + poi.longitude);
        android.util.Log.d("POI_ZONE_DEBUG", "Distance: " + distanceInMeters + "m");
        android.util.Log.d("POI_ZONE_DEBUG", "Radius: " + poi.radius + "m");
        android.util.Log.d("POI_ZONE_DEBUG", "Is in zone: " + (distanceInMeters <= poi.radius));
        
        return distanceInMeters <= poi.radius;
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
        bundle.putFloat("poiLatitude", (float) poi.latitude);
        bundle.putFloat("poiLongitude", (float) poi.longitude);
        bundle.putInt("poiScore", poi.score);
        bundle.putInt("poiOwningTeam", poi.owningTeam);
        
        // Navigate to PoiScoreFragment
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_homeFragment_to_poiScoreFragment, bundle);
    }
    
    private void displayUserInfo() {
        SharedPreferences prefs = requireContext().getSharedPreferences("VibingPrefs", android.content.Context.MODE_PRIVATE);
        String username = prefs.getString("username", "Joueur");
        String teamName = prefs.getString("team_name", "Équipe inconnue");
        int money = prefs.getInt("money", 0);
        
        TextView usernameTextView = binding.getRoot().findViewById(R.id.username_text_view);
        TextView teamTextView = binding.getRoot().findViewById(R.id.team_text_view);
        TextView moneyTextView = binding.getRoot().findViewById(R.id.money_text_view);
        
        if (usernameTextView != null) {
            usernameTextView.setText(username);
        }
        
        if (teamTextView != null) {
            teamTextView.setText("Équipe: " + teamName);
        }
        
        if (moneyTextView != null) {
            moneyTextView.setText("Argent: " + money + "€");
        }
    }
    

}