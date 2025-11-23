package com.example.vibing.ui.teams;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vibing.R;
import com.example.vibing.adapters.SimpleTeamAdapter;
import com.example.vibing.models.Team;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class TeamsFragment extends Fragment {

    private TeamsViewModel mViewModel;
    private RecyclerView recyclerView;
    private SimpleTeamAdapter teamAdapter;
    private List<Team> teamList;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Toast.makeText(getContext(), "TeamsFragment onCreateView start", Toast.LENGTH_SHORT).show();
        
        View view = inflater.inflate(R.layout.fragment_teams, container, false);
        Toast.makeText(getContext(), "Layout inflated", Toast.LENGTH_SHORT).show();
        
        recyclerView = view.findViewById(R.id.recyclerViewTeams);
        Toast.makeText(getContext(), "RecyclerView found", Toast.LENGTH_SHORT).show();

        try {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            Toast.makeText(getContext(), "LayoutManager set", Toast.LENGTH_SHORT).show();
             
            // Initialize empty list and adapter
            teamList = new ArrayList<>();
            teamAdapter = new SimpleTeamAdapter(teamList);
            recyclerView.setAdapter(teamAdapter);
            Toast.makeText(getContext(), "Adapter set with empty list", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Setup error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        
        Toast.makeText(getContext(), "About to return view", Toast.LENGTH_SHORT).show();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toast.makeText(getContext(), "TeamsFragment onActivityCreated start", Toast.LENGTH_SHORT).show();
        
        // Test Firebase diagnostic
        testFirebaseConfiguration();
        
        Toast.makeText(getContext(), "onActivityCreated completed", Toast.LENGTH_SHORT).show();
    }
    
    private void testFirebaseConfiguration() {
        Toast.makeText(getContext(), "Starting Firebase diagnostic", Toast.LENGTH_SHORT).show();
        
        // Test 1: Vérifier si Firebase App est initialisé
        try {
            com.google.firebase.FirebaseApp app = com.google.firebase.FirebaseApp.getInstance();
            Toast.makeText(getContext(), "FirebaseApp initialized: " + app.getName(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "FirebaseApp NOT initialized", Toast.LENGTH_LONG).show();
            return;
        }
        
        // Test 2: Vérifier Firestore
        try {
            db = FirebaseFirestore.getInstance();
            Toast.makeText(getContext(), "Firestore instance created", Toast.LENGTH_SHORT).show();
            
            // Load teams from Firebase
            loadTeamsFromFirebase();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Firestore error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void loadTeamsFromFirebase() {
        Toast.makeText(getContext(), "Loading teams from Firebase...", Toast.LENGTH_SHORT).show();
        
        db.collection("teams")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    teamList.clear();
                    for (com.google.firebase.firestore.QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Team team = document.toObject(Team.class);
                        teamList.add(team);
                    }
                    teamAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Loaded " + teamList.size() + " teams", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error loading teams: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}