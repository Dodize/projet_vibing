package com.example.vibing.ui.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vibing.R; // Import R for resources
import com.example.vibing.databinding.FragmentHomeBinding;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {

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

    // --- POI Model ---
    private static class PoiItem {
        String name;
        double distance;
        double latitude;
        double longitude;
        int score;
        int owningTeam; // 0 = neutral, 1-5 = teams
        Marker marker;

        PoiItem(String name, double latitude, double longitude, int score, int owningTeam) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
            this.score = score;
            this.owningTeam = owningTeam;
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
        }

        @Override
        public int getItemCount() {
            return poiList.size();
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


    private void initializePOIs() {
        poiList = new ArrayList<>();
        
        // POIs around Paris area with realistic coordinates
        poiList.add(new PoiItem("Tour Eiffel", 48.8584, 2.2945, 100, 0));
        poiList.add(new PoiItem("Musée du Louvre", 48.8606, 2.3376, 150, 2));
        poiList.add(new PoiItem("Notre Dame", 48.8530, 2.3499, 120, 1));
        poiList.add(new PoiItem("Arc de Triomphe", 48.8738, 2.2950, 80, 3));
        poiList.add(new PoiItem("Sacré-Cœur", 48.8867, 2.3431, 90, 0));
        poiList.add(new PoiItem("Panthéon", 48.8462, 2.3454, 110, 4));
        poiList.add(new PoiItem("Place des Vosges", 48.8566, 2.3639, 70, 0));
        
        // POIs around Toulouse area with realistic coordinates
        poiList.add(new PoiItem("Capitole", 43.6047, 1.4442, 130, 1));
        poiList.add(new PoiItem("Basilique Saint-Sernin", 43.6085, 1.4421, 95, 0));
        poiList.add(new PoiItem("Pont Neuf", 43.6037, 1.4326, 85, 3));
        poiList.add(new PoiItem("Jardin des Plantes", 43.6077, 1.4497, 75, 0));
        poiList.add(new PoiItem("Musée des Augustins", 43.6039, 1.4470, 105, 2));
        poiList.add(new PoiItem("Canal du Midi", 43.6053, 1.4389, 90, 4));
        poiList.add(new PoiItem("Hôtel d'Assézat", 43.6041, 1.4456, 80, 0));
        poiList.add(new PoiItem("Église des Jacobins", 43.6072, 1.4408, 100, 5));
        poiList.add(new PoiItem("Place du Capitole", 43.6047, 1.4442, 115, 1));
        poiList.add(new PoiItem("Quai de la Daurade", 43.6021, 1.4339, 70, 0));
        
        // Add markers to map
        for (PoiItem poi : poiList) {
            Marker poiMarker = new Marker(mapView);
            GeoPoint poiLocation = new GeoPoint(poi.latitude, poi.longitude);
            poiMarker.setPosition(poiLocation);
            poiMarker.setTitle(poi.name);
            poiMarker.setSnippet("Score: " + poi.score + " | Équipe: " + getTeamName(poi.owningTeam));
            poiMarker.setIcon(getTeamIcon(poi.owningTeam));
            poiMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            
            poi.marker = poiMarker;
            mapView.getOverlays().add(poiMarker);
        }
        
        updatePoiDistancesAndList();
        
        // Center map on Toulouse as default location
        mapView.getController().setCenter(new GeoPoint(43.6047, 1.4442));
        mapView.getController().setZoom(13.0);
        
        // Force map refresh
        mapView.invalidate();
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
        // Create a simple colored circle drawable for team markers
        android.graphics.drawable.ShapeDrawable shape = new android.graphics.drawable.ShapeDrawable(new android.graphics.drawable.shapes.OvalShape());
        shape.getPaint().setColor(getTeamColor(teamId));
        shape.setIntrinsicWidth(20);
        shape.setIntrinsicHeight(20);
        return shape;
    }
    
    private void updatePoiDistancesAndList() {
        GeoPoint referenceLocation = currentUserLocation;
        
        // Always use Toulouse center as reference for now (ignore emulator location)
        referenceLocation = new GeoPoint(43.6047, 1.4442); // Toulouse Capitole
        
        android.util.Log.d("DISTANCE_DEBUG", "Reference location: " + referenceLocation.getLatitude() + "," + referenceLocation.getLongitude());
        
        // Calculate distances from reference location
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
            
            // Debug log
            android.util.Log.d("DISTANCE_DEBUG", poi.name + ":");
            android.util.Log.d("DISTANCE_DEBUG", "  From: " + referenceLocation.getLatitude() + "," + referenceLocation.getLongitude());
            android.util.Log.d("DISTANCE_DEBUG", "  To: " + poi.latitude + "," + poi.longitude);
            android.util.Log.d("DISTANCE_DEBUG", "  Distance: " + distanceInMeters + "m (" + poi.distance + " km)");
        }
        
        // Sort by distance
        poiList.sort((a, b) -> Double.compare(a.distance, b.distance));
        
        // Update adapter
        if (poiListAdapter == null) {
            poiListAdapter = new PoiListAdapter(poiList);
            poiRecyclerView.setAdapter(poiListAdapter);
        } else {
            poiListAdapter.notifyDataSetChanged();
        }
        
        mapView.invalidate();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 1. Initialize MapView and OSMDroid configuration
        mapView = binding.mapView;
        
        Configuration.getInstance().setUserAgentValue(
            "VibingApp/1.0 (https://example.com/contact-if-needed)"
        );
        
        // Enable zoom controls and multi-touch
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        
        // Set tile source
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
        
        // Initialize Location Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        
        // Initialize Location Callback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                // Récupère la dernière location
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    // Center map on the received location and update marker
                    GeoPoint newLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                    currentUserLocation = newLocation;
                    mapView.getController().setCenter(newLocation);
                    mapView.getController().setZoom(17.0);

                    if (userMarker != null) {
                        userMarker.setPosition(newLocation);
                        userMarker.setTitle("Vous êtes ici");
                        userMarker.setVisible(true);
                    }
                    
                    // Update POI distances when location changes
                    updatePoiDistancesAndList();
                    mapView.invalidate();
                }
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
        
        // 3. Check Permissions and Start Location Logic
        checkLocationPermissions();
        
        // 4. Initialize POIs on the map
        initializePOIs();
        
        // Set initial reference location to Toulouse center for distance calculations
        if (currentUserLocation == null) {
            currentUserLocation = new GeoPoint(43.6047, 1.4442);
        }
        
        // 5. Center map on Toulouse as default location
        mapView.getController().setCenter(new GeoPoint(43.6047, 1.4442));
        mapView.getController().setZoom(13.0);

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
                // Permission denied. Center on Toulouse placeholder if userMarker is not set.
                if (userMarker != null) {
                    userMarker.setTitle("Permission de localisation refusée");
                    mapView.getController().setCenter(new GeoPoint(43.6047, 1.4442));
                    mapView.getController().setZoom(13.0);
                    userMarker.setVisible(false); // Hide marker if we can't get location
                }
            }
        }
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            
            LocationRequest locationRequest = LocationRequest.create()
                .setInterval(10000) // 10 seconds
                .setFastestInterval(5000) // 5 seconds
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY);

            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
            
            // Request an initial location update immediately if possible
            fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
                if (location != null && userMarker != null) {
                    GeoPoint initialLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                    currentUserLocation = initialLocation;
                    mapView.getController().setCenter(initialLocation);
                    mapView.getController().setZoom(17.0);
                    userMarker.setPosition(initialLocation);
                    userMarker.setTitle("Vous êtes ici (Dernière connue)");
                    userMarker.setVisible(true);
                    
                    // Update POI distances with initial location
                    updatePoiDistancesAndList();
                    mapView.invalidate();
                } else if (userMarker != null) {
                    userMarker.setTitle("Localisation en cours...");
                    userMarker.setVisible(true);
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
        binding = null;
    }
}