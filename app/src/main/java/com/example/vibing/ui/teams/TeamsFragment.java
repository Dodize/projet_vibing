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
        View view = inflater.inflate(R.layout.fragment_teams, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewTeams);

        try {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
             
            // Initialize empty list and adapter
            teamList = new ArrayList<>();
            teamAdapter = new SimpleTeamAdapter(teamList);
            recyclerView.setAdapter(teamAdapter);
        } catch (Exception e) {
            // Handle setup error silently
        }
        
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        // Test Firebase diagnostic
        testFirebaseConfiguration();
    }
    
    private void testFirebaseConfiguration() {
        // Test 1: Vérifier si Firebase App est initialisé
        try {
            com.google.firebase.FirebaseApp app = com.google.firebase.FirebaseApp.getInstance();
        } catch (Exception e) {
            return;
        }
        
        // Test 2: Vérifier Firestore
        try {
            db = FirebaseFirestore.getInstance();
            
            // Load teams from Firebase
            loadTeamsFromFirebase();
        } catch (Exception e) {
            // Handle Firestore error silently
        }
    }
    
    private void loadTeamsFromFirebase() {
        db.collection("teams")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    teamList.clear();
                    for (com.google.firebase.firestore.QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Team team = document.toObject(Team.class);
                        teamList.add(team);
                    }
                    teamAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle error silently
                });
    }
}