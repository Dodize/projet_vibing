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

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private MapView mapView;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Marker userMarker;
    private static final int REQUEST_PERMISSIONS_LOCATION = 1001;
    private RecyclerView poiRecyclerView;
    private PoiListAdapter poiListAdapter;

    // --- Simple POI Model ---
    private static class PoiItem {
        String name;
        double distance; // Distance in kilometers (placeholder)

        PoiItem(String name, double distance) {
            this.name = name;
            this.distance = distance;
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


    private void displayPlaceholderPOIs() {
        List<PoiItem> placeholderPois = new ArrayList<>();
        // Mocking data relative to the placeholder center (Paris)
        placeholderPois.add(new PoiItem("Tour Eiffel (POI)", 1.2)); 
        placeholderPois.add(new PoiItem("Musée du Louvre (POI)", 2.5));
        placeholderPois.add(new PoiItem("Notre Dame (POI)", 3.1));
        placeholderPois.add(new PoiItem("Local Cafe", 0.15));

        poiListAdapter = new PoiListAdapter(placeholderPois);
        poiRecyclerView.setAdapter(poiListAdapter);
        
        // Center map on the first POI as a starting point if location is not yet available
        if (mapView.getMapCenter().getLatitude() == 48.8566 && mapView.getMapCenter().getLongitude() == 2.3522) {
             mapView.getController().setCenter(new GeoPoint(48.8584, 2.2945));
             mapView.getController().setZoom(15.0);
        }

        mapView.invalidate(); // Redraw map
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
                    mapView.getController().setCenter(newLocation);
                    mapView.getController().setZoom(17.0);

                    if (userMarker != null) {
                        userMarker.setPosition(newLocation);
                        userMarker.setTitle("Vous êtes ici");
                        userMarker.setVisible(true);
                        mapView.invalidate();
                    }
                }
            }
        };

        // Initialize User Marker (will be positioned on first location update)
        userMarker = new Marker(mapView);
        userMarker.setTitle("Localisation en cours...");
        userMarker.setIcon(getResources().getDrawable(R.drawable.ic_launcher_foreground)); 
        mapView.getOverlays().add(userMarker);
        
        // 2. Initialize POI List RecyclerView
        poiRecyclerView = binding.poiListRecyclerView;
        poiRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // 3. Check Permissions and Start Location Logic
        checkLocationPermissions();
        
        // 4. Add static POIs regardless of location status
        displayPlaceholderPOIs();

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
                // Permission denied. Center on Paris placeholder if userMarker is not set.
                if (userMarker != null) {
                    userMarker.setTitle("Permission de localisation refusée");
                    mapView.getController().setCenter(new GeoPoint(48.8566, 2.3522));
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
                    mapView.getController().setCenter(initialLocation);
                    mapView.getController().setZoom(17.0);
                    userMarker.setPosition(initialLocation);
                    userMarker.setTitle("Vous êtes ici (Dernière connue)");
                    userMarker.setVisible(true);
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